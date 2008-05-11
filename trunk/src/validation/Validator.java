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
	
	public static void debugGeneration(String message) {
		if (Settings.debug) {
//			for (int i = 0; i < blockLevel; i++)
//				System.out.print("\t");

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
		ASTNode interfaceNode = (ASTNode) node.getOp(1);
		valInterface (interfaceNode);
		valStep ((ASTNode) node.getOp(2));
		valActions ((ASTNode) node.getOp(3));
	}

	//ParticipantFile ::= PARTICIPANT Meta:m Step:s
	private void valPartFile(ASTNode partFileNode) {
		String partFileName = (String) partFileNode.getOp(0);
		debugGeneration( "participant file: " + partFileName);
		valStep ((ASTNode) partFileNode.getOp(1));
	}

	//ParticipantFiles ::= ParticipantFile:p ParticipantFiles:pf
	private void valPartFiles (ASTNode node) {
		debugGeneration( "partFiles");
		if ( node != null && node.getNumberOfOperands() > 0) {
			ASTNode partFileNode = (ASTNode) node.getOp(0);
			valPartFile (partFileNode);
			if (node.getNumberOfOperands() > 1) {
				ASTNode partFilesNode1 = (ASTNode) node.getOp(1);
				valPartFiles (partFilesNode1);
			}
		}
	}
	
	private void validateSimulationFile (ASTNode node) {
		String metaName = (String) node.getOp(0);
		debugGeneration( "Simulation: " + metaName + "");
		ASTNode envConfigNode = (ASTNode) node.getOp(1);
		validateEnvConfig(envConfigNode);
		ASTNode partConfigListNode = (ASTNode) node.getOp(2);
		validatePartConfigList (partConfigListNode);
		ASTNode endNode = (ASTNode) node.getOp(3);
		validateEnd(endNode);
//		allsWell = false;
	}
	
//Interface ::= Environmental_vars:e Participant_vars:p
	private void valInterface (ASTNode node) {
		ASTNode envVarsNode = (ASTNode) node.getOp(0);
		valEnvVars (envVarsNode);
		ASTNode partVarsNode = (ASTNode) node.getOp(1);
		valPartVars (partVarsNode);
	}

//Environmental_vars ::= GLOBAL LBRC AttributeDeclarationList:l RBRC 
	private void valEnvVars(ASTNode envVarsNode) {
		if ( envVarsNode != null ) {
			ASTNode attDecListNode = (ASTNode) envVarsNode.getOp(0);
			valAttDecList (attDecListNode);
		}
	}

//	Participant_vars ::= ATTRIBUTES LBRC AttributeDeclarationList:l RBRC | empty
	private void valPartVars(ASTNode partVarsNode) {
		if (partVarsNode != null) {
			valAttDecList ((ASTNode) partVarsNode.getOp(0));
		}
	}
	
//Step ::= DEF REQUIRED STEP LPREN RPREN Block:b	
	private void valStep (ASTNode stepNode) {
		valBlock (stepNode);
	}
	
	private void valActions (ASTNode actionsNode) {
		if ( actionsNode != null) {
			ASTNode actionNode = (ASTNode) actionsNode.getOp(0);
			valAction (actionNode);
			if ( actionsNode.getNumberOfOperands() > 1 ) {
				ASTNode actionsNode1 = (ASTNode) actionsNode.getOp(1);
				valActions (actionsNode1);
			}
		}
	}
	
	private void valAction(ASTNode actionNode) {
		// TODO Auto-generated method stub
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
	}

	private void valArgList(ASTNode argListNode) {
		// TODO Auto-generated method stub
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
	}

//	Arg ::= DataType:d ID:i
	private void valArg(ASTNode argNode) {
		Integer dataTypeNode = (Integer) argNode.getOp(0);
		valDataType (dataTypeNode);
		String idNode = (String) argNode.getOp(1);
		valId (idNode);
	}

	private void validateEnvConfig (ASTNode node) {
		String envConfigName = (String) node.getDescriptor();
		debugGeneration( "env config: " + envConfigName + "");
		ASTNode assListNode = (ASTNode) node.getOp(0);
		valAssList(assListNode);
//		allsWell = false;
	}
	
//ParticipantConfigList ::= ParticipantConfig:c ParticipantConfigList:cs | empty
	private void validatePartConfigList (ASTNode node) {
		debugGeneration( "partconfiglist");
		if ( node != null && node.getNumberOfOperands() > 0) {
			ASTNode partConfigNode = (ASTNode) node.getOp(0);
			validatePartConfig(partConfigNode);
			if (node.getNumberOfOperands() > 1) {
				ASTNode partConfigListNode = (ASTNode) node.getOp(1);
				validatePartConfigList (partConfigListNode);
			}
		}
	}

//ParticipantConfig ::= PARTICIPANT STRING:s ID:i LBRC AssignmentList:l RBRC
//    | PARTICIPANT STRING:s ID:i SEMI					
    private void validatePartConfig (ASTNode partConfigNode) {
		String partName = (String) partConfigNode.getOp(0);
		debugGeneration( "participant name: " + partName + "");
		valId ((String) partConfigNode.getOp(1));
		if (partConfigNode.getNumberOfOperands() > 1) {
			valAssList ((ASTNode) partConfigNode.getOp(2));
		}
	}

//AssignmentList ::= Assignment:a SEMI AssignmentList:l 
//|empty
	private void valAssList(ASTNode assListNode) {
		debugGeneration( "assignment list");
		if ( assListNode != null && assListNode.getNumberOfOperands() > 0) {
			ASTNode assNode = (ASTNode) assListNode.getOp(0);
			valAss (assNode);
			if (assListNode.getNumberOfOperands() > 1) {
				ASTNode assListNode1 = (ASTNode) assListNode.getOp(1);
				valAssList (assListNode1);
			}
		}
	}

//End ::= DEF REQUIRED END LPREN RPREN Block:b
	private void validateEnd (ASTNode node) {
		ASTNode blockNode = (ASTNode) node.getOp(0);
		valBlock (blockNode);
	}

//Block ::= LBRC Lines:l RBRC
//|empty
	private void valBlock (ASTNode node) {
		debugGeneration( "block");
		if ( node != null ) {
			ASTNode linesNode = (ASTNode) node.getOp(0);
			valLines(linesNode);
		}
//		allsWell = false;
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
			debugGeneration("Return");
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
		default: { // is an assignment
			debugGeneration("assignment");
			valAss (node);
		}
		}
	}
	
	//IfStatement ::= IF LPREN Expression:e RPREN Statement:s
	private void valIf(ASTNode node) {
		valExp ((ASTNode) node.getOp(0));
		valStatement ((ASTNode) node.getOp(1));
	}
	
	//ElseStatement ::= IF LPREN Expression:e RPREN Statement:s1 ELSE Statement:s2
	private void valElse(ASTNode node) {
		valExp ((ASTNode) node.getOp(0));
		valStatement ((ASTNode) node.getOp(1));
		valStatement ((ASTNode) node.getOp(2));
	}
	
	private void valWhile(ASTNode node) {
		ASTNode expNode = (ASTNode) node.getOp(0);
		valExp (expNode);
		ASTNode statementNode = (ASTNode) node.getOp(1);
		valStatement (statementNode);
	}
	
	private void valReturn(ASTNode node) {
		ASTNode expNode = (ASTNode) node.getOp(0);
		valExp (expNode);
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
		valExp (node);
	}

