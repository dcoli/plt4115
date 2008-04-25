package compiler.settings;

import java.util.ArrayList;
import java.util.Iterator;

public class EnvironmentGenerator extends Generator {

	private String name;
	private String CLS = "Environment";
	private String PKG = "rumble.runtime";
	private ArrayList<String[]> rumVarsArray = null;
	
	public EnvironmentGenerator (String name, ArrayList rumVarsArray) {
		this.name = name;
		this.rumVarsArray = rumVarsArray;
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
		makeCloseBracket();
		makeRumVarGetAndSet("days", "int");
		makeToString("days");
		makeStep(rumVarsArray);
		makeEnd(rumVarsArray,"10");
		makeFooter();
	}
	
	private void decStandardVars () {
		String str = "\tString NAME = \"" + this.name + "\";\n";
		str += "\tprivate int num_steps";
		str += ";\n\n";
		print(str);
	}

	private void setStandardVar(String name, String value) {
		print("\tthis."+name+" = " + value +";\n");
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
		str += "}\n\n";
		print(str);
	}

	private void makeStep(ArrayList rumVarsArray) {
		String str = "public void step() {\n";
		str += "\t// standard rumble stuff\n";
		str += "\tthis.num_steps++;\n";
		
		str += "\t// everything else\n";
		
//colin: THIS PART WILL NEED TO INHERIT MORE INFORMATION FROM THE AST NODE, LIKE STEP INCREMENT VALUE, ETC. RUMVARSARRAY IS CREATED IN GENERATOR.JAVA
		Iterator<RumbleVariable> ri = rumVarsArray.iterator();
	    while ( ri.hasNext() ){
	    	String thename = ri.next().name;
	    	str += "\tthis.setRumVar_" + thename + "(this.getRumVar_" + thename + "() + 1);\n";
	    }
		str += "}\n\n";
	    print(str);
	}
	
	private void makeEnd(ArrayList rumVarsArray, String endValue) {
		String str = "public boolean end() {\n";
		Iterator<RumbleVariable> ri = rumVarsArray.iterator();
	    while ( ri.hasNext() ){
			str += "\treturn this.getRumVar_" + ri.next().name + "() == "+endValue+";\n";
		}
		str += "}\n\n";
	    print(str);
	}
}
