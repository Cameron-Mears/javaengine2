package game.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

import engine.core.input.InputHandler;
import engine.core.instance.InstanceID;
import engine.core.random.Rand;
import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import engine.core.tick.TickScheduler;
import engine.core.tick.TickableGroup;
import engine.util.TimeUtils;
import engine.util.json.JSONSerializable;
import engine.util.pathing.Path;
import engine.util.quadtree.ConcurrentQuadTreeNode;
import external.org.json.JSONObject;
import game.Level;
import game.entities.buildings.turrets.Turret;
import game.entities.enemies.BasicEnemy;
import game.entities.item.Item;
import game.inventory.Inventory;
import game.renderer.Renderer;
import graphics.Camera;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.CollisionLayer;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.collision.quadtree.CRQuadTree;
import physics.collision.quadtree.CRQuadTree.Node;
import physics.general.Transform;
import physics.general.Vector2;

public class Player extends Entity implements JSONSerializable
{
	
	private Sprite playerSprite;
	private PhysicsBody body;
	private InputHandler input;
	private HitBox hitbox;
	private CollisionLayer layer;
	private Camera camera;
	private BasicEnemy enemy;
	private double x;
	private Inventory inv;
	private LinkedList<Vector2> points;
	private Path p;
	
	private Rectangle bounds;
	
	private CRQuadTree<QuadTreeTestInstance> tree;
	
	private double vel = 600;
	private boolean c;
	private double periodic;
	
	private static class QuadTreeTestInstance
	{
		Rectangle rect;
		InstanceID<Node<QuadTreeTestInstance>> id;
		public QuadTreeTestInstance(Rectangle rect, InstanceID<Node<QuadTreeTestInstance>> id) 
		{
			this.rect = rect;
			this.id = id;
		}
	}
	
	public Player(int x)
	{
		tree = new CRQuadTree<QuadTreeTestInstance>(10, new Rectangle(10000, 10000));
		buildQuadTree();
		inv = new Inventory(100);
		input = new InputHandler(true);
		playerSprite = SpriteMap.getClonedSprite("player");
		body = new PhysicsBody(new MassData(1), new Material(), new Transform(100,100));
		input.setTransform(body.getTansform());
		hitbox = playerSprite.getHitBox(body.getPosition(), this);
		quadTreeNode = new ConcurrentQuadTreeNode<Player>(hitbox.getBounds(), this);
		addToLayers("default", null, null);
		inv.addItems(Item.getItem("Iron Ore"), 4);
		inv.addItems(Item.getItem("Iron Ore"), 1000);
		camera = Renderer.getInstance().getCamera("main");
		bounds = new Rectangle(10000, 10000);
		//camera.setBoundries(new Rectangle(0, 0, 64*32, 24*32));
		camera.setBoundries(bounds);
		Turret t = new Turret(new Vector2(128, 128+32));
		t.addToLayers(null, "default", "default");
	}
	
	public Player(JSONObject json)
	{
		this(json.getInt("test"));
	}
	
	
	@Override
	public void onTick(TickInfo info, Object s) 
	{
		Vector2 velocity = body.getVelocity();
		int yDirection = 0;
		if (input.isKeyDown(KeyEvent.VK_W)) --yDirection;
		if (input.isKeyDown(KeyEvent.VK_S)) ++yDirection;
		velocity.setY(vel * yDirection);
		int xDirection = 0;
		if (input.isKeyDown(KeyEvent.VK_D))++xDirection;
		if (input.isKeyDown(KeyEvent.VK_A)) --xDirection;
		
		velocity.setX(vel * xDirection);
		double friction_x = (Math.abs(body.getVelocity().getX()) > 0)? 40 * -Math.signum(body.getVelocity().getX()):0;
		double friction_y = (Math.abs(body.getVelocity().getY()) > 0)? 40 * -Math.signum(body.getVelocity().getY()):0;
		
		body.applyForce(friction_x, friction_y);
		body.tick(info);
		bounds.clamp(hitbox.getBounds());
		LinkedList<QuadTreeTestInstance> collisions = tree.queryPossible(hitbox.getBounds());
		for (QuadTreeTestInstance quadTreeTestInstance : collisions) 
		{
			tree.remove(quadTreeTestInstance.id);
		}
		
		playerSprite.tick(info);
		
	}


	@Override
	public void render(Graphics2D g2) 
	{
		int x = (int) getPosition().x;
		int y = (int) getPosition().y;
		camera.setPosition(x - 860/2,y - 540/2);
		g2.setColor(Color.blue);
		g2.fillRect(x- 860/2, y- 860/2, 1000, 1000);
		tree.render(g2,camera.getBounds());
		g2.drawImage(playerSprite.getCurrentFrame(), x, y, null);
		hitbox.drawHitBox(g2);
		/*
		TileMap tm = TileMapAssetMap.getInstance().getTileMap("background");
		tm.render(g2, camera.getBounds());
		if (input.isKeyDown(KeyEvent.VK_TAB))
		{
			inv.setActive(true);
		}
		inv.render(g2, (int)getPosition().getX(), (int)getPosition().getY());
		*/
		//Level.getLevelPath().drawPath(Color.RED, new BasicStroke(3), g2);
		//g2.translate(10, 100);
		
		//g2.translate(-10, -100);
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		return hitbox.getBounds();
	}

	@Override
	public JSONObject serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PhysicsBody getPhysicsBody() {
		
		return body;
	}

	@Override
	public Vector2 getPosition() 
	{
		return body.getPosition();
	}

	@Override
	public HitBox getHitBox() 
	{		
		return hitbox;
	}

	@Override
	public void onCollision(HitBox other) 
	{		
	}
	
