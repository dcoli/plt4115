/**
 * 
 */
package settings;

/**
 * @author colin
 *
 */
import java.util.*;

import settings.Action;

public class ActionTable extends GenericTable {

	private final HashMap<String,Action> table;
	
	public ActionTable () {
		table = new HashMap<String,Action>();
	}
	
	public void set( String actionId, List<String> argList) {
		Action action = new Action ( actionId, argList );
		table.put(actionId, action);
	}
	
	public boolean has ( String id ) {
		return table.containsKey(id);
	}

	public List get( String id ) {
		return table.get(id).getArgList();
	}
	
/*	public boolean has ( String id ) {
		return table.containsKey(id);
	}
*/
}

