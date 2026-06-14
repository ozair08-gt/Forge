package forge.ast;

public class AssignmentNode extends AbstractASTNode {
    private final String name;
    private final ASTNode value;

    public AssignmentNode(String name, ASTNode value, int line, int column) {
        super(line, column);
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public ASTNode getValue() { return value; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitAssignment(this); }

    public String toString() { return "Assignment(name=" + name + ")"; }
}
