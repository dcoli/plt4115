package compiler.syntax;

import java.util.LinkedList;

public class ASTNode {
	private Object descriptor;
	private LinkedList<Object> opList;
	private int lineNumber;
	
	public ASTNode (Object descriptor){
		this.descriptor = descriptor;
		opList = new LinkedList<Object>();
	}
	
	public void pushOp(Object o){
		opList.add(o);
	}
	
	public Object getOp(int i){
		return opList.get(i);
	}
	
	public Object getDescriptor(){
		return descriptor;
	}
	
	public void setLineNumber(int i){
		this.lineNumber = i;
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}
}
