package minijavax.ast;

public class IdentifierNode extends AbstractASTNode {
    private final String name;

    public IdentifierNode(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    public String getName() { return name; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitIdentifier(this); }

    public String toString() { return "Identifier(" + name + ")"; }
}
