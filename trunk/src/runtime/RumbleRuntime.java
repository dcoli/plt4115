import Rumble.Environment;

public class RumbleRuntime
{
	public static void main(String[] args) {
		Environment e = new Environment();
		
		while (!e.end()) {
			e.step();
		}
		System.out.println(e);		
	}
}
