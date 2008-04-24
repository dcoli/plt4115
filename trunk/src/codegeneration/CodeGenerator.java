package codegeneration;
import compiler.syntax.*;

public class CodeGenerator {
	
	private ASTNode root;
	private int currentLocation = 0;
	
	public CodeGenerator(ASTNode root){
		this.root = root;
	}
	
	public void go(){
		generateEnvironmentFile((ASTNode)root.getDescriptor(), (ASTNode)root.getOp(0));
		
		if (root.getOp(1) != null)
			generateParticipantFiles((ASTNode)root.getDescriptor(), (ASTNode)root.getOp(1));
		
	}
	
	public void generateEnvironmentFile(ASTNode simulationNode, ASTNode environmentNode){
		
	}
	
	public void generateParticipantFiles(ASTNode simulationNode, ASTNode participantFileList){
		
	}
	
}
