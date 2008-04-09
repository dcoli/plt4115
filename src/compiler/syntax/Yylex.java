package compiler.syntax;
import compiler.settings.*;
import java_cup.runtime.Symbol;
import java.util.LinkedList;
import java.io.*;


public class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

	public static LinkedList<String> files;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

	files = new LinkedList();
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"2:9,29,30,2,29,30,2:18,29,48,27,2,43,42,47,2,34,35,38,41,49,40,28,1,26:10,2" +
",33,44,39,45,2:2,25:26,36,2,37,2,24,2,4,12,16,19,6,20,18,22,8,25:2,17,5,3,1" +
"0,15,23,9,14,11,13,7,21,25:3,31,46,32,2:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,165,
"0,1,2,1,3,4,1:9,5,6,7,8,9,1,10,11,12,1,13,1,14:2,15,1:10,14:3,16,14:3,17,14" +
":15,16,15,18,19,15,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38" +
",39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63" +
",64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88" +
",89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,14,107,108,10" +
"9,110,111,112,113,114,115")[0];

	private int yy_nxt[][] = unpackFromString(116,50,
"1,2,3,4,142,65,92,155,69,159,155:4,160,161,155:2,162,93,163,164,155:4,5,64," +
"6,7:2,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,68,70,23,24,-1:51,25,-1:37" +
",26,-1:13,155,94,155:8,95,155:13,-1:49,66,-1,63,-1:60,30,-1:49,31,-1:49,32," +
"-1:49,33,-1:49,34,-1:49,35,-1:49,36,-1:49,39,-1:11,25:29,-1,25:19,-1:3,155:" +
"24,-1:24,67:26,29,67:2,-1,67:19,-1:26,43,-1:26,155:5,149,155:18,-1:26,155:3" +
",27,155:20,-1:49,66,-1:69,37,-1:6,72,155:16,28,155:6,-1:70,38,-1:5,155:4,15" +
"6,155:11,40,155:7,-1:26,155:8,41,155:15,-1:26,155:17,42,155:6,-1:26,155:3,4" +
"4,155:20,-1:26,155:3,45,155:20,-1:26,155:12,46,155:11,-1:26,155:8,47,155:15" +
",-1:26,155:5,48,155:11,49,155:6,-1:26,155:8,50,155:15,-1:26,155:3,51,155:20" +
",-1:26,52,155:23,-1:26,53,155:23,-1:26,155:14,54,155:9,-1:26,155:16,55,155:" +
"7,-1:26,155:11,56,155:12,-1:26,155:11,57,155:12,-1:26,155:11,58,155:12,-1:2" +
"6,59,155:23,-1:26,155:11,60,155:12,-1:26,155:8,61,155:15,-1:26,155:8,62,155" +
":15,-1:26,71,155:13,97,155:9,-1:26,155:3,73,155:20,-1:26,155:2,74,155:21,-1" +
":26,155:2,104,155:21,-1:26,155:8,105,155:15,-1:26,155:11,75,155:12,-1:26,10" +
"6,155:23,-1:26,155:8,107,155:11,147,155:3,-1:26,155:3,76,155:20,-1:26,155:6" +
",77,155:17,-1:26,155:7,108,155:16,-1:26,155:5,110,155:18,-1:26,155:21,111,1" +
"55:2,-1:26,155:6,112,155:17,-1:26,155:16,78,155:7,-1:26,155:10,115,155:13,-" +
"1:26,155:9,117,155:14,-1:26,155,79,155:22,-1:26,155:14,80,155:9,-1:26,155,1" +
"18,155:9,119,120,155:11,-1:26,155:5,121,155:18,-1:26,155:7,81,155:16,-1:26," +
"155:6,122,155:17,-1:26,155:6,82,155:17,-1:26,155:14,151,155:9,-1:26,155,83," +
"155:22,-1:26,155:13,150,155:10,-1:26,155:8,124,155:15,-1:26,155,152,155:22," +
"-1:26,155:9,125,155:14,-1:26,155:7,126,155:16,-1:26,155:6,127,155:17,-1:26," +
"155:3,130,155:20,-1:26,155:10,153,155:13,-1:26,132,155:23,-1:26,155:3,84,15" +
"5:20,-1:26,155:8,158,155:15,-1:26,155:5,133,155:18,-1:26,155:12,85,155:11,-" +
"1:26,155:8,86,155:15,-1:26,155:2,136,155:21,-1:26,155:12,138,155:11,-1:26,1" +
"55:7,139,155:16,-1:26,155:3,87,155:20,-1:26,155:3,140,155:20,-1:26,155:7,88" +
",155:16,-1:26,155,141,155:22,-1:26,89,155:23,-1:26,90,155:23,-1:26,91,155:2" +
"3,-1:26,155:8,96,155:4,144,155:10,-1:26,155:2,157,155:21,-1:26,155:8,146,15" +
"5:15,-1:26,155:7,109,155:16,-1:26,155:5,113,155:18,-1:26,155:10,148,155:13," +
"-1:26,155:5,123,155:18,-1:26,155:13,129,155:10,-1:26,155:8,154,155:15,-1:26" +
",155,128,155:22,-1:26,155:6,131,155:17,-1:26,155:8,135,155:15,-1:26,155:5,1" +
"34,155:18,-1:26,155:5,114,155:18,-1:26,155:10,116,155:13,-1:26,155:5,137,15" +
"5:18,-1:26,155,98,155,99,155:20,-1:26,155:5,143,155:2,100,155:15,-1:26,155," +
"101,155:22,-1:26,155:14,102,155:9,-1:26,155:14,145,155:9,-1:26,155:19,103,1" +
"55:4,-1:23");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

