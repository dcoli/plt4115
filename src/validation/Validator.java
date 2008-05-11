package validation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.Iterator;

import codegeneration.CodeGenerator;

import java_cup.runtime.Symbol;

import compiler.syntax.*;
import compiler.settings.*;

public class Validator {

	private ASTNode root;

	public static String emessage = "";
	
	private static boolean allsWell = true;

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

	public boolean go() {

		validateStart(root);
		if (allsWell) System.out.println("Validated.");
		else System.out.println(emessage);
		return allsWell;
	}

	private void validateStart (ASTNode root) {
		debugGeneration("start");
		ASTNode simulationFileNode = (ASTNode) root.getDescriptor();
		validateSimulationFile(simulationFileNode);
		ASTNode environmentFileNode = (ASTNode) root.getOp(0);
		validateEnvironmentFile(environmentFileNode);
		ASTNode participantFilesNode = (ASTNode) root.getOp(1);
		valPartFiles(participantFilesNode);
	}
	
//EnvironmentFile ::= ENVIRONMENT Meta:m Interface:i Step:s Actions:a 
	private void validateEnvironmentFile( ASTNode node ) {
		String metaName = (String) node.getOp(0);
		debugGeneration( "environment file: " + metaName + "");
		blockLevel++;
		valInterface ((ASTNode) node.getOp(1));
		valStep ((ASTNode) node.getOp(2));
		valActions ((ASTNode) node.getOp(3));
		blockLevel--;
	}

	//ParticipantFile ::= PARTICIPANT Meta:m Step:s
	private void valPartFile(ASTNode partFileNode) {
		String partFileName = (String) partFileNode.getOp(0);
		debugGeneration( "participant file: " + partFileName);
		blockLevel++;
		valStep ((ASTNode) partFileNode.getOp(1));
		blockLevel--;
	}

	//ParticipantFiles ::= ParticipantFile:p ParticipantFiles:pf
	private void valPartFiles (ASTNode node) {
		debugGeneration( "partFiles");
		blockLevel++;
		if ( node != null && node.getNumberOfOperands() > 0) {
			ASTNode partFileNode = (ASTNode) node.getOp(0);
			valPartFile (partFileNode);
			if (node.getNumberOfOperands() > 1) {
				ASTNode partFilesNode1 = (ASTNode) node.getOp(1);
				valPartFiles (partFilesNode1);
			}
		}
		blockLevel--;
	}
	
	private void validateSimulationFile (ASTNode node) {
		String metaName = (String) node.getOp(0);
		debugGeneration( "simulation: " + metaName + "");
		blockLevel++;
		ASTNode envConfigNode = (ASTNode) node.getOp(1);
		validateEnvConfig(envConfigNode);
		ASTNode partConfigListNode = (ASTNode) node.getOp(2);
		validatePartConfigList (partConfigListNode);
		ASTNode endNode = (ASTNode) node.getOp(3);
		valEnd(endNode);
//		allsWell = false;
		blockLevel--;
	}
	
//Interface ::= Environmental_vars:e Participant_vars:p
	private void valInterface (ASTNode node) {
		debugGeneration("interface");
		blockLevel++;
		ASTNode envVarsNode = (ASTNode) node.getOp(0);
		valEnvVars (envVarsNode);
		ASTNode partVarsNode = (ASTNode) node.getOp(1);
		valPartVars (partVarsNode);
		blockLevel--;
	}

//Environmental_vars ::= GLOBAL LBRC AttributeDeclarationList:l RBRC 
	private void valEnvVars(ASTNode envVarsNode) {
		if ( envVarsNode != null ) {
			debugGeneration("env vars");//+envVarsNode.getDescriptor());
			blockLevel++;
			ASTNode attDecListNode = (ASTNode) envVarsNode.getOp(0);
			valAttDecList (attDecListNode);
			blockLevel--;
		}
	}

//	Participant_vars ::= ATTRIBUTES LBRC AttributeDeclarationList:l RBRC | empty
	private void valPartVars(ASTNode partVarsNode) {
		if (partVarsNode != null) {
			debugGeneration("participant vars:"+partVarsNode.getDescriptor());
			blockLevel++;
			valAttDecList ((ASTNode) partVarsNode.getOp(0));
			blockLevel--;
		}
	}
	
//Step ::= DEF REQUIRED STEP LPREN RPREN Block:b	
	private void valStep (ASTNode stepNode) {
		debugGeneration("step");
		blockLevel++;
		valBlock (stepNode);
		blockLevel--;
	}
	
