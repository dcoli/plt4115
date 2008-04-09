java JLex.Main scanner.lex
copy scanner.lex.java Yylex.java
java java_cup.Main < parser.cup
javac -d . parser.java sym.java Yylex.java
del scanner.lex.java
move *.java ../compiler/syntax
