package forge.ast;

public interface ASTNode {
    <T> T accept(ASTVisitor<T> visitor);
    int getLine();
    int getColumn();
}
