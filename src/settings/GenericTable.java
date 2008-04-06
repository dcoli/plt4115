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
	
	/**
	 * This function either creates an new entry in the table or updates the old entry
	 * the new value.
	 * 
	 * @param id	an identifying name for the object to be inserted or updated
	 * @param obj	the value which is named "id". For the GenericTable class this
	 * is of type Object, but various inheriting classes use more specific class types.
	 */
	public void set( String id, Object obj) {
		this.table.put(id, obj);
	}
	
	/**
	 * 
	 * @param id	the identifying name (and the hashmap's key) for the object to be retrieved
	 * @return	the value stored at key "id". In the Generic class this is an Object; in other
	 * inheriting classes this could be a more specific type, like Symbol or Action.
	 */
	public Object get( String id ) {
		return this.table.get(id);
	}

	/**
	 * 
	 * @param id	the identifying name of the object to be stored
	 * @return	a boolean that reflects whether or not an object given 
	 * by the key, id, exists in the table.
	 */
	public boolean has ( String id ) {
		return this.table.containsKey(id);
	}

	/**
	 * 
	 * @return	returns a Set of keys for the hashmap table.
	 */
	public Set getKeys() {
	   return this.table.keySet();
	}
}
