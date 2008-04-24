package codegeneration;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

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
		writeEnvironmentFile((ASTNode)root.getDescriptor(), (ASTNode)root.getOp(0));
		
		if (root.getOp(1) != null)
			writeParticipantFiles((ASTNode)root.getDescriptor(), (ASTNode)root.getOp(1));
		
	}
	
	public void writeEnvironmentFile(ASTNode simulationNode, ASTNode environmentNode){
		try{
			//Get environment filename
			ASTNode environmentConfig = (ASTNode)simulationNode.getOp(1);
			String envFile = (String)environmentConfig.getDescriptor();
			
			
			
			pw = new PrintWriter(new FileOutputStream(Settings.outputPath + "/Environment.java"));
			pw.println("import java.util.*;");
			pw.println("public class Environment {");
			
			//system variables
			pw.println("\tprivate static Random rand;");
			pw.println("\tprivate static int numSteps;");
			pw.println("\tprivate static int numActions;");
			pw.println("\tprivate static ArrayList<Participant> participants;");
			
			//constructor
			pw.println("\tpublic Environment(){");
			pw.println("\t\trand = new Random();");
			pw.println("\t\tparticipants = new ArrayList();");
			
			pw.println("\t}");
			
			pw.println("\tpublic static int randi(){\n\t\treturn rand.nextInt();\n\t}");
			pw.println("\tpublic static int randf(){\n\t\treturn rand.nextFloat();\n\t}");
			pw.println("\tpublic static int getNumSteps(){\n\t\treturn numSteps;\n\t}");
			pw.println("\tpublic static int getNumActions(){\n\t\treturn numActions;\n\t}");
			pw.println("\tpublic static int getNumParts(){\n\t\treturn participants.size();\n\t}");
			
			pw.println("\tpublic static String ENVIRONMENT_NAME = \"" + environmentNode.getOp(0) + "\";");
			pw.println("\tpublic static String SIMULATION_NAME = \"" + simulationNode.getOp(0) + "\";");
			
			HashMap globalValues = assignmentListToHash((ASTNode)environmentConfig.getOp(0));
			
			
			pw.println("}");
			pw.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Could not write environment file.");
		}
	}
	
	public void writeParticipantFiles(ASTNode simulationNode, ASTNode participantFileList){
		
	}
	
	public HashMap assignmentListToHash(ASTNode assignmentList){
		HashMap h = new HashMap();
		
		while(assignmentList != null){
			//get the assignment statement
			ASTNode assignment = (ASTNode)assignmentList.getOp(0);
			h.put(assignment.getOp(0), generateExpression((ASTNode)assignment.getOp(1)));
			
			assignmentList = (ASTNode)assignmentList.getOp(1);
		}
		
		return h;
	}
	
	public String generateExpression(ASTNode e){
		if (e == null) return "";
		
		StringBuilder sb = new StringBuilder();
		switch ((Integer)e.getDescriptor()){
			case sym.OR:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " || " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.AND:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " && " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.EQEQ:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " == " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.NOTEQ:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " != " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.LT:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " < " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.GT:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " > " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.LTEQ:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " <= " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.GTEQ:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " >= " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.PLUS:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " + " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.MINUS:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " - " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.TIMES:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " * " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.MOD:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " % " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.DIVIDE:
				sb.append(generateExpression((ASTNode)e.getOp(0)) + " / " + generateExpression((ASTNode)e.getOp(1)));
			break;
			case sym.NOT:
				sb.append(" !" + generateExpression((ASTNode)e.getOp(0)));
			break;
			case sym.LPREN:
				sb.append(" (" + generateExpression((ASTNode)e.getOp(0)) + ") ");
			break;
			default:
				sb.append(generateData(e));
		}
		
		return sb.toString();
	}
	
	public String generateSystemVar(ASTNode systemVar){
		switch ((Integer)systemVar.getDescriptor()){
			case astsym.SYSTEM_VAR_NAME:
				return generateSystemVarName((Integer)systemVar.getOp(0));
			case astsym.SYSTEM_PART_REF:
				return generateSystemPartRef((ASTNode)systemVar.getOp(0)) + "." + (String)systemVar.getOp(1); 
		}
		return "";
	}
	
	public String generateSystemVarName(Integer type){
		switch (type){
			case sym.NUM_PARTS:
				return "Environment.getNumParts()";
			case sym.NUM_STEPS:
				return "Environment.getNumSteps()";
			case sym.NUM_ACTIONS:
				return "Environment.getNumActions()";
			case sym.RANDI:
				return "Environment.randi()";
			case sym.RANDF:
				return "Environment.randf()";
		}
		return "";
	}
	
	public String generateSystemPartRef(ASTNode pr){
		switch ((Integer)pr.getDescriptor()){
			case sym.PART:
				return "Environment.participants.get(" + ((Integer)pr.getOp(0)).toString() + ")";
			case sym.ME:
				return "this";
		}
		return "";
	}
	
	public String generateFunctionCall(ASTNode systemVar){
		return "";
	}
	
	public String generateData(ASTNode data){
		switch ((Integer)data.getDescriptor()){
			case sym.NUMBER:
				return ((Integer)data.getOp(0))).toString();
				break;
			case astsym.SYSTEM_VAR:
				generateSystemVar((ASTNode)data.getOp(0));
				break;
			case astsym.FUNCTION_CALL:
				generateFunctionCall((ASTNode)data.getOp(0));
				break;
			case sym.DECIMAL:
				return ((Float)data.getOp(0))).toString();
				break;
			case sym.ID:
				return (String)data.getOp(0);
			break;
			case sym.TRUE:
				return "true";
			break;
			case sym.FALSE:
				return "false";
			break;
		}
		return "";
	}
		
	
}
