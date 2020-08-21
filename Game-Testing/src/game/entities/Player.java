package game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import engine.core.Engine;
import engine.core.input.InputHandler;
import engine.core.instance.InstanceID;
import engine.core.random.Rand;
import engine.core.tick.TickInfo;
import engine.util.json.JSONSerializable;
import engine.util.math.MathUtils;
import engine.util.pathing.Path;
import external.org.json.JSONObject;
import game.Level;
import game.entities.buildings.turrets.BaseTurret;
import game.entities.enemies.BasicEnemy;
import game.entities.item.Item;
import game.entities.player.robot.RobotPlayer;
import game.inventory.Inventory;
import game.loader.LevelLoader;
import game.renderer.Renderer;
import game.weapons.bullet.LaserImageBuilder;
import graphics.Camera;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import graphics.transform.Matrix;
import graphics.util.GraphicsUtils;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.CollisionLayer;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.collision.quadtree.CRQuadTree;
import physics.collision.quadtree.CRQuadTree.Node;
import physics.collision.ray.Ray;
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
	private Renderer renderer;
	private Inventory inv;
	private LinkedList<Vector2> points;
	private Path p;
	private boolean drawGrid;
	private TileMap tm;
	private Rectangle raytest;
	private Ray ray;
	
	
	
	private Sprite spriteWalkUp;
	private Sprite spriteWalkDown;
	private Sprite spriteWalkLeft;
	private Sprite spriteWalkRight;
	
	private Sprite rifle;
	private Sprite laser;
	
	private Sprite currentSprite;
	
	LinkedList<Vector2> hits;
	
	private CRQuadTree<Rectangle> qt;
	private Rectangle bounds;
	
	private Vector2 enddraw;
	
	private CRQuadTree<QuadTreeTestInstance> tree;
	private LaserImageBuilder lib;
	private double vel = 300;
	private boolean c;
	private double periodic;
	
	double delta;
	
	private Matrix gunRotation;
	
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
		
		
		/*
		TickScheduler.getInstance().addPeriodic( new Tickable() {
			
			@Override
			public void onTick(TickInfo info) {
				System.out.print("Num enemies -> ");
				System.out.println(Level.enemyTree.size());
				System.out.print("Num tickable -> ");
				System.out.println(TickScheduler.getInstance().numScheduled());
				
			}
		}, TimeUtils.secondsToNanos(1));
		*/
		Level.player = this;
		inv = new Inventory(100);
		input = new InputHandler(true);
		
		spriteWalkUp = currentSprite = SpriteMap.getClonedSprite("player_walk_up");
		spriteWalkDown = SpriteMap.getClonedSprite("player_walk_down");
		spriteWalkRight = SpriteMap.getClonedSprite("player_walk_right");
		spriteWalkLeft = SpriteMap.getClonedSprite("player_walk_left");
		
		rifle = SpriteMap.getClonedSprite("gamma_ray_rifle");
		laser = SpriteMap.getClonedSprite("red_laser");
		
		playerSprite = SpriteMap.getClonedSprite("player_walk_up");
		body = new PhysicsBody(new MassData(1), new Material(), new Transform(100,100));
		input.setTransform(body.getTansform());
		hitbox = playerSprite.getHitBox(body.getPosition(), this);
		addToLayers("default", null, null);
		inv.addItems(Item.getItem("Iron Ore"), 4);
		inv.addItems(Item.getItem("Iron Ore"), 1000);
		camera = Renderer.getInstance().getCamera("main");
		bounds = Level.enemyTree.getBounds();
		this.camera.setBoundries(new Rectangle(0, 0, 64*32, 24*32));
		//Turret t = new Turret(new Vector2(128, 128+32));
		BaseTurret turret = new BaseTurret(new Vector2(128+256, 128+320));
		raytest = new Rectangle(100, 100);
		input.addMouseTranslation(camera.getBounds().getPosition(),1);
		Level.raytest = raytest;
		Level.turret = turret;
		
		LevelLoader.load(Engine.getGameFiles().get("Level1.json"));
		this.tm = TileMapAssetMap.getInstance().getTileMap("level1testmap");
		qt = Level.wallTree = tm.toQuadTree();
		lib = new LaserImageBuilder(laser);
		
	}
	
	public Player(JSONObject json)
	{
		this(json.getInt("test"));
		new RobotPlayer();
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{
		if (info.groupName.equals("default")) return;
		delta = info.delta;
		if (camera == null)
		{
			camera = Renderer.getInstance().getCamera("main");
			this.camera.setBoundries(new Rectangle(0, 0, 64*32, 24*32));
		}
		Vector2 velocity = body.getVelocity();
		int yDirection = 0;
		if (input.isKeyDown(KeyEvent.VK_W)) --yDirection;
		if (input.isKeyDown(KeyEvent.VK_S)) ++yDirection;
		int xDirection = 0;
		if (input.isKeyDown(KeyEvent.VK_D))++xDirection;
		if (input.isKeyDown(KeyEvent.VK_A)) --xDirection;
		
		
		double direction = MathUtils.direction(yDirection, xDirection);
		
		if (xDirection != 0 || yDirection != 0)
		{
			currentSprite = spriteWalkRight;
			if (direction > Math.PI/4) currentSprite = spriteWalkUp;
			if (direction > 3*Math.PI/4) currentSprite = spriteWalkLeft;
			if (direction > 5*Math.PI/4) currentSprite = spriteWalkDown;	
		}
		
		if (xDirection != 0 || yDirection != 0)
		{
			velocity.setDirMag(-direction, vel);
			currentSprite.tick(info);
		}
		else velocity.set(0, 0);
		double friction_x = (Math.abs(body.getVelocity().getX()) > 0)? 40 * -Math.signum(body.getVelocity().getX()):0;
		double friction_y = (Math.abs(body.getVelocity().getY()) > 0)? 40 * -Math.signum(body.getVelocity().getY()):0;
		
		body.applyForce(friction_x, friction_y);
		body.tick(info);
		
		Rectangle player = hitbox.getBounds();
		LinkedList<Rectangle> colls = qt.queryCollisions(player);
		if (colls.size() > 0)
		{
			for (Rectangle rectangle : colls) 
			{
				if (player.getY() > rectangle.getY()) rectangle.snapBottom(player);
				if (!rectangle.contains(player)) continue;
				if (player.getY() < rectangle.getY()) rectangle.snapTop(player);
				if (!rectangle.contains(player)) continue;
				if (player.getX() > rectangle.getX()) rectangle.snapRight(player);
				if (!rectangle.contains(player)) continue;
				if (player.getX() < rectangle.getX()) rectangle.snapLeft(player);
			}
		}
		
		bounds.clamp(hitbox.getBounds());
		
		ray = new Ray(getPosition(), -getPosition().angleTo(input.getMousePosition()), 600);
		raytest.getPosition().x = 500;
		raytest.getPosition().y = 400;
		hits = ray.getHits(raytest);
		enddraw = qt.closetHit(ray);
		
		if (enddraw == null)
		{
			
		}
		else
		{
			if (getPosition().distanceToSq(enddraw) > Level.turret.getPosition().distanceToSq(enddraw))
			{
				
				if (ray.intersects(Level.turret.renderBoundingArea())) Level.turret.getHealth().add(-100*info.delta);
				
			}
		}
		
		
	}


	@Override
	public void render(Graphics2D g2) 
	{
		/*
		int x = (int) getPosition().x;
		int y = (int) getPosition().y;
		//camera.setPosition(x - 860/2,y - 540/2);
		TileMap tm = TileMapAssetMap.getInstance().getTileMap("level1testmap");
		tm.render(g2, camera.getBounds());
		g2.drawOval(x, y, 64, 64);
		Level.enemyTree.cleanUp();
		//Level.enemyTree.render(g2, Level.enemyTree.getBounds());
		inv.render(g2, (int)getPosition().getX(), (int)getPosition().getY());
		AffineTransform tx = new AffineTransform();
		
		g2.drawImage(currentSprite.getCurrentFrame(), x, y, null);
		//hitbox.drawHitBox(g2);
		g2.setColor(Color.red);
		if (input.isMouseButtonDown(3))
		{
			Vector2 mouse = input.getMousePosition();
			Renderer.getInstance().translateCord(mouse);
			int mxg = (int) (Math.floor(mouse.x/32)*32);
			int myg = (int) (Math.floor(mouse.y/32)*32);
			g2.fillRect(mxg, myg, 32, 32);
			if (input.isMouseButtonDown(1)) 
			{
				Rectangle rect = new Rectangle(mxg+1,myg+1,30,30);
				if (Level.turretTree.queryCollisions(rect).size() == 0)
				{
					new BaseTurret(new Vector2(mxg,myg));
					System.out.println(Level.turretTree.size());
				}
			}
			drawGrid(g2);
		}
		GraphicsUtils.drawRect(g2, camera.getBounds());
		GraphicsUtils.drawRect(g2, raytest);
		if (ray != null)
		{
			g2.setColor(new Color(1f, 0f, 0f, 0.6f));
			GraphicsUtils.drawLine(g2, ray.origin(), ray.end());
			LinkedList<Rectangle> hits = new LinkedList<Rectangle>();
			qt.queryRay(ray,hits);
			qt.render(g2, camera.getBounds());
			double direction = ray.origin().angleTo(ray.end());
			AffineTransform c = g2.getTransform();
			
			this.gunRotation = new Matrix(direction);
			//Vector2 start = ray.origin().duplicate();
			Vector2 start;
			
			start = gunRotation.mulvec(new Vector2(rifle.getWidth()/2, -9));
			if (ray != null) start.add(ray.origin().x, ray.origin().y-start.y);
			double lx = Math.cos(-direction) * rifle.getWidth()/2 + getPosition().x + rifle.getWidth()/2;
			double ly = Math.sin(-direction) * (rifle.getHeight()/2) + getPosition().y + rifle.getHeight()/2;
			start = new Vector2(lx,ly);
			g2.setColor(new Color(1f, 0f, 0f, 0.5f));
			g2.setColor(Color.blue);
			lib.draw(g2, start, enddraw != null? enddraw: ray.end(), delta);
			g2.fillRect((int)(lx), (int)(ly), 3, 3);
			g2.rotate(-direction, x+rifle.getWidth()/2, y+rifle.getHeight()/2);
			g2.drawImage(rifle.getFrame(0), x, y+currentSprite.getWidth()/2, null);
			g2.setTransform(c);
			start.y += 10;
			
			Rectangle closest = null;
			g2.setColor(Color.GREEN);
			for (Rectangle rectangle : hits) 
			{
				GraphicsUtils.drawRect(g2, rectangle);
				if (closest != null)
				{
					if (rectangle.getPosition().distanceToSq(ray.origin()) < closest.getPosition().distanceToSq(ray.origin())) closest = rectangle;
				} else closest = rectangle;
			}
			
			g2.setColor(Color.ORANGE);
			if (closest != null) GraphicsUtils.drawRect(g2, closest);
			
			//GraphicsUtils.drawRect(g2, hitbox.getBounds());
		}
		
		g2.setColor(Color.YELLOW);
		drawGun(g2);
		if (hits == null) return;
		
		/*
		g2.setColor(new Color(1f, 0f, 0f, 0.2f));
		g2.setStroke(new BasicStroke(6));
		GraphicsUtils.drawLine(g2, ray.origin(), ray.end());
		*/
		
		
		/*
		//Level.getLevelPath().drawPath(Color.RED, new BasicStroke(3), g2);
		//g2.translate(10, 100);
		
		//g2.translate(-10, -100);
		*/
	}
	
	private void drawGrid(Graphics2D g2)
	{
		TileMap tm = TileMapAssetMap.getInstance().getTileMap("background");
		int rows = tm.rows();
		int cols = tm.columns();
		int cw = tm.getCellWidth();
		int ch = tm.getCellHeight();
		Vector2 pos = new Vector2();
		Rectangle rect = new Rectangle(cw, ch, pos);
		for (int i = 0; i < rows; i++) 
		{;
			for (int j = 0; j < cols; j++) 
			{
				pos.y += 32;
				GraphicsUtils.drawRect(g2, rect);
			}
			pos.y = 0;
			pos.x += 32;
		}
	}
	
	private void drawGun(Graphics2D g2)
	{
		double dir = -getPosition().angleTo(input.getMousePosition());
		Matrix rotMatix = new Matrix(dir);
		Vector2 laserStart = rotMatix.mulvec(new Vector2(rifle.getWidth()/2, rifle.getHeight()/2));
		laserStart.add(getPosition().x + rifle.getWidth()/2,getPosition().y+rifle.getWidth()/2);
		double x = Math.cos(dir) * rifle.getWidth()/2 + getPosition().x + rifle.getWidth()/2;
		double y = Math.sin(dir) * rifle.getHeight()/2 + getPosition().y + rifle.getHeight()/2;

		if (ray != null)GraphicsUtils.drawLine(g2, new Vector2(x,y), ray.end());
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		return bounds;
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
		
		for (int i = 0; i < nInstances; i++) 
		{
			//tree.put(test, "h");
			test = new Rectangle(Rand.range(0, 9800), Rand.range(0, 9800), Rand.range(0, 150), Rand.range(0, 150));
			QuadTreeTestInstance instance = new QuadTreeTestInstance(test, null);
			InstanceID<Node<QuadTreeTestInstance>> id = tree.put(test, instance);
			instance.id = id;
			
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
