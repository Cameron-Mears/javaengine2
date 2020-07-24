package engine.util.tree;


class RBNode<K,V> 
{
	static final int RED = 1;
	static final int BLACK = 0;
	
	V value;
	K k;
	int color;
	double key;
	
	RBNode<K,V> left;
	RBNode<K,V> right;
	RBNode<K,V> parent;
	
	int nRight;
	int nLeft;
	
	RBNode(K k, V v) 
	{
		this.k = k;
		this.value = v;
	}
}// end class RedBlackNode