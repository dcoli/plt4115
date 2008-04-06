package settings;

import java.util.ArrayList;

public class TableTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ActionTable myat = new ActionTable();
		ArrayList<String> list = new ArrayList<String>();
		list.add("arg0");
		list.add("arg1");
		myat.set("listitem1",list);
		if (myat.has("listitem1")){ System.out.println("myat listitem1: "+myat.get("listitem1").toString());
		} else System.out.println("no action found");

		ParticipantTable mypt = new ParticipantTable();
		mypt.set("mywrestler", new Wrestler("Hulk Hogan"));
		if ( mypt.has("mywrestler")) System.out.println("mywrestler name: "+ mypt.get("mywrestler").getName());
		else System.out.println("wrestler not found");
		
		RuntimeTable myrt = new RuntimeTable();
		myrt.set("rt1", new String("hello1"));
		if (myrt.has("rt1")) {
			System.out.println("myrt rt1: "+ myrt.get("rt1"));
		} else System.out.println("runtime not found");

		SymbolTable myst = new SymbolTable();
		myst.set("mysymbol", 1, Symbol.Type.INT, 3);
		if(myst.has("mysymbol:1")){
			System.out.println("mysymbol symbol: "+ myst.get("mysymbol", 1));
			System.out.println("mysymbol value: "+ myst.get("mysymbol", 1).getValue());
			System.out.println("mysymbol type: "+ myst.get("mysymbol", 1).getType());
		} else System.out.println("symbol not found");

		SystemVarsTable mysvt = new SystemVarsTable();
		mysvt.set("mysystemvar", new String("systemvarvalue"));
		if (mysvt.has("mysystemvar")) {
			System.out.println("mysystemvar value:" + mysvt.get("mysystemvar"));
		} else System.out.println("systemvar not found");

	}
}
