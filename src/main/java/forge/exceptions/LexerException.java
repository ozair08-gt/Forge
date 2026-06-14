package forge.exceptions;

public class LexerException extends ForgeException {
    public LexerException(String message, int line, int column) {
        super("[Lexer Error] " + message, line, column);
    }
}
