package minijavax.ast;

public class PrintNode extends AbstractASTNode {
    private final ASTNode expression;

    public PrintNode(ASTNode expression, int line, int column) {
        super(line, column);
        this.expression = expression;
    }

    public ASTNode getExpression() { return expression; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitPrint(this); }

    public String toString() { return "Print"; }
}
