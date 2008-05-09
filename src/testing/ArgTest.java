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
	
}
