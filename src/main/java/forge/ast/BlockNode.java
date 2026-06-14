package forge.ast;

import java.util.List;

public class BlockNode extends AbstractASTNode {
    private final List<ASTNode> statements;

    public BlockNode(List<ASTNode> statements, int line, int column) {
        super(line, column);
        this.statements = statements;
    }

    public List<ASTNode> getStatements() { return statements; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitBlock(this); }

    public String toString() { return "Block(statements=" + statements.size() + ")"; }
}
