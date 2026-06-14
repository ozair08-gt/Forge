package forge.ast;

public class ReturnNode extends AbstractASTNode {
    private final ASTNode value;

    public ReturnNode(ASTNode value, int line, int column) {
        super(line, column);
        this.value = value;
    }

    public ASTNode getValue() { return value; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitReturn(this); }

    public String toString() { return "Return"; }
}
