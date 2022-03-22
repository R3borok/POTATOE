package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import exception.PotatoeException;

public final class CodeAssembler {

    private final CodeAssemblerOutput output = new CodeAssemblerOutput();
    private final File fileToAssemble;

    public CodeAssembler(final File fileToAssemble) {
        this.fileToAssemble = Objects.requireNonNull(fileToAssemble, "Can not process null file.");
    }

    public CodeAssemblerOutput assembleCode() throws PotatoeException {
        List<String> lines = getLinesFromFile();

        return output;
    }

    private List<String> getLinesFromFile() throws PotatoeException {
        final List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileToAssemble))) {

            String line;
            while((line = br.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            throw new PotatoeException("Could not read in file.", e);
        }

        return lines;
    }

}
