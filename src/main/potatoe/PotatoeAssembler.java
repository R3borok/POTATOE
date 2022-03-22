package potatoe;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import potatoe.exception.PotatoeException;
import potatoe.microcode.MicroCodeAssembler;
import potatoe.code.CodeAssembler;
import potatoe.code.CodeAssemblerOutput;

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
                assembleMicrocode();
            }

            if (currentArg.toLowerCase().startsWith(FILE_ASSEMBLY_ARG)) {
                String filePath = currentArg.split("=")[1];
                assemblePotFile(filePath);
            }
        }

        System.out.println("Done.");
    }

    private static void assembleMicrocode() {
        System.out.println("Assembling Micro-main.code data...");

        final byte[] microCodeBytes = MicroCodeAssembler.assembleMicroCode();
        printBytesAsHexData(microCodeBytes, RAM_SIZE);
    }

    private static void assemblePotFile(final String filePath) throws PotatoeException {
        if (!Files.exists(Paths.get(filePath))) {
            throw new PotatoeException("Could not find passed file: " + filePath);
        }

        System.out.println("Assembling main.code data...");
        final File fileToAssemble = new File(filePath);
        final CodeAssembler codeAssembler = new CodeAssembler(fileToAssemble);
        final CodeAssemblerOutput output = codeAssembler.assembleCode();

        output.getWarnings().forEach((warning) -> System.out.println("WARN " + warning));

        if(output.getErrors().isEmpty()) {

            printBytesAsHexData(output.getAssembledBytes(), RAM_SIZE);

        } else {

            output.getErrors().forEach((error) -> System.out.println("ERROR :" + error));

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
