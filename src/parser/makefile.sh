java JLex.Main minimal.lex
mv minimal.lex.java Yylex.java
java java_cup.Main < minimal.cup
javac -d . parser.java sym.java Yylex.java