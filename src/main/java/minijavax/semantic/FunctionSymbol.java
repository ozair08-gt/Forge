package minijavax.semantic;

import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends Symbol {
    private final int arity;

    public FunctionSymbol(String name, int arity, int declarationLine) {
        super(name, "function", declarationLine);
        this.arity = arity;
    }

    public int getArity() { return arity; }

    public String toString() { return getName() + "(" + arity + " params) : function"; }
}
