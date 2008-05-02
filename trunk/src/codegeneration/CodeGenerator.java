package codegeneration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.Iterator;

import java_cup.runtime.Symbol;

import compiler.syntax.*;
import compiler.settings.*;

public class CodeGenerator {
	
	private ASTNode root;

	public static final String RUMVAR = "rumVar_";
	public static final String RUMACTION = "rumAction_";
	public static final String PACKAGE_STRUCTURE = "/rumble/runtime/";
	
	public CodeGenerator(ASTNode root){
		this.root = root;
	}
	
	public void go(){
		
		//create packages if they're not there already
		(new File(Settings.outputPath + PACKAGE_STRUCTURE)).mkdirs();
		 
		writeEnvironmentFile(root);
		//writeParticipantFiles((ASTNode)simulationRoot.getOp(1));
		
	}
	
	public void writeEnvironmentFile(ASTNode root){
		ASTNode simulationFileNode = (ASTNode)root.getDescriptor(); 
		ASTNode environmentConfig = (ASTNode)simulationFileNode.getOp(1);
		ASTNode environmentFileNode = (ASTNode)root.getOp(0);
		
		try{
			//Get environment filename
			String envFile = (String)environmentConfig.getDescriptor();
			
			PrintWriter pw = new PrintWriter(new FileOutputStream(Settings.outputPath + PACKAGE_STRUCTURE + "Environment.java"));
			pw.println("package rumble.runtime;");
			pw.println("import java.util.*;");
			pw.println("public class Environment {");
			
			//system variables
			pw.println("\tprivate static Random rand;");
			pw.println("\tprivate static int numSteps;");
			pw.println("\tprivate static int numActions;");
			pw.println("\tprivate static ArrayList<AbstractParticipant> participants;");
			
			pw.println("\tpublic static int randi(){\n\t\treturn rand.nextInt();\n\t}");
			pw.println("\tpublic static float randf(){\n\t\treturn rand.nextFloat();\n\t}");
			pw.println("\tpublic static int getNumSteps(){\n\t\treturn numSteps;\n\t}");
			pw.println("\tpublic static int getNumActions(){\n\t\treturn numActions;\n\t}");
			pw.println("\tpublic static int getNumParts(){\n\t\treturn participants.size();\n\t}");
			
			pw.println("\tpublic static String ENVIRONMENT_NAME = \"" + environmentFileNode.getOp(0) + "\";");
			pw.println("\tpublic static String SIMULATION_NAME = \"" + simulationFileNode.getOp(0) + "\";");
			
//			=========================================================================================
			
			/* WRTING ABSTRACT PARTICIPANT NOW */
			ASTNode interfaceNode = (ASTNode)environmentFileNode.getOp(1);
			writeAbstractParticipant(interfaceNode);
			
//			=========================================================================================
			
			
			//WRITE GLOBAL DECLARATIONS
			LinkedList<Attribute> globals = makeAttributeList((ASTNode)interfaceNode.getOp(0));
			
			for (Iterator<Attribute> iter = globals.iterator(); iter.hasNext();){
				Attribute global = iter.next();
				pw.println("\tprivate static " 
						+ generateDataType(global.getType()) + " " + global.getId() + ";");
			}

			//WRITE GLOBAL GETTERS AND SETTERS
			for (Iterator<Attribute> iter = globals.iterator(); iter.hasNext();){
				Attribute global = iter.next();
				
				//the getter
				pw.println("\tpublic static " + generateDataType(global.getType()) 
													+ " get" + global.getId() + "() {\n\t\t return " + 
													global.getId() + ";\n\t}");

				//the setter
				pw.println("\tpublic static boolean set" + global.getId() + "(" + 
						generateDataType(global.getType()) + " value) {");
						pw.println("\t\t if (" + global.getConstraint() + 
							") {\n\t\t\tEnvironment." + global.getId() + " = value;\n\t\t\treturn true;\n\t\t}");
					
				pw.println("\t\telse return false;\n\t}");

				
			}
			//END WRITE GLOBALS			
			
			//toString
			pw.println("\t public String toString(){");
			
			pw.println("\t\treturn \"{\\n\\tenvironment : \\\"\" + this.ENVIRONMENT_NAME");
			for (Iterator<Attribute> iter = globals.iterator(); iter.hasNext();){
				Attribute global = iter.next();
				pw.println("\t\t\t" + "+ \"\\\",\\n\\t" +
						global.getId().substring(RUMVAR.length()) + " : \" + this." + global.getId());
			}
 			pw.println("\t\t+ \",\\n}\\n\";");
			pw.println("\t }");
			//end toString
			
			
			//step function
			pw.println("\tpublic void step()");
				pw.println(generateBlockStatement((ASTNode)environmentFileNode.getOp(2)));
			//end step function
			
			//CONSTRUCTOR
			pw.println("\tpublic Environment(){");
			pw.println("\t\trand = new Random();");
			
			//initialize globals
			ASTNode globalBlock = (ASTNode)environmentConfig.getOp(1);
			StringBuilder result = new StringBuilder("\t\tAbstractParticipant newParticipant;");
			
			while(globalBlock != null){
				ASTNode assignment = (ASTNode)globalBlock.getOp(0);
				
				result.append("\t\tset" + 
								generateLValueData((ASTNode)assignment.getOp(0)) + 
								"(" + generateExpression((ASTNode)assignment.getOp(1)) + ");\n");			
				
				globalBlock = (ASTNode)globalBlock.getOp(1);
			}
			
			
			//initialize participants
			pw.println("\t\tEnvironment.participants = new ArrayList();");
			pw.println(generateParticipantInitializations((ASTNode)simulationFileNode.getOp(2)));
			
			pw.println("\t}");
			//END CONSTRUCTOR
			
			
			//end function
			pw.print("\tpublic boolean end()");
			pw.println(generateBlockStatement((ASTNode)simulationFileNode.getOp(3)));
				
			//end end function
			
			//write actions
			ASTNode actionList = (ASTNode)environmentFileNode.getOp(3);
			while(actionList != null){
				ASTNode action = (ASTNode)actionList.getOp(0);
				pw.println(generateAction(action));
				actionList= (ASTNode)actionList.getOp(1);
			}
			//end write actions
						
			
			pw.println("}");
			pw.close();
			

		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Could not write environment file.");
		}
	}
		
