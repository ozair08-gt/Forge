# MiniJavaX - Java-like Language Engine

A complete compiler/interpreter implementation demonstrating core compiler fundamentals.

## Quick Start

```bash
# Build
mvn clean package

# Run examples
java -jar target/minijavax-1.0.0.jar examples/example1.mjx
java -jar target/minijavax-1.0.0.jar examples/example3.mjx --trace
```

## Project Structure

```
src/main/java/minijavax/
├── Main.java              # CLI entry point
├── lexer/
│   └── Lexer.java         # Lexical analysis (tokenization)
├── parser/
│   └── Parser.java        # Recursive descent parser
├── ast/                   # Abstract Syntax Tree (19 nodes)
│   ├── ASTNode.java       # Node interface
│   ├── ASTVisitor.java    # Visitor pattern
│   ├── ProgramNode.java
│   ├── BinaryExpressionNode.java
│   ├── FunctionDeclarationNode.java
│   └── ... (15 more nodes)
├── semantic/
│   ├── SemanticAnalyzer.java  # Semantic validation
│   ├── Symbol.java        # Symbol table entry
│   ├── Scope.java         # Scope management
│   └── FunctionSymbol.java
├── interpreter/
│   ├── Interpreter.java   # AST-walking interpreter
│   └── ReturnValue.java   # Return control flow
├── token/
│   ├── TokenType.java     # Token type enum
│   └── Token.java         # Token class
└── exceptions/            # Error handling (5 exceptions)
```

## Language Features

- Variables: `int x = 5;`
- Arithmetic: `+ - * / %`
- Comparisons: `== != < <= > >=`
- Control flow: `if/else`, `while`
- Functions with recursion
- Nested scopes with shadowing

## CLI Options

```
--trace    Enable detailed execution tracing
--tokens   Print the token stream
--ast      Print the AST structure
--help     Show help
```

## Example Output (expected)

```
════════════════════════════════════════════════════════════════
MiniJavaX Language Engine v1.0.0
════════════════════════════════════════════════════════════════

Source: examples/example3.mjx
Size: 450 characters

────────────────────────────────────────────────────────────────
PHASE 1: LEXICAL ANALYSIS
────────────────────────────────────────────────────────────────
[Lexer] Generated 86 tokens

────────────────────────────────────────────────────────────────
PHASE 2: SYNTAX ANALYSIS (PARSING)
────────────────────────────────────────────────────────────────
[Parser] AST constructed successfully
[Parser] Top-level statements: 11

────────────────────────────────────────────────────────────────
PHASE 3: SEMANTIC ANALYSIS
────────────────────────────────────────────────────────────────
[Semantic] No semantic errors found

────────────────────────────────────────────────────────────────
PHASE 4: EXECUTION
────────────────────────────────────────────────────────────────
42
8
300
25
49
25
5
10
120
55

════════════════════════════════════════════════════════════════
Execution completed successfully
════════════════════════════════════════════════════════════════
```

## Compiler Concepts Demonstrated

1. **Lexical Analysis**: Tokenization, keyword recognition, position tracking
2. **Recursive Descent Parsing**: Operator precedence, grammar rules
3. **AST Design**: 19 node types, visitor pattern
4. **Semantic Analysis**: Symbol tables, scope management, validation
5. **Interpretation**: Tree-walking, scope stacks, function calls

## Build Requirements

- Java 11+
- Maven 3.6+
