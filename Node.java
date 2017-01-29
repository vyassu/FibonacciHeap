
// Datastructure for each node of fibbonacci Heap
public class Node {
	public
	Node parent;
	Node child;
	Node qnext;
	Node qprev;
	long value=0;
	String keys="";
	boolean childcut=false;
	int degree=0;
	
	// Constructor to initialize each node of the Fibbonacci Heap
	public Node(String keys, long value)
	{
		this.keys = keys;
		this.value = value;
		parent = this;
		child = this;
		qnext = this;
		qprev = this;
	}
}
