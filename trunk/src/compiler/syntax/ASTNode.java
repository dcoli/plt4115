package compiler.syntax;

import java.util.LinkedList;

public class ASTNode {
	private Object descriptor;
	private LinkedList opList;
	
	public ASTNode (Object descriptor){
		this.descriptor = descriptor;
		opList = new LinkedList();
	}
	
	public void pushOp(Object o){
		opList.addFirst(o);
	}
	
	public Object getOp(int i){
		return opList.get(i);
	}
	
	public Object getDescriptor(){
		return descriptor;
	}
}
