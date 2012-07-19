package dblp;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ObjectTermImpl;
import jason.asSyntax.Term;
import jason.asSyntax.VarTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import scraping.Node;
import scraping.Session;


public class scraping_search_citation extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		System.out.println("Search citation in DBLP");
		
		HashMap<String, ArrayList<String>> citations = (HashMap<String, ArrayList<String>>) ((VarTerm) args[0]).getObject();
		HashMap<String, ArrayList<String>> citationsFiltered = new HashMap<String, ArrayList<String>>();
		
		System.out.println("citations: "+citations);
	
		if(DEBUG) {
			ArrayList<String> citationsL = citations.get("prova1");
			citationsL.remove(0);
		} else {
			System.out.println("DOVREI FILTRARE CON DBLP");
			
			Session s = new Session("50002");
			String base_url = "http://www.dblp.org/search";
			
			try {
				s.setErrorTolerance(true);
				s.visit(base_url);
				
				for(Entry<String, ArrayList<String>> entry : citations.entrySet()) {
					
					ArrayList<String> filteredPapers = new ArrayList<String>();
					
					for(String paper : entry.getValue()) {
						ArrayList<Node> nodes = s.xpath("//*[@id=\"autocomplete_query\"]");
						Node n = nodes.get(0);
						n.set(paper);
						n.getForm().submit();			
						
						ArrayList<Node> divs = s.xpath("//*[@id=\"autocomplete_H_boxes_1_subtitle\"]");
						
						if(divs == null) {
							throw new Exception("don't autocomplete box!! in DBLP");
						} else {
							String textDiv = divs.get(0).text();
							System.out.println("autocomplete_H_boxes_1_subtitle: "+textDiv);
							
							if(!textDiv.startsWith("No hits")) {
								filteredPapers.add(paper);
							}
						}
					}
					
					if(filteredPapers.size() > 0) {
						citationsFiltered.put(entry.getKey(), filteredPapers);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("filtraggio eseguito: "+citationsFiltered);
        
        return un.unifies(args[1], new ObjectTermImpl(citationsFiltered));
    }
}
