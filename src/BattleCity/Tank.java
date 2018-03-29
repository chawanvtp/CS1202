package BattleCity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Tank {

	public static final int DIR_UP = 100;
	public static final int DIR_DOWN = 101;
	public static final int DIR_LEFT = 102;
	public static final int DIR_RIGHT = 103;
	
	public static final int STATE_ALIVE = 110;
	public static final int STATE_IS_DYING = 111;
	public static final int STATE_IS_DEAD = 112;	
	protected int state = STATE_ALIVE;
	
	protected int speed;
	protected int shootDelay;
	protected int weapon;
	protected Dimension dimension;
	protected int defence;
	protected Point point;
	protected int direction;
	protected Color bodyColor;
	protected int hp;
	protected long lastShoot;
	protected int live;
	protected int killScore;

	public Tank() {
		point = new Point(100,100);
		dimension = new Dimension(40,40);
		direction = DIR_UP;
		bodyColor = Color.white;
		shootDelay = 20;
		lastShoot = 0;
		killScore = 0;
		hp = 1;
		live = 5;
	}
	
	public void setLastShoot(long frame){
		lastShoot = frame;
	}
	
	public Tank(Color color, int x,int y) {
		this();
		point.x = x;
		point.y = y;
		bodyColor = color;
	}
	
	public void getHit() {
		hp--;
		if (hp <= 0) {
			die();
		}
	}
	
	public void think() {
		// do nothing
	}
	
	public boolean collides(Tank t) {
		Rectangle myself = new Rectangle(getX(),getY(),
				getWidth(),getHeight());
		Rectangle another = new Rectangle(t.getX(),t.getY(),
				t.getWidth(),t.getHeight());
		return myself.intersects(another);
	}
	
	public boolean collides(BulletEnemy t) {
		Rectangle myself = new Rectangle(getX(),getY(),
				getWidth(),getHeight());
		Rectangle another = new Rectangle(t.getX(),t.getY(),
				t.getWidth(),t.getHeight());
		return myself.intersects(another);
	}
	
	public boolean collides(Bullet b) {
		Rectangle myself = new Rectangle(getX(),getY(),
				getWidth(),getHeight());
		Rectangle another = new Rectangle(b.getX(),b.getY(),
				b.getWidth(),b.getHeight());
		return myself.intersects(another);
	}
	
	public int getWidth() { return dimension.width; }
	public int getHeight() { return dimension.height; }
	public int getX() { return point.x; }
	public int getY() { return point.y; }

	public void moveUp() {
		point.y -= 5;
		direction = DIR_UP;
	}

	public void moveDown() {
		point.y += 5;
		direction = DIR_DOWN;
	}

	public void moveLeft() {
		point.x -= 5;
		direction = DIR_LEFT;
	}

	public void moveRight() {
		point.x += 5;
		direction = DIR_RIGHT;
	}

	public void shoot() {}

	int dieFrame = 0;
	public void die() {
		live -= 1;
		if(live>0){
		state = STATE_IS_DYING;}else{ state = STATE_IS_DEAD; }
	}
	
	public void respawn(){
		state = STATE_ALIVE;
	}
	
	public void killedBy(Tank q,Tank d){
		q.killScore += 1;
		d.getHit();
		if(d.state==STATE_IS_DEAD){
		d.point.x = 1000;
		d.point.y = 1000;
		}
	}

	public void spawn() {}
	
	public void draw(Graphics g) {
		switch(state) {
		case STATE_IS_DYING:
			if (0 <= live) {
				g.setColor(Color.yellow);
				g.fillOval(point.x, point.y, dieFrame*10, dieFrame*10);
				this.respawn();
			}
			return;
		}
		
		if (state == STATE_IS_DEAD) {
			return;
		}
		
		g.setColor(bodyColor);
		g.fillRect(point.x, point.y, dimension.width, dimension.height);		
		g.setColor(Color.black);
		g.drawOval(point.x+10, point.y+10, 20, 20);
		
		
		switch(direction) {
		case DIR_RIGHT:		
			g.setColor(Color.gray);
			g.fillRect(point.x, point.y, dimension.width, dimension.height/4);
			g.fillRect(point.x, point.y+30, dimension.width, dimension.height/4);		
			g.setColor(Color.blue);
			g.fillRect(point.x+30, point.y+16, 14, 8);
			break;
		case DIR_LEFT:
			g.setColor(Color.gray);
			g.fillRect(point.x, point.y, dimension.width, dimension.height/4);
			g.fillRect(point.x, point.y+30, dimension.width, dimension.height/4);		
			g.setColor(Color.blue);
			g.fillRect(point.x-4, point.y+16, 14, 8);
			break;
		case DIR_DOWN:		
			g.setColor(Color.gray);
			g.fillRect(point.x, point.y, dimension.width/4, dimension.height);
			g.fillRect(point.x+30, point.y, dimension.width/4, dimension.height);		
			g.setColor(Color.blue);
			g.fillRect(point.x+16, point.y+30, 8, 14);
			break;
		case DIR_UP:		
			g.setColor(Color.gray);
			g.fillRect(point.x, point.y, dimension.width/4, dimension.height);
			g.fillRect(point.x+30, point.y, dimension.width/4, dimension.height);		
			g.setColor(Color.blue);
			g.fillRect(point.x+16, point.y-4, 8, 14);
			break;			
		}
	}

	public int getDirection() {
		return direction;
	}

}
