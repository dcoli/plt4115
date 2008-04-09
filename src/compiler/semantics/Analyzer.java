package compiler.semantics;
import compiler.syntax.*;

import java_cup.runtime.Symbol;
import syntax.parser;

public class Analyzer {
	private boolean verbose;
	private parser parser;
	
	public void setVerbose(boolean bool){
		verbose = bool;
	}
	
	public Analyzer(parser p){
		parser = p;
	}
	
	public void analyze() throws Exception{
		if (verbose) {
			System.out.println("analyzing...");
			Symbol s = parser.debug_parse();
			s = s;
		}
		else {
			Symbol s = parser.parse();			
		}
		
	}
}
