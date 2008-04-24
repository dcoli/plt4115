package compiler;
/*
 * Main class for Rumble.  Displays help info and takes in arguments for processing.
 */
import compiler.settings.*;
import java.io.*;

import compiler.semantics.*;
import compiler.syntax.*;

import java_cup.runtime.*;

public class RumbleCompiler {

	public static void main(String[] args) {
		String simulationFile = "";
		boolean debug = false, verbose = false;
		
		//loop through args
		for (int i = 0; i < args.length; i++){
			if (args[i].startsWith("-")){
				for (int j = 1; j < args[i].length(); j++){
					switch (args[i].charAt(j)){
						case 'v': verbose = true; break;
						case 'd': debug = true; break;
					}
				}
			}
			else simulationFile = args[i];
		}
		
		if (simulationFile.equals(""))
			displayHelp();
		
		try {
			//Begin tokenization process
			File file = new File (simulationFile);
			Yylex scanner = new Yylex(new FileInputStream(file));
			
			//Initialize the Settings class
			Settings.init(verbose, debug);
			
			//alert the scanner as to the current working directory
			if (file.getParent() != null)
				Settings.setCurrentWorkingDirectory(file.getParent() + "/");
			
			try {
				Analyzer analyzer = new Analyzer(new parser(scanner));
				analyzer.analyze();
				System.out.println("Compilation completed successfully!");
			}
			catch (Exception e){
				System.out.println("Compile time error: " + e.getMessage());
			}
		}
		catch (Exception e){
			displayHelp();
		}
		
	}
	
	public static void displayHelp(){
		System.out.println("==============RUMBLE SIMULATION INTERPRETER==============");
		System.out.println("To run type: java Rumble [options] [simulation file (*.rus)]");
		System.out.println("\n-v\t\t\tVerbose Mode");
		System.out.println("\n-v\t\t\tCompiler Debugging Mode");
		System.out.println("=========================================================");
		System.exit(0);
	}

}
