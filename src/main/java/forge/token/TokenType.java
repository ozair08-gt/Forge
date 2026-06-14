package forge.token;

public enum TokenType {
    // Keywords
    INT("int"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    RETURN("return"),
    PRINT("print"),
    TRUE("true"),
    FALSE("false"),

    // Literals
    NUMBER(null),
    IDENTIFIER(null),

    // Operators
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    PERCENT("%"),
    EQUAL("="),
    EQUAL_EQUAL("=="),
    BANG_EQUAL("!="),
    LESS("<"),
    LESS_EQUAL("<="),
    GREATER(">"),
    GREATER_EQUAL(">="),

    // Punctuation
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    SEMICOLON(";"),
    COMMA(","),

    // Special
    EOF("");

    private final String lexeme;

    TokenType(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public boolean isArithmeticOperator() {
        return this == PLUS || this == MINUS || this == STAR || this == SLASH || this == PERCENT;
    }

    public boolean isComparisonOperator() {
        return this == EQUAL_EQUAL || this == BANG_EQUAL || this == LESS || this == LESS_EQUAL || this == GREATER || this == GREATER_EQUAL;
    }
}
