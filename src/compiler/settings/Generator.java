package compiler.settings;

import java.util.*;

public class Generator {

	Printer pr;
	EnvironmentGenerator eg; 
	
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
	
	protected void makeCloseBracket(){
		print("}\n\n");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//for testing
		String ENVNAME = "My Very First Environment";
		RumbleVariable aRumVar = new RumbleVariable ("days","int","0");
		ArrayList<RumbleVariable> rumVarsArray = new ArrayList<RumbleVariable>();
		rumVarsArray.add(aRumVar);

		EnvironmentGenerator eg = new EnvironmentGenerator(ENVNAME,rumVarsArray);
	}

}
