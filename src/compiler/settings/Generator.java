package compiler.settings;

import java.util.*;

public class Generator {

	Printer pr;
	EnvironmentGenerator eg; 
	String ENVNAME = "My Very First Environment";
	
	public Generator () {
		pr = new Printer("Environment.java");
	}
	
	protected void print(String text) {
		pr.print(text);
	}
	
	protected void closeFile() {
		if (pr.close()) System.out.println("Generated file closed.");
	}

	protected void makeHeader(String pkg, String cls) {
		print("package "+pkg+";\n\n");
		print("public class "+cls+"\n\n");
		print("{\n");
	}

	protected void makeFooter() {
		print("}\n");
	}

	protected void makeConstructorHeader(String cls) {
		print(""+cls + "() {\n");
		print("// standard rumble stuff\n");
	}			

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Array varArray = {"int","days","0"};
		ArrayList rumVarsArray = new ArrayList(3);
//		rumVarsArray.add("int","days","0");
		EnvironmentGenerator eg = new EnvironmentGenerator("hello",rumVarsArray);
	}

}
