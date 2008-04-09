/**
 * Hashmap of Actions. Could have merely stored argument list directly in this table,
 * rather than creating Action objects, but I expect our Action object to grow.
 */
package compiler.settings;

/**
 * @author colin
 */
import java.util.*;

import compiler.settings.Action;


public class ActionTable extends GenericTable {

	private final HashMap<String,Action> table;
	
	public ActionTable () {
		table = new HashMap<String,Action>();
	}
	
	/**
	 * 
	 * @param actionId	the identifying name of the action, e.g., "print".
	 * @param argList	a list of string arguments in the form of a List.
	 */
	public void set( String actionId, List<Symbol.Type> argList) {
		Action action = new Action ( actionId, argList );
		table.put(actionId, action);
	}
	
	/**
	 * @return	tells whether an action whose name is given by "id" exists in the table.
	 */
	public boolean has ( String id ) {
		return table.containsKey(id);
	}

	/**
	 * @return	the List of arguments stored with an Action.
	 */
	public List get( String id ) {
		return table.get(id).getArgList();
	}
	
/*	IMPLEMENTED IN GENERICTABLE?
 * public boolean has ( String id ) {
		return table.containsKey(id);
	}
*/
}

