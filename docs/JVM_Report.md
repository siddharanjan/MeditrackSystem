# JVM Report

Quick notes from reading up on how the JVM works, while building MediTrack.

## JDK vs JRE vs JVM

- **JVM** - runs the bytecode. A spec, HotSpot is the common implementation.
- **JRE** - JVM + standard libraries, enough to run a compiled program.
- **JDK** - JRE + dev tools (`javac`, `javadoc`, etc). What you install to
  actually write Java.

JDK contains JRE contains JVM, basically.

## Class Loader

Loads `.class` files before anything can run:

1. **Loading** - reads the bytecode in
2. **Linking** - verify (bytecode isn't malformed), prepare (default values
   for static fields), resolve (symbolic refs -> real ones)
3. **Initialization** - runs static blocks / static field initializers,
   top to bottom, once

This is why the static block in `Constants` (creates the `data/` folder) is
guaranteed to run before anything else uses that class.

## Runtime Data Areas

- **Heap** - every object (`new Patient(...)` etc) lives here, shared
  across threads, garbage collected
- **Stack** - one per thread, holds local variables/method calls. Deep
  recursion blows this up (`StackOverflowError`)
- **Method Area** - class-level stuff: bytecode, static variables (aka
  Metaspace these days)
- **PC Register** - per thread, tracks which instruction is executing

## Execution Engine

- **Interpreter** - runs bytecode line by line, starts fast, slow if
  called a lot
- **JIT Compiler** - compiles "hot" code (loops, frequently called
  methods) to native machine code so it skips the interpreter next time

JVM starts interpreting, then JIT-compiles the hot paths as it goes.

## Write Once, Run Anywhere

`javac` compiles to bytecode, not machine code for a specific CPU. Same
`.class` file works on any OS. The JVM itself is platform-specific (a
different build per OS/arch) and handles turning that bytecode into
instructions the actual machine understands. So the "translation" step just
moves from compile time to run time.
