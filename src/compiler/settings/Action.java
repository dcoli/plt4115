/**
 * Action is the container for the information we can save about an action.
 * Right now this means the action's name and the arguments that the action
 * can take. This shouldn't need to be manipulated directly, but by adding 
 * items to the ActionTable, which handles creation of actions.
 */
package compiler.settings;

import java.util.List;

/**
 * @author dm2241
 * @param	id	the name by which the action is called.
 * @param	argList	the arguments that can be passed with the action.
 * @see	ActionTable
 */
public class Action {
	private final String id;
	private final List<Symbol.Type> argList;
	
	public Action ( String id, List<Symbol.Type> argList ) {
		this.id = id;
		this.argList = argList;
	}	
	
/**
 * Returns a List of arguments given as Strings.
 * @return	a list of arguments, which is List type of Strings.
 */
	public List<Symbol.Type> getArgList () {
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
	
/**
 * 
 * @return the action's id.
 */
	public String getId(){
		return this.id;
	}

}