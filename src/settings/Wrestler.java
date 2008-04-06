package settings;

//this is the sort of code we'll need to put together for each type of participant.
public class Wrestler implements Participant {
	
	private String name;
//	private int strength;
//	private int speed;
	
	public Wrestler ( String name ){
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
}
