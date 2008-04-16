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
	
	//custom globals
	private int numVar_moves;
	
	Environment() {
		// SRS
		this.name = "Wrestling Environment";
		this.num_steps = 0;
		
		participants = new ArrayList<AbstractParticipant>();
		participants.add(new Participant1(this));
		participants.add(new Participant2(this));
		
		//custom globals
		this.rumVar_moves = 0;
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
		for (AbstractParticipant p : this.participants) {
			participants.get(0).step();
		}
	}
	
	// NOT FINISHED -- copy participant list and remove calling participant
	public AbstractParticipant getParticipant(int i) {
		AbstractParticipant p = null;
		try {
			p = participants.get(i);´
			return p;
		} catch {
			System.out.println("Rumble runtime error: Participant " + i + " does not exist.");
		}
	}
	
	// NOT FINISHED
	public boolean end() {
		return (participants.get(0).)
	}
	
	// custom globals 
	public int getRumVar_moves() {
		return this.rumVar_moves;
	}
	
	private boolean setRumVar_moves(int moves) {
		this.rumVar_moves = moves;
		return true;
	}
	
	// NOT FINISHED -- do actions
	// "actions"
	public void rumAction_punch(AbstractParticipant p, AbstractParticipant enemy) {
		System.out.println("{\n\taction : \"'" + p.getName() + "' did 'punch' to '" 
							+ enemy.getName() + "'\",\n}\n");
		set(enemy.)		
	}
}