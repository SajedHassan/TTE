package LinkedList;

public class SNode {

	private SNode nextNode;
	private Object element;

	public SNode() {
		this(null, null);
	}

	public SNode(SNode nextNode, Object Value) {
		this.nextNode = nextNode;
		this.element = Value;
	}

	public void setNext(SNode nextNode) {
		this.nextNode = nextNode;
	}

	public SNode getNext() {
		return this.nextNode;
	}

	public void setValue(Object value) {
		this.element = value;
	}

	public Object getValue() {
		return this.element;
	}

}
