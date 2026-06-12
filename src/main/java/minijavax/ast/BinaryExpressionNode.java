package minijavax.ast;

import minijavax.token.TokenType;

public class BinaryExpressionNode extends AbstractASTNode {
    private final ASTNode left;
    private final TokenType operator;
    private final ASTNode right;

    public BinaryExpressionNode(ASTNode left, TokenType operator, ASTNode right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ASTNode getLeft() { return left; }
    public TokenType getOperator() { return operator; }
    public ASTNode getRight() { return right; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitBinaryExpression(this); }

    public String toString() { return "BinaryExpr(" + operator.getLexeme() + ")"; }
}
