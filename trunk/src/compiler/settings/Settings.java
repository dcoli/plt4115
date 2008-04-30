package compiler.settings;

import java.util.HashMap;

public class Settings {
	   
   public static ActionTable actionTable;
   public static ParticipantTable participantTable;
   public static SymbolTable symbolTable;
   
   public static HashMap<String, Attribute> attributes;
   public static HashMap<String, Attribute> globals;
   
   
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
	   Settings.globals = new HashMap();
	   Settings.attributes = new HashMap();
	   Settings.verbose = verbose;
	   Settings.debug = debug;
	   Settings.outputPath = outputPath;
   }
   
   public static void persistAttributeDeclaration(String id, String constraint, int type){
	   id = codegeneration.CodeGenerator.RUMVAR + id;
	   if (attributes.containsKey(id)){
		   Attribute a = attributes.get(id);
		   a.setType(type);
		   a.setConstraint(constraint);
	   }
	   else attributes.put(id, new Attribute(id, constraint, null, type));
   }
   
   public static void persistGlobalDeclaration(String id, String constraint, int type){
	   id = codegeneration.CodeGenerator.RUMVAR + id; 
	   if (globals.containsKey(id)){
		   Attribute a = globals.get(id);
		   a.setType(type);
		   a.setConstraint(constraint);
	   }
	   else globals.put(id, new Attribute(id, null, constraint, type));
   }
   
   public static void persistGlobalInitialization(String id, String value){
	   id = codegeneration.CodeGenerator.RUMVAR + id;
	   globals.put(id, new Attribute(id, value, null, 0));
   }
   
   public static void persistAttributeInitialization(String id, String value){
	   id = codegeneration.CodeGenerator.RUMVAR + id;
	   attributes.put(id, new Attribute(id, value, null, 0));
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