//	Declaration ::= DataType:d IdList:i
	private void valDeclaration(ASTNode node) {
		valDataType ((Integer) node.getOp(0));
		valIdList ((ASTNode) node.getOp(1));
	}

//IdList ::= ID:i COMMA IdList:l 
//			| ID:i								
	private void valIdList(ASTNode node) {
		valId ((String) node.getOp(0));
		if (node.getNumberOfOperands() > 1) {
			valIdList ((ASTNode) node.getOp(1));
		}
	}

//DataType ::= INT | FLOAT | BOOLEAN | PARTICIPANT
	private void valDataType(int dataType) {
		
	}	
	
//	Assignment ::= LValueData:i TIMESEQ Expression:e
//            | LValueData:i DIVIDEEQ Expression:e
//            | LValueData:i MINUSEQ Expression:e
//            | LValueData:i PLUSEQ Expression:e
//            | LValueData:i MODEQ Expression:e
//            | LValueData:i EQ Expression:e
	private void valAss(ASTNode assNode) {
		debugGeneration( "assignment");
		if (assNode != null)
			if ( assNode.getNumberOfOperands() > 1 ) {
				ASTNode lValNode = (ASTNode) assNode.getOp(0);
				ASTNode expNode = (ASTNode) assNode.getOp(1);
				switch ((Integer) assNode.getDescriptor()) {
				case (sym.TIMESEQ): valLValData( lValNode); valExp (expNode); break;
				case (sym.DIVIDEEQ): valLValData( lValNode); valExp (expNode); break;
				case (sym.MINUSEQ): valLValData( lValNode); valExp (expNode); break;
				case (sym.PLUSEQ): valLValData( lValNode); valExp (expNode); break;
				case (sym.MODEQ): valLValData( lValNode); valExp (expNode); break;
				case (sym.EQ): valLValData( lValNode); valExp (expNode); break;
				}
			}
			else if ( assNode.getNumberOfOperands() == 1 ) {
				if ((Integer) assNode.getDescriptor() == astsym.SYSTEM_VAR) {
					ASTNode sysVarNode = (ASTNode) assNode.getOp(0);
					valSysVar (sysVarNode);
			}
		}
		debugGeneration( "assignment type: " + (Integer) assNode.getDescriptor() + "");
	}