if (files.size() == 0)
  return new Symbol(sym.EOF);
else {
	yy_reader.close();
	String nextFile = Settings.getCurrentWorkingDirectory() + (String)files.removeFirst();
	yy_reader = new BufferedReader(new InputStreamReader(new FileInputStream(nextFile)));
	System.out.println("Now tokenizing " + nextFile);
}
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(sym.DIVIDE); }
					case -3:
						break;
					case 3:
						{ System.err.println("Illegal character: "+yytext()); }
					case -4:
						break;
					case 4:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -5:
						break;
					case 5:
						{ return new Symbol(sym.NUMBER, new Integer(yytext())); }
					case -6:
						break;
					case 6:
						{ return new Symbol(sym.DOT); }
					case -7:
						break;
					case 7:
						{ /* ignore white space. */ }
					case -8:
						break;
					case 8:
						{ return new Symbol(sym.LBRC); }
					case -9:
						break;
					case 9:
						{ return new Symbol(sym.RBRC); }
					case -10:
						break;
					case 10:
						{ return new Symbol(sym.SEMI); }
					case -11:
						break;
					case 11:
						{ return new Symbol(sym.LPREN); }
					case -12:
						break;
					case 12:
						{ return new Symbol(sym.RPREN); }
					case -13:
						break;
					case 13:
						{ return new Symbol(sym.LSBRK); }
					case -14:
						break;
					case 14:
						{ return new Symbol(sym.RSBRK); }
					case -15:
						break;
					case 15:
						{ return new Symbol(sym.TIMES); }
					case -16:
						break;
					case 16:
						{ return new Symbol(sym.EQ); }
					case -17:
						break;
					case 17:
						{ return new Symbol(sym.MINUS); }
					case -18:
						break;
					case 18:
						{ return new Symbol(sym.PLUS); }
					case -19:
						break;
					case 19:
						{ return new Symbol(sym.MOD); }
					case -20:
						break;
					case 20:
						{ return new Symbol(sym.DOLLAR); }
					case -21:
						break;
					case 21:
						{ return new Symbol(sym.LT); }
					case -22:
						break;
					case 22:
						{ return new Symbol(sym.GT); }
					case -23:
						break;
					case 23:
						{ return new Symbol(sym.NOT); }
					case -24:
						break;
					case 24:
						{ return new Symbol(sym.COMMA); }
					case -25:
						break;
					case 25:
						{ /* ignore comments. */ }
					case -26:
						break;
					case 26:
						{ return new Symbol(sym.DIVIDEEQ); }
					case -27:
						break;
					case 27:
						{ return new Symbol(sym.ME); }
					case -28:
						break;
					case 28:
						{ return new Symbol(sym.IF); }
					case -29:
						break;
					case 29:
						{ return new Symbol(sym.STRING, new String(yytext().substring(1, yytext().length() - 1))); }
					case -30:
						break;
					case 30:
						{ return new Symbol(sym.TIMESEQ); }
					case -31:
						break;
					case 31:
						{ return new Symbol(sym.EQEQ); }
					case -32:
						break;
					case 32:
						{ return new Symbol(sym.MINUSEQ); }
					case -33:
						break;
					case 33:
						{ return new Symbol(sym.PLUSEQ); }
					case -34:
						break;
					case 34:
						{ return new Symbol(sym.MODEQ); }
					case -35:
						break;
					case 35:
						{ return new Symbol(sym.LTEQ); }
					case -36:
						break;
					case 36:
						{ return new Symbol(sym.GTEQ); }
					case -37:
						break;
					case 37:
						{ return new Symbol(sym.OR); }
					case -38:
						break;
					case 38:
						{ return new Symbol(sym.AND); }
					case -39:
						break;
					case 39:
						{ return new Symbol(sym.NOTEQ); }
					case -40:
						break;
					case 40:
						{ return new Symbol(sym.END); }
					case -41:
						break;
					case 41:
						{ return new Symbol(sym.INT); }
					case -42:
						break;
					case 42:
						{ return new Symbol(sym.DEF); }
					case -43:
						break;
					case 43:
						{ return new Symbol(sym.DECIMAL, new Float(yytext())); }
					case -44:
						break;
					case 44:
						{ return new Symbol(sym.NAME); }
					case -45:
						break;
					case 45:
						{ return new Symbol(sym.ELSE); }
					case -46:
						break;
					case 46:
						{ return new Symbol(sym.STEP); }
					case -47:
						break;
					case 47:
						{ return new Symbol(sym.PART); }
					case -48:
						break;
					case 48:
						{ return new Symbol(sym.RANDI); }
					case -49:
						break;
					case 49:
						{ return new Symbol(sym.RANDF); }
					case -50:
						break;
					case 50:
						{ return new Symbol(sym.FLOAT); }
					case -51:
						break;
					case 51:
						{ return new Symbol(sym.WHILE); }
					case -52:
						break;
					case 52:
						{ return new Symbol(sym.ACTION); }
					case -53:
						break;
					case 53:
						{ return new Symbol(sym.RETURN); }
					case -54:
						break;
					case 54:
						{ return new Symbol(sym.GLOBAL); }
					case -55:
						break;
					case 55:
						{ return new Symbol(sym.REQUIRED); }
					case -56:
						break;
					case 56:
						{ return new Symbol(sym.NUM_STEPS); }
					case -57:
						break;
					case 57:
						{ return new Symbol(sym.NUM_PARTS); }
					case -58:
						break;
					case 58:
						{ return new Symbol(sym.ATTRIBUTES); }
					case -59:
						break;
					case 59:
						{ return new Symbol(sym.SIMULATION); }
					case -60:
						break;
					case 60:
						{ return new Symbol(sym.NUM_ACTIONS); }
					case -61:
						break;
					case 61:
						{ return new Symbol(sym.ENVIRONMENT); }
					case -62:
						break;
					case 62:
						{ return new Symbol(sym.PARTICIPANT); }
					case -63:
						break;
					case 64:
						{ System.err.println("Illegal character: "+yytext()); }
					case -64:
						break;
					case 65:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -65:
						break;
					case 66:
						{ return new Symbol(sym.NUMBER, new Integer(yytext())); }
					case -66:
						break;
					case 68:
						{ System.err.println("Illegal character: "+yytext()); }
					case -67:
						break;
					case 69:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -68:
						break;
					case 70:
						{ System.err.println("Illegal character: "+yytext()); }
					case -69:
						break;
					case 71:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -70:
						break;
					case 72:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -71:
						break;
					case 73:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -72:
						break;
					case 74:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -73:
						break;
					case 75:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -74:
						break;
					case 76:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -75:
						break;
					case 77:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -76:
						break;
					case 78:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -77:
						break;
					case 79:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -78:
						break;
					case 80:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -79:
						break;
					case 81:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -80:
						break;
					case 82:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -81:
						break;
					case 83:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -82:
						break;
					case 84:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -83:
						break;
					case 85:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -84:
						break;
					case 86:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -85:
						break;
					case 87:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -86:
						break;
					case 88:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -87:
						break;
					case 89:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -88:
						break;
					case 90:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -89:
						break;
					case 91:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -90:
						break;
					case 92:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -91:
						break;
					case 93:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -92:
						break;
					case 94:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -93:
						break;
					case 95:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -94:
						break;
					case 96:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -95:
						break;
					case 97:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -96:
						break;
					case 98:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -97:
						break;
					case 99:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -98:
						break;
					case 100:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -99:
						break;
					case 101:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -100:
						break;
					case 102:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -101:
						break;
					case 103:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -102:
						break;
					case 104:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -103:
						break;
					case 105:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -104:
						break;
					case 106:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -105:
						break;
					case 107:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -106:
						break;
					case 108:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -107:
						break;
					case 109:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -108:
						break;
					case 110:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -109:
						break;
					case 111:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -110:
						break;
					case 112:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -111:
						break;
					case 113:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -112:
						break;
					case 114:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -113:
						break;
					case 115:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -114:
						break;
					case 116:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -115:
						break;
					case 117:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -116:
						break;
					case 118:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -117:
						break;
					case 119:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -118:
						break;
					case 120:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -119:
						break;
					case 121:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -120:
						break;
					case 122:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -121:
						break;
					case 123:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -122:
						break;
					case 124:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -123:
						break;
					case 125:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -124:
						break;
					case 126:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -125:
						break;
					case 127:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -126:
						break;
					case 128:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -127:
						break;
					case 129:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -128:
						break;
					case 130:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -129:
						break;
					case 131:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -130:
						break;
					case 132:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -131:
						break;
					case 133:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -132:
						break;
					case 134:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -133:
						break;
					case 135:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -134:
						break;
					case 136:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -135:
						break;
					case 137:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -136:
						break;
					case 138:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -137:
						break;
					case 139:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -138:
						break;
					case 140:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -139:
						break;
					case 141:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -140:
						break;
					case 142:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -141:
						break;
					case 143:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -142:
						break;
					case 144:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -143:
						break;
					case 145:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -144:
						break;
					case 146:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -145:
						break;
					case 147:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -146:
						break;
					case 148:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -147:
						break;
					case 149:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -148:
						break;
					case 150:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -149:
						break;
					case 151:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -150:
						break;
					case 152:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -151:
						break;
					case 153:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -152:
						break;
					case 154:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -153:
						break;
					case 155:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -154:
						break;
					case 156:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -155:
						break;
					case 157:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -156:
						break;
					case 158:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -157:
						break;
					case 159:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -158:
						break;
					case 160:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -159:
						break;
					case 161:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -160:
						break;
					case 162:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -161:
						break;
					case 163:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -162:
						break;
					case 164:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -163:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
