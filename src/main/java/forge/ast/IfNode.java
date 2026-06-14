package forge.ast;

public class IfNode extends AbstractASTNode {
    private final ASTNode condition;
    private final ASTNode thenBranch;
    private final ASTNode elseBranch;

    public IfNode(ASTNode condition, ASTNode thenBranch, ASTNode elseBranch, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ASTNode getCondition() { return condition; }
    public ASTNode getThenBranch() { return thenBranch; }
    public boolean hasElseBranch() { return elseBranch != null; }
    public ASTNode getElseBranch() { return elseBranch; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitIf(this); }

    public String toString() { return "If" + (hasElseBranch() ? "(with else)" : ""); }
}
