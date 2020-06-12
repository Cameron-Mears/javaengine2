package engine.util.bst;

import java.util.function.Function;

public class BST<Key,Value> 
{
	
	Node<Value> root;
	
	public BST()
	{
		
	}
	
	
	public Value find(Key k)
	{
		if (root != null)
		{
			Node<Value> focus = root;
			double key = (double)(k.hashCode());
			if (root.key == key) return root.value;
			
			while (focus.key != key)
			{
				
				if (key < focus.key)
				{
					focus = focus.leftChild;
				}
				else
				{
					focus = focus.rightChild;
				}
				
				if (focus == null) return null;
			}
			
			return focus.value;
		}
		return null;
	}
	
	
	public boolean findNode(Key k)
	{
		if (root != null)
		{
			Node<Value> focus = root;
			double key = (double)(k.hashCode());
			if (root.key == key) return true;
			
			while (focus.key != key)
			{
				
				if (key < focus.key)
				{
					focus = focus.leftChild;
				}
				else
				{
					focus = focus.rightChild;
				}
				
				if (focus == null) return false;
			}
			
			return true;
		}
		return false;
	}
	
	
	Node<Value> insertRec(int key, Value value)
	{
		return null;
	}
	
	long minValue(Node<Value> root) 
    { 
        long minv = root.key; 
        while (root.leftChild != null) 
        { 
            minv = root.leftChild.key; 
            root = root.leftChild; 
        } 
        return minv; 
    }
	
	
	public Node<Value> deleteRec(Node<Value> root, long key) 
    { 
        /* Base Case: If the tree is empty */
        if (root == null)  return root; 
  
        /* Otherwise, recur down the tree */
        if (key < root.key) 
            root.leftChild = deleteRec(root.leftChild, key); 
        else if (key > root.key) 
            root.rightChild = deleteRec(root.rightChild, key); 
  
        // if key is same as root's key, then This is the node 
        // to be deleted 
        else
        { 
            // node with only one child or no child 
            if (root.leftChild == null) 
                return root.rightChild; 
            else if (root.rightChild == null) 
                return root.leftChild; 
  
            // node with two children: Get the inorder successor (smallest 
            // in the right subtree) 
            root.key = minValue(root.rightChild); 
  
            // Delete the inorder successor 
            root.rightChild = deleteRec(root.rightChild, root.key); 
        } 
  
        return root; 
    }
	
	public boolean addNode(Key k,Value value)
	{
		if (findNode(k)) return false;
		
		Node<Value> focus = root;
		long key = (long)(k.hashCode());
		if (focus == null)
		{
			root = new Node<Value>(key, value);
			return true;
		}
		
		while (true)
		{
			
			if (key < focus.key)
			{
				if (focus.leftChild != null)
				{
					focus = focus.leftChild;
				}
				else
				{
					focus.leftChild = new Node<Value>(key,value);
					return true;
				}
			}
			else
			{
				if (focus.rightChild != null)
				{
					focus = focus.rightChild;
				}
				else
				{
					focus.rightChild = new Node<Value>(key,value);
					return true;
				}
			}
		}
	}
	
	
	
	public Node<Value> getRoot()
	{
		return root;
	}
	
	public void inOrderTraverse(Node<Value> root, Function<Value, Void> func)
	{
		if (root.leftChild != null) inOrderTraverse(root.leftChild, func);
		
		func.apply(root.value);
		
		if (root.rightChild != null) inOrderTraverse(root.rightChild, func);
		
			
	}
	
	
}