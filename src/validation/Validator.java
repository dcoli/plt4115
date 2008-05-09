package validation;

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

public class Validator {

	private ASTNode root;

	private static int place;

	public static final int IN_ENVIRONMENT_STEP = 1;

	public static final int IN_ENVIRONMENT_END = 1;

	public static final int IN_ACTION = 2;

	public static final int IN_PARTICIPANT_STEP = 3;

	public static final String RUMVAR = "rumVar_";

	public static final String RUMACTION = "rumAction_";

	public static final String PACKAGE_STRUCTURE = "/rumble/runtime/";

	public static final String PARTICIPANT_CLASS_NAME = "participant";

	private static int blockLevel = 0;

	public static void debugGeneration(String message) {
		if (Settings.debug) {
			for (int i = 0; i < blockLevel; i++)
				System.out.print("\t");

			System.out.println(message);
		}
	}

	public Validator(ASTNode root) {
		this.root = root;
	}

	public void go() {

		// create packages if they're not there already
//		(new File(Settings.outputPath + PACKAGE_STRUCTURE)).mkdirs();

		checkEnvironmentFile(root);
//		writeParticipantFiles((ASTNode) root.getOp(1));
//		writeRuntimeFile();

		checkNot(root);
	}

	public boolean checkNot( ASTNode root ) {
		ASTNode endnode = (ASTNode) root.getOp(3);
		ASTNode linesNode = (ASTNode) endnode.getOp(0);
		ASTNode statementNode = (ASTNode) linesNode.getOp(0);
		switch ((Integer)statementNode.getDescriptor()) {
		case sym.RETURN:
//			ASTNode returnNode = statementNode.getOp(0);
			ASTNode expressionNode = (ASTNode) statementNode.getOp(0);
		
		return true;
	}
	
	public void checkEnvironmentFile(ASTNode root) {
		ASTNode simulationFileNode = (ASTNode) root.getDescriptor();
		ASTNode environmentConfig = (ASTNode) simulationFileNode.getOp(1);
		ASTNode environmentFileNode = (ASTNode) root.getOp(0);

		debugGeneration("Validating environment file.");
//		System.out.println((String)environmentFileNode.getOp(0));
//			if (
//				Character.isDigit(((String)environmentFileNode.getOp(0)).charAt(0)) 
//				)
//			System.out.println("Environment file name must begin with a letter.");
//			// =========================================================================================
//
//			/* WRTING ABSTRACT PARTICIPANT NOW */
			ASTNode interfaceNode = (ASTNode) environmentFileNode.getOp(1);
			writeAbstractParticipant(interfaceNode);
//
//			// =========================================================================================
//
//			debugGeneration("Writing globals...");
//			// WRITE GLOBAL DECLARATIONS
//			LinkedList<Attribute> globals = makeAttributeList((ASTNode) interfaceNode
//					.getOp(0));
//
//			for (Iterator<Attribute> iter = globals.iterator(); iter.hasNext();) {
//				Attribute global = iter.next();
//				pw.println("\tprivate static "
//						+ generateDataType(global.getType()) + " "
//						+ global.getId() + ";");
//			}
//
//			// WRITE GLOBAL GETTERS AND SETTERS
//			for (Iterator<Attribute> iter = globals.iterator(); iter.hasNext();) {
//				Attribute global = iter.next();
//
//				// the getter
//				pw.println("\tpublic static "
//						+ generateDataType(global.getType()) + " get"
//						+ global.getId() + "() {\n\t\t return "
//						+ global.getId() + ";\n\t}");
//
//				// the setter
//				pw.println("\tpublic static boolean set" + global.getId() + "("
//						+ generateDataType(global.getType()) + " value) {");
//				pw.println("\t\t if (" + global.getConstraint()
//						+ ") {\n\t\t\tEnvironment." + global.getId()
//						+ " = value;\n\t\t\treturn true;\n\t\t}");
//
//				pw.println("\t\telse return false;\n\t}");
//
//			}
//			// END WRITE GLOBALS
//
//			// participantStrings
//			pw.println("\tpublic String participantStrings() {");
//			pw.println("\t\tStringBuilder sb = new StringBuilder();");
//
//			pw.println("\t\tsb.append(\"\\tparticpants :\\n\\t[\\n\");");
//
//			pw.println("\t\tfor (participant p : this.participants)");
//			pw.println("\t\t\tsb.append(p);");
//
//			pw.println("\t\tsb.append(\"\\t],\\n\");");
//			pw.println("\treturn sb.toString();");
//			pw.println("\t}");
//			// end participantStrings()
//
//			// toString
//			pw.println("\t public String toString(){");
//
//			pw
//					.println("\t\treturn \"{\\n\\tenvironment : \\\"\" + this.ENVIRONMENT_NAME + \"\\\",\\n\" + participantStrings()");
//			pw.println("\t\t\t"
//					+ "+ \"\\n\\tsimulation : \" + this.SIMULATION_NAME");
//			for (Iterator<Attribute> iter = globals.iterator(); iter.hasNext();) {
//				Attribute global = iter.next();
//				pw.println("\t\t\t" + "+ \"\\\",\\n\\t"
//						+ global.getId().substring(RUMVAR.length())
//						+ " : \" + this." + global.getId());
//			}
//			pw.println("\t\t+ \",\\n}\\n\";");
//			pw.println("\t }");
//			// end toString
//
//			// getLastParticipant
//			pw.println("\tpublic static int getLastParticipant(){");
//			pw.println("\t\tif (lastParticipantId == null) return 0;");
//			pw.println("\t\t\tfor (int i = 0; i < participants.size(); i++)");
//			pw.println("\t\t\t\tif (participants.get(i).getId().equals(lastParticipantId))");
//			pw.println("\t\treturn i;");
//
//			pw.println("\t\treturn 0;");
//			pw.println("\t}");
//
//			debugGeneration("\nWriting environment step function.");
//			place = IN_ENVIRONMENT_STEP;
//			// step function
//			pw.println("\tpublic void step()");
//			pw.println(generateBlockStatement((ASTNode) environmentFileNode
//					.getOp(2), "", "numSteps++;"));
//			// end step function
//			debugGeneration("End environment step.");
//
//			// CONSTRUCTOR
//			pw.println("\tpublic Environment(){");
//			pw.println("\t\trand = new Random(System.currentTimeMillis());");
//
//			// initialize globals
//			ASTNode globalBlock = (ASTNode) environmentConfig.getOp(1);
//			StringBuilder result = new StringBuilder("\t\t"
//					+ PARTICIPANT_CLASS_NAME + " newParticipant;");
//
//			while (globalBlock != null) {
//				ASTNode assignment = (ASTNode) globalBlock.getOp(0);
//
//				result.append("\t\tset"
//						+ generateLValueData((ASTNode) assignment.getOp(0))
//						+ "("
//						+ generateExpression((ASTNode) assignment.getOp(1))
//						+ ");\n");
//
//				globalBlock = (ASTNode) globalBlock.getOp(1);
//			}
//
//			// initialize participants
//			pw.println("\t\tEnvironment.participants = new ArrayList();");
//			pw
//					.println(generateParticipantInitializations((ASTNode) simulationFileNode
//							.getOp(2)));
//
//			pw.println("\t}");
//			// END CONSTRUCTOR
//
//			debugGeneration("\nWriting end function.");
//			place = IN_ENVIRONMENT_END;
//			// end function
//			pw.print("\tpublic boolean end()");
//			pw.println(generateBlockStatement((ASTNode) simulationFileNode
//					.getOp(3)));
//			// end end function
//			debugGeneration("End end function.");
//
//			// write actions
//			place = IN_ACTION;
//			ASTNode actionList = (ASTNode) environmentFileNode.getOp(3);
//			while (actionList != null) {
//				ASTNode action = (ASTNode) actionList.getOp(0);
//				pw.println(generateAction(action));
//				actionList = (ASTNode) actionList.getOp(1);
//			}
//			// end write actions
//
//			pw.println("}");
//			pw.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.err.println("Could not write environment file.");
//		}
	}

	public void writeAbstractParticipant(ASTNode interfaceNode) {
//		try {

			debugGeneration("\nChecking abstract participant file.");

//			PrintWriter abstractParticipantWriter = new PrintWriter(
//					Settings.outputPath + PACKAGE_STRUCTURE
//							+ PARTICIPANT_CLASS_NAME + ".java");
//
//			abstractParticipantWriter.println("package rumble.runtime;");
//			abstractParticipantWriter
//					.println("import rumble.runtime.Environment;");
//
//			abstractParticipantWriter.println("public abstract class "
//					+ PARTICIPANT_CLASS_NAME + " extends Object");
//			abstractParticipantWriter.println("{");
//			abstractParticipantWriter.println("\t// SRS");
//			abstractParticipantWriter.println("\tprotected String name;");
//			abstractParticipantWriter.println("\tprotected String id;");
//
//			abstractParticipantWriter.println("\t// custom attributes");
//
			LinkedList<Attribute> attributes = makeAttributeList((ASTNode) interfaceNode
					.getOp(1));

			for (Iterator<Attribute> iter = attributes.iterator(); iter
					.hasNext();) {
				Attribute attribute = iter.next();
				System.out.println(generateDataType(attribute.getType()));
				System.out.println(" was " + attribute.getId());
			}
//
//			// CONSTRUCTOR
//
//			// default
//			abstractParticipantWriter.println("\tpublic "
//					+ PARTICIPANT_CLASS_NAME
//					+ " (String id){ this.id = id; }\n");
//			// END CONSTRUCTOR
//
//			// GETTERS AND SETTERS
//			for (Iterator<Attribute> iter = attributes.iterator(); iter
//					.hasNext();) {
//				Attribute attribute = iter.next();
//
//				// the getter
//				abstractParticipantWriter.println("\tpublic "
//						+ generateDataType(attribute.getType()) + " get"
//						+ attribute.getId() + "() {\n\t\t return this."
//						+ attribute.getId() + ";\n\t}");
//
//				// the setter
//				abstractParticipantWriter.println("\tpublic boolean set"
//						+ attribute.getId() + "("
//						+ generateDataType(attribute.getType()) + " value) {");
//				abstractParticipantWriter.println("\t\t" + generateDataType(attribute.getType())
//						+ " oldValue = " + attribute.getId() + ";");
//
//				abstractParticipantWriter.println("\t\t if ("
//						+ attribute.getConstraint() + ") {\n\t\t\tthis."
//						+ attribute.getId()
//						+ " = value;\n\t\t\treturn true;\n\t\t}");
//
//				abstractParticipantWriter.println("\t\t" + attribute.getId()
//						+ " = oldValue;");
//
//				abstractParticipantWriter.println("\t\treturn false;\n\t}");
//
//			}
//
//			// toString
//			abstractParticipantWriter.println("\t public String toString(){");
//			abstractParticipantWriter
//					.println("\t\treturn \"\\t\\t{\\n\\t\\t\\tparticipant : \\\"\" + this.id");
//			for (Iterator<Attribute> iter = attributes.iterator(); iter
//					.hasNext();) {
//				Attribute attribute = iter.next();
//				abstractParticipantWriter.println("\t\t\t"
//						+ "+ \"\\\",\\n\\t\\t\\t"
//						+ attribute.getId().substring(RUMVAR.length())
//						+ " : \" + this." + attribute.getId());
//			}
//			abstractParticipantWriter.println("\t\t+ \"\\n\\t\\t},\\n\";");
//			abstractParticipantWriter.println("\t }");
//			// end toString
//
//			// step function
//			abstractParticipantWriter
//					.println("\tpublic abstract void step();\n\n");
//
//			abstractParticipantWriter
//					.println("\tpublic String getId(){ return id; }\n\n");
//
//			abstractParticipantWriter.println("}");
//			abstractParticipantWriter.close();
//			debugGeneration("Done.\n");
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.err.println("Could not write abstract participant class.");
//		}

	}

	public String generateParticipantInitializations(
			ASTNode participantConfigList) {
//		StringBuilder result = new StringBuilder("\t\t "
//				+ PARTICIPANT_CLASS_NAME + " newParticipant;");
//
//		while (participantConfigList != null) {
//			// get the assignment statement
//			ASTNode participantConfig = (ASTNode) participantConfigList
//					.getOp(0);
//			String participantFileName = ((String) participantConfig.getOp(0));
//			String participantType = participantFileName.substring(0,
//					participantFileName.indexOf('.'));
//
//			result.append("\t\t\n\nnewParticipant = new " + participantType
//					+ "(\"" + participantConfig.getOp(1) + "\");\n");
//
//			if (participantConfig.getNumberOfOperands() == 3) {
//				ASTNode assignmentList = (ASTNode) participantConfigList
//						.getOp(2);
//
//				while (assignmentList != null) {
//					ASTNode assignment = (ASTNode) assignmentList.getOp(0);
//
//					result.append("\t\tnewParticipant.set"
//							+ generateLValueData((ASTNode) assignment.getOp(0))
//							+ "("
//							+ generateExpression((ASTNode) assignment.getOp(1))
//							+ ");\n");
//
//					assignmentList = (ASTNode) assignmentList.getOp(1);
//				}
//			}
//
//			result.append("Environment.participants.add(newParticipant);\n\n");
//
//			participantConfigList = (ASTNode) participantConfigList.getOp(1);
//		}
//
//		return result.toString();
		return "";
	}

	public void writeParticipantFiles(ASTNode participantFileList) {
//		while (participantFileList != null) {
//			writeParticipant((ASTNode) participantFileList.getOp(0));
//			participantFileList = (ASTNode) participantFileList.getOp(1);
//		}
	}

	// returns an initialzation string of a participant instance
	public void writeParticipant(ASTNode participant) {
//		try {
//			PrintWriter pw = new PrintWriter(Settings.outputPath
//					+ PACKAGE_STRUCTURE + participant.getDescriptor() + ".java");
//
			debugGeneration("\nChecking participant file: "
					+ participant.getDescriptor());

//			pw.println("package rumble.runtime;");
//			pw.println("import java.util.*;");
//			pw.println("public class " + participant.getDescriptor()
//					+ " extends " + PARTICIPANT_CLASS_NAME + " {");
//			pw.println("public " + participant.getDescriptor()
//					+ "(String id){ super(id); }");
//			pw.println("\tprotected String name = \"" + participant.getOp(0)
//					+ "\";");
//
//			// step function
//			place = IN_PARTICIPANT_STEP;
//			pw.println("\tpublic void step()");
//			pw.println(generateBlockStatement((ASTNode) participant.getOp(1),
//					"\nEnvironment.lastParticipantId = this.id;\n", ""));
//			pw.println("}");
//			// end step function
//			pw.close();
			debugGeneration("End participant file.\n");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Could not write participant file: "
//					+ participant.getDescriptor());
//		}
	}

	public LinkedList makeAttributeList(ASTNode participantVarsNode) {
//		if (participantVarsNode == null)
//			return new LinkedList();
//		else {
//			ASTNode attributeDeclarationList = (ASTNode) participantVarsNode
//					.getOp(0);
			LinkedList<Attribute> list = new LinkedList();
//
//			while (attributeDeclarationList != null) {
//				ASTNode attributeDeclaration = (ASTNode) attributeDeclarationList
//						.getOp(0);
//				ASTNode declaration = (ASTNode) attributeDeclaration.getOp(0);
//
//				// There should only be one id in the list
//				String id = RUMVAR
//						+ (String) ((ASTNode) declaration.getOp(1)).getOp(0);
//				String constraint = "true";
//
//				// if there's a constraint
//				if (attributeDeclaration.getNumberOfOperands() == 2)
//					constraint = generateExpression((ASTNode) attributeDeclaration
//							.getOp(1));
//
//				Attribute attribute = new Attribute(id, null, constraint,
//						(Integer) declaration.getOp(0));
//				list.addLast(attribute);
//				attributeDeclarationList = (ASTNode) attributeDeclarationList
//						.getOp(1);
//			}
			return list;
//		}
	}

	public String generateExpression(ASTNode e) {
		debugGeneration("Checking expression.");
		if (e == null)
			return "";

//		StringBuilder sb = new StringBuilder();
		switch (((Integer) e.getDescriptor()).intValue()) {
//		case sym.OR:
////			sb.append(generateExpression((ASTNode) e.getOp(0)) + " || "
////					+ generateExpression((ASTNode) e.getOp(1)));
////			generateExpression((ASTNode) e.getOp(0)) generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.AND:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " && "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.EQEQ:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " == "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.NOTEQ:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " != "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.LT:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " < "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.GT:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " > "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.LTEQ:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " <= "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.GTEQ:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " >= "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
		case sym.PLUS:
			if( ((Integer)((ASTNode)e.getOp(1)).getDescriptor()) == sym.NOT) {
				System.out.println("Rumble won't let you use 'not' in an arithmetic expression");
			}
			break;
//		case sym.MINUS:
//			if (e.getNumberOfOperands() == 2)
//				sb.append(generateExpression((ASTNode) e.getOp(0)) + " - "
//						+ generateExpression((ASTNode) e.getOp(1)));
//			else
//				sb.append(" -" + generateExpression((ASTNode) e.getOp(0)));
//			break;
//		case sym.TIMES:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " * "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.MOD:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " % "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.DIVIDE:
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " / "
//					+ generateExpression((ASTNode) e.getOp(1)));
//			break;
//		case sym.NOT:
//			sb.append(" !" + generateExpression((ASTNode) e.getOp(0)));
//			break;
//		case sym.LPREN:
//			sb.append(" (" + generateExpression((ASTNode) e.getOp(0)) + ") ");
//			break;
//		default:
//			sb.append(generateData(e));
//		}
//		return sb.toString();
		}
		return ("");
	}

	public String generateAssignment(ASTNode a) {
		debugGeneration("Checking assignment statement.");

//		StringBuilder sb = new StringBuilder();
//		switch (((Integer) a.getDescriptor()).intValue()) {
//		case sym.TIMESEQ:
//			sb.append(generateLValueData((ASTNode) a.getOp(0)) + " *= "
//					+ generateExpression((ASTNode) a.getOp(1)));
//			break;
//		case sym.DIVIDEEQ:
//			sb.append(generateLValueData((ASTNode) a.getOp(0)) + " /= "
//					+ generateExpression((ASTNode) a.getOp(1)));
//			break;
//		case sym.MINUSEQ:
//			sb.append(generateLValueData((ASTNode) a.getOp(0)) + " -= "
//					+ generateExpression((ASTNode) a.getOp(1)));
//			break;
//		case sym.PLUSEQ:
//			sb.append(generateLValueData((ASTNode) a.getOp(0)) + " += "
//					+ generateExpression((ASTNode) a.getOp(1)));
//			break;
//		case sym.MODEQ:
//			sb.append(generateLValueData((ASTNode) a.getOp(0)) + " %= "
//					+ generateExpression((ASTNode) a.getOp(1)));
//			break;
//		case sym.EQ:
//			sb.append(generateLValueData((ASTNode) a.getOp(0)) + " = "
//					+ generateExpression((ASTNode) a.getOp(1)));
//			break;
//		}
//
//		sb.append(";\n");
//
//		return sb.toString();
		return ("");
	}

	public String generateLValueData(ASTNode a) {
		debugGeneration("Checking lvalue data.");

//		switch (((Integer) a.getDescriptor()).intValue()) {
//		case sym.ID:
//			return Validator.RUMVAR + (String) a.getOp(0);
//		case astsym.SYSTEM_VAR:
//			return generateSystemVar((ASTNode) a.getOp(0));
//		}
		return "";
	}

	public String generateSystemVar(ASTNode systemVar) {
		debugGeneration("Checking system variable.");

//		switch (((Integer) systemVar.getDescriptor()).intValue()) {
//		case astsym.SYSTEM_VAR_NAME:
//			return generateSystemVarName((Integer) systemVar.getOp(0));
//		case astsym.SYSTEM_PART_REF:
//			return generateSystemPartRef((ASTNode) systemVar.getOp(0)) + ".get"
//					+ RUMVAR + (String) systemVar.getOp(1) + "()";
//		case astsym.SYSTEM_GLOBAL:
//			return "Environment.get" + RUMVAR + (String) systemVar.getOp(0)
//					+ "()";
//		}
		return "";
	}

	public String generateSystemVarName(Integer type) {
		debugGeneration("Checking system variable name.");

//		switch (type.intValue()) {
//		case sym.NUM_PARTS:
//			return "Environment.getNumParts()";
//		case sym.NUM_STEPS:
//			return "Environment.getNumSteps()";
//		case sym.NUM_ACTIONS:
//			return "Environment.getNumActions()";
//		case sym.RANDI:
//			return "Environment.randi()";
//		case sym.RANDF:
//			return "Environment.randf()";
//		case sym.LAST_PART:
//			return "Environment.getLastParticpant()";
//		}
		return "";
	}

	public String generateSystemPartRef(ASTNode pr) {
		debugGeneration("Checking system participant reference.");

//		switch (((Integer) pr.getDescriptor()).intValue()) {
//		case sym.PART:
//			return "Environment.participants.get("
//					+ ((Integer) pr.getOp(0)).toString() + ")";
//		case sym.ME:
//			if (place == IN_ACTION)
//				return "doer";
//			else if (place == IN_PARTICIPANT_STEP)
//				return "this";
//		}
		return "";
	}

	public String generateFunctionCall(ASTNode functionCall) {
		debugGeneration("Checking function call.");

//		StringBuilder sb = new StringBuilder();
//		switch (((Integer) functionCall.getOp(0)).intValue()) {
//		case sym.ID:
//			if (functionCall.getOp(1).equals("set")) {
//				// we've got a set call !
//				ASTNode operandList = (ASTNode) functionCall.getOp(2);
//				ASTNode operand1Node = (ASTNode) ((ASTNode) operandList
//						.getOp(0)).getOp(0);
//				ASTNode operand2Node = (ASTNode) ((ASTNode) operandList
//						.getOp(1)).getOp(0);
//
//				if ((Integer) operand1Node.getDescriptor() == astsym.SYSTEM_PART_REF) {
//					if ((Integer) ((ASTNode) operand1Node.getOp(0))
//							.getDescriptor() == sym.PART)
//						return "Environment.participants.get("
//								+ (Integer) ((ASTNode) operand1Node.getOp(0))
//										.getOp(0) + ").set" + RUMVAR
//								+ (String) operand1Node.getOp(1) + "("
//								+ generateExpression(operand2Node) + ")";
//					else if ((Integer) ((ASTNode) operand1Node.getOp(0))
//							.getDescriptor() == sym.ME)
//						return "doer.set" + RUMVAR
//								+ (String) operand1Node.getOp(1) + "("
//								+ generateExpression(operand2Node) + ")";
//				} else if ((Integer) operand1Node.getDescriptor() == astsym.SYSTEM_GLOBAL)
//					return "Environment.set" + RUMVAR
//							+ (String) operand1Node.getOp(0) + "("
//							+ generateExpression(operand2Node) + ")";
//
//			} else
//				sb.append("Environment." + Validator.RUMACTION
//						+ (String) functionCall.getOp(1));
//			if (functionCall.getOp(2) != null)
//				sb.append("(this,"
//						+ generateExpressionList((ASTNode) functionCall
//								.getOp(2)) + ")");
//			else
//				sb.append("(this)");
//			break;
//		case astsym.STEP_CALL:
//			sb.append(generateSystemPartRef((ASTNode) functionCall.getOp(1))
//					+ ".step()");
//			break;
//		}
//
//		return sb.toString();
		return ("");
	}

	public String generateExpressionList(ASTNode list) {
		debugGeneration("Checking expression list.");

//		StringBuilder sb = new StringBuilder();
//		while (list != null) {
//			sb.append(generateExpression((ASTNode) list.getOp(0)));
//			if (list.getOp(1) != null)
//				sb.append(", ");
//			list = (ASTNode) list.getOp(1);
//		}
//		return sb.toString();
		return "";
	}

	public String generateData(ASTNode data) {
		debugGeneration("Checking data.");

//		switch (((Integer) data.getDescriptor()).intValue()) {
//		case sym.NUMBER:
//			return ((Integer) data.getOp(0)).toString();
//		case astsym.SYSTEM_VAR:
//			return generateSystemVar((ASTNode) data.getOp(0));
//		case astsym.FUNCTION_CALL:
//			return generateFunctionCall((ASTNode) data.getOp(0));
//		case sym.DECIMAL:
//			return ((Float) data.getOp(0)).toString();
//		case sym.ID:
//			return Validator.RUMVAR + (String) data.getOp(0);
//		case sym.TRUE:
//			return "true";
//		case sym.FALSE:
//			return "false";
//		}
		return "";
	}

	public String generateIfStatement(ASTNode s) {
		debugGeneration("Checking if statement.");
//		blockLevel++;
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("if (" + generateExpression((ASTNode) s.getOp(0)) + ") ");
//		sb.append(generateStatement((ASTNode) s.getOp(1)));
//		blockLevel--;
//		return sb.toString();
		return "";
	}

	public String generateElseStatement(ASTNode s) {
		debugGeneration("Checking else statement.");
//		blockLevel++;
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("if (" + generateExpression((ASTNode) s.getOp(0)) + ") ");
//		sb.append(generateStatement((ASTNode) s.getOp(1)));
//		sb.append("else " + generateStatement((ASTNode) s.getOp(2)));
//		blockLevel--;
//		return sb.toString();
		return "";
	}

	public String generateStatement(ASTNode s) {
//		switch (((Integer) s.getDescriptor()).intValue()) {
//		case astsym.BLOCK:
//			return generateBlockStatement(s);
//		case sym.IF:
//			return generateIfStatement(s);
//		case sym.ELSE:
//			return generateElseStatement(s);
//		case sym.WHILE:
//			return generateWhileStatement(s);
//		case sym.RETURN:
//			return generateReturnStatement(s);
//		case astsym.FUNCTION_CALL:
//		case astsym.STEP_CALL:
//			return generateFunctionCall(s) + ";";
//		case sym.TIMESEQ:
//		case sym.DIVIDEEQ:
//		case sym.MINUSEQ:
//		case sym.PLUSEQ:
//		case sym.MODEQ:
//		case sym.EQ:
//			return generateAssignment(s);
//		case astsym.DECLARATION:
//			return generateDeclaration(s);
		return ""; // just to make Eclipse shut up about not returning a
		// string...
	}

	public String generateDeclaration(ASTNode d) {
		debugGeneration("Checking declaration.");

//		StringBuilder sb = new StringBuilder();
//
//		sb.append(generateDataType((Integer) d.getOp(0)) + " "
//				+ generateIDList((ASTNode) d.getOp(1)));
//
//		return sb.toString();
		return "";
	}

	public String generateDataType(Integer dt) {
		debugGeneration("Checking datatype.");

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
		debugGeneration("Checking id list.");

//		StringBuilder sb = new StringBuilder();
//
//		if (list == null)
//			return "";
//
//		sb.append(generateID((ASTNode) list.getOp(0)));
//		String s = generateIDList((ASTNode) list.getOp(1));
//		if (!s.equals(""))
//			sb.append(", ");
//		sb.append(s);
//
//		return sb.toString();
		return "";
	}

	public String generateID(ASTNode i) {
		debugGeneration("Checking identifier.");

//		return Validator.RUMVAR + (String) (((Symbol) i.getOp(0)).value);
		return "";
	}

	public String generateWhileStatement(ASTNode s) {
//		blockLevel++;
		debugGeneration("Checking while statement.");
//
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("while (" + generateExpression((ASTNode) s.getOp(0)) + ") ");
//		sb.append(generateStatement((ASTNode) s.getOp(1)));
//
//		blockLevel--;
//		return sb.toString();
		return "";
	}

	public String generateReturnStatement(ASTNode s) {
		debugGeneration("Checking return statement.");

//		StringBuilder sb = new StringBuilder();
//
//		sb.append("return " + generateExpression((ASTNode) s.getOp(0)) + ";\n");
//
//		return sb.toString();
		return "";
	}

	public String generateBlockStatement(ASTNode b, String linesBefore,
			String linesAfter) {
//		blockLevel++;
		debugGeneration("Checking block.");
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(" {\n" + linesBefore + "\n"
//				+ generateLines((ASTNode) b.getOp(0)) + "\n" + linesAfter
//				+ "\n}\n");
//		blockLevel--;
//		return sb.toString();
		return "";
	}

	public String generateBlockStatement(ASTNode b) {
//		return generateBlockStatement(b, "", "");
		return "";
	}

	public String generateLines(ASTNode l) {
//		StringBuilder sb = new StringBuilder();
//
//		if (l == null) // base case
//			return "";
//
//		sb.append(generateStatement((ASTNode) l.getOp(0)) + "\n");
//		sb.append(generateLines((ASTNode) l.getOp(1)));
//
//		return sb.toString();
		return "";
	}

	public String generateActions(ASTNode al) {
		debugGeneration("Checking actions.");

//		StringBuilder sb = new StringBuilder();
//
//		if (al == null)
//			return "";
//
//		sb.append(generateAction((ASTNode) al.getOp(0)) + "\n\n");
//		sb.append(generateActions((ASTNode) al.getOp(1)));
//
//		return sb.toString();
		return "";
	}

	public String generateAction(ASTNode a) {
//		blockLevel++;
		debugGeneration("Checking action: " + a.getDescriptor());

//		StringBuilder sb = new StringBuilder();
//
//		sb.append("public static void " + Validator.RUMACTION
//				+ a.getDescriptor());
//		String addLine = "System.out.println(\"{\\n\\taction : \\\"'\" + doer.getId() + \"' did '"
//				+ a.getDescriptor() + "'\\\",\\n}\\n\");";
//
//		if (a.getNumberOfOperands() == 2) {
//			sb.append("(" + PARTICIPANT_CLASS_NAME + " doer, ");
//			sb.append(generateArgList((ASTNode) a.getOp(0)));
//			sb.append(")");
//			sb
//					.append(generateBlockStatement((ASTNode) a.getOp(1),
//							addLine, ""));
//		} else {
//			sb.append("(" + PARTICIPANT_CLASS_NAME + " doer)");
//			sb
//					.append(generateBlockStatement((ASTNode) a.getOp(0),
//							addLine, ""));
//		}
//
//		blockLevel--;
//		return sb.toString();
		return "";
	}

	public String generateArgList(ASTNode a) {
		debugGeneration("Checking arg list.");
//		StringBuilder sb = new StringBuilder();
//
//		if (((Integer) a.getDescriptor()).equals(astsym.ARG))
//			return sb.append(generateArg(a)).toString();
//
//		sb.append(generateArg((ASTNode) a.getOp(0)) + ", ");
//		sb.append(generateArgList((ASTNode) a.getOp(1)));
//
//		return sb.toString();
		return "";
	}

	public String generateArg(ASTNode a) {
		debugGeneration("Checking argument.");
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(generateDataType((Integer) a.getOp(0)));
//		sb.append(" " + ((Symbol) a.getOp(1)).value);
//
//		return sb.toString();
		return "";
	}

	public String generateAttributeDeclarationList(ASTNode adl, int attType) {
//		blockLevel++;
		debugGeneration("Checking attribute declaration list.");
//
//		StringBuilder sb = new StringBuilder();
//
//		if (adl == null)
//			return "";
//
//		sb
//				.append(generateAttributeDeclaration((ASTNode) adl.getOp(0),
//						attType));
//		sb.append(generateAttributeDeclarationList((ASTNode) adl.getOp(1),
//				attType));
//		blockLevel--;
//		return sb.toString();
		return "";
	}

	public String generateAttributeDeclaration(ASTNode ad, int attType) {
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("protected " + generateDeclaration((ASTNode) ad.getOp(0)));
//
//		saveAttribute(ad, attType);
//
//		return sb.toString();
		return "";
	}

	// This will put the declaration in the settings structure
	public void saveAttribute(ASTNode dec, int attType) {

		int type = ((Integer) dec.getOp(0)).intValue();

		// This gets an IdList and then extracts the first ID.
		// Validation should make sure that only one ID exists for this
		// declaration.
//		String id = (String) ((ASTNode) dec.getOp(1)).getOp(0);
//
//		String constraint = "true";
//		if (dec.getNumberOfOperands() == 2)
//			constraint = generateExpression((ASTNode) dec.getOp(1));
//
//		if (attType == sym.GLOBAL)
//			Settings.persistGlobalDeclaration(id, constraint, type);
//		else if (attType == sym.ATTRIBUTES)
//			Settings.persistAttributeDeclaration(id, constraint, type);

	}

}