//LValueData ::= ID:i 	| SystemVar
	private void valLValData(ASTNode valNode) {
		switch ((Integer) valNode.getDescriptor()) {
		case sym.ID:
			valId ((String) valNode.getOp(0));
			break;
		case astsym.SYSTEM_VAR:
			valSysVar ((String) valNode.getOp(0));
			break;
		}
	}

//FunctionCall ::= ID:i LPREN ExpressionList:l RPREN
//				| ID:i LPREN RPREN  
//				| DOLLAR SystemPartRef:s DOT STEP LPREN RPREN  
	private void valFunc(ASTNode node) {
		if ((Integer) node.getOp(0) == sym.ID) {
			valId ((String) node.getOp(0));
			if (node.getNumberOfOperands() > 1) {
				valExpList ((ASTNode) node.getOp(1));
			}
		}
		else if ((Integer) node.getOp(0) == astsym.STEP_CALL) {
			valSysPartRef ((ASTNode) node.getOp(1));
		}
	}

//ExpressionList ::= Expression:e COMMA ExpressionList:el
//		| Expression:e
	private void valExpList(ASTNode node) {
		valExp ((ASTNode) node.getOp(0));
		if (node.getNumberOfOperands() > 1) {
			valExpList ((ASTNode) node.getOp(1));
		}
	}

//	Expression	::= OrExpression:o							
	private void valExp(ASTNode expNode) {
		valOr (expNode);
	}

//OrExpression ::= OrExpression:o OR AndExpression:a
//    | AndExpression:a									
	private void valOr (ASTNode expNode) {
		System.out.println(expNode.getOp(0));
		if (expNode.getNumberOfOperands() == 1) {
			valAnd ((ASTNode) expNode.getOp(0));
		}
		else {
			valOr ((ASTNode) expNode.getOp(0));
			valAnd ((ASTNode) expNode.getOp(1));
		}
	}

