package minijavax.semantic;

public class Symbol {
    private final String name;
    private final String type;
    private final int declarationLine;

    public Symbol(String name, String type, int declarationLine) {
        this.name = name;
        this.type = type;
        this.declarationLine = declarationLine;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getDeclarationLine() { return declarationLine; }

    public String toString() { return name + " : " + type; }
}
