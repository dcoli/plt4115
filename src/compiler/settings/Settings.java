package compiler.settings;

public class Settings {
	   
	   public static ActionTable actionTable;
	   public static ParticipantTable participantTable;
	   public static SymbolTable symbolTable;
	   
	   /* COMPILER SETTINGS AND PATHS */
	   public static String currentWorkingDirectory;
	   public static boolean verbose;
	   public static boolean debug;
	   public static String outputPath;
	   
	   
	   public static void init(boolean verbose, boolean debug, String outputPath) {
	      // Exists only to defeat instantiation.
		   
		   Settings.actionTable = new ActionTable();
		   Settings.participantTable = new ParticipantTable();
		   Settings.symbolTable = new SymbolTable();
		   
		   Settings.verbose = verbose;
		   Settings.debug = debug;
		   Settings.outputPath = outputPath;
	   }


	public static String getCurrentWorkingDirectory() {
		return currentWorkingDirectory;
	}


	public static void setCurrentWorkingDirectory(String currentWorkingDirectory) {
		Settings.currentWorkingDirectory = currentWorkingDirectory;
	}


	public static boolean isVerbose() {
		return verbose;
	}


	public static void setVerbose(boolean verbose) {
		Settings.verbose = verbose;
	}


	public static boolean isDebug() {
		return debug;
	}


	public static void setDebug(boolean debug) {
		Settings.debug = debug;
	}
	}