package validation;

import compiler.syntax.ASTNode;
import compiler.syntax.sym;
import compiler.syntax.astsym;
import validation.SymHash;

import java.util.List;
import java.util.LinkedList;

public class NewValidator {
	private ASTNode root;
	private SymHash sh;
	private List<String> errors;
	
	public NewValidator(ASTNode root) {
		this.root = root;
		sh = new SymHash();
		errors = new LinkedList<String>();
	}
	
	public boolean isValid() {
		walk(root, 0);
		return errors.size() == 0;
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	// Walks the AST performing actions as we go
	private void walk(Object o, int tabLevel) {
		if (!(o instanceof ASTNode)) {
			//StringBuffer sb = new StringBuffer();
			
			//for (int i = 0; i < tabLevel; i++) sb.append("\t");
			
			//if (o != null) sb.append("Leaf: " + o.getClass());
			//else sb.append("NULL (Most likely list end)");
				
			//System.out.println(sb);
			return;
		}
		
		ASTNode node = (ASTNode)o;
		int numOps = node.getNumberOfOperands();
				
		// Perform actions here
		
		// print the AST, for debugging purposes
		//printNode(node, tabLevel);
		
		// Lets do some validation!
		if (node.getDescriptor() instanceof Integer) {
			switch ((Integer)node.getDescriptor()) {
			case sym.IF:
			case sym.ELSE:
			case sym.WHILE:
				validateIfWhile(node);
				break;
			}
		}
		
		// Stupid hack for when the descriptor contains a node
		// for instance the root node
		if (node.getDescriptor() instanceof ASTNode)
			walk(node.getDescriptor(), tabLevel+1);
		
		for (int i = 0; i < numOps; i++) {
			walk(node.getOp(i), tabLevel+1);
		}
	}
		
	private void printNode(ASTNode node, int tabLevel) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < tabLevel; i++) sb.append("\t");
		
		if (node.getDescriptor() instanceof Integer) {
			Integer desc;
			sb.append(node.getLineNumber() + ": " + sh.get(desc = (Integer)node.getDescriptor()));
			
			// Stupid hacks for malformed and odd nodes start here
			
			// Function calls store the type of function call
			// (step() vs normal) in the first operand
			if (desc == astsym.FUNCTION_CALL)
				sb.append(" " + sh.get((Integer)node.getOp(0)));
			
			// Participant stores the filename in the descriptor
			if (desc == sym.PARTICIPANT)
				sb.append(" Filename: \"" + (String)node.getOp(0) + "\"");			
		} else if (node.getDescriptor() == null) {
			sb.append("Null Descriptor");
		} else {
			sb.append("Non-Integer Descriptor");
		}
		System.out.println(sb);
	}
	
	private String makeError(String errorString, int lineNumber) {
		return "Error: " + errorString;
	}
	
	// Makes sure that if, if-else, and while statements contain
	// boolean expressions and not numeric expressions
	private void validateIfWhile(ASTNode node) {
		ASTNode expression = (ASTNode)node.getOp(0);
		int exprType = (Integer)expression.getDescriptor();
		
		String blockType;
		if ((Integer)(node.getDescriptor()) == sym.IF || (Integer)(node.getDescriptor()) == sym.ELSE)
			blockType = "an IF";
		else
			blockType = "a WHILE";
		
		switch (exprType) {
		case sym.LPREN:
			validateIfWhile((ASTNode)node.getOp(0));
			break;
		case astsym.FUNCTION_CALL:
			if ((String)node.getOp(1) != "set")
				errors.add(makeError("Only boolean expressions are allowed as the condition for " + blockType + " statement.  The set() function is the only function that returns a boolean value",
									 node.getLineNumber()));
			break;
		case sym.PLUS:
		case sym.MINUS:
		case sym.TIMES:
		case sym.DIVIDE:
		case sym.MOD:
		case sym.NUMBER:
		case sym.DECIMAL:
			errors.add(makeError("Only boolean expressions are allowed as the condition for " + blockType + " statement.",
								 node.getLineNumber()));
			break;
		}
		
	}
}
