package potatoe.code;

import potatoe.AssemblyData;

public enum PotCommands {
    MOV(AssemblyData.MOV, AssemblyData.MODE_0),
    MVR(AssemblyData.MOV, AssemblyData.MODE_1),

    LDR(AssemblyData.LOD, AssemblyData.MODE_0),
    LDI(AssemblyData.LOD, AssemblyData.MODE_1),

    STR(AssemblyData.STR, AssemblyData.MODE_0),
    STV(AssemblyData.STR, AssemblyData.MODE_1),

    PSI(AssemblyData.PSH, AssemblyData.MODE_0),
    PSR(AssemblyData.PSH, AssemblyData.MODE_1),
    POP(AssemblyData.POP, AssemblyData.MODE_0),

    JMP(AssemblyData.JMP, AssemblyData.MODE_0),

    ADI(AssemblyData.ADD, AssemblyData.MODE_0),
    ADA(AssemblyData.ADD, AssemblyData.MODE_1),
    SBI(AssemblyData.SUB, AssemblyData.MODE_0),
    SBA(AssemblyData.SUB, AssemblyData.MODE_1),
    CPI(AssemblyData.CMP, AssemblyData.MODE_0),
    CPR(AssemblyData.CMP, AssemblyData.MODE_1),
    NDI(AssemblyData.AND, AssemblyData.MODE_0),
    NDA(AssemblyData.AND, AssemblyData.MODE_1),
    ORI(AssemblyData.OR,  AssemblyData.MODE_0),
    ORA(AssemblyData.OR,  AssemblyData.MODE_1),
    XOI(AssemblyData.XOR, AssemblyData.MODE_0),
    XOA(AssemblyData.XOR, AssemblyData.MODE_1);

    private final byte identifier;
    private final byte mode;

    private PotCommands(final byte identifier, final byte mode) {
        this.identifier = identifier;
        this.mode = mode;
    }

    public byte getIdentifier() {
        return this.identifier;
    }

    public byte getMode() {
        return this.mode;
    }
}
