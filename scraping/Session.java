package scraping;

import java.util.ArrayList;

public class Session {
	private Webkit w;
	private NodeFactory nf;

	public Session() {
		w = new Webkit();
		nf = new NodeFactory(w);
	}
	
	public void setErrorTolerance(Boolean errorTolerance) throws Exception {
		w.sendCommand("SetErrorTolerance", errorTolerance.toString());
	}
	
	public void visit(String url) throws Exception {
		w.sendCommand("Visit", url);
	}
	
	public ArrayList<Node> xpath(String xpath) throws Exception {
		String result = this.get_id_by_xpath(xpath);
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
		
		try {
			s.setErrorTolerance(true);
			s.visit("http://localhost/scholar.htm");
			nodes = s.xpath("//*[@name='q']");
			Node n = nodes.get(0);
			n.set("Giuseppe Vizzari");
			n.getForm().submit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
