package compilersettings;

import java.util.*;

import compilersettings.Symbol;
import compilersettings.Symbol.Type;

public class SymbolTable {

	private final HashMap<String,Symbol> table = new HashMap<String,Symbol>();
	
	public SymbolTable () {
		
	}
	
	public void set( String id, int scopedepth, Type type, int value ) {
		String catid = id + ":" + scopedepth;
		Symbol symbol = new Symbol ( id, scopedepth, type, value );
		table.put(catid, symbol);
	}
	
	public int get( String id, int scopedepth ) {
		String catid = id + ":" + scopedepth;
		int value = table.get(catid).getValue();
		return value;
	}
	
}
