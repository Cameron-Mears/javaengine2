package test.quadtree;

import java.util.Iterator;
import java.util.LinkedList;

import engine.core.instance.InstanceID;
import engine.core.random.Rand;
import engine.util.TimeUtils;
import physics.collision.Rectangle;
import physics.collision.quadtree.CRQuadTree;
import physics.collision.quadtree.CRQuadTree.Node;

public class QuadTreeTester 
{
	
	public static void main(String[] args)
	{
		CRQuadTree<String> tree = new CRQuadTree<String>(10, new Rectangle(10000, 10000));
		
		//Thread 2 for concurrency testing
		/*
		new Thread(()->{
			
			Rectangle test = new Rectangle(10, 10, 100, 100);
			int nInstances = 1000000;
			long now = System.nanoTime();
			for (int i = 0; i < nInstances; i++) 
			{
				test = new Rectangle(Rand.range(0, 980), Rand.range(0, 980), Rand.range(0, 150), Rand.range(0, 150));
				tree.put(test, "h");
			}
			System.out.println(TimeUtils.nanosToSeconds(System.nanoTime()  - now));
			test = new Rectangle(10, 10, 100, 100);
			now = System.nanoTime();
			tree.put(test, "d");
			System.out.println(TimeUtils.nanosToSeconds(System.nanoTime()  - now));
			
		}).start();
		*/
		Rectangle test = new Rectangle(10, 10, 100, 100);
		int nInstances = 1000000;
		long now = System.nanoTime();
		
		LinkedList<InstanceID<Node<String>>> toRemove = new LinkedList<>();
		LinkedList<InstanceID<Node<String>>> toMoveIDs = new LinkedList<>();
		LinkedList<Rectangle> toMoveRects = new LinkedList<>();
		
		for (int i = 0; i < nInstances; i++) 
		{
			//tree.put(test, "h");
			test = new Rectangle(Rand.range(0, 9800), Rand.range(0, 980), Rand.range(0, 150), Rand.range(0, 150));
			InstanceID<Node<String>> id = tree.put(test, "h");
			if (i % 10 == 0) toRemove.add(id); 
			if (i % 3 == 0 && i % 10 != 0)
			{
				toMoveIDs.add(id); 
				toMoveRects.add(test);
			}
		}
		test = new Rectangle(10, 10, 100, 100);
		double seconds = TimeUtils.nanosToSeconds(System.nanoTime()  - now);
		System.out.println("Insertion of 1000000 nodes -> " + Double.toString(seconds) +  " seconds");
		System.out.println("Size -> " + Integer.toString(tree.size()));
		now = System.nanoTime();
		int size = toRemove.size();
		for (InstanceID<Node<String>> instanceID : toRemove) 
		{
			//System.out.println(++i);
			tree.remove(instanceID);
		}
		seconds = TimeUtils.nanosToSeconds(System.nanoTime()  - now);
		System.out.println("Deletion of " + Integer.toString(size) +" nodes - > " + Double.toString(seconds)  + " seconds");	
		
		now = System.nanoTime();
		size = toMoveRects.size();
		Iterator<Rectangle> rectIter = toMoveRects.iterator();
		Iterator<InstanceID<Node<String>>> idIter = toMoveIDs.iterator();
		for (; rectIter.hasNext();) 
		{
			Rectangle rectangle = rectIter.next();
			InstanceID<Node<String>> id = idIter.next();
			rectangle.getPosition().add(Rand.range(0, 100), Rand.range(0, 100));
			tree.updateEntry(id);
		}
		seconds = TimeUtils.nanosToSeconds(System.nanoTime()  - now);
		System.out.println("Move " + Integer.toString(size) +" nodes - > " + Double.toString(seconds) + " seconds");
		
	}
	
	/*
	public static void main(String[] args)
	{		
		int nInstances = 100000;
		double w = 250, h = 250;
		Rectangle rect = new Rectangle(0,0,100000,100000);
		ConcurrentQuadTree tree = new ConcurrentQuadTree(rect);
		
		Thread p = new Thread(()->{
			long insertbegin = System.nanoTime();
			for (int i = 0; i < nInstances; i++) 
			{
				double width = Rand.range(0, w);
				double height = Rand.range(0, h);
				
				width = MathUtils.clamp(width, 50, 150);
				height = MathUtils.clamp(height, 50, 150);
				
				if (width > 150 || height > 150) System.out.println("haha");
				
				double x = Rand.range(0, w-width);
				double y =  Rand.range(0, h-height);
				
				Rectangle area = new Rectangle(x, y, width, height);
				ConcurrentQuadTreeNode<?> node = new ConcurrentQuadTreeNode<QuadTreeTester>(rect,null);
				tree.put(node);
			}
			System.out.println("Insert Time " + Integer.toString(nInstances) +  " new instances: " +Double.toString((System.nanoTime() - insertbegin)/1.0e9) + " seconds");
			
			
			long queryBegin = System.nanoTime();
			LinkedList<ConcurrentQuadTreeNode<?>> query = tree.query(rect);
			//System.out.println("Query Entire tree of " + Integer.toString(nInstances) +  " instances: " + Double.toString((System.nanoTime() - insertbegin)/1.0e9) + " seconds");
			
			ConcurrentQuadTree preInstancetree = new ConcurrentQuadTree(rect);
			
			insertbegin = System.nanoTime();
			for (ConcurrentQuadTreeNode<?> concurrentQuadTreeNode : query) {
				preInstancetree.put(concurrentQuadTreeNode);
			}
			Thread.currentThread().interrupt();
			
			//System.out.println("Insert Time " + Integer.toString(nInstances) +  " pre instances: " +Double.toString((System.nanoTime() - insertbegin)/1.0e9) + " seconds");
		});
		//p.start();
		long insertbegin = System.nanoTime();
		for (int i = 0; i < nInstances; i++) 
		{
			double width = Rand.range(0, w);
			double height = Rand.range(0, h);
			
			width = MathUtils.clamp(width, 50, 250);
			height = MathUtils.clamp(height, 50, 250);
			
			double x = Rand.range(0, w-width);
			double y =  Rand.range(0, h-height);
			
			Rectangle area = new Rectangle(x, y, width, height);
			ConcurrentQuadTreeNode<?> node = new ConcurrentQuadTreeNode<QuadTreeTester>(rect,null);
			tree.put(node);
			if (i % 1000 == 0) System.out.println(i); 
		}
		System.out.println("Insert Time " + Integer.toString(nInstances) +  " new instances: " +Double.toString((System.nanoTime() - insertbegin)/1.0e9) + " seconds");
		
		
		long queryBegin = System.nanoTime();
		LinkedList<ConcurrentQuadTreeNode<?>> query = tree.query(rect);
		//System.out.println("Query Entire tree of " + Integer.toString(nInstances) +  " instances: " + Double.toString((System.nanoTime() - insertbegin)/1.0e9) + " seconds");
		
		ConcurrentQuadTree preInstancetree = new ConcurrentQuadTree(rect);
		
		insertbegin = System.nanoTime();
		for (ConcurrentQuadTreeNode<?> concurrentQuadTreeNode : query) {
			preInstancetree.put(concurrentQuadTreeNode);
		}
		
		//System.out.println("Insert Time " + Integer.toString(nInstances) +  " pre instances: " +Double.toString((System.nanoTime() - insertbegin)/1.0e9) + " seconds");
		
		while (!p.isInterrupted()) {}
		System.out.println(tree.query(rect).size());
	}
	*/
		
}
