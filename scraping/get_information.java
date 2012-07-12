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
        //String id = ts.getUserAgArch().getAgName().substring(3);
        //int bid = Integer.parseInt(id) * 10;
        // args[0] is the unattended luggage Report Number
        
		System.out.println("Search information about "+args[1]+" with |"+args[0]+"|");
		
		String type = ((StringTerm) args[0]).getString();
		
		String result = "No match";
		
		if(type.equals(SCHOLAR)) {
			System.out.println("ciao");
			Session s = new Session();
			ArrayList<String> papers = new ArrayList<String>();
			String base_url = "http://scholar.google.it";
			
			try {
				s.setErrorTolerance(true);
				//s.visit("http://localhost/scholar.htm");
				s.visit(base_url);
				ArrayList<Node> nodes = s.xpath("//*[@name='q']");
				Node n = nodes.get(0);
				n.set("Giuseppe Vizzari");
				n.getForm().submit();
				
				int npage = 1;
				boolean new_page = true;

				while(new_page) {
					new_page = false;
					ArrayList<Node> divs = s.xpath("//*[@class=\"gs_r\"]");
	 
				    for(int i=1; i<divs.size(); i++) {
				    	Node div = divs.get(i);
				    	ArrayList<Node> page_papers = div.xpath("./*[@class=\"gs_rt\"]/a");
				        System.out.println("page_papers: "+page_papers);
				        
				        for(Node page_paper : page_papers) {
				        	papers.add(page_paper.text());
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
			result = "result from dblp";
		}
        
        return un.unifies(args[2], new StringTermImpl(result));
    }
}
