/**
 * Not quite sure yet why this stores objects that adhere to the Participant interface, as
 * opposed to Participant objects themselves. This is the place where participants are 
 * stored. This is NOT the place where the general attribute parameters for the simulation 
 * are stored.
 * @see	ParticipantSettings
 */
package compiler.settings;

import java.util.ArrayList;

import compiler.settings.GenericTable;
import compiler.settings.Participant;


/**
 * @author colin
 *
 */
public class ParticipantTable extends GenericTable {

	private final ArrayList<Participant> table;
	
	public ParticipantTable () {
		this.table = new ArrayList();
	}
}
