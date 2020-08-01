package test.quadtree;

import java.util.LinkedList;

import engine.core.random.Rand;
import engine.util.math.MathUtils;
import engine.util.quadtree.ConcurrentQuadTree;
import engine.util.quadtree.ConcurrentQuadTreeNode;
import physics.collision.Rectangle;

public class QuadTreeTester 
{
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
		
}
