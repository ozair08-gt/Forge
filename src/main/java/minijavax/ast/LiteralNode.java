package minijavax.ast;

public class LiteralNode extends AbstractASTNode {
    private final Object value;

    public LiteralNode(Object value, int line, int column) {
        super(line, column);
        this.value = value;
    }

    public Object getValue() { return value; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitLiteral(this); }

    public String toString() { return "Literal(" + value + ")"; }
}