	private void valActions (ASTNode actionsNode) {
		if ( actionsNode != null) {
			debugGeneration("actions: "+actionsNode.getDescriptor());
			blockLevel++;
			ASTNode actionNode = (ASTNode) actionsNode.getOp(0);
			valAction (actionNode);
			if ( actionsNode.getNumberOfOperands() > 1 ) {
				ASTNode actionsNode1 = (ASTNode) actionsNode.getOp(1);
				valActions (actionsNode1);
			}
			blockLevel--;
		}
	}
	
	private void valAction(ASTNode actionNode) {
		debugGeneration("action: "+actionNode.getDescriptor());
		blockLevel++;
		if (actionNode.getNumberOfOperands() > 1) {
			ASTNode argListNode = (ASTNode) actionNode.getOp(0);
			valArgList (argListNode);
			ASTNode blockNode = (ASTNode) actionNode.getOp(1);
			valBlock (blockNode);
		}
		else if (actionNode.getNumberOfOperands() == 1) {
			ASTNode blockNode = (ASTNode) actionNode.getOp(1);
			valBlock (blockNode);
		}
		blockLevel--;
	}

	private void valArgList(ASTNode argListNode) {
		debugGeneration("arglist");
		blockLevel++;
		if ( argListNode.getNumberOfOperands() == 2 ) {
			ASTNode argNode = (ASTNode) argListNode.getOp(0);
			valArg (argNode);
			ASTNode argListNode1 = (ASTNode) argListNode.getOp(1);
			valArgList (argListNode1);
		}
		else {
			ASTNode argNode = argListNode;
			valArg (argNode);
		}
		blockLevel--;
	}

//	Arg ::= DataType:d ID:i
	private void valArg(ASTNode argNode) {
		debugGeneration("Arg: "+argNode.getDescriptor());
		Integer dataTypeNode = (Integer) argNode.getOp(0);
		int type = valDataType (dataTypeNode);
		String idNode = (String) argNode.getOp(1);
		valId (idNode);
	}

