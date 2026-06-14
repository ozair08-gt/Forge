package forge.ast;

import java.util.List;

public class CallExpressionNode extends AbstractASTNode {
    private final String callee;
    private final List<ASTNode> arguments;

    public CallExpressionNode(String callee, List<ASTNode> arguments, int line, int column) {
        super(line, column);
        this.callee = callee;
        this.arguments = arguments;
    }

    public String getCallee() { return callee; }
    public List<ASTNode> getArguments() { return arguments; }
    public int getArgumentCount() { return arguments.size(); }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitCallExpression(this); }

    public String toString() { return "Call(" + callee + ", args=" + arguments.size() + ")"; }
}
