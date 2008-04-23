package codegeneration;

public class EnvironmentGenerator extends Generator {

	private String name;

	/**
	 * @param args
	 */
/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
*/		
	public EnvironmentGenerator (String name) {
		super(); 
		this.name = name;
		this.makeStandardVars();
		this.makeRumVar("int", "days", "0");
		super.closeFile();
	}
	
	private void makeStandardVars () {
		String str = "\tprivate int num_steps";
		str += ";\n\n";
		super.print(str);
	}
	
	private void makeRumVar (String type, String name, String value) {
		String str = "\tprivate "+type+" rumVar_"+name;
		if ( value != null ) str += " = " + value;
		str += ";\n\n";
		super.print(str);
	}

}
