package potatoe.code;

public enum StructureCommands {
    ORIGIN,
    IMPORT,
    DECLARE,
    ROM,
    VARS;

    public static StructureCommands fromString(final String commandString) {
        try {
            return Enum.valueOf(StructureCommands.class, commandString);
        } catch (IllegalArgumentException e) {
        }

        return null;
    }
}
