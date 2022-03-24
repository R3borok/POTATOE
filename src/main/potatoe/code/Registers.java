package potatoe.code;

import potatoe.AssemblyData;

public enum Registers {
    AREG(AssemblyData.AREG),
    BREG(AssemblyData.BREG),
    CREG(AssemblyData.CREG),
    DREG(AssemblyData.DREG),

    UNUSED(AssemblyData.AREG);

    private final byte identifier;

    private Registers(final byte identifier) {
        this.identifier = identifier;
    }

    public byte getIdentifier() {
        return this.identifier;
    }

    public static Registers fromString(final String registerString) {
        try {
            return Enum.valueOf(Registers.class, registerString.toUpperCase());
        } catch (IllegalArgumentException e) {
        }

        return null;
    }
}
