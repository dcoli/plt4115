package compiler.settings;

import java.util.ArrayList;

public class EnvironmentGenerator extends Generator {

	private String name;
	private String CLS = "Environment";
	private String PKG = "rumble.runtime";

	public EnvironmentGenerator (String name, ArrayList rumVarsArray) {
		this.name = name;
		makes();
		closeFile();
	}
	
	private void makes() {
		makeHeader(PKG,CLS);
		decStandardVars();
		decRumVar("int", "days", "0");
		makeConstructorHeader(CLS);
		setStandardVar("num_steps","0");
		print("// custom globals\n\n");
		setRumVar("days","0");
		makeRumVarGetAndSet("days", "int");
		makeToString("days");
		makeFooter();
	}
	
	private void decStandardVars () {
		String str = "\tString NAME = \"" + this.name + "\"\n";
		str += "\tprivate int num_steps";
		str += ";\n\n";
		print(str);
	}

	private void setStandardVar(String name, String value) {
		print("\tthis."+name+" = " + value +";+n");
	}
	
	private void decRumVar (String type, String name, String value) {
		String str = "\tprivate "+type+" rumVar_"+name;
		if ( value != null ) str += " = " + value;
		str += ";\n\n";
		print(str);
	}

	private void setRumVar (String name, String value) {
		String str = "\tthis.rumVar_"+name+" = "+value;
		str += ";\n\n";
		print(str);
	}

	private void makeRumVarGetAndSet(String name, String type) {
		String str = "public "+type+" getRumVar_"+name+"() {\n";
		str += "\treturn this.rumVar_"+name+";\n}\n\n";
		
		str += "private boolean setRumVar_"+name+"("+type+" "+name+") {\n";
		str += "\tthis.rumVar_"+name+" = "+name+";\n";
		str += "\treturn true;\n";
		str += "}\n\n";
		
		print(str);
	}
	
	private void makeToString(String name) {
		String str = "public String toString() {\n";
		str += "\treturn \"{\\n\\tenvironment : \\\"\" + this.NAME + \"\\\",\\n\\t"+name+" : \" + this.rumVar_days + \",\\n}\\n\";\n";
		str += "}";
		print(str);
	}

/*	private void makeStep(ArrayList rumVarsArray)
	public void step() {
		// standard rumble stuff
		this.num_steps++;
		
		// everything else
		this.setRumVar_days(this.getRumVar_days() + 1);
	}
	
	public boolean end() {
		return this.getRumVar_days() == 10;
	}
*/}
