package scraping;

public class NodeFactory {
	private Webkit w;
	
	public NodeFactory(Webkit w) {
		this.w = w;
	}
	
	public Node create(String node_id) {
		return new Node(w, node_id, this);
	}
}
