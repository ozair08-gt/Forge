package minijavax.ast;

public class WhileNode extends AbstractASTNode {
    private final ASTNode condition;
    private final ASTNode body;

    public WhileNode(ASTNode condition, ASTNode body, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() { return condition; }
    public ASTNode getBody() { return body; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitWhile(this); }

    public String toString() { return "While"; }
}
