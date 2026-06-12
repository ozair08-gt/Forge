package minijavax.interpreter;

import minijavax.ast.*;
import minijavax.exceptions.RuntimeErrorException;
import minijavax.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements ASTVisitor<Object> {
    private final Map<String, FunctionDeclarationNode> functions = new HashMap<>();
    private final List<Map<String, Object>> scopes = new ArrayList<>();
    private boolean traceMode = false;
    private int indentLevel = 0;

    public void setTraceMode(boolean traceMode) { this.traceMode = traceMode; }
    public boolean isTraceMode() { return traceMode; }

    public void interpret(ProgramNode program) {
        for (ASTNode stmt : program.getStatements()) {
            if (stmt instanceof FunctionDeclarationNode) {
                FunctionDeclarationNode func = (FunctionDeclarationNode) stmt;
                functions.put(func.getName(), func);
            }
        }
        scopes.add(new HashMap<>());
        try {
            for (ASTNode stmt : program.getStatements()) {
                if (!(stmt instanceof FunctionDeclarationNode)) {
                    stmt.accept(this);
                }
            }
        } finally {
            scopes.remove(scopes.size() - 1);
        }
    }

    private void pushScope() { scopes.add(new HashMap<>()); }
    private void popScope() { scopes.remove(scopes.size() - 1); }
    private void define(String name, Object value) { scopes.get(scopes.size() - 1).put(name, value); }

    private Object getVariable(String name, int line, int column) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Map<String, Object> scope = scopes.get(i);
            if (scope.containsKey(name)) return scope.get(name);
        }
        throw RuntimeErrorException.undefinedVariable(name, line, column);
    }

    private void assign(String name, Object value, int line, int column) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Map<String, Object> scope = scopes.get(i);
            if (scope.containsKey(name)) { scope.put(name, value); return; }
        }
        throw RuntimeErrorException.undefinedVariable(name, line, column);
    }

    private void trace(String message) {
        if (!traceMode) return;
        String indent = "  ".repeat(indentLevel);
        System.out.println("[Interpreter] " + indent + message);
    }

    private String valueString(Object value) { return value == null ? "null" : value.toString(); }

    public Object visitProgram(ProgramNode node) {
        for (ASTNode stmt : node.getStatements()) stmt.accept(this);
        return null;
    }

    public Object visitVariableDeclaration(VariableDeclarationNode node) {
        trace("Declaring variable: " + node.getName());
        Object value = node.getInitializer().accept(this);
        trace("  Initial value: " + valueString(value));
        define(node.getName(), value);
        return null;
    }

    public Object visitAssignment(AssignmentNode node) {
        trace("Assigning to variable: " + node.getName());
        Object value = node.getValue().accept(this);
        trace("  New value: " + valueString(value));
        assign(node.getName(), value, node.getLine(), node.getColumn());
        return value;
    }

    public Object visitIf(IfNode node) {
        trace("If statement");
        boolean cond = toBoolean(node.getCondition().accept(this));
        trace("  Condition: " + cond);
        if (cond) {
            node.getThenBranch().accept(this);
        } else if (node.hasElseBranch()) {
            node.getElseBranch().accept(this);
        }
        return null;
    }

    public Object visitWhile(WhileNode node) {
        trace("While loop");
        int iterations = 0;
        while (toBoolean(node.getCondition().accept(this))) {
            trace("  Iteration #" + (iterations + 1));
            node.getBody().accept(this);
            iterations++;
        }
        trace("  Completed after " + iterations + " iteration(s)");
        return null;
    }

    public Object visitFunctionDeclaration(FunctionDeclarationNode node) {
        trace("Registering function: " + node.getName() + "(" + node.getParameters().size() + " params)");
        functions.put(node.getName(), node);
        return null;
    }

    public Object visitReturn(ReturnNode node) {
        trace("Return statement");
        Object value = node.getValue().accept(this);
        trace("  Returning: " + valueString(value));
        throw new ReturnValue(value);
    }

    public Object visitPrint(PrintNode node) {
        Object value = node.getExpression().accept(this);
        trace(">>> PRINT: " + valueString(value));
        System.out.println(valueString(value));
        return null;
    }

    public Object visitBlock(BlockNode node) {
        trace("Block");
        indentLevel++;
        pushScope();
        try {
            for (ASTNode stmt : node.getStatements()) stmt.accept(this);
        } finally {
            popScope();
            indentLevel--;
        }
        return null;
    }

    public Object visitBinaryExpression(BinaryExpressionNode node) {
        trace("BinaryExpr: " + node.getOperator().getLexeme());
        indentLevel++;
        Object left = node.getLeft().accept(this);
        Object right = node.getRight().accept(this);
        indentLevel--;

        TokenType op = node.getOperator();
        if (op.isArithmeticOperator() || op == TokenType.PERCENT) {
            int l = (Integer) left;
            int r = (Integer) right;
            switch (op) {
                case PLUS: return l + r;
                case MINUS: return l - r;
                case STAR: return l * r;
                case SLASH:
                    if (r == 0) throw RuntimeErrorException.divisionByZero(node.getLine(), node.getColumn());
                    return l / r;
                case PERCENT:
                    if (r == 0) throw RuntimeErrorException.divisionByZero(node.getLine(), node.getColumn());
                    return l % r;
            }
        }
        if (op.isComparisonOperator()) {
            int l = (Integer) left;
            int r = (Integer) right;
            switch (op) {
                case EQUAL_EQUAL: return l == r;
                case BANG_EQUAL: return l != r;
                case LESS: return l < r;
                case LESS_EQUAL: return l <= r;
                case GREATER: return l > r;
                case GREATER_EQUAL: return l >= r;
            }
        }
        return null;
    }

    public Object visitUnaryExpression(UnaryExpressionNode node) {
        trace("UnaryExpr: " + node.getOperator().getLexeme());
        Object operand = node.getOperand().accept(this);
        if (node.getOperator() == TokenType.MINUS) return -(Integer) operand;
        return operand;
    }

    public Object visitCallExpression(CallExpressionNode node) {
        trace("Call: " + node.getCallee() + "()");
        indentLevel++;
        FunctionDeclarationNode function = functions.get(node.getCallee());
        if (function == null) {
            throw RuntimeErrorException.notAFunction(node.getCallee(), node.getLine(), node.getColumn());
        }
        List<Object> argValues = new ArrayList<>();
        for (ASTNode arg : node.getArguments()) argValues.add(arg.accept(this));
        trace("  Calling with " + argValues.size() + " argument(s)");

        pushScope();
        try {
            List<String> params = function.getParameters();
            for (int i = 0; i < params.size(); i++) define(params.get(i), argValues.get(i));
            function.getBody().accept(this);
            return 0;
        } catch (ReturnValue rv) {
            trace("  Function returned: " + valueString(rv.getValue()));
            return rv.getValue();
        } finally {
            popScope();
            indentLevel--;
        }
    }

    public Object visitLiteral(LiteralNode node) {
        trace("Literal: " + valueString(node.getValue()));
        return node.getValue();
    }

    public Object visitIdentifier(IdentifierNode node) {
        Object value = getVariable(node.getName(), node.getLine(), node.getColumn());
        trace("Identifier '" + node.getName() + "' = " + valueString(value));
        return value;
    }

    public Object visitExpressionStatement(ExpressionStatementNode node) {
        return node.getExpression().accept(this);
    }

    private boolean toBoolean(Object value) {
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Integer) return ((Integer) value) != 0;
        return value != null;
    }
}
