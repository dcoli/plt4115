package semantics;
import syntax.*;

public class Analyzer {
	private boolean verbose;
	private parser parser;
	
	public void setVerbose(boolean bool){
		verbose = bool;
	}
	
	public Analyzer(parser p){
		parser = p;
	}
	
	public void analyze(){
		System.out.println("analyzing...");
	}
}
