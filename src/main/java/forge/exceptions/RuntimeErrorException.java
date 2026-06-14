package forge.exceptions;

public class RuntimeErrorException extends ForgeException {
    public RuntimeErrorException(String message, int line, int column) {
        super("[Runtime Error] " + message, line, column);
    }

    public static RuntimeErrorException divisionByZero(int line, int column) {
        return new RuntimeErrorException("Division by zero", line, column);
    }

    public static RuntimeErrorException undefinedVariable(String name, int line, int column) {
        return new RuntimeErrorException("Undefined variable: '" + name + "'", line, column);
    }

    public static RuntimeErrorException notAFunction(String name, int line, int column) {
        return new RuntimeErrorException("'" + name + "' is not a function", line, column);
    }
}
