package compiler.settings;

import java.io.*;

public class Printer {

	FileWriter fw;
	
	public Printer(String name){
		try {
			fw = new FileWriter(name);
		}
		catch (IOException ioe) {
			System.out.println("Could not open file to hold generated code.");
		}
	};

	public boolean print (String text) {
		try {
			fw.write(text);
			return true;
		}
		catch (IOException ioe) {
			System.out.println("Could not write generated code to file.");
			return false;
		}
	}

	public boolean close (){
		try {
			fw.close();
			return true;
		}
		catch (IOException ioe) {
			System.out.println("Could not close generated code file.");
			return false;
		}
	}

}
