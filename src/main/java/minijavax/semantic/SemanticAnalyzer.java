package minijavax.semantic;

import minijavax.ast.*;
import minijavax.exceptions.SemanticException;

import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer implements ASTVisitor<Void> {
    private Scope currentScope;
    private final Map<String, FunctionSymbol> functions = new HashMap<>();
    private boolean hasErrors = false;

    public boolean analyze(ProgramNode program) {
        currentScope = new Scope("global", null);
        collectFunctions(program);
        try {
            program.accept(this);
        } catch (SemanticException e) {
            hasErrors = true;
            System.err.println(e.getMessage());
        }
        return !hasErrors;
    }

    private void collectFunctions(ProgramNode program) {
        for (ASTNode stmt : program.getStatements()) {
            if (stmt instanceof FunctionDeclarationNode) {
                FunctionDeclarationNode func = (FunctionDeclarationNode) stmt;
                functions.put(func.getName(), new FunctionSymbol(func.getName(), func.getArity(), func.getLine()));
            }
        }
    }

    private void enterScope(String name) {
        currentScope = new Scope(name, currentScope);
    }

    private void exitScope() {
        currentScope = currentScope.getEnclosing();
    }

    public Void visitProgram(ProgramNode node) {
        for (ASTNode stmt : node.getStatements()) stmt.accept(this);
        return null;
    }

    public Void visitVariableDeclaration(VariableDeclarationNode node) {
        if (currentScope.resolve(node.getName()) != null && currentScope.resolve(node.getName()).getType().equals("variable")) {
            throw SemanticException.duplicateVariable(node.getName(), node.getLine(), node.getColumn());
        }
        node.getInitializer().accept(this);
        currentScope.define(new Symbol(node.getName(), "variable", node.getLine()));
        return null;
    }

    public Void visitAssignment(AssignmentNode node) {
        Symbol symbol = currentScope.resolve(node.getName());
        if (symbol == null) {
            throw SemanticException.undefinedVariable(node.getName(), node.getLine(), node.getColumn());
        }
        node.getValue().accept(this);
        return null;
    }

    public Void visitIf(IfNode node) {
        node.getCondition().accept(this);
        enterScope("if-then");
        node.getThenBranch().accept(this);
        exitScope();
        if (node.hasElseBranch()) {
            enterScope("if-else");
            node.getElseBranch().accept(this);
            exitScope();
        }
        return null;
    }

    public Void visitWhile(WhileNode node) {
        node.getCondition().accept(this);
        enterScope("while-body");
        node.getBody().accept(this);
        exitScope();
        return null;
    }

    public Void visitFunctionDeclaration(FunctionDeclarationNode node) {
        enterScope("function:" + node.getName());
        for (String param : node.getParameters()) {
            currentScope.define(new Symbol(param, "variable", node.getLine()));
        }
        node.getBody().accept(this);
        exitScope();
        return null;
    }

    public Void visitReturn(ReturnNode node) {
        node.getValue().accept(this);
        return null;
    }

    public Void visitPrint(PrintNode node) {
        node.getExpression().accept(this);
        return null;
    }

    public Void visitBlock(BlockNode node) {
        enterScope("block");
        for (ASTNode stmt : node.getStatements()) stmt.accept(this);
        exitScope();
        return null;
    }

    public Void visitBinaryExpression(BinaryExpressionNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        return null;
    }

    public Void visitUnaryExpression(UnaryExpressionNode node) {
        node.getOperand().accept(this);
        return null;
    }

    public Void visitCallExpression(CallExpressionNode node) {
        FunctionSymbol func = functions.get(node.getCallee());
        if (func == null) {
            throw SemanticException.undefinedFunction(node.getCallee(), node.getLine(), node.getColumn());
        }
        if (func.getArity() != node.getArgumentCount()) {
            throw SemanticException.argumentCountMismatch(node.getCallee(), func.getArity(), node.getArgumentCount(), node.getLine(), node.getColumn());
        }
        for (ASTNode arg : node.getArguments()) arg.accept(this);
        return null;
    }

    public Void visitLiteral(LiteralNode node) { return null; }

    public Void visitIdentifier(IdentifierNode node) {
        if (currentScope.resolve(node.getName()) == null) {
            throw SemanticException.undefinedVariable(node.getName(), node.getLine(), node.getColumn());
        }
        return null;
    }

    public Void visitExpressionStatement(ExpressionStatementNode node) {
        node.getExpression().accept(this);
        return null;
    }
}
