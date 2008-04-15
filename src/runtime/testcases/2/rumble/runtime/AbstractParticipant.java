package rumble.runtime;
import rumble.runtime.Environment;

public abstract class AbstractParticipant extends Object
{
	// SRS
	protected String name;
	protected Environment env;
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return "\t\t{\n\t\t\tparticipant : \"" + this.name + 
				"\",\n\t\t},\n";
	}
	
	public abstract void step();
}
