package compiler.semantics;

import compiler.syntax.*;
import compiler.settings.*;

public class Analyzer {
	private parser parser;
	
	public Analyzer(parser p){
		parser = p;
	}
	
	public ASTNode analyze() throws Exception{
		if (Settings.isVerbose())
			System.out.println("analyzing...");
		
		java_cup.runtime.Symbol s;
		
		if (Settings.isDebug())
			s = parser.debug_parse();
		else 
			s = parser.parse();		
		
		ASTNode root = (ASTNode)s.value;
		
		validateSimulation(root.getDescriptor());
		validateEnvironment(root.getOp(0));
		validateParticipants(root.getOp(1));
		
		return root;
	}

	public void validateSimulation(Object o){
		
	}
	
	public void validateEnvironment(Object o){
		
	}

	public void validateParticipants(Object o){
		
	}
}
