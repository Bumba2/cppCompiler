package parser;

// Beispiel: "50 ? 20 : 10;"
public class Tenary<T> {
	private Node<T> trueNode; // In dem Beispiel wäre der wahre Knoten 20.
	private Node<T> falseNode; // und der falsche Knoten 10 (-> repräsentieren if und else).
	
	public Tenary(Node<T> trueNode, Node<T> falseNode) {
		this.trueNode = trueNode;
		this.falseNode = falseNode;
	}
	
	public Node<T> getTrueNode() {
		return trueNode;
	}
	
	public void setTrueNode(Node<T> trueNode) {
		this.trueNode = trueNode;
	}
	
	public Node<T> getFalseNode() {
		return falseNode;
	}
	
	public void setFalseNode(Node<T> falseNode) {
		this.falseNode = falseNode;
	}
}
