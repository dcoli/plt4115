package compilersettings;

public class Symbol {

	public enum Type { INT, FLOAT };

	private final String id;
	private final int scopedepth;
	private final Type type;
	private final Object value;
	
	public Symbol ( String id, int scopedepth, Type type, Object value ) {
		this.id = id;
		this.scopedepth = scopedepth;
		this.type = type;
		this.value = value;
	}	
	
	public Object getValue () {
		return this.value;
	}

	public Type getType () {
		return this.type;
	}

}
