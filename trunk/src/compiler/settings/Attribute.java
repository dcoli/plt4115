package compiler.settings;

public class Attribute {
	
	private String id;
	private String val;
	private String constraint;
	private int type;
	
	public Attribute(String id, String val, String constraint, int type){
		this.id = id;
		this.val = val;
		this.constraint = constraint;
		this.type = type;	
	}

	public String getId() {
		return id;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getConstraint() {
		return constraint;
	}

	public int getType() {
		return type;
	}
	
	
}
