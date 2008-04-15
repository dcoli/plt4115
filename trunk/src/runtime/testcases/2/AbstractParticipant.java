package rumble.runtime;

public abstract class AbstractParticipant extends Object
{
	// SRS
	private String name;
	private env;
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return "\t\t{\n\t\t\tparticipant: " + this.NAME + 
				"\n\t\t},";
	}
	
	public abstract void step();
}
