package minijavax.exceptions;

public class LexerException extends MiniJavaXException {
    public LexerException(String message, int line, int column) {
        super("[Lexer Error] " + message, line, column);
    }
}
