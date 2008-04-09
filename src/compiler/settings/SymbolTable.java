/**
 * Stores the symbol information for the application. The identifying name for each symbol is
 * a concatenation of the id, the ":", and an integer representing scope depth. The methods in
 * this class handle the details of this concatenation transparently.
 */
package compiler.settings;

import java.util.*;

import compiler.settings.Symbol;
import compiler.settings.Symbol.Type;


public class SymbolTable extends GenericTable{

	private final HashMap<String,Symbol> table;
	
	public SymbolTable () {
		table = new HashMap<String,Symbol>();		
	}
	
	/**
	 * This function either creates an new entry in the table or updates the old entry
	 * the new value.
	 * 
	 * @param id	an identifying name for the object to be inserted or updated
	 * @param p		the Participant who is named "id". 
	 */
	public void set( String id, int scopeDepth, Type type) {
		String catid = id + ":" + scopeDepth;
		Symbol symbol = new Symbol ( id, scopeDepth, type);
		table.put(catid, symbol);
	}
	
	/**
	 * 
	 * @param id	the identifying name (and the hashmap's key) for the object to be retrieved
	 * @return	the Symbol stored at key "id". 
	 */
	public Symbol get( String id, int scopeDepth ) {
		String catid = id + ":" + scopeDepth;
		return table.get(catid);
	}


	/**
	 * 
	 * @param id	the identifying name (and the hashmap's key) for the object to be retrieved
	 * @return	the Type of the Symbol stored at key "id". (This goes into the Symbol object
	 * and ferrets out the symbol's type so you don't have to.)
	 */
	public Type getType( String id, int scopeDepth ) {
		String catid = id + ":" + scopeDepth;
		return table.get(catid).getType();
	}
	
	/**
	 * 
	 * @param id	the identifying name of the object to be stored
	 * @return	a boolean that reflects whether or not an object given 
	 * by the key, id, exists in the table.
	 */
	public boolean has ( String id ) {
		return table.containsKey(id);
	}

}
