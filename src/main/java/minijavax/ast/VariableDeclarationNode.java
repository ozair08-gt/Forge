package minijavax.ast;

public class VariableDeclarationNode extends AbstractASTNode {
    private final String name;
    private final ASTNode initializer;

    public VariableDeclarationNode(String name, ASTNode initializer, int line, int column) {
        super(line, column);
        this.name = name;
        this.initializer = initializer;
    }

    public String getName() { return name; }
    public ASTNode getInitializer() { return initializer; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitVariableDeclaration(this); }

    public String toString() { return "VariableDeclaration(name=" + name + ")"; }
}
