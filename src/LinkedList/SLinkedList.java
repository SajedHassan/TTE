package LinkedList;


public class SLinkedList {

	private int size;
	private SNode head;
	private SNode tail;

	public SLinkedList() {
		this.size = 0;
		this.tail = null;
		this.head = null;
	}

	public void add(int index, Object element) {

		if (index > this.size || index < 0)
			throw null;
		if (index == this.size) {
			this.add(element);
		} else if (index == 0) {
			SNode newNode = new SNode(this.head, element);
			this.head = newNode;
			this.size++;
		} else {
			SNode tmp = this.head;
			for (int i = 0; i < index - 1; i++) {
				tmp = tmp.getNext();
			}
			SNode addedNode = new SNode(tmp.getNext(), element);
			tmp.setNext(addedNode);
			this.size++;
		}
	}

	public void add(Object element) {
		SNode addedNode = new SNode(null, element);
		if (isEmpty())
			this.head = addedNode;
		else
			this.tail.setNext(addedNode);
		this.tail = addedNode;
		this.size++;
	}

	public Object get(int index) {

		if (isEmpty() || index >= this.size || index < 0)
			throw null;
		else {
			SNode temp = this.head;
			for (int i = 0; i < index; i++) {
				temp = temp.getNext();
			}
			return temp.getValue();
		}

	}

	public void set(int index, Object element) {

		if (isEmpty() || index >= this.size || index < 0)
			throw null;
		else {
			SNode temp = this.head;
			for (int i = 0; i < index; i++) {
				temp = temp.getNext();
			}
			temp.setValue(element);
		}
	}

	public void clear() {
		if (isEmpty())
			throw null;
		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	public boolean isEmpty() {
		if (this.size == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void remove(int index) {

		if (isEmpty() || index >= this.size || index < 0)
			throw null;
		else if (index == 0) {
			this.head = this.head.getNext();
			this.size--;
			if (isEmpty()) {
				this.tail = null;
			}
		} else {
			SNode temp = this.head;
			for (int i = 0; i < index - 1; i++) {
				temp = temp.getNext();
			}

			if (temp.getNext() == this.tail) {
				this.tail = temp;
				temp.setNext(null);
			} else {
				temp.setNext(temp.getNext().getNext());
			}
			this.size--;
		}

	}

	public int size() {
		return this.size;
	}
	
	public boolean contains(Object o) {
		if (this.isEmpty()) {
			return false;
		} else {
			SNode tmp = this.head;
			for (int i = 0; i < this.size; i++) {
				if (o.equals(tmp.getValue())) {
					return true;
				}
				tmp = tmp.getNext();
			}
		}
		return false;
	}
	public SNode getHead()
	{
		SNode temp = new SNode();
		temp = head;
		return temp;
	}

}
