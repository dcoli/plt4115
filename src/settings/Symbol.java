package settings;

public class Symbol {

	public enum Type { INT, FLOAT, PARTICIPANT };

	private final String id;
	private final int scopeDepth;
	private final Type type;
	private final Object value;
	
	public Symbol ( String id, int scopeDepth, Type type, Object value ) {
		this.id = id;
		this.scopeDepth = scopeDepth;
		this.type = type;
		this.value = value;
	}	
	
	public Object getValue () {
		return this.value;
	}

	public Type getType () {
		return this.type;
	}
	
	public String getName(){ return this.id; }

	public int getScopeDepth(){ return this.scopeDepth; }
}