	public void writeAbstractParticipant(ASTNode interfaceNode){
		try{
			
			PrintWriter abstractParticipantWriter = 
				new PrintWriter(Settings.outputPath + PACKAGE_STRUCTURE + "AbstractParticipant.java");
			
			abstractParticipantWriter.println("package rumble.runtime;");
			abstractParticipantWriter.println("import rumble.runtime.Environment;");

			abstractParticipantWriter.println("public abstract class AbstractParticipant extends Object");
			abstractParticipantWriter.println("{");
			abstractParticipantWriter.println("\t// SRS");
			abstractParticipantWriter.println("\tprotected String name;");
			abstractParticipantWriter.println("\tprotected String id;");
			abstractParticipantWriter.println("\tprotected Environment env;");
				
			abstractParticipantWriter.println("\t// custom attributes");

			LinkedList<Attribute> attributes = makeAttributeList((ASTNode)interfaceNode.getOp(1));
			
			for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();){
				Attribute attribute = iter.next();
				abstractParticipantWriter.println("\tprivate " 
						+ generateDataType(attribute.getType()) + " " + attribute.getId() + ";");
			}

			
			//CONSTRUCTOR
			
			//default 
			abstractParticipantWriter.println("\tpublic AbstractParticipant(String id){ this.id = id; }\n");
			
			//constructor with attributes
			/*abstractParticipantWriter.print("\tpublic AbstractParticipant(String id");
			for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();){
				Attribute attribute = iter.next();
				abstractParticipantWriter.print(", ");
				abstractParticipantWriter.print(generateDataType(attribute.getType()) + " " + attribute.getId());	
			}
			abstractParticipantWriter.println("){");
			
			for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();){
				Attribute attribute = iter.next();
				abstractParticipantWriter.println("\t\tset" + attribute.getId() + "(" + attribute.getId() + ");");
			}
			
			abstractParticipantWriter.println("\t\tthis.id = id;");
			
			abstractParticipantWriter.println("\t}");*/
			//END CONSTRUCTOR
			
			
			//GETTERS AND SETTERS
			for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();){
				Attribute attribute = iter.next();
				
				//the getter
				abstractParticipantWriter.println("\tpublic " + generateDataType(attribute.getType()) 
													+ " get" + attribute.getId() + "() {\n\t\t return this." + 
													attribute.getId() + ";\n\t}");

				//the setter
				abstractParticipantWriter.println("\tpublic boolean set" + attribute.getId() + "(" + 
						generateDataType(attribute.getType()) + " value) {");
					abstractParticipantWriter.println("\t\t if (" + attribute.getConstraint() + 
							") {\n\t\t\tthis." + attribute.getId() + " = value;\n\t\t\treturn true;\n\t\t}");
					
				abstractParticipantWriter.println("\t\telse return false;\n\t}");

				
			}			
			
