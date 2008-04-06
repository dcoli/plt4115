/**
 * 
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
	
	public void set ( String id, Participant p) {
		this.table.put(id, p);
//		get(id);
	}
	
	public Participant get ( String id) {
		System.out.println("getting "+id);
		return this.table.get(id);
	}	

	public boolean has ( String id ) {
		return this.table.containsKey(id);
	}
}
