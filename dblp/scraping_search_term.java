package dblp;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ObjectTermImpl;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

import java.util.ArrayList;

import scraping.Node;
import scraping.Session;


public class scraping_search_term extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = true;
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		System.out.println("Search information about "+args[1]+" with "+args[0]);
		
		String search_term = ((StringTerm) args[0]).getString();
		
		ArrayList<String> papers = new ArrayList<String>();
		
		if(DEBUG) {
			papers.add("prova1");
			papers.add("prova3");
		} else {
			Session s = new Session("50002");
			String base_url = "http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/index.html";
			
			try {
				s.setErrorTolerance(true);
				s.visit(base_url);
				ArrayList<Node> nodes = s.xpath("//*[@name=\"author\"]");
				Node n = nodes.get(0);
				n.set(search_term);
				n.getForm().submit();
				
				
				ArrayList<Node> trs = s.xpath("//p[1]/table/tbody/tr");
	 
				    for(Node tr : trs) {
				    	ArrayList<Node> tds = tr.xpath("./td");
			    	
			    	if(tds != null) {
			    		String all_title = tds.get(2).text();
			    		String title = all_title.substring(all_title.indexOf(":")+1);
			    		title = title.substring(0, title.indexOf("."));
			    		papers.add(java.net.URLDecoder.decode(title.trim(), "UTF-8"));
			    	}
			    }
				
				System.out.println("papers: "+papers);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        
        return un.unifies(args[1], new ObjectTermImpl(papers));
    }
}
