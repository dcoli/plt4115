package compiler.semantics;

import compiler.syntax.*;
import compiler.settings.*;

public class Analyzer {
	private parser parser;
	
	public Analyzer(parser p){
		parser = p;
	}
	
	public void analyze() throws Exception{
		if (Settings.isVerbose())
			System.out.println("analyzing...");
		
		java_cup.runtime.Symbol s;
		
		if (Settings.isDebug())
			s = parser.debug_parse();
		else 
			s = parser.parse();			
		
	}
}
