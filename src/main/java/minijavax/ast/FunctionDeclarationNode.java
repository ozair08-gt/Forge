package minijavax.ast;

import java.util.List;

public class FunctionDeclarationNode extends AbstractASTNode {
    private final String name;
    private final List<String> parameters;
    private final ASTNode body;

    public FunctionDeclarationNode(String name, List<String> parameters, ASTNode body, int line, int column) {
        super(line, column);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() { return name; }
    public List<String> getParameters() { return parameters; }
    public int getArity() { return parameters.size(); }
    public ASTNode getBody() { return body; }

    public <T> T accept(ASTVisitor<T> visitor) { return visitor.visitFunctionDeclaration(this); }

    public String toString() { return "Function(name=" + name + ", params=" + parameters.size() + ")"; }
}
