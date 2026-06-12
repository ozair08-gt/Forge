package minijavax.ast;

public interface ASTVisitor<T> {
    T visitProgram(ProgramNode node);
    T visitVariableDeclaration(VariableDeclarationNode node);
    T visitAssignment(AssignmentNode node);
    T visitIf(IfNode node);
    T visitWhile(WhileNode node);
    T visitFunctionDeclaration(FunctionDeclarationNode node);
    T visitReturn(ReturnNode node);
    T visitPrint(PrintNode node);
    T visitBlock(BlockNode node);
    T visitBinaryExpression(BinaryExpressionNode node);
    T visitUnaryExpression(UnaryExpressionNode node);
    T visitCallExpression(CallExpressionNode node);
    T visitLiteral(LiteralNode node);
    T visitIdentifier(IdentifierNode node);
    T visitExpressionStatement(ExpressionStatementNode node);
}
