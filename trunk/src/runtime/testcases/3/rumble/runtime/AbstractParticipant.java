package rumble.runtime;
import rumble.runtime.Environment;

public abstract class AbstractParticipant extends Object
{
	// SRS
	protected String name;
	protected Environment env;
	
	// custom attributes
	private int rumVar_health;
	private int rumVar_strength;
	
	public String getName() {
		return this.name;
	}
	
	
	// NOT DONE
	public String toString() {
		return "\t\t{\n\t\t\tparticipant : \"" + this.name + 
				"\",\n\t\t},\n";
	}
	
	public abstract void step();
	
	//custom attributes
	
	public boolean setRumVar_health(int health) {
		if (health >= 0) {
			this.rumVar_health = health;
			return true;
		} else {
			return false;
		}
	}
	
	public int getRumVar_health() {
		return this.rumVar_health;
	}
	
	public boolean setRumVar_strength(int strength) {
		if (strength >= 0) {
			this.rumVar_strength = strength;
			return true;
		} else {
			return false;
		}
	} 
	
	public int getRumVar_strength() {
		return this.rumVar_strength;
	}
}
