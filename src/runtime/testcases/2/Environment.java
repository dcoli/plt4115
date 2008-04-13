package rumble;
import java.util.ArrayList;
import rumble.runtime.AbstractParticipant;
import rumble.runtime.Participant1;

public class Environmnet
{
	// standard rumble stuff
	public static String NAME = "world";
	private ArrayList<AbstractParticipant> participants = new ArrayList<AbstractParticipant>();
	
	private int num_steps;
	
	Environment() {
		// SRS
		this.num_steps = 0;
		
		participants.append(new Participant1(this));
	}
	
	public String participantStrings() {
		StringBuilder sb;
		
		sb.append("\tparticpants:\n\t[");
		
		for (AbstractParticipant p : this.participants) {
			sb.append(p);
		}
		
		sb.append("\t],\n");
		return sb.toString();
	}
	
	public String toString() {
		return "{\n\tenvironment: " + this.NAME + "\n" + participantStrings() + 
				"\n}";
	}
	
	public void step() {
		// standard rumble stuff
		this.num_steps++;
		
		// everything else
		participants[0].step();
	}
	
	public boolean end() {
		return this.num_steps == 1;
	}
	
	// "actions"
	
	public void rumAction_hello(AbstractParticipant p) {
		
	}
}