	private void buildQuadTree()
	{
		Rectangle test = new Rectangle(10, 10, 100, 100);
		int nInstances = 100000;
		long now = System.nanoTime();
		
		LinkedList<InstanceID<Node<QuadTreeTestInstance>>> toRemove = new LinkedList<>();
		LinkedList<InstanceID<Node<QuadTreeTestInstance>>> toMoveIDs = new LinkedList<>();
		LinkedList<Rectangle> toMoveRects = new LinkedList<>();
		
		for (int i = 0; i < nInstances; i++) 
		{
			//tree.put(test, "h");
			test = new Rectangle(Rand.range(0, 9800), Rand.range(0, 9800), Rand.range(0, 150), Rand.range(0, 150));
			QuadTreeTestInstance instance = new QuadTreeTestInstance(test, null);
			InstanceID<Node<QuadTreeTestInstance>> id = tree.put(test, instance);
			instance.id = id;
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
		for (InstanceID<Node<QuadTreeTestInstance>> instanceID : toRemove) 
		{
			//System.out.println(++i);
			tree.remove(instanceID);
		}
	}
	

	/*
	 * private static class MathFunction 
	{
		private String expression;
		private String rawExpression;
		
		public MathFunction(String exp)
		{
			this.expression = this.rawExpression = exp;
			this.expression = expression.replaceAll("\\s", "");
			StringBuilder sb = new StringBuilder();
			for (int index = 0; index < expression.length(); index++) 
			{
				char ch =expression.charAt(index);
				if (expression.charAt(index) == ')')
				{
					if (expression.length()-1 == index)
					{
						sb.append(expression.charAt(index));
						break;
					}
					else
					{
						char next = expression.charAt(index+1);
						if (Character.isDigit(next) || Character.isAlphabetic(next) || next == '(')
						{
							sb.append(expression.charAt(index));
							sb.append('*');
						}
						else sb.append(expression.charAt(index));
						continue;
					}
				}
				
				if (Character.isDigit(ch))
				{
					if (expression.length()-1 == index)
					{
						sb.append(expression.charAt(index));
						break;
					}
					else
					{
						char next = expression.charAt(index+1);
						if (Character.isAlphabetic(next) || next == '(')
						{
							sb.append(expression.charAt(index));
							sb.append('*');
							continue;
						}
						else
						{
							sb.append(expression.charAt(index));
							continue;
						}
					}
					
				}
				
				if (ch == 'x')
				{
					if (index - 1 > 0)
					{
						char prev = expression.charAt(index -1);
						if (Character.isAlphabetic(prev))
						{
							sb.append('(');
							sb.append(expression.charAt(index));
							continue;
						}
					}
					else
					{
						if (expression.length()-1 == index)
						{
							sb.append(expression.charAt(index));
							break;
						}
						char next = expression.charAt(index+1);
						if (Character.isAlphabetic(next))
						{
							sb.append(ch);
							sb.append('*');
							continue;
						}
					}
				}
				sb.append(expression.charAt(index));
			}
			this.expression = sb.toString();
			this.expression = expression.replaceAll("pi", Double.toString(Math.PI));
			this.expression = expression.replaceAll("e", Double.toString(Math.E));
			System.out.println(this.expression);
			
		}
		
		private double eval(final String str) 
		{
		    return new Object() {
		        int pos = -1, ch;

		        void nextChar() {
		            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
		        }

		        boolean eat(int charToEat) {
		            while (ch == ' ') nextChar();
		            if (ch == charToEat) {
		                nextChar();
		                return true;
		            }
		            return false;
		        }

		        double parse() {
		            nextChar();
		            double x = parseExpression();
		            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
		            return x;
		        }

		        // Grammar:
		        // expression = term | expression `+` term | expression `-` term
		        // term = factor | term `*` factor | term `/` factor
		        // factor = `+` factor | `-` factor | `(` expression `)`
		        //        | number | functionName factor | factor `^` factor

		        double parseExpression() {
		            double x = parseTerm();
		            for (;;) {
		                if      (eat('+')) x += parseTerm(); // addition
		                else if (eat('-')) x -= parseTerm(); // subtraction
		                else return x;
		            }
		        }

		        double parseTerm() {
		            double x = parseFactor();
		            for (;;) {
		                if      (eat('*')) x *= parseFactor(); // multiplication
		                else if (eat('/')) x /= parseFactor(); // division
		                else return x;
		            }
		        }

		        double parseFactor() {
		            if (eat('+')) return parseFactor(); // unary plus
		            if (eat('-')) return -parseFactor(); // unary minus

		            double x;
		            int startPos = this.pos;
		            if (eat('(')) { // parentheses
		                x = parseExpression();
		                eat(')');
		            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
		                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
		                x = Double.parseDouble(str.substring(startPos, this.pos));
		            } else if (ch >= 'a' && ch <= 'z') { // functions
		                while (ch >= 'a' && ch <= 'z') nextChar();
		                String func = str.substring(startPos, this.pos);
		                x = parseFactor();
		                if (func.equals("sqrt")) x = Math.sqrt(x);
		                else if (func.equals("sin")) x = Math.sin(x);
		                else if (func.equals("cos")) x = Math.cos(x);
		                else if (func.equals("tan")) x = Math.tan(x);
		                else if (func.equals("log")) x = Math.log(x);
		                else throw new RuntimeException("Unknown function: " + func);
		            } else {
		                throw new RuntimeException("Unexpected: " + (char)ch);
		            }

		            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

		            return x;
		        }
		    }.parse();
		}
		
		
		public double at(double x)
		{
			String xAsString = Double.toString(x);
			String finalExpression = expression.replaceAll("x", xAsString);
			return eval(finalExpression);
		}
		
		public String getRaw()
		{
			return rawExpression;
		}
	}
	*/
	 
	

	
}
