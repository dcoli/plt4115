package compilersettings;

public class SymbolTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SymbolTable myst = new SymbolTable();
		myst.set("mysymbol", 1, Symbol.Type.INT, 3);
		System.out.println("mysymbol symbol: "+ myst.get("mysymbol", 1));
		System.out.println("mysymbol value: "+ myst.getValue("mysymbol", 1));
		System.out.println("mysymbol type: "+ myst.getType("mysymbol", 1));
	}

}
