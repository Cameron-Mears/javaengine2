package engine.util.tree;

public class RBTree<K,V>
{
	private RBNode<K,V> root;
	
	private double toKey(K k)
	{
		return (k instanceof Number)? ((Number) k).doubleValue() : (double) k.hashCode();
	}
	
	public V put(K key, V value)
	{
		RBNode<K, V> node = new RBNode<K, V>(key,value);
		node.key = toKey(key);
		repair(node);
		return insert(root, root).value;
	}
	
	private RBNode<K, V> insert(RBNode<K,V> root, RBNode<K,V> node)
	{
		if (root == null)
		{
			root = node;
			return null;
		}
		while (true)
		{
			if (node.key < root.key)
			{
				if (root.left != null)
				{
					root = root.left;
					continue;
				}
				else break;
			}
			else // >= key 
			{
				if (root.right != null)
				{
					root = root.right;
					continue;
				}
				else break;
			}
			
		}
		node.parent = root;
		node.color = RBNode.RED;
		if (node.key < root.key)
		{
			root.left = node;
			return node;
		}
		else
		{
			if (node.key == root.key)
			{
				root.value = node.value;
			}
			root.right = node;
			return node;
		}
	}

	
	private void repair(RBNode<K, V> node)
	{
		if (node.parent == null) insertcase1(node);
		else if (node.parent.color == RBNode.BLACK) insertcase2(node);
	}
	
	private void insertcase1(RBNode<K,V> n)
	{
		
	}
	
	private void insertcase2(RBNode<K,V> n)
	{
		
	}
	
	private void insertcase3(RBNode<K,V> n)
	{
		
	}
	
	private void insertcase4(RBNode<K,V> n)
	{
		
	}
	
	
}
