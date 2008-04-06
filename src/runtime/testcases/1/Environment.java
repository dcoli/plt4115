package rumble;
import rumble.runtime.Participant;

public class Environment
{
	public static String NAME = "My Very First Environment";
	private int rumVar_days;
	
	Environment () {
		this.rumVar_days = 0;
	}
	
	public int getRumVar_days() {
		return this.rumVar_days;
	}	
	
	private boolean setRumVar_days(int days) {
		this.rumVar_days = days;
		return true;
	}
	
	public String toString() {
		return "{\n\tenvironment: " + this.NAME +
				",\n\tdays: " + this.rumVar_days + "\n}";
	}
	
	public void step() {
		this.setRumVar_days(this.getRumVar_days() + 1);
	}
	
	public boolean end() {
		return this.getRumVar_days() == 10;
	}
}