//AndExpression ::= AndExpression:a AND EqualityExpression:e
//      			| EqualityExpression:e
	private void valAnd (ASTNode node) {
		if (node.getNumberOfOperands() == 0) {
			valEquality ((ASTNode) node.getOp(0));
		}
		else {
			valAnd ((ASTNode) node.getOp(0));
			valEquality ((ASTNode) node.getOp(1));
		}
	}

//EqualityExpression ::= EqualityExpression:e EQEQ RelationalExpression:r
//                   | EqualityExpression:e NOTEQ RelationalExpression:r
//                   | RelationalExpression:r						
	private void valEquality(ASTNode node) {
		if (node.getNumberOfOperands() == 0) {
			valRelational ((ASTNode) node.getOp(0));
		}
		else if ((Integer) node.getDescriptor() == sym.EQEQ) {
			valEquality ((ASTNode) node.getOp(0));
			valRelational ((ASTNode) node.getOp(1));
		}
		else {//if ((Integer) node.getDescriptor() == sym.NOTEQ) 
			valEquality ((ASTNode) node.getOp(0));
			valRelational ((ASTNode) node.getOp(1));
		}
	}

//RelationalExpression ::= AdditiveExpression:al LT AdditiveExpression:ae
//                      | AdditiveExpression:al GT AdditiveExpression:ae
//                      | AdditiveExpression:al LTEQ AdditiveExpression:ae
//                      | AdditiveExpression:al GTEQ AdditiveExpression:ae
//					  | AdditiveExpression:ae			
	private void valRelational(ASTNode node) {
		valAdditive ((ASTNode) node.getOp(0)); 
		if ((Integer) node.getDescriptor() == sym.LT) {
			valAdditive ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.GT) {
			valAdditive ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.LTEQ) {
			valAdditive ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.GTEQ) {
			valAdditive ((ASTNode) node.getOp(1));
		}
	}

//AdditiveExpression ::= AdditiveExpression:a PLUS MultiplicativeExpression:m
//                   | AdditiveExpression:a MINUS MultiplicativeExpression:m
//                   | MultiplicativeExpression:m
	private void valAdditive(ASTNode node) {
		if (node.getNumberOfOperands() == 1) 
			valMultiplicative ((ASTNode) node.getOp(0)); 
		else if ((Integer) node.getDescriptor() == sym.PLUS) {
			valAdditive ((ASTNode) node.getOp(0));
			valMultiplicative ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.MINUS) {
			valAdditive ((ASTNode) node.getOp(0));
			valMultiplicative ((ASTNode) node.getOp(1));
		}
	}

//MultiplicativeExpression ::= MultiplicativeExpression:m TIMES UnaryExpression:u
//                         |  MultiplicativeExpression:m DIVIDE UnaryExpression:u
//                         |  MultiplicativeExpression:m MOD UnaryExpression:u
	private void valMultiplicative(ASTNode node) {	
		if (node.getNumberOfOperands() == 1) {
			valUnary ((ASTNode) node.getOp(0)); 
		}
		else if ((Integer) node.getDescriptor() == sym.TIMES) {
			valMultiplicative ((ASTNode) node.getOp(0));
			valUnary ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.DIVIDE) {
			valMultiplicative ((ASTNode) node.getOp(0));
			valUnary ((ASTNode) node.getOp(1));
		}
		else if ((Integer) node.getDescriptor() == sym.MOD) {
			valMultiplicative ((ASTNode) node.getOp(0));
			valUnary ((ASTNode) node.getOp(1));
		}
	}

	private void valUnary(ASTNode node) {
	// TODO Auto-generated method stub
	
	}

	private void valSysPartRef(ASTNode node) {
	// TODO Auto-generated method stub
	
	}

	private void valId(String string) {
		// TODO Auto-generated method stub
		
	}
	
	private void valSysVar(ASTNode sysVarNode) {
		// TODO Auto-generated method stub
	}

	private void valSysVar(String string) {
		// TODO Auto-generated method stub
		
	}

}
