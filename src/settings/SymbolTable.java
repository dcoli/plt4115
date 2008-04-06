package settings;

import java.util.*;

import settings.Symbol;
import settings.Symbol.Type;

public class SymbolTable {

	private final HashMap<String,Symbol> table;
	
	public SymbolTable () {
		table = new HashMap<String,Symbol>();		
	}
	
	public void set( String id, int scopeDepth, Type type, Object value ) {
		String catid = id + ":" + scopeDepth;
		Symbol symbol = new Symbol ( id, scopeDepth, type, value );
		table.put(catid, symbol);
	}
	
	public Symbol get( String id, int scopeDepth ) {
		String catid = id + ":" + scopeDepth;
		return table.get(catid);
	}

	public Object getValue( String id, int scopeDepth ) {
		String catid = id + ":" + scopeDepth;
		return table.get(catid).getValue();
	}

	public Type getType( String id, int scopeDepth ) {
		String catid = id + ":" + scopeDepth;
		return table.get(catid).getType();
	}
	
	public boolean has ( String id ) {
		return table.containsKey(id);
	}

}
