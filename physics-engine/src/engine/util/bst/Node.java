package engine.util.bst;

public class Node<Value>
{
	long key;
	Value value;
	
	Node<Value> leftChild;
	Node<Value> rightChild;
	
	Node(long key, Value val)
	{
		this.key = key;
		this.value = val;
	}
}
