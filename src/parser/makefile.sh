java JLex.Main scanner.lex
mv scanner.lex.java Yylex.java
java java_cup.Main < parser.cup
javac -d . parser.java sym.java Yylex.java
rm scanner.lex.java
mv *.java ../syntax