	private void validateEnvConfig (ASTNode node) {
		String envConfigName = (String) node.getDescriptor();
		debugGeneration( "env config: " + envConfigName + "");
		blockLevel++;
		ASTNode assListNode = (ASTNode) node.getOp(0);
		valAssList(assListNode);
//		allsWell = false;
		blockLevel--;
	}
	
//ParticipantConfigList ::= ParticipantConfig:c ParticipantConfigList:cs | empty
	private void validatePartConfigList (ASTNode node) {
		if ( node != null && node.getNumberOfOperands() > 0) {
			debugGeneration( "partconfiglist: "+node.getDescriptor());
			blockLevel++;
			ASTNode partConfigNode = (ASTNode) node.getOp(0);
			validatePartConfig(partConfigNode);
			if (node.getNumberOfOperands() > 1) {
				ASTNode partConfigListNode = (ASTNode) node.getOp(1);
				validatePartConfigList (partConfigListNode);
			}
			blockLevel--;
		}
	}

//ParticipantConfig ::= PARTICIPANT STRING:s ID:i LBRC AssignmentList:l RBRC
//    | PARTICIPANT STRING:s ID:i SEMI					
    private void validatePartConfig (ASTNode partConfigNode) {
		String partName = (String) partConfigNode.getOp(0);
		debugGeneration( "participant name: " + partName + ", "+partConfigNode.getDescriptor());
		blockLevel++;
		valId ((String) partConfigNode.getOp(1));
		if (partConfigNode.getNumberOfOperands() > 1) {
			valAssList ((ASTNode) partConfigNode.getOp(2));
		}
		blockLevel--;
	}

//AssignmentList ::= Assignment:a SEMI AssignmentList:l 
//|empty
	private void valAssList(ASTNode assListNode) {
		if ( assListNode != null && assListNode.getNumberOfOperands() > 0) {
//			debugGeneration( "assignment list");
//			blockLevel++;
			ASTNode assNode = (ASTNode) assListNode.getOp(0);
			valAss (assNode);
			if (assListNode.getNumberOfOperands() > 1) {
				ASTNode assListNode1 = (ASTNode) assListNode.getOp(1);
				valAssList (assListNode1);
			}
//			blockLevel--;
		}
	}

//End ::= DEF REQUIRED END LPREN RPREN Block:b
	private void valEnd (ASTNode node) {
		debugGeneration("end");
		ASTNode blockNode = (ASTNode) node.getOp(0);
		valBlock (blockNode);
	}

//Block ::= LBRC Lines:l RBRC
//|empty
	private void valBlock (ASTNode node) {
		debugGeneration( "block");
		blockLevel++;
		if ( node != null ) {
			ASTNode linesNode = (ASTNode) node.getOp(0);
			valLines(linesNode);
		}
//		allsWell = false;
		blockLevel--;
	}

//Lines ::= Statement:s Lines:l
//|empty	
	private void valLines (ASTNode node) {
		if (node != null) {
			ASTNode statementNode = (ASTNode) node.getOp(0);
			valStatement(statementNode);
			if (node.getNumberOfOperands() > 1)
				valLines((ASTNode) node.getOp(1));			
		}
	}

	private void valStatement (ASTNode node) {
		blockLevel++;
		debugGeneration(((Integer) node.getDescriptor()).toString());
		switch ((Integer) node.getDescriptor()) {
		case sym.IF: {
			debugGeneration("IF");
			valIf (node);
			break;
		}
		case sym.ELSE: {
			debugGeneration("ELSE");
			valElse (node);
			break;
		}
		case sym.WHILE: {
			debugGeneration("WHILE");
			valWhile (node);
			break;
		}			
		case sym.RETURN: {
			debugGeneration("return");
			valReturn (node);
			break;
		}		
		case astsym.DECLARATION: {
			debugGeneration("declaration");
			valDeclaration (node);
			break;
		}
		case astsym.FUNCTION_CALL: {
			debugGeneration("function call");
			valFunc (node);
			break;
		}
		case astsym.STEP_CALL:
			valFunc (node);
		case sym.TIMESEQ:
		case sym.DIVIDEEQ:
		case sym.MINUSEQ:
		case sym.PLUSEQ:
		case sym.MODEQ:
		case sym.EQ:
			valAss (node);
		default: { // is an assignment
			debugGeneration("assignment");
			valAss (node);
		}
		}
		blockLevel--;
	}
	
	//IfStatement ::= IF LPREN Expression:e RPREN Statement:s
	private void valIf(ASTNode node) {
		int type = valExp ((ASTNode) node.getOp(0));
		valStatement ((ASTNode) node.getOp(1));
	}
	
	//ElseStatement ::= IF LPREN Expression:e RPREN Statement:s1 ELSE Statement:s2
	private void valElse(ASTNode node) {
		int type = valExp ((ASTNode) node.getOp(0));
		valStatement ((ASTNode) node.getOp(1));
		valStatement ((ASTNode) node.getOp(2));
	}
	
	private void valWhile(ASTNode node) {
		ASTNode expNode = (ASTNode) node.getOp(0);
		int type = valExp (expNode);
		ASTNode statementNode = (ASTNode) node.getOp(1);
		valStatement (statementNode);
	}
	
