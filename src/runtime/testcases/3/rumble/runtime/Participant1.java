package rumble.runtime;
import rumble.runtime.Environment;

public class Participant1 extends AbstractParticipant
{	
	Participant1 (Environment e) {
		// SRS
		this.env = e;
		this.name = "Hulk Hogan";
		
		this.rumVar_health = 30;
		this.rumVar_strength = 20;
	}
	
	public void step() {
		this.env.rumAction_polish_hammer(this, this.env.getParticipant(this, 0));
	}
}
