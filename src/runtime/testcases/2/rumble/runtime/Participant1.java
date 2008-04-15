package rumble.runtime;
import rumble.runtime.Environment;

public class Participant1 extends AbstractParticipant
{	
	Participant1 (Environment e) {
		// SRS
		this.env = e;
		this.name = "I";
	}
	
	public void step() {
		this.env.rumAction_hello(this);
	}
}
