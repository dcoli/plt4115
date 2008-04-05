package compilersettings;

import java.util.*;

import compilersettings.Symbol;
import compilersettings.Symbol.Type;

public class SymbolTable {

	private final HashMap<String,Symbol> table = new HashMap<String,Symbol>();
	
	public SymbolTable () {
		
	}
	
	public void set( String id, int scopedepth, Type type, Object value ) {
		String catid = id + ":" + scopedepth;
		Symbol symbol = new Symbol ( id, scopedepth, type, value );
		table.put(catid, symbol);
	}
	
	public Symbol get( String id, int scopedepth ) {
		String catid = id + ":" + scopedepth;
		return table.get(catid);
	}

	public Object getValue( String id, int scopedepth ) {
		String catid = id + ":" + scopedepth;
		return table.get(catid).getValue();
	}

	public Type getType( String id, int scopedepth ) {
		String catid = id + ":" + scopedepth;
		return table.get(catid).getType();
	}
}
