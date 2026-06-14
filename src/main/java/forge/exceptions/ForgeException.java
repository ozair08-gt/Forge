package forge.exceptions;

public abstract class ForgeException extends RuntimeException {
    private final int line;
    private final int column;

    public ForgeException(String message, int line, int column) {
        super(formatMessage(message, line, column));
        this.line = line;
        this.column = column;
    }

    public ForgeException(String message) {
        super(message);
        this.line = -1;
        this.column = -1;
    }

    private static String formatMessage(String message, int line, int column) {
        return String.format("[Line %d, Column %d] %s", line, column, message);
    }

    public int getLine() { return line; }
    public int getColumn() { return column; }
}
