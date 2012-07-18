package dblp;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ObjectTermImpl;
import jason.asSyntax.Term;
import jason.asSyntax.VarTerm;

import java.util.ArrayList;
import java.util.HashMap;


public class scraping_search_citation extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = true;
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		System.out.println("Search citation in DBLP");
		
		HashMap<String, ArrayList<String>> citations = (HashMap<String, ArrayList<String>>) ((VarTerm) args[0]).getObject();
	
		if(DEBUG) {
			ArrayList<String> citationsL = citations.get("prova1");
			citationsL.remove(0);
		} else {
			
		}
		
		System.out.println("fatto!!!");
        
        return un.unifies(args[1], new ObjectTermImpl(citations));
    }
}
