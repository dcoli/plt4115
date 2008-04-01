package semantics;
import java_cup.runtime.Symbol;
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
		try {
			if (verbose) {
				System.out.println("analyzing...");
				Symbol s = parser.debug_parse();
			}
			else {
				Symbol s = parser.parse();
			}
			
		}
		catch(Exception e){
			Object[] s = Yylex.files.toArray();
			e.printStackTrace();
		}
	}
}
