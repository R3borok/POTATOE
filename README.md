# POTATOE-1

Basic micro-code generation and code-assembler for POTATOE-1 CPU.

## Micro-code assembler

When running the application with <code>-mc</code> it will generate the micro-code binary data in hex format. This code is used to drive the internal control logic of the CPU.

## Code assembler
When running the application with <code>-file=TEST-FILE.pot</code> the passed file will be parsed and converted to binary data in hex format. Whenever there are problems the output should display the reason of the program failing (e.g. FileNotFound, InvalidArguments etc.)