package forge.ast;

import java.util.List;

public class ProgramNode extends AbstractASTNode {
    private final List<ASTNode> statements;

    public ProgramNode(List<ASTNode> statements, int line, int column) {
        super(line, column);
        this.statements = statements;
    }

    public List<ASTNode> getStatements() { return statements; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitProgram(this); }

    public String toString() { return "Program(statements=" + statements.size() + ")"; }
}
