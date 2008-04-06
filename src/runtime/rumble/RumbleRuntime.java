package rumble;
import rumble.Environment;

public class RumbleRuntime
{
	public static void main(String[] args) {
		Environment e = new Environment();
		
		boolean verbose = (args[1] == "-v"); 
		
		while (!e.end()) {
			e.step();
			if (verbose) { System.out.println(e); }
		}
		System.out.println(e);		
	}
}
