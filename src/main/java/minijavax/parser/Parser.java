package minijavax.parser;

import minijavax.ast.*;
import minijavax.exceptions.ParserException;
import minijavax.token.Token;
import minijavax.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        List<ASTNode> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return new ProgramNode(statements, 1, 1);
    }

    private ASTNode declaration() {
        if (match(TokenType.INT)) return intDeclaration();
        return statement();
    }

    private ASTNode intDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expected variable name");
        if (match(TokenType.LEFT_PAREN)) return functionDeclaration(name);
        consume(TokenType.EQUAL, "Expected '=' after variable name");
        ASTNode initializer = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after variable declaration");
        return new VariableDeclarationNode(name.getLexeme(), initializer, name.getLine(), name.getColumn());
    }

    private ASTNode functionDeclaration(Token name) {
        List<String> parameters = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                consume(TokenType.INT, "Expected parameter type 'int'");
                Token param = consume(TokenType.IDENTIFIER, "Expected parameter name");
                parameters.add(param.getLexeme());
            } while (match(TokenType.COMMA));
        }
        consume(TokenType.RIGHT_PAREN, "Expected ')' after parameters");
        ASTNode body = block();
        return new FunctionDeclarationNode(name.getLexeme(), parameters, body, name.getLine(), name.getColumn());
    }

    private ASTNode statement() {
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.IF)) return ifStatement();
        if (match(TokenType.WHILE)) return whileStatement();
        if (match(TokenType.RETURN)) return returnStatement();
        if (match(TokenType.LEFT_BRACE)) return block();
        return expressionStatement();
    }

    private ASTNode printStatement() {
        consume(TokenType.LEFT_PAREN, "Expected '(' after 'print'");
        ASTNode value = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after print argument");
        Token semi = consume(TokenType.SEMICOLON, "Expected ';' after print statement");
        return new PrintNode(value, semi.getLine(), semi.getColumn());
    }

    private ASTNode ifStatement() {
        consume(TokenType.LEFT_PAREN, "Expected '(' after 'if'");
        ASTNode condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after if condition");
        ASTNode thenBranch = statement();
        ASTNode elseBranch = null;
        if (match(TokenType.ELSE)) elseBranch = statement();
        return new IfNode(condition, thenBranch, elseBranch, peek().getLine(), peek().getColumn());
    }

    private ASTNode whileStatement() {
        consume(TokenType.LEFT_PAREN, "Expected '(' after 'while'");
        ASTNode condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after while condition");
        ASTNode body = statement();
        return new WhileNode(condition, body, peek().getLine(), peek().getColumn());
    }

    private ASTNode returnStatement() {
        Token keyword = previous();
        ASTNode value = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after return value");
        return new ReturnNode(value, keyword.getLine(), keyword.getColumn());
    }

    private BlockNode block() {
        Token brace = previous();
        List<ASTNode> statements = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(TokenType.RIGHT_BRACE, "Expected '}' after block");
        return new BlockNode(statements, brace.getLine(), brace.getColumn());
    }

    private ASTNode expressionStatement() {
        ASTNode expr = expression();
        Token semi = consume(TokenType.SEMICOLON, "Expected ';' after expression");
        return new ExpressionStatementNode(expr, semi.getLine(), semi.getColumn());
    }

    private ASTNode expression() { return assignment(); }

    private ASTNode assignment() {
        ASTNode expr = equality();
        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            if (expr instanceof IdentifierNode) {
                ASTNode value = assignment();
                return new AssignmentNode(((IdentifierNode) expr).getName(), value, equals.getLine(), equals.getColumn());
            }
            throw new ParserException("Invalid assignment target", equals);
        }
        return expr;
    }

    private ASTNode equality() {
        ASTNode expr = comparison();
        while (match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token op = previous();
            ASTNode right = comparison();
            expr = new BinaryExpressionNode(expr, op.getType(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private ASTNode comparison() {
        ASTNode expr = term();
        while (match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            Token op = previous();
            ASTNode right = term();
            expr = new BinaryExpressionNode(expr, op.getType(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private ASTNode term() {
        ASTNode expr = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token op = previous();
            ASTNode right = factor();
            expr = new BinaryExpressionNode(expr, op.getType(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private ASTNode factor() {
        ASTNode expr = unary();
        while (match(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
            Token op = previous();
            ASTNode right = unary();
            expr = new BinaryExpressionNode(expr, op.getType(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private ASTNode unary() {
        if (match(TokenType.MINUS)) {
            Token op = previous();
            ASTNode right = unary();
            return new UnaryExpressionNode(op.getType(), right, op.getLine(), op.getColumn());
        }
        return call();
    }

    private ASTNode call() {
        ASTNode expr = primary();
        if (expr instanceof IdentifierNode && match(TokenType.LEFT_PAREN)) {
            String callee = ((IdentifierNode) expr).getName();
            List<ASTNode> arguments = new ArrayList<>();
            if (!check(TokenType.RIGHT_PAREN)) {
                do { arguments.add(expression()); } while (match(TokenType.COMMA));
            }
            consume(TokenType.RIGHT_PAREN, "Expected ')' after arguments");
            return new CallExpressionNode(callee, arguments, expr.getLine(), expr.getColumn());
        }
        return expr;
    }

    private ASTNode primary() {
        if (match(TokenType.NUMBER)) return new LiteralNode(previous().getLiteral(), previous().getLine(), previous().getColumn());
        if (match(TokenType.TRUE)) return new LiteralNode(true, previous().getLine(), previous().getColumn());
        if (match(TokenType.FALSE)) return new LiteralNode(false, previous().getLine(), previous().getColumn());
        if (match(TokenType.IDENTIFIER)) return new IdentifierNode(previous().getLexeme(), previous().getLine(), previous().getColumn());
        if (match(TokenType.LEFT_PAREN)) {
            ASTNode expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expected ')' after expression");
            return expr;
        }
        throw new ParserException("Unexpected token: " + peek().getType().name(), peek().getLine(), peek().getColumn());
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new ParserException(message + ", got '" + peek().getLexeme() + "'", peek().getLine(), peek().getColumn());
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) { advance(); return true; }
        }
        return false;
    }

    private boolean check(TokenType type) { return !isAtEnd() && peek().getType() == type; }
    private Token advance() { if (!isAtEnd()) current++; return previous(); }
    private boolean isAtEnd() { return peek().getType() == TokenType.EOF; }
    private Token peek() { return tokens.get(current); }
    private Token previous() { return tokens.get(current - 1); }
}
