java JLex.Main minimal.lex
copy minimal.lex.java Yylex.java
java java_cup.Main < minimal.cup
javac -d . parser.java sym.java Yylex.java