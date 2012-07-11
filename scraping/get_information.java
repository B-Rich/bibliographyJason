package scraping;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.*;


public class get_information extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SCHOLAR = "SCHOLAR";
	private static final String DBLP = "DBLP";

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        //String id = ts.getUserAgArch().getAgName().substring(3);
        //int bid = Integer.parseInt(id) * 10;
        // args[0] is the unattended luggage Report Number
        
		System.out.println("Search information about "+args[1]+" with "+args[0]);
		
		String result = "No match";
		
		if(args[0].equals(SCHOLAR)) {
			result = "result from scholar";
		} 
		
		if(args[0].equals(DBLP)) {
			result = "result from dblp";
		}
        
        return un.unifies(args[2], new StringTermImpl(result));
    }
}
