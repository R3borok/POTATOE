package potatoe.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import potatoe.exception.PotatoeException;
import potatoe.exception.PotatoeRuntimeException;

public final class CodeAssembler {

    private static final int BYTE_MAX_VALUE = 255;
    private static final int SHORT_MAX_VALUE = 65535;
    private static final String STRUCTURE_IDENTIFIER = "@";
    private static final String REFERENCE_IDENTIFIER = ":";

    private final CodeAssemblerOutput mainOutput = new CodeAssemblerOutput();
    private final File fileToAssemble;

    public CodeAssembler(final File fileToAssemble) {
        this.fileToAssemble = Objects.requireNonNull(fileToAssemble, "Can not process null file.");
    }

    public CodeAssemblerOutput assembleCode() throws PotatoeException {
        List<String> lines = getLinesFromFile();

        // first pass to collect all references
        for (Iterator<String> i = lines.iterator(); i.hasNext(); ) {
            final String line = i.next().toLowerCase().trim();

            final CodeAssemblerOutput lineOutput = preProcessLine(line);
            mainOutput.addWarnings(lineOutput.getWarnings());
            mainOutput.addErrors(lineOutput.getErrors());

            if (lineOutput.getErrors().isEmpty()) {
                lines.remove(line);
            }
        }

        // second pass to do the actual assembly
        List<Byte>  assembledBytes = new ArrayList<>();
        lines.forEach((line) -> {
            final CodeAssemblerOutput lineOutput = assembleLine(line);
            mainOutput.addWarnings(lineOutput.getWarnings());
            mainOutput.addErrors(lineOutput.getErrors());
            for (int i=0; i< lineOutput.getAssembledBytes().length; i++) {
                assembledBytes.add(lineOutput.getAssembledBytes()[i]);
            }
        });

        // assemble complete bytes with imports etc if no error occurred
        if (mainOutput.getErrors().isEmpty()) {
            byte[] finalBytes = postAssembly(assembledBytes);
            mainOutput.setAssembledBytes(finalBytes);
        }

        return mainOutput;
    }

