package validation;

import java.util.HashMap;

public class SymHash {
	public HashMap symtable = new HashMap();


public SymHash () {
	symtable.put("55","DIVIDE");
	symtable.put("51","GTEQ");
	symtable.put("47","LSBRK");
	symtable.put("34","LBRC");
	symtable.put("50","LTEQ");
	symtable.put("44","MODEQ");
	symtable.put("24","NUM_PARTS");
	symtable.put("40","TIMESEQ");
	symtable.put("28","ME");
	symtable.put("36","SEMI");
	symtable.put("37","LPREN");
	symtable.put("25","NUM_ACTIONS");
	symtable.put("15","INT");
	symtable.put("53","MINUS");
	symtable.put("26","RANDI");
	symtable.put("27","RANDF");
	symtable.put("63","NOT");
	symtable.put("59","AND");
	symtable.put("61","LT");
	symtable.put("60","OR");
	symtable.put("39","COMMA");
	symtable.put("19","REQUIRED");
	symtable.put("43","PLUSEQ");
	symtable.put("42","MINUSEQ");
	symtable.put("52","PLUS");
	symtable.put("6","SIMULATION");
	symtable.put("48","RSBRK");
	symtable.put("11","IF");
	symtable.put("17","ATTRIBUTES");
	symtable.put("49","DOT");
	symtable.put("41","DIVIDEEQ");
	symtable.put("33","ID");
	symtable.put("0","EOF");
	symtable.put("20","BOOLEAN");
	symtable.put("32","DECIMAL");
	symtable.put("14","RETURN");
	symtable.put("21","TRUE");
	symtable.put("1","error");
	symtable.put("35","RBRC");
	symtable.put("38","RPREN");
	symtable.put("57","EQEQ");
	symtable.put("3","ENVIRONMENT");
	symtable.put("56","MOD");
	symtable.put("31","NUMBER");
	symtable.put("7","GLOBAL");
	symtable.put("9","ACTION");
	symtable.put("45","EQ");
	symtable.put("2","NAME");
	symtable.put("54","TIMES");
	symtable.put("5","PART");
	symtable.put("4","PARTICIPANT");
	symtable.put("29","LAST_PART");
	symtable.put("12","ELSE");
	symtable.put("13","WHILE");
	symtable.put("46","DOLLAR");
	symtable.put("16","FLOAT");
	symtable.put("10","END");
	symtable.put("30","STRING");
	symtable.put("18","DEF");
	symtable.put("23","NUM_STEPS");
	symtable.put("22","FALSE");
	symtable.put("62","GT");
	symtable.put("58","NOTEQ");
	symtable.put("8","STEP");
}

	public String get (int key) {
		return (String) symtable.get(String.valueOf(key));
	}

	public String get (String key) {
		return (String) symtable.get(key);
	}
}