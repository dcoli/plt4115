package codegeneration;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java_cup.runtime.Symbol;

import compiler.syntax.*;
import compiler.settings.*;

public class CodeGenerator {
	
	private ASTNode root;
	private int currentLocation = 0;
	private PrintWriter pw;
	
	public static final String RUMVAR = "rumVar_";
	public static final String RUMACT = "rumAction_";
	
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
			pw.println("package rumble.runtime;");
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
			pw.println("\tpublic static String SIMULATION_NAME = \"" + simulationNode.getOp(0) + "\";");//meta op
			
			HashMap globalValues = assignmentListToHash((ASTNode)environmentConfig.getOp(0));
			
//			pw.println(generateInterface((ASTNode)environmentConfig.getOp(1)));
			
// other props of simulation node
//			   n.pushOp(ec);env config op 1
//			   n.pushOp(pcl);part config list op 2
			pw.println(generateEnd((ASTNode)simulationNode.getOp(3))); //end op

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
			h.put(generateLValueData((ASTNode)assignment.getOp(0)), generateExpression((ASTNode)assignment.getOp(1)));
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
		
		return "";
	}
		
	public String generateAssignment(ASTNode a){
		StringBuilder sb = new StringBuilder();
		switch ((Integer)a.getDescriptor()){
			case sym.TIMESEQ:
				sb.append(generateLValueData((ASTNode)a.getOp(0)) + " *= " + generateExpression((ASTNode)a.getOp(1)));
			break;
			case sym.DIVIDEEQ:
				sb.append(generateLValueData((ASTNode)a.getOp(0)) + " /= " + generateExpression((ASTNode)a.getOp(1)));
			break;
			case sym.MINUSEQ:
				sb.append(generateLValueData((ASTNode)a.getOp(0)) + " -= " + generateExpression((ASTNode)a.getOp(1)));
			break;
			case sym.PLUSEQ:
				sb.append(generateLValueData((ASTNode)a.getOp(0)) + " += " + generateExpression((ASTNode)a.getOp(1)));
			break;
			case sym.MODEQ:
				sb.append(generateLValueData((ASTNode)a.getOp(0)) + " %= " + generateExpression((ASTNode)a.getOp(1)));
			break;
			case sym.EQ:
				sb.append(generateLValueData((ASTNode)a.getOp(0)) + " = " + generateExpression((ASTNode)a.getOp(1)));
			break;
		}
	
		// sb.append(";\n"); // we think this line is necessary but aren't positive -- N&D
		
		return sb.toString();
	}
	
	public String generateLValueData(ASTNode a){
		switch ((Integer)a.getDescriptor()){
			case sym.ID:
				return CodeGenerator.RUMVAR + (String)a.getOp(0);
			case astsym.SYSTEM_VAR:
				return generateSystemVar((ASTNode)a.getOp(0));
		}	
		return "";	
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
	
	public String generateFunctionCall(ASTNode functionCall){
		StringBuilder sb = new StringBuilder();
		switch ((Integer)functionCall.getDescriptor()){
			case sym.ID:
				sb.append(CodeGenerator.RUMACT + (String)functionCall.getOp(0));
				if (functionCall.getOp(1) != null)
					sb.append("(this," + generateExpressionList((ASTNode)functionCall.getOp(1)) + ")");
				else
					sb.append("(this)");
			break;
			case astsym.STEP_CALL:
				sb.append(generateSystemPartRef((ASTNode)functionCall.getOp(0)) + ".step()");
			break;
		}
		
		return sb.toString();
	}
	
	public String generateExpressionList(ASTNode list){
		StringBuilder sb = new StringBuilder();
		while(list != null){
			sb.append(generateExpression((ASTNode)list.getOp(0)));
			if (list.getOp(1) != null) sb.append(", ");
			list = (ASTNode)list.getOp(1);
		}
		return sb.toString();
	}
	
	
	public String generateData(ASTNode data){
		switch ((Integer)data.getDescriptor()){
			case sym.NUMBER:
				return ((Integer)data.getOp(0)).toString();
			case astsym.SYSTEM_VAR:
				return generateSystemVar((ASTNode)data.getOp(0));
			case astsym.FUNCTION_CALL:
				return generateFunctionCall((ASTNode)data.getOp(0));
			case sym.DECIMAL:
				return ((Float)data.getOp(0)).toString();
			case sym.ID:
				return CodeGenerator.RUMVAR + (String)data.getOp(0);
			case sym.TRUE:
				return "true";
			case sym.FALSE:
				return "false";
		}
		return "";
	}
	
	public String generateIfStatement(ASTNode s) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("if (" + generateExpression((ASTNode)s.getOp(0)) + ") ");
		sb.append(generateStatement((ASTNode)s.getOp(1)));
		
		return sb.toString();
	}
	
	public String generateElseStatement(ASTNode s) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("if (" + generateExpression((ASTNode)s.getOp(0)) + ") ");
		sb.append(generateStatement((ASTNode)s.getOp(1)));
		sb.append("else " + generateStatement((ASTNode)s.getOp(2)));
		
		return sb.toString();
	}
	
	public String generateStatement(ASTNode s) {		
		switch ((Integer)s.getDescriptor()) {
			case astsym.BLOCK:
				return generateBlockStatement(s);
			case sym.IF:
				return generateIfStatement(s);
			case sym.ELSE:
				return generateElseStatement(s);
			case sym.WHILE:
				return generateWhileStatement(s);
			case sym.RETURN:
				return generateReturnStatement(s);
			case astsym.FUNCTION_CALL:
			case astsym.STEP_CALL:
				return generateFunctionCall(s); 
			case sym.TIMESEQ:
			case sym.DIVIDEEQ:
			case sym.MINUSEQ:
			case sym.PLUSEQ:
			case sym.MODEQ:
			case sym.EQ:
				return generateAssignment(s);
			case astsym.DECLARATION:
				return generateDeclaration(s);
		}
		
		return ""; // just to make Eclipse shut up about not returning a string...
	}
	
	public String generateDeclaration(ASTNode d) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(generateDataType((Integer)d.getOp(0)) + " " + generateIDList((ASTNode)d.getOp(1)));
		
		return sb.toString();
	}
	
	public String generateDataType(Integer dt) {
		
		switch (dt) {
		case sym.INT:
			return "int ";
		case sym.FLOAT:
			return "float ";
		case sym.BOOLEAN:
			return "boolean ";
		case sym.PARTICIPANT:
			return "participant ";
		}
		
		return "";
	}
	
	public String generateIDList(ASTNode list) {
		StringBuilder sb = new StringBuilder();
		
		if (list == null)
			return "";
		
		sb.append(generateID((ASTNode)list.getOp(0)));
		String s = generateIDList((ASTNode)list.getOp(1));
		if (!s.equals(""))
			sb.append(", ");
		sb.append(s);
		
		return sb.toString();
	}
	
	public String generateID(ASTNode i) {
		return CodeGenerator.RUMVAR + (String)(((Symbol)i.getOp(0)).value);
	}
	
	public String generateWhileStatement(ASTNode s) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("while (" + generateExpression((ASTNode)s.getOp(0)) + ") ");
		sb.append(generateStatement((ASTNode)s.getOp(1)));
		
		return sb.toString();
	}
	
	public String generateReturnStatement(ASTNode s) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("return " + generateExpression((ASTNode)s.getOp(0)) + ";\n");
		
		return sb.toString();
	}
	
	public String generateBlockStatement(ASTNode b) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{\n" + generateLines((ASTNode)b.getOp(0)) + "}\n");
		
		return sb.toString();
	}
	
	public String generateLines(ASTNode l) {
		StringBuilder sb = new StringBuilder();
		
		if (l == null) // base case
			return "";
		
		sb.append(generateStatement((ASTNode)l.getOp(0)) + "\n");
		sb.append(generateLines((ASTNode)l.getOp(1)));
		
		return sb.toString();
	}
	
	public String generateActions(ASTNode al) {
		StringBuilder sb = new StringBuilder();
		
		if (al == null)
			return "";
		
		sb.append(generateAction((ASTNode)al.getOp(0)) + "\n\n");
		sb.append(generateActions((ASTNode)al.getOp(1)));
		
		return sb.toString();
	}
	
	public String generateAction(ASTNode a) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("public void " + CodeGenerator.RUMACT + ((Symbol)a.getOp(0)).value);
		sb.append("(");
		
		if (((Integer)((ASTNode)a.getOp(1)).getDescriptor()) != astsym.BLOCK) {
			sb.append(generateArgList((ASTNode)a.getOp(1)));
		}
		sb.append(")");
		
		sb.append(generateBlockStatement((ASTNode)a.getOp(2)));
		
		return sb.toString();
	}
	
	public String generateArgList(ASTNode a) {
		StringBuilder sb = new StringBuilder();
		
		if (((Integer)a.getDescriptor()).equals(astsym.ARG))
			return sb.append(generateArg(a)).toString();
		
		sb.append(generateArg((ASTNode)a.getOp(0)) + ", ");
		sb.append(generateArgList((ASTNode)a.getOp(1)));
		
		return sb.toString();
	}
	
	public String generateArg(ASTNode a) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(generateDataType((Integer)a.getOp(0)));
		sb.append(" " + ((Symbol)a.getOp(1)).value);
		
		return sb.toString();
	}
	
	public String generateAttributeDeclarationList(ASTNode adl, int attType) {
		StringBuilder sb = new StringBuilder();
		
		if (adl == null)
			return "";
		
		sb.append(generateAttributeDeclaration((ASTNode)adl.getOp(0), attType));
		sb.append(generateAttributeDeclarationList((ASTNode)adl.getOp(1), attType));
		
		return sb.toString();
	}
	
	public String generateAttributeDeclaration(ASTNode ad, int attType) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("protected" + generateDeclaration((ASTNode)ad.getOp(0)));
		
		saveAttribute(ad, attType);
		
		return sb.toString();
	}
	
	// This will put the declaration in the settings structure
	public void saveAttribute(ASTNode dec, int attType) {
		
		int type = ((Integer)dec.getOp(0)).intValue();
		
		//This gets an IdList and then extracts the first ID.
		//Validation should make sure that only one ID exists for this declaration.
		String id = (String)((ASTNode)dec.getOp(1)).getOp(0);
		
		String constraint = "true";
		if (dec.getNumberOfOperands() == 2)
			constraint = generateExpression((ASTNode)dec.getOp(1));
		
		if (attType == sym.GLOBAL)
			Settings.persistGlobalDeclaration(id, constraint, type);
		else if (attType == sym.ATTRIBUTES)
			Settings.persistAttributeDeclaration(id, constraint, type);
		
		
	}

	public String generateEnd(ASTNode a) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("public boolean end()");
		
		sb.append(generateBlockStatement((ASTNode)a.getOp(0)));
		
		return sb.toString();
	}

}
