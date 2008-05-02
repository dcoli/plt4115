package compiler.syntax;

import java.util.LinkedList;

public class ASTNode {
	private Object descriptor;
	//private LinkedList<Object> opList;
	private Object[] opList;
	private int lineNumber;
	private int numOps = 0;
	
	public ASTNode (Object descriptor){
		this.descriptor = descriptor;
		//opList = new LinkedList<Object>();
		
		// did this for debugging purposes.  talk to me b4 changing back.
		opList = new Object[4];
	}
	
	public void pushOp(Object o){
		opList[numOps++] = o;
		//opList.add(o);
	}
	
	public Object getOp(int i){
		Object o;
		
		try {
			return opList[i];
		}
		catch (IndexOutOfBoundsException e) {
			o = null;
		}
		
		return o;
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
	
	public int getNumberOfOperands(){
		return numOps;
	}
}
