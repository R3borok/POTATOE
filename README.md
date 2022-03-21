# POTATOE-1

POTATOE-1 CPU documentation for architecture and binary machine code structure and generation/assembly.

## Architecture

Designed to be an 8 bit programmable mini-cpu with 16 bit ram-addresses. Basic arithmetic and logic functionality.<br>
Output via VGA signal (800x600 60Hz) with vram to store downscaled 4:3 with 200x150 pixels.<br>
Basic I/O with one byte size.

### Components

- 16 bit program counter with jump
- 4 general purpose 8 bit registers (addressed by 2 bits RR)
- ALU with 8 bit operations
    - arithmetic: add, sub, add with carry
    - logic: and, or, xor (not with 0xFF)
- 16 bit addressable 8 bit RAM
- 16 bit addressable 8 bit VRAM
- 8 bit stack with 8 bit stackpointer (max size 255)

### Instructions

Instructions are one byte and followed by one or two more bytes which are buffered in the instruction register. <br>
Intructions consist of the following format:

<p>XXXXYURR BBBBBBBB [BBBBBBBBB}</p>

| bitID       | Description                 |
| ----------- | --------------------------- |
| XXXX        | the instruction identifier  |
| Y           | the mode of the instruction |
| U           | UNUSED                      |
| RR          | the register identifier     |
| BBBBBBBB    | buffered instruction byte   |

Instructions (XXXX) - Sub-Modes (Y):
| hexID | Description                                    | .pot Command     |
| ----- | ---------------------------------------------- | ---------------- |
| 0x0   | [MOV] (Move)                                   |                  |
|       | MOV: move imm8 into register                   | MOV reg, imm8    |
|       | MVR: move register into register               | MVR reg, reg     |
| 0x1   | [LOD] (Load)                                   |                  |
|       | LDR: load adr16 value into register            | LDR reg, [adr16] |
|       | LDI: load input byte into register             | LDI reg          |
| 0x2   | [STR] (Store)                                  |                  |
|       | STR: store register at ram adr16               | STR reg, [adr16] |
|       | STV: store register at vram adr16              | STV reg, [adr16] |
| 0x3   | [PSH] (StackPush)                              |                  |
|       | PSI: push imm8 to stack                        | PSI imm8         |
|       | PSR: push register to stack                    | PSR reg          |
| 0x4   | POP (StackPop) pop stack value into register   | POP reg          |
| 0x5   |                                                |                  |
| 0x6   |                                                |                  |
| 0x7   |                                                |                  |
| 0x8   |                                                |                  |
| 0x9   |                                                |                  |
| 0xA   | [ADD] (Add)                                    |                  |
|       | ADI: add im8 to register                       | ADI reg, imm8    |
|       | ADA: add adr16 from ram to register            | ADA reg, [adr16] |
| 0xB   | [ADC] (Add with carry)                         |                  |
|       | ACI: with flags add im8 to register            | ACI reg, imm8    |
|       | ACA: with flags add adr16 from ram to register | ACA reg, [adr16] |
| 0xC   | [SUB] (Subtract)                               |                  |
|       | SBI: subtract im8 to register                  | SBI reg, imm8    |
|       | SBA: subtract adr16 from ram to register       | SBA reg, [adr16] |
| 0xD   | [AND] (Logical AND)                            |                  |
|       | NDI: logical and im8 with register             | NDI reg, imm8    |
|       | NDA: logical and adr16 from ram with register  | NDA reg, [adr16] |
| 0xE   | [OR] (Logical OR)                              |                  |
|       | ORI: logical or im8 with register              | ORI reg, imm8    |
|       | ORA: logical or adr16 from ram with register   | ORA reg, [adr16] |
| 0xF   | [XOR] (Logical XOR)                            |                  |
|       | XOI: logical xor im8 with register             | XOI reg, imm8    |
|       | XOA: logical xor adr16 from ram with register  | XOA reg, [adr16] |

Registers (RR):
| regID | Description                 |
| ----- | --------------------------- |
| 00    | A register                  |
| 01    | B register                  |
| 10    | C register                  |
| 11    | D register                  |

## Micro-code assembler

When running the application with <code>-mc</code> it will generate the micro-code binary data in hex format. This code is used to drive the internal control logic of the CPU.

## Code assembler
When running the application with <code>-file=TEST-FILE.pot</code> the passed file will be parsed and converted to binary data in hex format. Whenever there are problems the output displays the reason of the program failing (e.g. FileNotFound, InvalidArguments etc.)