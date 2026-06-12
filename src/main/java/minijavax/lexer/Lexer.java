package minijavax.lexer;

import minijavax.exceptions.LexerException;
import minijavax.token.Token;
import minijavax.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final String source;
    private final List<Token> tokens;
    private final Map<String, TokenType> keywords;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;

    public Lexer(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.keywords = new HashMap<>();
        keywords.put("int", TokenType.INT);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("return", TokenType.RETURN);
        keywords.put("print", TokenType.PRINT);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
    }

    public List<Token> tokenize() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line, column));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case ' ': case '\r': case '\t': break;
            case '\n': line++; column = 1; break;
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case ',': addToken(TokenType.COMMA); break;
            case '+': addToken(TokenType.PLUS); break;
            case '-': addToken(TokenType.MINUS); break;
            case '*': addToken(TokenType.STAR); break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case '%': addToken(TokenType.PERCENT); break;
            case '=':
                if (match('=')) addToken(TokenType.EQUAL_EQUAL);
                else addToken(TokenType.EQUAL);
                break;
            case '!':
                if (match('=')) addToken(TokenType.BANG_EQUAL);
                else throw error("Unexpected '!'");
                break;
            case '<':
                if (match('=')) addToken(TokenType.LESS_EQUAL);
                else addToken(TokenType.LESS);
                break;
            case '>':
                if (match('=')) addToken(TokenType.GREATER_EQUAL);
                else addToken(TokenType.GREATER);
                break;
            default:
                if (isDigit(c)) number();
                else if (isAlpha(c)) identifier();
                else throw error("Unexpected character: '" + c + "'");
        }
    }

    private void number() {
        while (isDigit(peek())) advance();
        String value = source.substring(start, current);
        addToken(TokenType.NUMBER, Integer.parseInt(value));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        if (type == TokenType.TRUE) addToken(type, true);
        else if (type == TokenType.FALSE) addToken(type, false);
        else addToken(type);
    }

    private boolean isAtEnd() { return current >= source.length(); }
    private char peek() { return isAtEnd() ? '\0' : source.charAt(current); }
    private char advance() { char c = source.charAt(current++); if (c != '\n') column++; return c; }
    private boolean match(char expected) {
        if (isAtEnd() || source.charAt(current) != expected) return false;
        current++;
        return true;
    }
    private boolean isDigit(char c) { return c >= '0' && c <= '9'; }
    private boolean isAlpha(char c) { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'; }
    private boolean isAlphaNumeric(char c) { return isAlpha(c) || isDigit(c); }

    private void addToken(TokenType type) { addToken(type, null); }
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        int tokenColumn = column - (current - start);
        tokens.add(new Token(type, text, literal, line, tokenColumn));
    }

    private LexerException error(String message) { return new LexerException(message, line, column); }
}
