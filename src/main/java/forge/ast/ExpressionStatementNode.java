package forge.ast;

public class ExpressionStatementNode extends AbstractASTNode {
    private final ASTNode expression;

    public ExpressionStatementNode(ASTNode expression, int line, int column) {
        super(line, column);
        this.expression = expression;
    }

    public ASTNode getExpression() { return expression; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitExpressionStatement(this); }

    public String toString() { return "ExpressionStmt"; }
}
