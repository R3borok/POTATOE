package potatoe.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CodeAssemblerOutput {
    
    public final class OutputNotification {

        private final int line;
        private final int column;
        private final String message;

        public OutputNotification(final int line, final int column, final String message) {
            this.line = line;
            this.column = column;
            this.message = message;
        }

        public int getLine() {
            return this.line;
        }

        public int getColumn() {
            return this.column;
        }

        public String getMessage() {
            return this.message;
        }

        @Override
        public String toString() {
            return "(" + line + "," + column + "): " + message;
        }

    }

    private final List<OutputNotification> warningList = new ArrayList<>();
    private final List<OutputNotification> errorList = new ArrayList<>();

    private byte[] assembledBytes = new byte[0];
    
    public CodeAssemblerOutput() {
    }

    public void addError(final int line, final int column, final String message) {
        this.errorList.add(new OutputNotification(line, column, message));
    }

    public void addErrors(final List<OutputNotification> errors) {
        this.errorList.addAll(errors);
    }

    public List<OutputNotification> getErrors() {
        return this.errorList;
    }

    public void addWarning(final int line, final int column, final String message) {
        warningList.add(new OutputNotification(line, column, message));
    }

    public void addWarnings(final List<OutputNotification> warnings) {
        this.warningList.addAll(warnings);
    }

    public List<OutputNotification> getWarnings() {
        return this.warningList;
    }

    public void addAssembledBytes(final byte[] assembledBytes) {
        final int finalSize = (this.assembledBytes.length + assembledBytes.length);
        final byte[] finalBytes = Arrays.copyOf(this.assembledBytes, finalSize);

        for (int i=0; i<assembledBytes.length; i++) {
            finalBytes[(this.assembledBytes.length + i)] = assembledBytes[i];
        }

        this.setAssembledBytes(finalBytes);
    }

    public void setAssembledBytes(final byte[] assembledBytes) {
        this.assembledBytes = assembledBytes;
    }

    public byte[] getAssembledBytes() {
        return this.assembledBytes;
    }

}