	private void valReturn(ASTNode node) {
		ASTNode expNode = (ASTNode) node.getOp(0);
		int type = valExp (expNode);
	}

//AttributeDeclarationList ::= AttributeDeclaration:ad SEMI AttributeDeclarationList:adl 
	private void valAttDecList(ASTNode attDecListNode) {
		if ( attDecListNode != null ) {
			valAttDec ((ASTNode) attDecListNode.getOp(0));
			if (attDecListNode.getNumberOfOperands() > 1) {
				valAttDecList ((ASTNode) attDecListNode.getOp(1));
			}
		}
	}
	
//AttributeDeclaration ::= Declaration:d Constraint:c | Declaration
	private void valAttDec(ASTNode node) {
		valDeclaration ((ASTNode) node.getOp(0));
		if (node.getNumberOfOperands() > 1) {
			valConstraint ((ASTNode) node.getOp(1));
		}
	}

//Constraint ::= LBRC Expression:e RBRC
	private void valConstraint(ASTNode node) {
		int type = valExp (node);
	}

//	Declaration ::= DataType:d IdList:i
	private int valDeclaration(ASTNode node) {
		debugGeneration("declaration: "+(Integer) node.getOp(0));
		int type = valDataType ((Integer) node.getOp(0));
		valIdList ((ASTNode) node.getOp(1));
		return type;
	}

//IdList ::= ID:i COMMA IdList:l 
//			| ID:i								
	private void valIdList(ASTNode node) {
		valId ((String) node.getOp(0));
		debugGeneration((String) node.getOp(0));
		if (node.getNumberOfOperands() > 1) {
			valIdList ((ASTNode) node.getOp(1));
		}
	}

//DataType ::= INT | FLOAT | BOOLEAN | PARTICIPANT
	private int valDataType(int dataType) {
		return dataType;
	}	
	
//	Assignment ::= LValueData:i TIMESEQ Expression:e
//            | LValueData:i DIVIDEEQ Expression:e
//            | LValueData:i MINUSEQ Expression:e
//            | LValueData:i PLUSEQ Expression:e
//            | LValueData:i MODEQ Expression:e
//            | LValueData:i EQ Expression:e
	private void valAss(ASTNode assNode) {
		debugGeneration( "assignment");
		int type;
		if (assNode != null)
			if ( assNode.getNumberOfOperands() > 1 ) {
				ASTNode lValNode = (ASTNode) assNode.getOp(0);
				ASTNode expNode = (ASTNode) assNode.getOp(1);
				switch ((Integer) assNode.getDescriptor()) {
				case (sym.TIMESEQ): valLValData( lValNode); type = valExp (expNode); break;
				case (sym.DIVIDEEQ): valLValData( lValNode); type = valExp (expNode); break;
				case (sym.MINUSEQ): valLValData( lValNode); type = valExp (expNode); break;
				case (sym.PLUSEQ): valLValData( lValNode); type = valExp (expNode); break;
				case (sym.MODEQ): valLValData( lValNode); type = valExp (expNode); break;
				case (sym.EQ): {
					debugGeneration("=");
					valLValData( lValNode); 
					valExpList (expNode); 
					break;
				}
				}
			}
			else if ( assNode.getNumberOfOperands() == 1 ) {
				if ((Integer) assNode.getDescriptor() == astsym.SYSTEM_VAR) {
					ASTNode sysVarNode = (ASTNode) assNode.getOp(0);
					if (assNode.getOp(0) instanceof ASTNode) debugGeneration("valsysvar receives node");
					valSysVar (sysVarNode);
			}
		}
	}

//LValueData ::= ID:i 	| SystemVar
	private void valLValData(ASTNode valNode) {
		switch ((Integer) valNode.getDescriptor()) {
		case sym.ID:
			debugGeneration(valNode.getOp(0).toString());
			valId ((String) valNode.getOp(0));
			break;
		case astsym.SYSTEM_VAR:
			if (valNode.getOp(0) instanceof ASTNode) {
				valSysVar ((ASTNode) valNode.getOp(0));
			}
			else valSysVar ((String) valNode.getOp(0));

			break;
		}
	}

//FunctionCall ::= ID:i LPREN ExpressionList:l RPREN
//				| ID:i LPREN RPREN  
//				| DOLLAR SystemPartRef:s DOT STEP LPREN RPREN  
	private void valFunc(ASTNode node) {
		if ((Integer) node.getOp(0) == sym.ID) {
			debugGeneration(((String) node.getOp(1)));
			valId ((String) node.getOp(1));
			if (node.getNumberOfOperands() > 2) {
				valExpList ((ASTNode) node.getOp(2));
			}
		}
		else if ((Integer) node.getOp(0) == astsym.STEP_CALL) {
			valSysPartRef ((ASTNode) node.getOp(1));
		}
	}

//ExpressionList ::= Expression:e COMMA ExpressionList:el
//		| Expression:e
	private void valExpList(ASTNode node) {
		int type;
		if (node.getOp(0) instanceof ASTNode){
//			debugGeneration("node");
			if (node.getNumberOfOperands() == 1) {
				type = valExp (node);
			}
			else if (node.getNumberOfOperands() > 1) {
				debugGeneration("explist");
				type = valExp((ASTNode) node.getOp(0));
				valExpList ((ASTNode) node.getOp(1));
			}
		}
//		else if (node.getOp(0) instanceof Float) { 
//			debugGeneration("float");
//			type = sym.DECIMAL;
//		}
//		else if (node.getOp(0) instanceof Integer) { 
//			debugGeneration("int "+node.getOp(0));
//			type = sym.NUMBER;
//		}
//		else if (node.getOp(0) instanceof String) {
//			debugGeneration("string");
//			type = sym.STRING;
//		}
//		else {
//			debugGeneration("participant");
//			type = sym.PARTICIPANT;
////			type = valExp ((ASTNode) node.getOp(0));
//		}
//		return type;
	}

//	Expression	::= OrExpression:o							
	private int valExp(ASTNode expNode) {
		debugGeneration("expression type: "+((Integer) expNode.getDescriptor()).intValue());
		int type;
		switch (((Integer) expNode.getDescriptor()).intValue()) {
		case sym.OR:
			type = valOr (expNode);
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " || "
//					+ generateExpression((ASTNode) e.getOp(1)));
			break;
		case sym.AND:
			type = valAnd (expNode);
//			sb.append(generateExpression((ASTNode) e.getOp(0)) + " && "
//					+ generateExpression((ASTNode) e.getOp(1)));
			break;
		case sym.EQEQ:
		case sym.NOTEQ:
			type = valEquality (expNode);
			break;
		case sym.LT:
		case sym.GT:
		case sym.LTEQ:
		case sym.GTEQ:
			type = valRelational (expNode);
		case sym.PLUS:
		case sym.MINUS:
			if (expNode.getNumberOfOperands() == 2)
				type = valAdditive (expNode);
			else
				type = valUnary (expNode);
		case sym.TIMES:
		case sym.MOD:
		case sym.DIVIDE:
			type = valMultiplicative (expNode);
		case sym.NOT:
		case sym.LPREN:
			type = valUnary (expNode);
		case astsym.SYSTEM_VAR:
			if (expNode.getOp(0) instanceof ASTNode)
				valSysVar ((ASTNode) expNode.getOp(0));
			else valSysVar ((String) expNode.getOp(0));
		default:
			type = valData (expNode);
		}
//		if (expNode.getOp(0) instanceof ASTNode) 
//		else if (expNode.getOp(0) instanceof Float) 
//			type = sym.DECIMAL;
//		else if (expNode.getOp(0) instanceof Integer) 
//			type = sym.NUMBER;
//		else if (expNode.getOp(0) instanceof String)
//			type = sym.STRING;
//		else
//			type = sym.PARTICIPANT;
			
		debugGeneration("expression type: " + type);
		return type;
	}

//OrExpression ::= OrExpression:o OR AndExpression:a
//    | AndExpression:a									
	private int valOr (ASTNode expNode) {
		int type1, type2;
		if (expNode.getOp(0) instanceof ASTNode) 
			if ((Integer) expNode.getDescriptor() == sym.OR) {
				debugGeneration("OR operator 0: "+expNode.getOp(0));
				debugGeneration("OR operator 1: "+expNode.getOp(1));
				type1 = valOr ((ASTNode) expNode.getOp(0));
				type2 = valAnd ((ASTNode) expNode.getOp(1));
				if ((type1 != sym.BOOLEAN) || (type2 != sym.BOOLEAN)) {
					allsWell = false;
					emessage += "The || (or) operator can only work on two expressions that evaluate to booleans.";
					return -1;
				}
				else return sym.BOOLEAN;
			}
			else {
				debugGeneration("skipping to And");
				return valAnd ((ASTNode) expNode.getOp(0));
			}
//		else if (expNode.getOp(0) instanceof String) 
//			return Integer.parseInt(expNode.getOp(0).toString());
		else {
			debugGeneration("node descriptor: "+expNode.getDescriptor());
			return (Integer) expNode.getDescriptor();
		}
	}
	

//AndExpression ::= AndExpression:a AND EqualityExpression:e
//      			| EqualityExpression:e
	private int valAnd (ASTNode node) {
		int type1, type2;
		if (node != null) {
			if (node.getOp(0) instanceof ASTNode) 
				if (node.getNumberOfOperands() == 0) {
					debugGeneration("skipping to Equality");
					return valEquality ((ASTNode) node.getOp(0));
				}
				else {
					type1 = valAnd ((ASTNode) node.getOp(0));
					type2 = valEquality ((ASTNode) node.getOp(1));
					if ((type1 == sym.BOOLEAN) && (type2 == sym.BOOLEAN)) {
						return sym.BOOLEAN;
					}
					else {
						allsWell = false;
						emessage += "&& operator (and) must operate on two boolean expressions";
						return -1;
					}
				}
			else return (Integer) node.getDescriptor();
		}
		return -1;
	}

//EqualityExpression ::= EqualityExpression:e EQEQ RelationalExpression:r
//                   | EqualityExpression:e NOTEQ RelationalExpression:r
//                   | RelationalExpression:r						
	private int valEquality(ASTNode node) {
		int type1, type2;
		if (node != null) {
			if (node.getNumberOfOperands() == 0) {
				debugGeneration("skipping to Relational");
				return valRelational ((ASTNode) node.getOp(0));
			}
			else if ((Integer) node.getDescriptor() == sym.EQEQ) {
				type1 = valEquality ((ASTNode) node.getOp(0));
				type2 = valRelational ((ASTNode) node.getOp(1));
	//			if ((type1 == sym.PARTICIPANT) && (type2 == sym.PARTICIPANT)) {
	//				return sym.BOOLEAN;
	//			}
			}
			else if ((Integer) node.getDescriptor() == sym.ID) {
				return (Integer) node.getDescriptor();
				//			if ((type1 == sym.PARTICIPANT) && (type2 == sym.PARTICIPANT)) {
	//				return sym.BOOLEAN;
	//			}
			}
			else if ((Integer) node.getDescriptor() == sym.NOTEQ) {
				type1 = valEquality ((ASTNode) node.getOp(0));
				type2 = valRelational ((ASTNode) node.getOp(1));
	//			if ((type1 == sym.BOOLEAN) && (type2 == sym.BOOLEAN)) {
	//				return sym.BOOLEAN;
	//			}
			}
		}
		return sym.BOOLEAN;
	}

//RelationalExpression ::= AdditiveExpression:al LT AdditiveExpression:ae
//                      | AdditiveExpression:al GT AdditiveExpression:ae
//                      | AdditiveExpression:al LTEQ AdditiveExpression:ae
//                      | AdditiveExpression:al GTEQ AdditiveExpression:ae
//					  | AdditiveExpression:ae			
	private int valRelational(ASTNode node) {
		int type1 = valAdditive ((ASTNode) node.getOp(0)); 
		int type2;
		if ((Integer) node.getDescriptor() == sym.LT) {
			type2 = valAdditive ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.GT) {
			type2 = valAdditive ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.LTEQ) {
			type2 = valAdditive ((ASTNode) node.getOp(1));
		}
		else //if ((Integer) node.getDescriptor() == sym.GTEQ) {
			type2 = valAdditive ((ASTNode) node.getOp(1));
//		}
		if ((type1 == sym.BOOLEAN) || (type2 == sym.BOOLEAN) || (type1 == sym.PARTICIPANT) || (type2 == sym.PARTICIPANT)) {
			allsWell = false;
			emessage += "Relational operators in Rumble must operate on two expressions that each evaluate to a number.";
			return -1;
		}
		return sym.BOOLEAN;
	}

//AdditiveExpression ::= AdditiveExpression:a PLUS MultiplicativeExpression:m
//                   | AdditiveExpression:a MINUS MultiplicativeExpression:m
//                   | MultiplicativeExpression:m
	private int valAdditive(ASTNode node) {
		int type1, type2;
		if (node.getNumberOfOperands() == 1) {
			debugGeneration("skipping to Multiplicative");
			return valMultiplicative ((ASTNode) node.getOp(0)); 
		}
		else if ((Integer) node.getDescriptor() == sym.PLUS) {
			type1 = valAdditive ((ASTNode) node.getOp(0));
			type2 = valMultiplicative ((ASTNode) node.getOp(1));
			if ((type1 == sym.BOOLEAN) || (type2 == sym.BOOLEAN) || (type1 == sym.PARTICIPANT) || (type2 == sym.PARTICIPANT)) {
				allsWell = false;
				emessage += "'+' in Rumble must operate on two expressions that each evaluate to a number.";
				return -1;
			}
		}
		else {//((Integer) node.getDescriptor() == sym.MINUS) {
			type1 = valAdditive ((ASTNode) node.getOp(0));
			type2 = valMultiplicative ((ASTNode) node.getOp(1));
			if ((type1 == sym.BOOLEAN) || (type2 == sym.BOOLEAN) || (type1 == sym.PARTICIPANT) || (type2 == sym.PARTICIPANT)) {
				allsWell = false;
				emessage += "'-' (minus) in Rumble must operate on two expressions that each evaluate to a number.";
				return -1;
			}
		}
		if (type1 == sym.FLOAT || type2 == sym.FLOAT) return sym.FLOAT;
		else return sym.INT;
	}

//MultiplicativeExpression ::= MultiplicativeExpression:m TIMES UnaryExpression:u
//                         |  MultiplicativeExpression:m DIVIDE UnaryExpression:u
//                         |  MultiplicativeExpression:m MOD UnaryExpression:u
	private int valMultiplicative(ASTNode node) {	
		int type1, type2;
		if (node.getNumberOfOperands() == 1) {
			debugGeneration("skipping to Unary");
			return valUnary ((ASTNode) node.getOp(0)); 
		}
		else if ((Integer) node.getDescriptor() == sym.TIMES) {
			type1 = valMultiplicative ((ASTNode) node.getOp(0));
			type2 = valUnary ((ASTNode) node.getOp(1));
			if ((type1 == sym.BOOLEAN) || (type2 == sym.BOOLEAN) || (type1 == sym.PARTICIPANT) || (type2 == sym.PARTICIPANT)) {
				allsWell = false;
				emessage += "'*' (multiply) in Rumble must operate on two expressions that each evaluate to a number.";
			}
		}
		else if ((Integer) node.getDescriptor() == sym.DIVIDE) {
			type1 = valMultiplicative ((ASTNode) node.getOp(0));
			type2 = valUnary ((ASTNode) node.getOp(1));
			if ((type1 == sym.BOOLEAN) || (type2 == sym.BOOLEAN) || (type1 == sym.PARTICIPANT) || (type2 == sym.PARTICIPANT)) {
				allsWell = false;
				emessage += "'/' (divide) in Rumble must operate on two expressions that each evaluate to a number.";
			}
		}
		else {//if ((Integer) node.getDescriptor() == sym.MOD) {
			type1 = valMultiplicative ((ASTNode) node.getOp(0));
			type2 = valUnary ((ASTNode) node.getOp(1));
			if ((type1 == sym.BOOLEAN) || (type2 == sym.BOOLEAN) || (type1 == sym.PARTICIPANT) || (type2 == sym.PARTICIPANT)) {
				allsWell = false;
				emessage += "'%' (modulus) in Rumble must operate on two expressions that each evaluate to a number.";
			}
		}
		if (type1 == sym.FLOAT || type2 == sym.FLOAT) return sym.FLOAT;
		else return sym.INT;
	}

//UnaryExpression ::= NOT UnaryExpression:u
//				| MINUS UnaryExpression:u
//                | LPREN Expression:e RPREN 				
//                | Data:d								
	private int valUnary(ASTNode node) {
		int type;
		switch ((Integer) node.getDescriptor()) {
		case sym.NOT: {
			type = valUnary ((ASTNode) node.getOp(0));
			if (type != sym.BOOLEAN) {
				allsWell = false;
				emessage += "Rumble only allows the negation operator on boolean values.";
				return -1;
			}
			return type;
		}
		case sym.MINUS: {
			type = valUnary ((ASTNode) node.getOp(0));
			if (type != sym.INT || type != sym.FLOAT) {
				allsWell = false;
				emessage += "Rumble only allows the unary minus operator on integers or floating point values.";
				return -1;
			}
			return type;
		}
		case sym.LPREN: {
			type = valExp ((ASTNode) node.getOp(0));
			return type;
		}
		default: {
			valData ((ASTNode) node.getOp(0));
			return -1;
		}
		}
	}

//Data ::= ID:i
//			| SystemVar:s							
//			| NUMBER:num 							
//			| DECIMAL:d 							
//			| FunctionCall:f						
//			| TRUE								
//			| FALSE
	private int valData(ASTNode node) {
		// TODO Auto-generated method stub
		int type;
		debugGeneration("valData node descriptor: "+((Object) node.getDescriptor()).toString());
		if (((Integer) node.getDescriptor()) == sym.ID) {
			debugGeneration(((Object) node.getOp(0)).toString());
			type = -1;
//			valSysVar ((ASTNode) node.getOp(0));
		}
		else if (((Integer) node.getDescriptor()) == astsym.SYSTEM_VAR) {
			debugGeneration(((Object) node.getOp(0)).toString());
			type = -1;
		}
		else if (node.getOp(0) instanceof Float) { 
			debugGeneration("float: " + (Float)node.getOp(0));
			type = sym.DECIMAL;
		}
		else if (node.getOp(0) instanceof Integer) { 
			debugGeneration("int: " + (Integer)node.getOp(0));
			type = sym.NUMBER;
		}
		else if (node.getOp(0) instanceof String) {
			debugGeneration("string: " + (String)node.getOp(0));
			type = sym.STRING;
		}
		else if (node.getOp(0) instanceof Boolean) {
			debugGeneration("boolean: " + (Boolean)node.getOp(0));
			type = sym.BOOLEAN;
		}
		else {
			debugGeneration("participant");
			type = sym.PARTICIPANT;
//			type = valExp ((ASTNode) node.getOp(0));
		}
		return type;
	}

	private void valId(String name) {
		// TODO Auto-generated method stub
		
	}
	
	private void valSysVar(ASTNode sysVarNode) {
		// TODO Auto-generated method stub
		debugGeneration(sysVarNode.getDescriptor().toString());
	}

	private void valSysVar(String string) {
		// TODO Auto-generated method stub
		debugGeneration("sys var: "+string);
	}

//SystemPartRef ::= PART LSBRK NUMBER:i RSBRK 
//		| ME
	private void valSysPartRef(ASTNode node) {
		// TODO Auto-generated method stub
		
	}
	
}