			//toString
			abstractParticipantWriter.println("\t public String toString(){");
			abstractParticipantWriter.println("\t\treturn \"\\t\\t{\\n\\t\\t\\tparticipant : \\\"\" + this.id");
			for (Iterator<Attribute> iter = attributes.iterator(); iter.hasNext();){
				Attribute attribute = iter.next();
				abstractParticipantWriter.println("\t\t\t" + "+ \"\\\",\\n\\t\\t\\t" +
						attribute.getId().substring(RUMVAR.length()) + " : \" + this." + attribute.getId());
			}
 			abstractParticipantWriter.println("\t\t+ \"\\n\\t\\t},\\n\";");
			abstractParticipantWriter.println("\t }");
			//end toString
			
			//step function
			abstractParticipantWriter.println("\tpublic abstract void step();\n\n");
			
			abstractParticipantWriter.println("}");
			abstractParticipantWriter.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("Could not write AbstractParticipant file.");			
		}
		
	}
	
	public String generateParticipantInitializations(ASTNode participantConfigList){
		StringBuilder result = new StringBuilder("\t\tAbstractParticipant newParticipant;");
		
		while(participantConfigList != null){
			//get the assignment statement
			ASTNode participantConfig = (ASTNode)participantConfigList.getOp(0);
			String participantFileName = ((String)participantConfig.getOp(0));
			String participantType = participantFileName.substring(0, participantFileName.indexOf('.'));
			
			result.append("\t\t\n\nnewParticipant = new " + participantType + "(" + participantConfigList.getOp(1) + ");\n");
			
			if (participantConfig.getNumberOfOperands() == 3){
				ASTNode assignmentList = (ASTNode)participantConfigList.getOp(2);
				
				while(assignmentList != null){
					ASTNode assignment = (ASTNode)assignmentList.getOp(0);
					
					result.append("\t\tnewParticipant.set" + 
									generateLValueData((ASTNode)assignment.getOp(0)) + 
									"(" + generateExpression((ASTNode)assignment.getOp(1)) + ");\n");			
					
					assignmentList = (ASTNode)assignmentList.getOp(1);
				}
			}
			
			result.append("Environment.participants.add(newParticipant);\n\n");
			
			participantConfigList = (ASTNode)participantConfigList.getOp(1);
		}
		
		return result.toString();
	}
	
	public void writeParticipantFiles(ASTNode participantFileList){
		while(participantFileList != null){
			writeParticipant((ASTNode)participantFileList.getOp(0));			
			participantFileList = (ASTNode)participantFileList.getOp(1);
		}
	}
	
	//returns an initialzation string of a participant instance
	public void writeParticipant(ASTNode participant) 
	{
		try {
			PrintWriter pw = 
				new PrintWriter(Settings.outputPath + PACKAGE_STRUCTURE + participant.getDescriptor() + ".java");
			
			pw.println("package rumble.runtime;");
			pw.println("import java.util.*;");
			pw.println("public class " + participant.getDescriptor() + " extends AbstractParticipant {");
			pw.println("\tprotected String name = " + participant.getOp(0));
			
			//step function
			pw.println("\tpublic void step();\n\n");
				pw.println(generateBlockStatement((ASTNode)participant.getOp(1)));
			pw.println("}");
			//end step function
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Could not write participant file: " + participant.getDescriptor());
		}
	}
	
	
	
	public LinkedList makeAttributeList(ASTNode participantVarsNode){
		if (participantVarsNode == null) 
			return new LinkedList();
		else {
			ASTNode attributeDeclarationList = (ASTNode)participantVarsNode.getOp(0);
			LinkedList<Attribute> list = new LinkedList();
			
			while(attributeDeclarationList != null){
				ASTNode attributeDeclaration = (ASTNode)attributeDeclarationList.getOp(0);
					ASTNode declaration = (ASTNode)attributeDeclaration.getOp(0);
					
					//There should only be one id in the list
					String id = RUMVAR + (String)((ASTNode)declaration.getOp(1)).getOp(0);
					String constraint = "true"; 
					
					//if there's a constraint
					if (attributeDeclaration.getNumberOfOperands() == 2)
						constraint = generateExpression((ASTNode)attributeDeclaration.getOp(1));
					
				Attribute attribute = new Attribute(id, null, constraint, (Integer)declaration.getOp(0));
				list.addLast(attribute);
				attributeDeclarationList = (ASTNode)attributeDeclarationList.getOp(1);
			}
			return list;	
		}
	}
	
	public String generateExpression(ASTNode e){
		if (e == null) return "";
		
		StringBuilder sb = new StringBuilder();
		switch (((Integer)e.getDescriptor()).intValue()){
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
		
	public String generateAssignment(ASTNode a){
		StringBuilder sb = new StringBuilder();
		switch (((Integer)a.getDescriptor()).intValue()){
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
		switch (((Integer)a.getDescriptor()).intValue()){
			case sym.ID:
				return CodeGenerator.RUMVAR + (String)a.getOp(0);
			case astsym.SYSTEM_VAR:
				return generateSystemVar((ASTNode)a.getOp(0));
		}	
		return "";	
	}
	
	public String generateSystemVar(ASTNode systemVar){
		switch (((Integer)systemVar.getDescriptor()).intValue()){
			case astsym.SYSTEM_VAR_NAME:
				return generateSystemVarName((Integer)systemVar.getOp(0));
			case astsym.SYSTEM_PART_REF:
				return generateSystemPartRef((ASTNode)systemVar.getOp(0)) + "." + (String)systemVar.getOp(1); 
			case astsym.SYSTEM_GLOBAL:
				return "Environment." + CodeGenerator.RUMVAR + (String)systemVar.getOp(0);
		}
		return "";
	}
	
	public String generateSystemVarName(Integer type){
		switch (type.intValue()){
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
		switch (((Integer)pr.getDescriptor()).intValue()){
			case sym.PART:
				return "Environment.participants.get(" + ((Integer)pr.getOp(0)).toString() + ")";
			case sym.ME:
				return "this";
		}
		return "";
	}
	
	public String generateFunctionCall(ASTNode functionCall){
		StringBuilder sb = new StringBuilder();
		switch (((Integer)functionCall.getOp(0)).intValue()){
			case sym.ID:
				if(functionCall.getOp(1).equals("set")){
					//we've got a set call !
					ASTNode operandList = (ASTNode)functionCall.getOp(2);
					String operand1 = generateExpression((ASTNode)operandList.getOp(0));
					ASTNode operand2Node = (ASTNode)((ASTNode)operandList.getOp(1)).getOp(0); 
					
					if (operand1.startsWith("Environment.")) 
						return "Environment.set" + operand1.replace("Environment.", "") + "(" +	generateExpression(operand2Node) + ")";
					else 
						return "set" + operand1 + "(" +	generateExpression(operand2Node) + ")";
					
				}
				else
					sb.append(CodeGenerator.RUMACTION + (String)functionCall.getOp(1));
				if (functionCall.getOp(2) != null)
					sb.append("(this," + generateExpressionList((ASTNode)functionCall.getOp(2)) + ")");
				else
					sb.append("(this)");
			break;
			case astsym.STEP_CALL:
				sb.append(generateSystemPartRef((ASTNode)functionCall.getOp(1)) + ".step()");
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
		switch (((Integer)data.getDescriptor()).intValue()){
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
		switch (((Integer)s.getDescriptor()).intValue()) {
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
				return generateFunctionCall(s) + ";"; 
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
		
		sb.append(" {\n" + generateLines((ASTNode)b.getOp(0)) + "}\n");
		
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
		
		sb.append("public void " + CodeGenerator.RUMACTION + ((Symbol)a.getOp(0)).value);
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
		
		sb.append("protected " + generateDeclaration((ASTNode)ad.getOp(0)));
		
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


}
