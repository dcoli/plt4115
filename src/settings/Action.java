/**
 * 
 */
package settings;

import java.util.List;

/**
 * @author colin
 *
 */
public class Action {
	private final String id;
	private final List<String> argList;
	
	public Action ( String id, List<String> argList ) {
		this.id = id;
		this.argList = argList;
	}	
	
	public List<String> getArgList () {
/*		Iterator itr = this.argList.iterator();
		System.out.println("getArgList args:");
		while(itr.hasNext()){
			String element = (String) itr.next();
			System.out.print(element +" ");
		}
		System.out.println();
*/
		return this.argList;
	}
	
	public String getId(){
		return this.id;
	}

}