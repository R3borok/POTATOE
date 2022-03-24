package potatoe.code;

import potatoe.AssemblyData;

public enum PotCommands {
    MOV(AssemblyData.MOV, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    MVR(AssemblyData.MOV, AssemblyData.MODE_1, CommandArg.REG, CommandArg.REG),

    LDR(AssemblyData.LOD, AssemblyData.MODE_0, CommandArg.REG, CommandArg.ADR16),
    LDI(AssemblyData.LOD, AssemblyData.MODE_1, CommandArg.REG, CommandArg.NONE),

    STR(AssemblyData.STR, AssemblyData.MODE_0, CommandArg.REG, CommandArg.ADR16),
    STV(AssemblyData.STR, AssemblyData.MODE_1, CommandArg.REG, CommandArg.ADR16),

    PSI(AssemblyData.PSH, AssemblyData.MODE_0, CommandArg.IMM8, CommandArg.NONE),
    PSR(AssemblyData.PSH, AssemblyData.MODE_1, CommandArg.REG, CommandArg.NONE),
    POP(AssemblyData.POP, AssemblyData.MODE_0, CommandArg.REG, CommandArg.NONE),

    JMP(AssemblyData.JMP, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),

    ADI(AssemblyData.ADD, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    ADA(AssemblyData.ADD, AssemblyData.MODE_1, CommandArg.REG, CommandArg.ADR16),
    ACI(AssemblyData.ADC, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    ACA(AssemblyData.ADC, AssemblyData.MODE_1, CommandArg.REG, CommandArg.ADR16),
    SBI(AssemblyData.SUB, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    SBA(AssemblyData.SUB, AssemblyData.MODE_1, CommandArg.REG, CommandArg.ADR16),
    CPI(AssemblyData.CMP, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    CPR(AssemblyData.CMP, AssemblyData.MODE_1, CommandArg.REG, CommandArg.REG),
    NDI(AssemblyData.AND, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    NDA(AssemblyData.AND, AssemblyData.MODE_1, CommandArg.REG, CommandArg.ADR16),
    ORI(AssemblyData.OR,  AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    ORA(AssemblyData.OR,  AssemblyData.MODE_1, CommandArg.REG, CommandArg.ADR16),
    XOI(AssemblyData.XOR, AssemblyData.MODE_0, CommandArg.REG, CommandArg.IMM8),
    XOA(AssemblyData.XOR, AssemblyData.MODE_1, CommandArg.REG, CommandArg.ADR16);

    public enum CommandArg {
        REG,
        IMM8,
        ADR16,
        NONE;
    };

    private final byte identifier;
    private final byte mode;
    private final CommandArg arg0;
    private final CommandArg arg1;

    private PotCommands(final byte identifier, final byte mode,
                        final CommandArg arg0, final CommandArg arg1) {
        this.identifier = identifier;
        this.mode = mode;
        this.arg0 = arg0;
        this.arg1 = arg1;
    }

    public byte getIdentifier() {
        return this.identifier;
    }

    public byte getMode() {
        return this.mode;
    }

    public CommandArg getArg0() {
        return this.arg0;
    }

    public CommandArg getArg1() {
        return this.arg1;
    }

    public static PotCommands fromString(final String commandString) {
        try {
            return Enum.valueOf(PotCommands.class, commandString.toUpperCase());
        } catch (IllegalArgumentException e) {
        }

        return null;
    }

}
