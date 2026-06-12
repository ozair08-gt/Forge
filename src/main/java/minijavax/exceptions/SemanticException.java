package minijavax.exceptions;

public class SemanticException extends MiniJavaXException {
    public SemanticException(String message, int line, int column) {
        super("[Semantic Error] " + message, line, column);
    }

    public static SemanticException undefinedVariable(String name, int line, int column) {
        return new SemanticException("Undefined variable: '" + name + "'", line, column);
    }

    public static SemanticException duplicateVariable(String name, int line, int column) {
        return new SemanticException("Variable '" + name + "' is already declared in this scope", line, column);
    }

    public static SemanticException undefinedFunction(String name, int line, int column) {
        return new SemanticException("Undefined function: '" + name + "'", line, column);
    }

    public static SemanticException argumentCountMismatch(String functionName, int expected, int actual, int line, int column) {
        return new SemanticException("Function '" + functionName + "' expects " + expected + " argument(s) but " + actual + " were provided", line, column);
    }
}
