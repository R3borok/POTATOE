import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import exception.PotatoeException;

public final class PotatoeAssembler {
 
    private static final String MICRO_CODE_ARG = "mc";
    private static final String FILE_ASSEMBLY_ARG = "file=";

    private static final int RAM_SIZE = 32766;

    private PotatoeAssembler() {
    }

    public static void main(String[] args) {
        try {

            PotatoeAssembler.handleArguments(args);

        } catch (PotatoeException e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }

    private static void handleArguments(String[] args) throws PotatoeException {
        if (args.length == 0) {
            throw new PotatoeException("Can not execute assembler without arguments!");
        }

        for (String currentArg : args) {
            if (currentArg.toLowerCase().startsWith(MICRO_CODE_ARG)) {
                System.out.println("Assembling Micro-code data...");

                final byte[] microCodeBytes = MicroCodeAssembler.assembleMicroCode();
                printBytesAsHexData(microCodeBytes, RAM_SIZE);
            }

            if (currentArg.toLowerCase().startsWith(FILE_ASSEMBLY_ARG)) {
                String filePathToAssemble = currentArg.split("=")[1];
                if (!Files.exists(Paths.get(filePathToAssemble))) {
                    throw new PotatoeException("Could not find passed file: " + filePathToAssemble);
                }

                System.out.println("Assembling code data...");
                final File fileToAssemble = new File(filePathToAssemble);
                final byte[] codeBytes = CodeAssembler.assembleCode(fileToAssemble);
                printBytesAsHexData(codeBytes, RAM_SIZE);
            }
        }
    }

    private static void printBytesAsHexData(final byte[] bytesToPrint, final int fillSize) {
        for (int address=0; address<fillSize; address+=16) {
            final String addressHexString = String.format("%04X: ", (short) address);
            String currentLine = addressHexString;

            for (int counter=0; counter<16; counter++) {
                if ((address + counter) < bytesToPrint.length) {
                    final String byteHexString = String.format("%02X ", bytesToPrint[address + counter]);
                    currentLine += byteHexString;
                } else {
                    currentLine += "00 ";
                }
            }
            System.out.println(currentLine);
        }
    }

}
