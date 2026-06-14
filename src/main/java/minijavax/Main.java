package minijavax;

import minijavax.ast.ProgramNode;
import minijavax.exceptions.*;
import minijavax.interpreter.Interpreter;
import minijavax.lexer.Lexer;
import minijavax.parser.Parser;
import minijavax.semantic.SemanticAnalyzer;
import minijavax.token.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static boolean traceMode = false;
    private static boolean showTokens = false;
    private static boolean showAST = false;

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("--help")) {
            printUsage();
            return;
        }

        String filename = args[0];
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--trace": traceMode = true; break;
                case "--tokens": showTokens = true; break;
                case "--ast": showAST = true; break;
            }
        }

        String source;
        try {
            source = Files.readString(Paths.get(filename));
        } catch (IOException e) {
            System.err.println("Error: Could not read file '" + filename + "'");
            return;
        }

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("Forge Compiler & Interpreter v1.0.0");
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("Source: " + filename);
        System.out.println("Size: " + source.length() + " characters");
        System.out.println();

        // PHASE 1: Lexical Analysis
        System.out.println("────────────────────────────────────────────────────────────────");
        System.out.println("PHASE 1: LEXICAL ANALYSIS");
        System.out.println("────────────────────────────────────────────────────────────────");

        Lexer lexer = new Lexer(source);
        List<Token> tokens;
        try {
            tokens = lexer.tokenize();
            System.out.println("[Lexer] Generated " + tokens.size() + " tokens");
        } catch (LexerException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (showTokens) {
            System.out.println("\n=== Token Stream ===");
            for (int i = 0; i < tokens.size(); i++) {
                System.out.println("  [" + i + "] " + tokens.get(i));
            }
        }

        // PHASE 2: Parsing
        System.out.println();
        System.out.println("────────────────────────────────────────────────────────────────");
        System.out.println("PHASE 2: SYNTAX ANALYSIS (PARSING)");
        System.out.println("────────────────────────────────────────────────────────────────");

        Parser parser = new Parser(tokens);
        ProgramNode program;
        try {
            program = parser.parse();
            System.out.println("[Parser] AST constructed successfully");
            System.out.println("[Parser] Top-level statements: " + program.getStatements().size());
        } catch (ParserException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (showAST) {
            System.out.println("\n=== Abstract Syntax Tree ===");
            printAST(program, 0);
        }

        // PHASE 3: Semantic Analysis
        System.out.println();
        System.out.println("────────────────────────────────────────────────────────────────");
        System.out.println("PHASE 3: SEMANTIC ANALYSIS");
        System.out.println("────────────────────────────────────────────────────────────────");

        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        boolean semanticOk = analyzer.analyze(program);
        if (semanticOk) {
            System.out.println("[Semantic] No semantic errors found");
        } else {
            System.out.println("[Semantic] Semantic analysis failed");
            return;
        }

        // PHASE 4: Execution
        System.out.println();
        System.out.println("────────────────────────────────────────────────────────────────");
        System.out.println("PHASE 4: EXECUTION");
        System.out.println("────────────────────────────────────────────────────────────────");

        if (traceMode) System.out.println("[Interpreter] Trace mode enabled\n");

        Interpreter interpreter = new Interpreter();
        interpreter.setTraceMode(traceMode);
        try {
            interpreter.interpret(program);
        } catch (RuntimeErrorException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println();
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("Execution completed successfully");
        System.out.println("════════════════════════════════════════════════════════════════");
    }

    private static void printUsage() {
        System.out.println("Forge - A Java-like language engine");
        System.out.println();
        System.out.println("Usage: forge <source-file.mjx> [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --trace    Enable detailed execution tracing");
        System.out.println("  --tokens   Print the token stream");
        System.out.println("  --ast      Print the AST structure");
        System.out.println("  --help     Show this help message");
    }

    private static void printAST(Object node, int indent) {
        String prefix = "  ".repeat(indent);
        if (node == null) {
            System.out.println(prefix + "null");
            return;
        }
        System.out.println(prefix + node.toString());
    }
}
