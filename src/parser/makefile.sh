java JLex.Main minimal.lex
mv scanner.lex.java Yylex.java
java java_cup.Main < parser.cup
javac -d . parser.java sym.java Yylex.java