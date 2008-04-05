package compilersettings;

public class Symbol {

	public enum Type { INT, FLOAT };

	private final String id;
	private final int scopedepth;
	private final Type type;
	private final int value;
	
	public Symbol ( String id, int scopedepth, Type type, int value ) {
		this.id = id;
		this.scopedepth = scopedepth;
		this.type = type;
		this.value = value;
	}	
	
	public int getValue () {
		return this.value;
	}

	public Type getType () {
		return this.type;
	}

}
