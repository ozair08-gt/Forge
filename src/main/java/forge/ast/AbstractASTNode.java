package forge.ast;

public abstract class AbstractASTNode implements ASTNode {
    protected final int line;
    protected final int column;

    protected AbstractASTNode(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() { return line; }
    public int getColumn() { return column; }
}
