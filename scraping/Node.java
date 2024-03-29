package scraping;

import java.util.ArrayList;

public class Node {
	private String node_id;
	private Webkit w;
	private NodeFactory nf;
	
	public Node(Webkit w, String node_id, NodeFactory nf) {
		this.w = w;
		this.node_id = node_id;
		this.nf = nf;
	}
	
	public void set(String value) throws Exception {
		w.sendCommand("Node", "set", node_id, value);
	}
	
	public String get(String value) throws Exception {
		return w.sendCommand("Node", "attribute", node_id, value);
	}
	
	public String text() throws Exception {
		return w.sendCommand("Node", "text", node_id);
	}
	
	public String html() throws Exception {
		return w.sendCommand("Node", "html", node_id);
	}
	
	public ArrayList<Node> xpath(String xpath) throws Exception {
		String result = this.get_id_by_xpath(xpath);
		
		if(result.equals("")) {
			return null;
		}
		
		String[] node_ids = result.split(",");
		
		ArrayList<Node> nodes = new ArrayList<Node>();  
		for(String node_id : node_ids) {
			Node n = nf.create(node_id);
			nodes.add(n);
		}
		
		return nodes;
	}
	
	public String get_id_by_xpath(String xpath) throws Exception {
		return w.sendCommand("Node", "findWithin", node_id, xpath);
	}
	
	public Node getForm() throws Exception {
		ArrayList<Node> nodes = this.xpath("ancestor::form");
		return nodes.get(0);
	}
	
	public void submit() throws Exception {
		w.sendCommand("Submit", "var node = Capybara.nodes["+node_id+"]; node.submit();");
	}
	
	@Override
	public String toString() {
		return "["+node_id+"]";
	}
}