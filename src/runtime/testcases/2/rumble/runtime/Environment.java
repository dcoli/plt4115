package rumble.runtime;
import java.util.ArrayList;
import rumble.runtime.AbstractParticipant;
import rumble.runtime.Participant1;

public class Environment
{
	// standard rumble stuff
	private String name;
	private ArrayList<AbstractParticipant> participants;
	
	private int num_steps;
	
	Environment() {
		// SRS
		this.name = "world";
		this.num_steps = 0;
		
		participants = new ArrayList<AbstractParticipant>();
		participants.add(new Participant1(this));
	}
	
	public String getName() {
		return this.name;
	}
	
	public String participantStrings() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\tparticpants :\n\t[\n");
		
		for (AbstractParticipant p : this.participants) {
			sb.append(p);
		}
		
		sb.append("\t],\n");
		return sb.toString();
	}
	
	public String toString() {
		return "{\n\tenvironment : \"" + this.name + "\",\n" + participantStrings() + 
				"\n}";
	}
	
	public void step() {
		// standard rumble stuff
		this.num_steps++;
		
		// everything else
		participants.get(0).step();
	}
	
	public boolean end() {
		return this.num_steps == 1;
	}
	
	// "actions"
	
	public void rumAction_hello(AbstractParticipant p) {
		System.out.println("{\n\taction : \"'" + p.getName() + "' did 'hello'\",\n}\n");		
	}
}