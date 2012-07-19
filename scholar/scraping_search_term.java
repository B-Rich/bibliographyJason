package scholar;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ObjectTermImpl;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import scraping.Node;
import scraping.Session;


public class scraping_search_term extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		System.out.println("Search information about "+args[1]+" with "+args[0]);
		
		String search_term = ((StringTerm) args[0]).getString();
		
		ArrayList<String> papers = new ArrayList<String>();
		HashMap<String, ArrayList<String>> citations = new HashMap<String, ArrayList<String>>();
		
		if(DEBUG) {
			papers.add("prova1");
			papers.add("prova2");
			
			ArrayList<String> citationsLocal = new ArrayList<String>();
			citationsLocal.add("citazione1");
			citationsLocal.add("citazione2");
			citationsLocal.add("citazione3");
			
			citations.put("prova1", citationsLocal);
		} else {
			String base_url = "http://scholar.google.it";
			Session s = new Session("50001");
			
			try {
				s.setErrorTolerance(true);
				s.visit(base_url);
				ArrayList<Node> nodes = s.xpath("//*[@name='q']");
				Node n = nodes.get(0);
				n.set(search_term);
				n.getForm().submit();
				HashMap<String, String> citiedBies = new HashMap<String, String>();
				
				int npage = 1;
				boolean new_page = true;
	
				while(new_page) {
					new_page = false;
					ArrayList<Node> divs = s.xpath("//*[@class=\"gs_r\"]");
	 
				    for(int i=1; i<divs.size(); i++) {
				    	Node div = divs.get(i);
				    	ArrayList<Node> page_papers = div.xpath("./*[@class=\"gs_rt\"]/a");
				    	
				    	if(page_papers != null) {
					        for(Node page_paper : page_papers) {
					        	String paperTitle = java.net.URLDecoder.decode(page_paper.text().trim(), "UTF-8");
					        	papers.add(paperTitle);
					        	
					        	ArrayList<Node> citedbiesPaper = page_paper.xpath("ancestor::div/*[@class=\"gs_fl\"]/a");
					        	
					        	if(citedbiesPaper != null) {
						        	String citedbiesPaperUrl = citedbiesPaper.get(0).get("href");
						        	
						        	if(citedbiesPaperUrl.contains("cites=")) {
						        		System.out.println("citedbiesPaper.get(0) url: "+citedbiesPaperUrl);
						        		citiedBies.put(paperTitle, citedbiesPaperUrl);
						        	}
					        	}
					        }
				    	}
				    }
				    
				    //System.out.println("npage: "+npage);
				    ArrayList<Node> pages = s.xpath("//*[@id=\"gs_n\"]/center/table/tbody/tr/td/a[text()=\""+(npage+1)+"\"]");
				    //System.out.println("pages: "+pages);
	
				    if(pages != null && pages.size() > 0) {
				        String next_page = pages.get(0).get("href");
				        npage += 1;
				        new_page = true;
				        System.out.println("next_page: "+next_page);
				        s.visit(base_url+next_page);
				    }
				}
				
				for(Entry<String, String> citiedBy : citiedBies.entrySet()) {
					s.visit(base_url+citiedBy.getValue());
					ArrayList<String> papersThatCite = new ArrayList<String>();
					
					npage = 1;
					new_page = true;
		
					while(new_page) {
						new_page = false;
						ArrayList<Node> divs = s.xpath("//*[@class=\"gs_r\"]");
		 
					    for(int i=1; i<divs.size(); i++) {
					    	Node div = divs.get(i);
					    	ArrayList<Node> page_papers = div.xpath("./*[@class=\"gs_rt\"]/a");
					    	
					    	if(page_papers != null) {
						        for(Node page_paper : page_papers) {
						        	String paperTitle = java.net.URLDecoder.decode(page_paper.text().trim(), "UTF-8");
						        	papersThatCite.add(paperTitle);
						        }
					    	}
					    }
					    
					    //System.out.println("npage: "+npage);
					    ArrayList<Node> pages = s.xpath("//*[@id=\"gs_n\"]/center/table/tbody/tr/td/a[text()=\""+(npage+1)+"\"]");
					    //System.out.println("pages: "+pages);
		
					    if(pages != null && pages.size() > 0) {
					        String next_page = pages.get(0).get("href");
					        npage += 1;
					        new_page = true;
					        System.out.println("next_page: "+next_page);
					        s.visit(base_url+next_page);
					    }
					}
					
					citations.put(citiedBy.getKey(), papersThatCite);
				}
				
				System.out.println("papers: "+papers);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        
        return un.unifies(args[1], new ObjectTermImpl(papers)) && un.unifies(args[2], new ObjectTermImpl(citations)) ;
    }
}
