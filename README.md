# POTATOE-1

POTATOE-1 CPU documentation for architecture and binary machine main.code structure and generation/assembly.

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

<code>XXXXYURR BBBBBBBB [BBBBBBBBB]</code>

| bitID       | Description                 |
| ----------- | --------------------------- |
| XXXX        | the instruction identifier  |
| Y           | the mode of the instruction |
| U           | UNUSED                      |
| RR          | the register identifier     |
| BBBBBBBB    | buffered instruction byte   |

Instructions (XXXX) - Sub-Modes (Y):
(* updates the flags registers)

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
| 0x4   | POP (StackPop): pop stack value into register  | POP reg          |
| 0x5   | JMP (Jump): jump to ram adr16                  | JMP [adr16]      |
| 0x6   |                                                |                  |
| 0x7   |                                                |                  |
| 0x8   |                                                |                  |
| 0x9   | [ADD] (Add) *                                  |                  |
|       | ADI: add im8 to register                       | ADI reg, imm8    |
|       | ADA: add adr16 from ram to register            | ADA reg, [adr16] |
| 0xA   | [ADC] (Add with carry) *                       |                  |
|       | ACI: with flags add im8 to register            | ACI reg, imm8    |
|       | ACA: with flags add adr16 from ram to register | ACA reg, [adr16] |
| 0xB   | [SUB] (Subtract) *                             |                  |
|       | SBI: subtract im8 to register                  | SBI reg, imm8    |
|       | SBA: subtract adr16 from ram to register       | SBA reg, [adr16] |
| 0xC   | [CMP] (Compare) *                              |                  |
|       | CPI: compare imm8 to register                  | CPI reg, imm8    |
|       | CPR: compare register to register              | CPR reg, reg     |
| 0xD   | [AND] (Logical AND)                            |                  |
|       | NDI: logical and im8 with register             | NDI reg, imm8    |
|       | NDA: logical and adr16 from ram with register  | NDA reg, [adr16] |
| 0xE   | [OR] (Logical OR)                              |                  |
|       | ORI: logical or im8 with register              | ORI reg, imm8    |
|       | ORA: logical or adr16 from ram with register   | ORA reg, [adr16] |
| 0xF   | [XOR] (Logical XOR)                            |                  |
|       | XOI: logical xor im8 with register             | XOI reg, imm8    |
|       | XOA: logical xor adr16 from ram with register  | XOA reg, [adr16] |

### Registers

| regID | Description                 |
| ----- | --------------------------- |
| 00    | A register                  |
| 01    | B register                  |
| 10    | C register                  |
| 11    | D register                  |

## Micro-main.code assembler

When running the application with <code>-mc</code> it will generate the micro-main.code binary data in hex format. 
This main.code is used to drive the internal control logic of the CPU.

### Control-word

The control word is a 32 bit (4 byte) long sequence with the following format:

<code>HBBBBBBB BBBBBBBB BBBBBBBBB YYYAXRRE</code>

| bitID       | Description                 |
| ----------- | --------------------------- |
| H           | clock halt signal           |
| E           | register enable             |
| RR          | register identifier         |
|             | [register](#registers)   |
| X           | register operation          |
|             | 0: load                     |
|             | 1: out                      |
| A           | alu enable                  |
| YYY         | alu operation               |
|             | 0: add                      |
|             | 1: add with carry           |
|             | 2: subtract                 |
|             | 3: logical and              |
|             | 4: logical or               |
|             | 5: logical xor              |
|             | 6: -                        |
|             | 7: -                        |
|             |                             |
|             |                             |

## Code assembler

When running the application with <code>-file=TEST-FILE.pot</code> the passed file will be parsed and converted to binary data in hex format.<br>
Whenever the passed file can not be assembled the output should be structured in the following format:

<code>ERROR|WARN (line:column): error-description</code>

Available commands can be found under section [instructions](#instructions).

