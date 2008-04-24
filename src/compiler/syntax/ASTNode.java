package compiler.syntax;

import java.util.LinkedList;

public class ASTNode {
	private Object descriptor;
	private Object[] opList;
	private int lineNumber;
	private int opCount = 0;
	
	public ASTNode (Object descriptor){
		this.descriptor = descriptor;
		opList = new Object[4];
	}
	
	public void pushOp(Object o){
		opList[opCount++] = o;
	}
	
	public Object getOp(int i){
		return opList[i];
	}
	
	public Object getDescriptor(){
		return descriptor;
	}
	
	public void setLineNumber(int i){
		lineNumber = i;
	}
}
