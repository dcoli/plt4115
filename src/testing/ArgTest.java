package testing;

import org.junit.Test;
import static org.junit.Assert.*;

import java_cup.runtime.Symbol;
import compiler.syntax.ASTNode;
import compiler.syntax.astsym;
import compiler.syntax.sym;
import codegeneration.CodeGenerator;

public class ArgTest {
	
	@Test
	public void intTest() {
        Symbol i = new Symbol(sym.INT, 0, 0, "foo");
        
        ASTNode n = new ASTNode(astsym.ARG);//sym.int,sym.float,etc.
        n.pushOp(sym.INT);
        n.pushOp(i);
        
        CodeGenerator c = new CodeGenerator(n);
        System.out.println(c.generateArg(n));
        
        String s = "int foo";
        
        assertEquals(s, c.generateArg(n));
	}
	
	@Test
	public void floatTest() {
        Symbol i = new Symbol(sym.FLOAT, 0, 0, "foo");
        
        ASTNode n = new ASTNode(astsym.ARG);//sym.int,sym.float,etc.
        n.pushOp(sym.FLOAT);
        n.pushOp(i);
        
        CodeGenerator c = new CodeGenerator(n);
        System.out.println(c.generateArg(n));
        
        String s = "float foo";
        
        assertEquals(s, c.generateArg(n));
	}
	
	@Test
	public void booleanTest() {
        Symbol i = new Symbol(sym.BOOLEAN, 0, 0, "foo");
        
        ASTNode n = new ASTNode(astsym.ARG);//sym.int,sym.float,etc.
        n.pushOp(sym.BOOLEAN);
        n.pushOp(i);
        
        CodeGenerator c = new CodeGenerator(n);
        System.out.println(c.generateArg(n));
        
        String s = "boolean foo";
        
        assertEquals(s, c.generateArg(n));
	}
	
	@Test
	public void participantTest() {
        Symbol i = new Symbol(sym.PARTICIPANT, 0, 0, "foo");
        
        ASTNode n = new ASTNode(astsym.ARG);//sym.int,sym.float,etc.
        n.pushOp(sym.PARTICIPANT);
        n.pushOp(i);
        
        CodeGenerator c = new CodeGenerator(n);
        System.out.println(c.generateArg(n));
        
        String s = "participant foo";
        
        assertEquals(s, c.generateArg(n));
	}
}
