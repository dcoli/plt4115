package codegeneration;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import compiler.syntax.*;
import compiler.settings.*;

public class CodeGenerator {
	
	private ASTNode root;
	private int currentLocation = 0;
	private PrintWriter pw;
	
	public CodeGenerator(ASTNode root){
		this.root = root;
	}
	
	public void go(){
		generateEnvironmentFile((ASTNode)root.getDescriptor(), (ASTNode)root.getOp(0));
		
		if (root.getOp(1) != null)
			generateParticipantFiles((ASTNode)root.getDescriptor(), (ASTNode)root.getOp(1));
		
	}
	
	public void generateEnvironmentFile(ASTNode simulationNode, ASTNode environmentNode){
		try{
			//Get environment filename
			ASTNode environmentConfig = (ASTNode)simulationNode.getOp(1);
			String envFile = (String)environmentConfig.getDescriptor();
			
			
			
			pw = new PrintWriter(new FileOutputStream(Settings.outputPath + "/Environment.java"));
			pw.println("public class Environment {");
			pw.println("\tpublic static String ENVIRONMENT_NAME = \"" + environmentNode.getOp(0) + "\";");
			pw.println("\tpublic static String SIMULATION_NAME = \"" + simulationNode.getOp(0) + "\";");
			
			pw.println("}");
			pw.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Could not write environment file.");
		}
	}
	
	public void generateParticipantFiles(ASTNode simulationNode, ASTNode participantFileList){
		
	}
	
}
