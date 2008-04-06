/**
 * 
 */
package settings;

import java.util.HashMap;
import java.util.Set;

/**
 * @author colin
 *
 */
public class GenericTable {

	private final HashMap<String,Object> table;
	
	public GenericTable () {
		table = new HashMap<String,Object>();
	}
	
	public void set( String id, Object obj) {
		this.table.put(id, obj);
	}
	
	public Object get( String id ) {
		return this.table.get(id);
	}

	public boolean has ( String id ) {
		return this.table.containsKey(id);
	}

	public Set getKeys() {
	   return this.table.keySet();
	}
}
