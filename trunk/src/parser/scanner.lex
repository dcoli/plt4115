package compiler.syntax;
import compiler.settings.*;
import java_cup.runtime.Symbol;
import java.util.LinkedList;
import java.io.*;

%%
%cup
%public

%{
	public static LinkedList<String> files;
	public static int currentLine;
	public static String nextFile;
	public static String baseFile;
	
	public static void displayError(String message){
		System.err.println("Error in " + nextFile + ", line " + currentLine + ": " + message);
	}
	
	public static void determineNeedToParse(String participantFilePath){
		if (!Settings.participantFiles.containsKey(participantFilePath)){
			Yylex.files.addLast(participantFilePath);
			Settings.participantFiles.put(participantFilePath, participantFilePath);
		}
	}
%}

%init{
	currentLine = 1;
	files = new LinkedList();
%init} 

%eofval{
if (files.size() == 0)
  return new Symbol(sym.EOF);
else {
	yy_reader.close();
	baseFile = (String)files.removeFirst();
	nextFile = Settings.getCurrentWorkingDirectory() + baseFile;
	baseFile = baseFile.substring(0, baseFile.indexOf('.'));
	
	yy_reader = new BufferedReader(new InputStreamReader(new FileInputStream(nextFile)));
	currentLine = 1;
	System.out.println("Now tokenizing " + baseFile);
}
%eofval}

%%

"//".* { /* ignore comments. */ }	 
"name" { return new Symbol(sym.NAME); }
"environment" { return new Symbol(sym.ENVIRONMENT); }
"attributes" { return new Symbol(sym.ATTRIBUTES); }
"participant" { return new Symbol(sym.PARTICIPANT); }
"part" { return new Symbol(sym.PART); }
"simulation" { return new Symbol(sym.SIMULATION); }
"global" { return new Symbol(sym.GLOBAL); }
"step" { return new Symbol(sym.STEP); }
"action" { return new Symbol(sym.ACTION); }
"end" { return new Symbol(sym.END); }
"if" { return new Symbol(sym.IF); }
"else" { return new Symbol(sym.ELSE); }
"while" { return new Symbol(sym.WHILE); }
"return" { return new Symbol(sym.RETURN); }
"int" { return new Symbol(sym.INT); }
"float" { return new Symbol(sym.FLOAT); }
"boolean" { return new Symbol(sym.BOOLEAN); }
"true" { return new Symbol(sym.TRUE); }
"false" { return new Symbol(sym.FALSE); }
"def" { return new Symbol(sym.DEF); }
"required" { return new Symbol(sym.REQUIRED); }

"num_steps" { return new Symbol(sym.NUM_STEPS); }
"num_parts" { return new Symbol(sym.NUM_PARTS); }
"num_actions" { return new Symbol(sym.NUM_ACTIONS); }
"randi" { return new Symbol(sym.RANDI); }
"randf" { return new Symbol(sym.RANDF); }
"me" { return new Symbol(sym.ME); }

[A-Za-z_][A-Za-z_0-9]* { return new Symbol(sym.ID, new String(yytext())); }
\".*\" { return new Symbol(sym.STRING, new String(yytext().substring(1, yytext().length() - 1))); }
[0-9]+ { return new Symbol(sym.NUMBER, new Integer(yytext())); }
[0-9]\.[0-9]+ { return new Symbol(sym.DECIMAL, new Float(yytext())); }
[ \t\r\f] { /* ignore white space. */ }
[\n]	{ currentLine++; }

"{" { return new Symbol(sym.LBRC); }
"}" { return new Symbol(sym.RBRC); }
";" { return new Symbol(sym.SEMI); }
"(" { return new Symbol(sym.LPREN); }
")" { return new Symbol(sym.RPREN); }
"[" { return new Symbol(sym.LSBRK); }
"]" { return new Symbol(sym.RSBRK); }
"*=" { return new Symbol(sym.TIMESEQ); }
"/=" { return new Symbol(sym.DIVIDEEQ); }
"-=" { return new Symbol(sym.MINUSEQ); }
"+=" { return new Symbol(sym.PLUSEQ); }
"%=" { return new Symbol(sym.MODEQ); }
"=" { return new Symbol(sym.EQ); }
"$" { return new Symbol(sym.DOLLAR); }
"." { return new Symbol(sym.DOT); }
"<" { return new Symbol(sym.LT); }
">" { return new Symbol(sym.GT); }
"<=" { return new Symbol(sym.LTEQ); }
">=" { return new Symbol(sym.GTEQ); }
"||" { return new Symbol(sym.OR); }
"&&" { return new Symbol(sym.AND); }
"+" { return new Symbol(sym.PLUS); }
"-" { return new Symbol(sym.MINUS); }
"*" { return new Symbol(sym.TIMES); }
"/" { return new Symbol(sym.DIVIDE); }
"%" { return new Symbol(sym.MOD); }
"==" { return new Symbol(sym.EQEQ); }
"!=" { return new Symbol(sym.NOTEQ); }
"!" { return new Symbol(sym.NOT); }
"," { return new Symbol(sym.COMMA); }
. { System.err.println("Illegal character: "+yytext()); }
