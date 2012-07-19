package scraping;

import java.util.ArrayList;

public class Session {
	private Webkit w;
	private NodeFactory nf;

	public Session(String port) {
		w = new Webkit(port);
		nf = new NodeFactory(w);
	}
	
	public Session() {
		this(null);
	}
	
	public void setErrorTolerance(Boolean errorTolerance) throws Exception {
		w.sendCommand("SetErrorTolerance", errorTolerance.toString());
	}
	
	public void visit(String url) throws Exception {
		w.sendCommand("Visit", url);
	}
	
	public ArrayList<Node> xpath(String xpath) throws Exception {
		String result = this.get_id_by_xpath(xpath);
		
		if(result.equals("")) {
			return null;
		}
		
		//System.out.println("xpath: "+result);
		String[] node_ids = result.split(",");
		
		ArrayList<Node> nodes = new ArrayList<Node>();  
		for(String node_id : node_ids) {
			Node n = nf.create(node_id);
			nodes.add(n);
		}
		
		return nodes;
	}
	
	public String get_id_by_xpath(String xpath) throws Exception {
		return w.sendCommand("Find", xpath);
	}
	
	public static void main(String[] args) {
		Session s = new Session();
		ArrayList<Node> nodes;
		
		String base_url = "http://scholar.google.it";
		
		try {
			s.setErrorTolerance(true);
			//s.visit("http://localhost/scholar.htm");
			s.visit(base_url);
			nodes = s.xpath("//*[@name='q']");
			Node n = nodes.get(0);
			n.set("Giuseppe Vizzari");
			n.getForm().submit();
			
			int npage = 1;
			boolean new_page = true;
			
			ArrayList<String> papers = new ArrayList<String>();
			
			while(new_page) {
				new_page = false;
				ArrayList<Node> divs = s.xpath("//*[@class=\"gs_r\"]");
 
			    for(int i=1; i<divs.size(); i++) {
			    	Node div = divs.get(i);
			    	ArrayList<Node> page_papers = div.xpath("./*[@class=\"gs_rt\"]/a");
			        //System.out.println("page_papers: "+page_papers);
			        
			        for(Node page_paper : page_papers) {
			        	papers.add(page_paper.text());
			        }
			        
			        //ArrayList<Node> citedbies = div.xpath("./*[@class=\"gs_fl\"]/a");
			        ////System.out.println("citedbies: "+citedbies);
			    }
			    
			    //System.out.println("npage: "+npage);
			    ArrayList<Node> pages = s.xpath("//*[@id=\"gs_n\"]/center/table/tbody/tr/td/a[text()=\""+(npage+1)+"\"]");
			    //System.out.println("pages: "+pages);

			    if(pages != null && pages.size() > 0) {
			        String next_page = pages.get(0).get("href");
			        npage += 1;
			        new_page = true;
			        //System.out.println("next_page: "+next_page);
			        s.visit(base_url+next_page);
			    }
			}
			
			//System.out.println("papers: "+papers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
