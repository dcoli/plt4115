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
	public static int currentLine;
	public static String nextFile;
	public static void displayError(String message){
		System.err.println("Error in " + nextFile + ", line " + currentLine + ": " + message);
	}
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

	currentLine = 1;
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
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
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
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"2:9,29,31,2,29,30,2:18,29,49,27,2,44,43,48,2,35,36,39,42,50,41,28,1,26:10,2" +
",34,45,40,46,2:2,25:26,37,2,38,2,24,2,4,12,16,19,6,20,18,22,8,25:2,17,5,3,1" +
"0,15,23,9,14,11,13,7,21,25:3,32,47,33,2:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,181,
"0,1,2,1,3,4,1:10,5,6,7,8,9,1,10,11,12,1,13,1,14:2,15,1:10,14:3,16,14:4,17,1" +
"4:17,16,15,18,19,15,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,3" +
"8,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,6" +
"3,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,8" +
"8,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109," +
"110,111,112,113,114,115,14,116,117,118,119,120,121,122,123,124,125,126,127")[0];

	private int yy_nxt[][] = unpackFromString(128,51,
"1,2,3,4,154,69,99,168,73,173,168,174,175,168,176,177,168:2,178,100,179,180," +
"168:4,5,68,6,7:2,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,72,74,24,25," +
"-1:52,26,-1:38,27,-1:13,168,101,168:8,102,168:13,-1:50,70,-1,67,-1:62,31,-1" +
":50,32,-1:50,33,-1:50,34,-1:50,35,-1:50,36,-1:50,37,-1:50,40,-1:11,26:29,-1" +
":2,26:19,-1:3,168:24,-1:25,71:26,30,71:2,-1:2,71:19,-1:26,44,-1:27,168:5,16" +
"2,168:18,-1:27,168:3,28,168:20,-1:50,70,-1:71,38,-1:6,76,168:16,29,168:6,-1" +
":72,39,-1:5,168:4,170,168:11,41,168:7,-1:27,168:8,42,168:15,-1:27,168:17,43" +
",168:6,-1:27,168:3,45,168:20,-1:27,168:3,46,168:20,-1:27,168:3,47,168:20,-1" +
":27,168:12,48,168:11,-1:27,168:8,49,168:15,-1:27,168:5,50,168:11,51,168:6,-" +
"1:27,168:3,52,168:20,-1:27,168:8,53,168:15,-1:27,168:3,54,168:20,-1:27,55,1" +
"68:23,-1:27,56,168:23,-1:27,168:14,57,168:9,-1:27,58,168:23,-1:27,168:16,59" +
",168:7,-1:27,168:11,60,168:12,-1:27,168:11,61,168:12,-1:27,168:11,62,168:12" +
",-1:27,63,168:23,-1:27,168:11,64,168:12,-1:27,168:8,65,168:15,-1:27,168:8,6" +
"6,168:15,-1:27,75,168:13,104,168:9,-1:27,168:3,77,168:20,-1:27,168:2,78,168" +
":21,-1:27,168:2,113,168:21,-1:27,168:8,114,168:15,-1:27,168:11,79,168:12,-1" +
":27,115,168:23,-1:27,168:8,116,168:11,160,168:3,-1:27,168:10,80,168:13,-1:2" +
"7,168:7,158,168:16,-1:27,168:3,81,168:20,-1:27,168:6,82,168:17,-1:27,168:14" +
",118,168:9,-1:27,168:5,120,168:18,-1:27,168:21,121,168:2,-1:27,168:6,122,16" +
"8:17,-1:27,168:16,83,168:7,-1:27,168:10,125,168:13,-1:27,168:9,128,168:14,-" +
"1:27,168:11,84,168:12,-1:27,168,85,168:22,-1:27,168:14,86,168:9,-1:27,168,1" +
"29,168:9,130,131,168:11,-1:27,168:5,132,168:18,-1:27,168:7,87,168:16,-1:27," +
"168:6,133,168:17,-1:27,168:6,88,168:17,-1:27,168:3,135,168:20,-1:27,168:14," +
"164,168:9,-1:27,168,89,168:22,-1:27,168:13,163,168:10,-1:27,168:8,136,168:1" +
"5,-1:27,168,165,168:22,-1:27,168:9,137,168:14,-1:27,168:7,138,168:16,-1:27," +
"168:6,139,168:17,-1:27,168,90,168:22,-1:27,168:3,142,168:20,-1:27,168:10,16" +
"6,168:13,-1:27,144,168:23,-1:27,168:3,91,168:20,-1:27,168:8,172,168:15,-1:2" +
"7,168:5,145,168:18,-1:27,168:12,92,168:11,-1:27,168:8,93,168:15,-1:27,168:2" +
",148,168:21,-1:27,168:12,150,168:11,-1:27,168:7,151,168:16,-1:27,168:3,94,1" +
"68:20,-1:27,168:3,152,168:20,-1:27,168:7,95,168:16,-1:27,168,153,168:22,-1:" +
"27,96,168:23,-1:27,97,168:23,-1:27,98,168:23,-1:27,168:8,103,168:4,156,168:" +
"10,-1:27,168:2,171,168:21,-1:27,168:8,159,168:15,-1:27,168:7,117,168:16,-1:" +
"27,168:14,126,168:9,-1:27,168:5,123,168:18,-1:27,168:10,161,168:13,-1:27,16" +
"8:5,134,168:18,-1:27,168:13,141,168:10,-1:27,168:8,167,168:15,-1:27,168,140" +
",168:22,-1:27,168:6,143,168:17,-1:27,168:8,147,168:15,-1:27,168:5,146,168:1" +
"8,-1:27,168:7,119,168:16,-1:27,168:5,124,168:18,-1:27,168:10,127,168:13,-1:" +
"27,168:5,149,168:18,-1:27,168,105,168,106,168:20,-1:27,168:6,107,168:17,-1:" +
"27,168:7,108,168:16,-1:27,168:5,155,168:2,109,168:15,-1:27,168,110,168:22,-" +
"1:27,168:14,157,168:9,-1:27,168,111,168:12,169,168:9,-1:27,168:19,112,168:4" +
",-1:24");

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
	nextFile = Settings.getCurrentWorkingDirectory() + (String)files.removeFirst();
	yy_reader = new BufferedReader(new InputStreamReader(new FileInputStream(nextFile)));
	currentLine = 1;
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
						{ currentLine++; }
					case -9:
						break;
					case 9:
						{ return new Symbol(sym.LBRC); }
					case -10:
						break;
					case 10:
						{ return new Symbol(sym.RBRC); }
					case -11:
						break;
					case 11:
						{ return new Symbol(sym.SEMI); }
					case -12:
						break;
					case 12:
						{ return new Symbol(sym.LPREN); }
					case -13:
						break;
					case 13:
						{ return new Symbol(sym.RPREN); }
					case -14:
						break;
					case 14:
						{ return new Symbol(sym.LSBRK); }
					case -15:
						break;
					case 15:
						{ return new Symbol(sym.RSBRK); }
					case -16:
						break;
					case 16:
						{ return new Symbol(sym.TIMES); }
					case -17:
						break;
					case 17:
						{ return new Symbol(sym.EQ); }
					case -18:
						break;
					case 18:
						{ return new Symbol(sym.MINUS); }
					case -19:
						break;
					case 19:
						{ return new Symbol(sym.PLUS); }
					case -20:
						break;
					case 20:
						{ return new Symbol(sym.MOD); }
					case -21:
						break;
					case 21:
						{ return new Symbol(sym.DOLLAR); }
					case -22:
						break;
					case 22:
						{ return new Symbol(sym.LT); }
					case -23:
						break;
					case 23:
						{ return new Symbol(sym.GT); }
					case -24:
						break;
					case 24:
						{ return new Symbol(sym.NOT); }
					case -25:
						break;
					case 25:
						{ return new Symbol(sym.COMMA); }
					case -26:
						break;
					case 26:
						{ /* ignore comments. */ }
					case -27:
						break;
					case 27:
						{ return new Symbol(sym.DIVIDEEQ); }
					case -28:
						break;
					case 28:
						{ return new Symbol(sym.ME); }
					case -29:
						break;
					case 29:
						{ return new Symbol(sym.IF); }
					case -30:
						break;
					case 30:
						{ return new Symbol(sym.STRING, new String(yytext().substring(1, yytext().length() - 1))); }
					case -31:
						break;
					case 31:
						{ return new Symbol(sym.TIMESEQ); }
					case -32:
						break;
					case 32:
						{ return new Symbol(sym.EQEQ); }
					case -33:
						break;
					case 33:
						{ return new Symbol(sym.MINUSEQ); }
					case -34:
						break;
					case 34:
						{ return new Symbol(sym.PLUSEQ); }
					case -35:
						break;
					case 35:
						{ return new Symbol(sym.MODEQ); }
					case -36:
						break;
					case 36:
						{ return new Symbol(sym.LTEQ); }
					case -37:
						break;
					case 37:
						{ return new Symbol(sym.GTEQ); }
					case -38:
						break;
					case 38:
						{ return new Symbol(sym.OR); }
					case -39:
						break;
					case 39:
						{ return new Symbol(sym.AND); }
					case -40:
						break;
					case 40:
						{ return new Symbol(sym.NOTEQ); }
					case -41:
						break;
					case 41:
						{ return new Symbol(sym.END); }
					case -42:
						break;
					case 42:
						{ return new Symbol(sym.INT); }
					case -43:
						break;
					case 43:
						{ return new Symbol(sym.DEF); }
					case -44:
						break;
					case 44:
						{ return new Symbol(sym.DECIMAL, new Float(yytext())); }
					case -45:
						break;
					case 45:
						{ return new Symbol(sym.NAME); }
					case -46:
						break;
					case 46:
						{ return new Symbol(sym.ELSE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(sym.TRUE); }
					case -48:
						break;
					case 48:
						{ return new Symbol(sym.STEP); }
					case -49:
						break;
					case 49:
						{ return new Symbol(sym.PART); }
					case -50:
						break;
					case 50:
						{ return new Symbol(sym.RANDI); }
					case -51:
						break;
					case 51:
						{ return new Symbol(sym.RANDF); }
					case -52:
						break;
					case 52:
						{ return new Symbol(sym.FALSE); }
					case -53:
						break;
					case 53:
						{ return new Symbol(sym.FLOAT); }
					case -54:
						break;
					case 54:
						{ return new Symbol(sym.WHILE); }
					case -55:
						break;
					case 55:
						{ return new Symbol(sym.ACTION); }
					case -56:
						break;
					case 56:
						{ return new Symbol(sym.RETURN); }
					case -57:
						break;
					case 57:
						{ return new Symbol(sym.GLOBAL); }
					case -58:
						break;
					case 58:
						{ return new Symbol(sym.BOOLEAN); }
					case -59:
						break;
					case 59:
						{ return new Symbol(sym.REQUIRED); }
					case -60:
						break;
					case 60:
						{ return new Symbol(sym.NUM_STEPS); }
					case -61:
						break;
					case 61:
						{ return new Symbol(sym.NUM_PARTS); }
					case -62:
						break;
					case 62:
						{ return new Symbol(sym.ATTRIBUTES); }
					case -63:
						break;
					case 63:
						{ return new Symbol(sym.SIMULATION); }
					case -64:
						break;
					case 64:
						{ return new Symbol(sym.NUM_ACTIONS); }
					case -65:
						break;
					case 65:
						{ return new Symbol(sym.ENVIRONMENT); }
					case -66:
						break;
					case 66:
						{ return new Symbol(sym.PARTICIPANT); }
					case -67:
						break;
					case 68:
						{ System.err.println("Illegal character: "+yytext()); }
					case -68:
						break;
					case 69:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -69:
						break;
					case 70:
						{ return new Symbol(sym.NUMBER, new Integer(yytext())); }
					case -70:
						break;
					case 72:
						{ System.err.println("Illegal character: "+yytext()); }
					case -71:
						break;
					case 73:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -72:
						break;
					case 74:
						{ System.err.println("Illegal character: "+yytext()); }
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
					case 165:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -164:
						break;
					case 166:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -165:
						break;
					case 167:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -166:
						break;
					case 168:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -167:
						break;
					case 169:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -168:
						break;
					case 170:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -169:
						break;
					case 171:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -170:
						break;
					case 172:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -171:
						break;
					case 173:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -172:
						break;
					case 174:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -173:
						break;
					case 175:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -174:
						break;
					case 176:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -175:
						break;
					case 177:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -176:
						break;
					case 178:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -177:
						break;
					case 179:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -178:
						break;
					case 180:
						{ return new Symbol(sym.ID, new String(yytext())); }
					case -179:
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
