package compiler.settings;

public class Symbol {

	public static enum Type { INT, FLOAT, PARTICIPANT };

	private final String id;
	private final int scopeDepth;
	private final Type type;
	
	public Symbol ( String id, int scopeDepth, Type type) {
		this.id = id;
		this.scopeDepth = scopeDepth;
		this.type = type;
	}	

	public Type getType () {
		return this.type;
	}
	
	public String getName(){ return this.id; }

	public int getScopeDepth(){ return this.scopeDepth; }
}
