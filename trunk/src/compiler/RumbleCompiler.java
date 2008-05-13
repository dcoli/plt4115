package compiler;
/*
 * Main class for Rumble.  Displays help info and takes in arguments for processing.
 */
import compiler.settings.*;
import codegeneration.*;
import validation.*;

import java.io.*;

import compiler.syntax.*;

import java_cup.runtime.*;

public class RumbleCompiler {

	public static void main(String[] args) {
		String simulationFile = "";
		boolean debug = false, verbose = false;
		String outputPath = null;
		
		//loop through args
		for (int i = 0; i < args.length; i++){
			if (args[i].startsWith("-")){
				switch (args[i].charAt(1)){
						case 'v': verbose = true; break;
						case 'd': verbose = true; debug = true; break;
						case 'o': outputPath = args[++i]; break;
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
			Settings.init(verbose, debug, outputPath);
			
			//alert the scanner as to the current working directory
			if (file.getParent() != null)
				Settings.setCurrentWorkingDirectory(file.getParent() + "/");
			
			if (outputPath == null)
				Settings.setOutputPath(Settings.getCurrentWorkingDirectory());
			
			try {				
				java_cup.runtime.Symbol s;
				parser parser = new parser(scanner);
				if (Settings.isDebug())
					s = parser.debug_parse();
				else 
					s = parser.parse();		
				
				ASTNode root = (ASTNode)s.value;
				
				// Make sure we've seen all of the parser's output
				System.out.flush();
				
				if (Settings.isVerbose())
					System.out.println("Validating code...");
								
				//generate code
				//Validator validator = new Validator(root);
				//if (!validator.go()) throw new Exception("Problems in your Rumble code.");
				
				CodeGenerator generator = new CodeGenerator(root);
				generator.go();
				
				System.out.println("Compilation completed successfully!");
				
				String os = System.getProperty("os.name").toLowerCase();
				
				if (os.startsWith("windows")){
					System.out.println("Jarring output...");
					Runtime.getRuntime().exec("cmd make.bat", null, new File(Settings.outputPath + "\\"));
				} else {
					System.out.println("Jarring output...");
					Runtime.getRuntime().exec("sh make.sh", null, new File(Settings.outputPath + "/"));
				}
				
				
				
			}
			catch (Exception e){
				System.out.println("giving up.");
				System.out.println("Compile time error: " + e.getMessage());
				e.printStackTrace();
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
		System.out.println("\n-d\t\t\tCompiler Debugging Mode");
		System.out.println("\n-o [output path]\t\t\tCompiler Debugging Mode");
		System.out.println("=========================================================");
		System.exit(0);
	}

}
