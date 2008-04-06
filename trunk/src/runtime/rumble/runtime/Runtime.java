package rumble;
import rumble.runtime.Environment;

public class Runtime
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
