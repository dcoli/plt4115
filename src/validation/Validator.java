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

	public static String emessage = "";
	
	private static boolean allsWell = true;
	
	/*	private static int place;

	public static final int IN_ENVIRONMENT_STEP = 1;

	public static final int IN_ENVIRONMENT_END = 1;

	public static final int IN_ACTION = 2;

	public static final int IN_PARTICIPANT_STEP = 3;

	public static final String RUMVAR = "rumVar_";

	public static final String RUMACTION = "rumAction_";

	public static final String PACKAGE_STRUCTURE = "/rumble/runtime/";

	public static final String PARTICIPANT_CLASS_NAME = "participant";
*/
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

		// create packages if they're not there already
//		(new File(Settings.outputPath + PACKAGE_STRUCTURE)).mkdirs();
		validateStart(root);
		if (allsWell) System.out.println("Validated.");
		else System.out.println(emessage);
		return allsWell;
//		checkEnvironmentFile(root);
//		writeParticipantFiles((ASTNode) root.getOp(1));
//		writeRuntimeFile();
//		checkNot(root);
	}

	private void validateStart (ASTNode root) {
		ASTNode simulationFileNode = (ASTNode) root.getDescriptor();
		validateSimulationFile(simulationFileNode);
		ASTNode environmentFileNode = (ASTNode) root.getOp(0);
		validateEnvironmentFile(environmentFileNode);
		ASTNode participantFilesNode = (ASTNode) root.getOp(1);
		valPartFiles(participantFilesNode);
//		allsWell = false;
	}
	
//EnvironmentFile ::= ENVIRONMENT Meta:m Interface:i Step:s Actions:a 
	private void validateEnvironmentFile( ASTNode node ) {
		String metaName = (String) node.getOp(0);
//		debugGeneration("environment file: " + metaName + "");
		debugGeneration( "environment file: " + metaName + "");
		ASTNode interfaceNode = (ASTNode) node.getOp(1);
		valInterface (interfaceNode);
		valStep ((ASTNode) node.getOp(2));
		valActions ((ASTNode) node.getOp(3));
//		allsWell = false;
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

	private void valArg(ASTNode argNode) {
		ASTNode dataTypeNode = (ASTNode) argNode.getOp(0);
		valDataType (dataTypeNode);
		ASTNode idNode = (ASTNode) argNode.getOp(1);
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
			ASTNode expNode = (ASTNode) node.getOp(0);
			valExp (expNode);
			ASTNode statementNode = (ASTNode) node.getOp(1);
			valStatement (statementNode);
			break;
		}			
		case sym.RETURN: {
			debugGeneration("Return");
			ASTNode expNode = (ASTNode) node.getOp(0);
			valExp (expNode);
			break;
		}		
		case astsym.DECLARATION: {
			debugGeneration("declaration");
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
	
	private void valFunc(ASTNode node) {
		// TODO Auto-generated method stub
		
	}

	//IfStatement ::= IF LPREN Expression:e RPREN Statement:s
	private void valIf(ASTNode node) {
		valExp ((ASTNode) node.getOp(0));
		valStatement ((ASTNode) node.getOp(1));
	}
	
	//		debugGeneration( "statement sucks";
	//		allsWell = false;

	//ElseStatement ::= IF LPREN Expression:e RPREN Statement:s1 ELSE Statement:s2
	private void valElse(ASTNode node) {
		valExp ((ASTNode) node.getOp(0));
		valStatement ((ASTNode) node.getOp(1));
		valStatement ((ASTNode) node.getOp(2));
	}
	

	
	
	private void valAss(ASTNode assNode) {
		// TODO Auto-generated method stub
		debugGeneration( "assignment");
		if (assNode != null)
			if ( assNode.getNumberOfOperands() > 1 ) {
				ASTNode lValNode = (ASTNode) assNode.getOp(0);
				ASTNode expNode = (ASTNode) assNode.getOp(1);
				switch ((Integer) assNode.getDescriptor()) {
				case (sym.TIMESEQ): valLVal( lValNode); valExp (expNode); break;
				case (sym.DIVIDEEQ): valLVal( lValNode); valExp (expNode); break;
				case (sym.MINUSEQ): valLVal( lValNode); valExp (expNode); break;
				case (sym.PLUSEQ): valLVal( lValNode); valExp (expNode); break;
				case (sym.MODEQ): valLVal( lValNode); valExp (expNode); break;
				case (sym.EQ): valLVal( lValNode); valExp (expNode); break;
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

	private void valId(String string) {
		// TODO Auto-generated method stub
		
	}

	private void valId(ASTNode idNode) {
		// TODO Auto-generated method stub
		
	}

	private void valDataType(ASTNode dataTypeNode) {
		// TODO Auto-generated method stub
		
	}

	private void valAttDecList(ASTNode attDecListNode) {
		// TODO Auto-generated method stub
		
	}

	private void validateId(ASTNode idNode) {
		// TODO Auto-generated method stub
		
	}

	
	
	private void valSysVar(ASTNode sysVarNode) {
		// TODO Auto-generated method stub
		
	}

	private void valExp(ASTNode expNode) {
		// TODO Auto-generated method stub
		
	}

	private void valLVal(ASTNode valNode) {
		// TODO Auto-generated method stub
		
	}

}
