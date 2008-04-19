package rumble.runtime;
import rumble.runtime.Environment;
import java.util.Random;

public class Participant1 extends AbstractParticipant
{	
	Participant1 (Environment e) {
		// SRS
		this.env = e;
		this.name = "The Rock";
		
		this.rumVar_health = 20;
		this.rumVar_strength = 30;
	}
	
	public void step() {
		Random generator = new Random();
		
		if (generator.nextInt(10) % 2 == 0) {
			this.evn.rumAction_kick(this, this.env.getParticipant(this, 0));
		} else {
			this.env.rumAction_punch(this, this.env.getParticipant(this, 0));
		}
	}
}
