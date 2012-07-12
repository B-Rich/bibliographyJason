package scraping;

import java.util.ArrayList;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.*;


public class get_information extends DefaultInternalAction {
	
	private static final long serialVersionUID = 1L;
	
	private static String SCHOLAR = "SCHOLAR";
	private static String DBLP = "DBLP";

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		System.out.println("Search information about "+args[1]+" with "+args[0]);
		
		String type = ((StringTerm) args[0]).getString();
		String search_term = ((StringTerm) args[1]).getString();
		
		String result = "No match";
		
		if(type.equals(SCHOLAR)) {
			Session s = new Session("50001");
			ArrayList<String> papers = new ArrayList<String>();
			String base_url = "http://scholar.google.it";
			
			try {
				s.setErrorTolerance(true);
				s.visit(base_url);
				ArrayList<Node> nodes = s.xpath("//*[@name='q']");
				Node n = nodes.get(0);
				n.set(search_term);
				n.getForm().submit();
				
				int npage = 1;
				boolean new_page = true;

				while(new_page) {
					new_page = false;
					ArrayList<Node> divs = s.xpath("//*[@class=\"gs_r\"]");
	 
				    for(int i=1; i<divs.size(); i++) {
				    	Node div = divs.get(i);
				    	ArrayList<Node> page_papers = div.xpath("./*[@class=\"gs_rt\"]/a");
				    	
				    	if(page_papers != null) {
					        System.out.println("page_papers: "+page_papers);
					        
					        for(Node page_paper : page_papers) {
					        	papers.add(page_paper.text());
					        }
				    	}
				        
				        //ArrayList<Node> citedbies = div.xpath("./*[@class=\"gs_fl\"]/a");
				        //System.out.println("citedbies: "+citedbies);
				    }
				    
				    System.out.println("npage: "+npage);
				    ArrayList<Node> pages = s.xpath("//*[@id=\"gs_n\"]/center/table/tbody/tr/td/a[text()=\""+(npage+1)+"\"]");
				    System.out.println("pages: "+pages);

				    if(pages != null && pages.size() > 0) {
				        String next_page = pages.get(0).get("href");
				        npage += 1;
				        new_page = true;
				        System.out.println("next_page: "+next_page);
				        s.visit(base_url+next_page);
				    }
				}
				
				System.out.println("papers: "+papers);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			result = Utils.join(papers, ",");
		}
		
		if(type.equals(DBLP)) {
			Session s = new Session("50002");
			ArrayList<String> papers = new ArrayList<String>();
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
			    		papers.add(title);
			    	}
			    }
				
				System.out.println("papers: "+papers);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			result = Utils.join(papers, ",");
		}
        
        return un.unifies(args[2], new StringTermImpl(result));
    }
}