    private List<String> getLinesFromFile() throws PotatoeException {
        final List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileToAssemble))) {

            String line;
            while((line = br.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            throw new PotatoeException("Could not read in file: " + e.getMessage());
        }

        return lines;
    }

    protected CodeAssemblerOutput preProcessLine(final String line) {
        final CodeAssemblerOutput lineOutput = new CodeAssemblerOutput();

        // check structure commands
        if (line.startsWith(STRUCTURE_IDENTIFIER)) {

            final String command = line.substring(1, line.length() - 1);
            final StructureCommands structureCommand = StructureCommands.fromString(command);
            if (structureCommand == null) {

            }

        } else if (line.endsWith(REFERENCE_IDENTIFIER)) {

        }

        return lineOutput;
    }

    protected CodeAssemblerOutput assembleLine(final String line) {
        final CodeAssemblerOutput lineOutput = new CodeAssemblerOutput();

        final String modifiedLine = line.toLowerCase().trim();
        final String[] split = modifiedLine.split(" ");

        // find the command in knwon commands
        final PotCommands command = PotCommands.fromString(split[0]);
        if (command == null) {
            // TODO add line and column
            lineOutput.addError(0, 0, "Unknown command '" + split[0] + "'");
            return lineOutput;
        }

        // check that the expected format exists
        final PotCommands.CommandArg arg0 = command.getArg0();
        final PotCommands.CommandArg arg1 = command.getArg1();

        final AtomicReference<Registers> affectedRegister = new AtomicReference<>(null);
        final AtomicReference<Buffered> buffered = new AtomicReference<>(null);
        final AtomicReference<Byte> byteBuff = new AtomicReference<>((byte) 0x00000000);
        final AtomicReference<Short> shortBuff = new AtomicReference<>((short) 0x0000000000000000);

        if (arg1.equals(PotCommands.CommandArg.NONE)) {

            processArguments(arg0, split[1],
                             affectedRegister, buffered, byteBuff, shortBuff,
                             lineOutput);
            if (!lineOutput.getErrors().isEmpty()) {
                return lineOutput;
            }

        } else {

            final String[] argSplit = split[1].split(",");
            processArguments(arg0, argSplit[0],
                             affectedRegister, buffered, byteBuff, shortBuff,
                             lineOutput);
            if (!lineOutput.getErrors().isEmpty()) {
                return lineOutput;
            }

            final AtomicReference<Registers> secondRegister = new AtomicReference<>(Registers.UNUSED);
            processArguments(arg1, argSplit[1],
                             secondRegister, buffered, byteBuff, shortBuff,
                             lineOutput);
            if (!lineOutput.getErrors().isEmpty()) {
                return lineOutput;
            }

            // second arg was a register
            if (!secondRegister.equals(Registers.UNUSED)) {
                buffered.set(Buffered.IMM8);
            }
        }

        if (affectedRegister == null) {
            // TODO add line and column
            lineOutput.addError(0, 0, "Unknown argument '" + split[0] + "'");
            return lineOutput;
        }

        if (buffered == null) {
            // TODO add line and column
            lineOutput.addError(0, 0, "Buffered value expected after instruction but found '" + split[0] + "'");
            return lineOutput;
        }

        byte[] bufferedBytes;
        if (buffered.get().equals(Buffered.IMM8)) {
            bufferedBytes = new byte[] { byteBuff.get() };
        } else if (buffered.get().equals(Buffered.ADR16)) {
            bufferedBytes = new byte[] { shortBuff.get().byteValue(), (byte) (shortBuff.get() >> 8) };
        } else {
            throw new PotatoeRuntimeException("Buffered state for command is unknown: " + buffered);
        }

        // assemble the actual bytes
        final byte[] assembledBytes = assembleInstruction(command, affectedRegister.get(), bufferedBytes);
        lineOutput.setAssembledBytes(assembledBytes);

        return lineOutput;
    }

    protected byte[] assembleInstruction(final PotCommands command, final Registers register, final byte[] buffer) {
        // assemble final instruction byte with mode and affected register
        final byte instructionByte = (byte)
                                     (((command.getIdentifier()  & 0b00001111) << 4) | // mask instruction and shift to final position
                                      ((command.getMode()        & 0b00000001) << 3) | // mask instruction mode and shift to final position
                                      ((register.getIdentifier() & 0b00000011)));      // mask register identifier

        // take instruction byte and passed buffer and assemble final assembled instruction
        final byte[] assembledInstruction = new byte[(1 + buffer.length)];
        assembledInstruction[0] = instructionByte;
        if (buffer.length > 0) {
            for (int i=0; i<buffer.length; i++) {
                assembledInstruction[i + 1] = buffer[i];
            }
        }

        return assembledInstruction;
    }

    private void processArguments(final PotCommands.CommandArg arg, final String strArg,
                                  final AtomicReference<Registers> affectedRegister, final AtomicReference<Buffered> buffered,
                                  final AtomicReference<Byte> byteBuff, final AtomicReference<Short> shortBuff,
                                  final CodeAssemblerOutput lineOutput) {
        int numericValue = 0;
        switch (arg) {
            case REG: // check that arg is a valid register
                affectedRegister.set(Registers.fromString(strArg));
                buffered.set(Buffered.UNUSED);
                break;
            case IMM8: // check that arg is a valid imm8
                affectedRegister.set(Registers.UNUSED);
                // TODO add support for math functions
                buffered.set(Buffered.IMM8);

                numericValue = 0;
                try {
                    numericValue = Integer.parseInt(strArg);
                } catch (NumberFormatException e) {
                    lineOutput.addError(0, 0, "Could not parse as numeric '" + strArg + "'");
                    return;
                }

                byteBuff.set((byte) numericValue);
                if (numericValue > BYTE_MAX_VALUE) {
                    // TODO add line and column
                    lineOutput.addWarning(0, 0, "Value bigger than one byte. Cropped to fit: " +
                            numericValue + " -> " + byteBuff);
                }

                break;
            case ADR16: // check that arg is a valid adr16
                affectedRegister.set(Registers.UNUSED);
                // TODO add support for math functions
                buffered.set(Buffered.ADR16);

                numericValue = 0;
                try {
                    numericValue = Integer.parseInt(strArg);
                } catch (NumberFormatException e) {
                    lineOutput.addError(0, 0, "Could not parse as numeric '" + strArg + "'");
                    return;
                }

                shortBuff.set((short) numericValue);
                if (numericValue > SHORT_MAX_VALUE) {
                    // TODO add line and column
                    lineOutput.addWarning(0, 0, "Value bigger than one byte. Cropped to fit: " +
                            numericValue + " -> " + byteBuff);
                }

                break;
            default:
                throw new PotatoeRuntimeException("Unsupported command argument '" + arg.toString() + "'");
        }
    }

    protected byte[] postAssembly(final List<Byte> assembledBytes) {
        // convert assembled code bytes
        byte[] primitiveBytes = new byte[assembledBytes.size()];
        for (int i=0; i<primitiveBytes.length; i++) {
            primitiveBytes[i] = assembledBytes.get(i).byteValue();
        }

        // check imports

        // assemble final bytes
        // final byte size = 1 (initial jump to origin) + assembled codeBytes
        final int finalArraySize = 1 + primitiveBytes.length;
        final byte[] finalBytes = new byte[finalArraySize];

        return primitiveBytes;
    }

}
