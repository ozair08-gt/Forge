package minijavax.semantic;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, Symbol> symbols = new HashMap<>();
    private final Scope enclosing;
    private final String scopeName;

    public Scope(String scopeName, Scope enclosing) {
        this.scopeName = scopeName;
        this.enclosing = enclosing;
    }

    public boolean define(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) return false;
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol resolveLocal(String name) {
        return symbols.get(name);
    }

    public Symbol resolve(String name) {
        if (symbols.containsKey(name)) return symbols.get(name);
        if (enclosing != null) return enclosing.resolve(name);
        return null;
    }

    public Scope getEnclosing() { return enclosing; }
    public String getScopeName() { return scopeName; }
}
