package engine.util.bst;

public class Node<Value>
{
	double key;
	Value value;
	
	Node<Value> leftChild;
	Node<Value> rightChild;
	
	Node(double key, Value val)
	{
		this.key = key;
		this.value = val;
	}
}
