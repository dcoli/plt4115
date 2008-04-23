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
}
