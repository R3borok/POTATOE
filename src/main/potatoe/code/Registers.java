package potatoe.code;

import potatoe.AssemblyData;

public enum Registers {
    AREG(AssemblyData.AREG),
    BREG(AssemblyData.BREG),
    CREG(AssemblyData.CREG),
    DREG(AssemblyData.DREG);

    private final byte identifier;

    private Registers(final byte identifier) {
        this.identifier = identifier;
    }

    public byte getIdentifier() {
        return this.identifier;
    }
}
