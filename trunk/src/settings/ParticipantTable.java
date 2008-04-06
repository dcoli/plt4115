/**
 * Not quite sure yet why this stores objects that adhere to the Participant interface, as
 * opposed to Participant objects themselves. This is the place where participants are 
 * stored. This is NOT the place where the general attribute parameters for the simulation 
 * are stored.
 * @see	ParticipantSettings
 */
package settings;

import java.util.HashMap;

import settings.Participant;

/**
 * @author colin
 *
 */
public class ParticipantTable extends GenericTable {

	private final HashMap<String,Participant> table;
	
	public ParticipantTable () {
		this.table = new HashMap<String,Participant>();
	}
	
	/**
	 * This function either creates an new entry in the table or updates the old entry
	 * the new value.
	 * 
	 * @param id	an identifying name for the object to be inserted or updated
	 * @param p		the Participant who is named "id". 
	 */
	public void set ( String id, Participant p) {
		this.table.put(id, p);
//		get(id);
	}
	
	/**
	 * 
	 * @param id	the identifying name (and the hashmap's key) for the object to be retrieved
	 * @return	the Participant stored at key "id". 
	 */
	public Participant get ( String id) {
		System.out.println("getting "+id);
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
}
