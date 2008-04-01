/*
 * Main class for Rumble.  Displays help info and takes in arguments for processing.
 */
import syntax.*;
import semantics.*;
import java.io.*;
import java_cup.runtime.*;

public class Rumble {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String simulationFile = "";
		boolean verbose = false;
		for (int i = 0; i < args.length; i++){
			if (args[i].startsWith("-")){
				for (int j = 1; j < args[i].length(); j++){
					switch (args[i].charAt(j)){
						case 'v':
							verbose = true;
						break;
					}
				}
			}
			else simulationFile = args[i];
		}
		
		if (simulationFile.equals("")){
			displayHelp();
		}
		try {
			//Start processing
			File file = new File (simulationFile);
			Yylex scanner = new Yylex(new FileInputStream(file));

			//alert the scanner as to the current working directory
			if (file.getParent() != null)
				scanner.setCWD(file.getParent() + "\\");
			
			Analyzer analyzer = new Analyzer(new parser(scanner));
			analyzer.setVerbose(verbose);
			analyzer.analyze();
		}
		catch (Exception e){
			displayHelp();
		}
		
	}
	
	public static void displayHelp(){
		System.out.println("==============RUMBLE SIMULATION INTERPRETER==============");
		System.out.println("To run type: java Rumble [options] [simulation file (*.rus)]");
		System.out.println("\n-v\t\t\tVerbose Mode");
		System.out.println("=========================================================");
		System.exit(0);
	}

}
