package minijavax.exceptions;

import minijavax.token.Token;
import minijavax.token.TokenType;

public class ParserException extends MiniJavaXException {
    public ParserException(String message, int line, int column) {
        super("[Parser Error] " + message, line, column);
    }

    public ParserException(String message, Token token) {
        super("[Parser Error] " + message + " at '" + token.getLexeme() + "'", token.getLine(), token.getColumn());
    }

    public static ParserException unexpectedToken(Token found, TokenType expected) {
        return new ParserException("Expected " + expected.name() + " but found " + found.getType().name(), found);
    }
}
