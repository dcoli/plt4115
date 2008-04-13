package rumble;
import rumble.runtime.Environment;

public class Participant1 extends AbstractParticipant
{
	// SRS
	public static String NAME = "I";
	private env;
	
	Participant1 (Environment e) {
		this.env = e;
	}
	
	public String toString() {
		return "{\n\tparticipant: " + this.NAME + 
				"\n}";
	}
	
	public void step() {
		e.rumAction_hello(this);
	}
	
}
