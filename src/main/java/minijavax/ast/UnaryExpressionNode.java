package minijavax.ast;

import minijavax.token.TokenType;

public class UnaryExpressionNode extends AbstractASTNode {
    private final TokenType operator;
    private final ASTNode operand;

    public UnaryExpressionNode(TokenType operator, ASTNode operand, int line, int column) {
        super(line, column);
        this.operator = operator;
        this.operand = operand;
    }

    public TokenType getOperator() { return operator; }
    public ASTNode getOperand() { return operand; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitUnaryExpression(this); }

    public String toString() { return "UnaryExpr(" + operator.getLexeme() + ")"; }
}
