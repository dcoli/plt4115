package compiler.settings;

public class RumbleVariable {
	// contains name, type, and value
	public String name, type, value;
	
	public RumbleVariable(String name, String type, String value) {
		System.out.println(type + " " + name + " = " + value);
		this.name = name;
		this.type = type;
		this.value = value;
	}
}
