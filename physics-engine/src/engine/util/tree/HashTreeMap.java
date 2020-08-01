package engine.util.tree;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import engine.util.tree.AVL.Entry;

/**
 *  The {@code BST} class represents an ordered symbol table of generic
 *  key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides ordered methods for finding the <em>minimum</em>,
 *  <em>maximum</em>, <em>floor</em>, and <em>ceiling</em>.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  It requires that
 *  the key type implements the {@code Comparable} interface and calls the
 *  {@code compareTo()} and method to compare two keys. It does not call either
 *  {@code equals()} or {@code hashCode()}.
 *  <p>
 *  This implementation uses a <em>left-leaning red-black BST</em>. 
 *  The <em>put</em>, <em>get</em>, <em>contains</em>, <em>remove</em>,
 *  <em>minimum</em>, <em>maximum</em>, <em>ceiling</em>, <em>floor</em>,
 *  <em>rank</em>, and <em>select</em> operations each take
 *  &Theta;(log <em>n</em>) time in the worst case, where <em>n</em> is the
 *  number of key-value pairs in the symbol table.
 *  The <em>size</em>, and <em>is-empty</em> operations take &Theta;(1) time.
 *  The <em>keys</em> methods take
 *  <em>O</em>(log <em>n</em> + <em>m</em>) time, where <em>m</em> is
 *  the number of keys returned by the iterator.
 *  Construction takes &Theta;(1) time.
 *  <p>
 *  For alternative implementations of the symbol table API, see {@link ST},
 *  {@link BinarySearchST}, {@link SequentialSearchST}, {@link BST},
 *  {@link SeparateChainingHashST}, {@link LinearProbingHashST}, and
 *  {@link AVLTreeST}.
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/33balanced">Section 3.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */


/**
 * 
 * 
 *
 * 	Combination of HashMap and a RedBlack tree
 * 	insert -> log(n)
 *  delete -> log(n)
 *  find() -> O(1)
 *  
 *  tree can always be traverse by key order
 *  low key to high values -> inOrderTraverse
 *  high to low -> reverseOrderTraverse
 * 
 * @param <Key> key of the tree
 * @param <Value> value of node
 */public class HashTreeMap<Key, Value>
 {
	private ConcurrentHashMap<Key, Value> map;
    private AVL<Key, Value> tree;
    
    public void put(Key key, Value value)
    {
    	if (key == null) throw new NullPointerException("Null key");
    	
    	if (value == null)
    	{
    		map.put(key, null);
        	tree.remove(key);
        	return;
    	}
    	map.put(key, value);
    	tree.put(key, value);
    }
    
    public int size()
    {
    	return tree.size();
    }
    
    public Value get(Key key)
    {
    	return map.get(key);
    }
    
    public HashTreeMap()
    {
    	map = new ConcurrentHashMap<Key, Value>();
    	tree= new AVL<Key, Value>();
    }
    
    public void inOrderTraverse(TraverseFunction<Value> traverse)
    {
    	Entry<Key, Value> root = tree.getFirstEntry();
    	if (root != null) inOrderTraverse(traverse, root);
    }
    private void inOrderTraverse(TraverseFunction<Value> traverse, Entry<Key, Value> n)
    {
    	if (n.left != null) inOrderTraverse(traverse, n.left);
    	traverse.apply(n.value);
    	if (n.right != null) inOrderTraverse(traverse, n.right);
    }

}
