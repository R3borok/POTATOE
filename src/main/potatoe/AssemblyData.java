package potatoe;

public final class AssemblyData {

    private AssemblyData() {}

    // control logic/ control word (32 bit)
    public static final int HLT = 0b00000000000000000000000000000000;

    // instructions
    public static final byte MOV = 0b0000; // move
    public static final byte LOD = 0b0001; // load
    public static final byte STR = 0b0010; // store
    public static final byte PSH = 0b0011; // push
    public static final byte POP = 0b0100; // pop
    public static final byte JMP = 0b0101; // jump
    public static final byte NOP_0 = 0b0110;
    public static final byte NOP_1 = 0b0111;
    public static final byte NOP_2 = 0b1000;
    public static final byte ADD = 0b1001; // add
    public static final byte ADC = 0b1010; // add with carry
    public static final byte SUB = 0b1011; // sub
    public static final byte CMP = 0b1100; // compare
    public static final byte AND = 0b1101; // logical and
    public static final byte OR  = 0b1110; // logical or
    public static final byte XOR = 0b1111; // logical xor

    // instruction-modes
    public static final byte MODE_0 = 0b0;
    public static final byte MODE_1 = 0b1;

    // registers
    public static final byte AREG = 0b00;
    public static final byte BREG = 0b01;
    public static final byte CREG = 0b10;
    public static final byte DREG = 0b11;
}