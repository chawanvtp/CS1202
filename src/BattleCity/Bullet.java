package BattleCity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Bullet {
	private Dimension dimension;
	private Point point;
	private Color bodyColor;
	private int direction;

	public Bullet(Color color, int x, int y, int direction) {
		dimension = new Dimension(20,20);
		point = new Point(x,y);
		bodyColor = color;
		this.direction = direction;
	}
	
	public int getX() { return point.x; }
	public int getY() { return point.y; }
	public int getWidth() { return dimension.width; }
	public int getHeight() { return dimension.height; }
	
	public void update() {
		switch(direction) {
		case Tank.DIR_UP:
			point.y -= 10;
			break;
		case Tank.DIR_DOWN:
			point.y += 10;
			break;
		case Tank.DIR_LEFT:
			point.x -= 10;
			break;
		case Tank.DIR_RIGHT:
			point.x += 10;
			break;
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(bodyColor);
		g.fillRect(point.x, point.y, dimension.width, dimension.height);
	}

	public boolean isOnScreen(int canvasWidth, int canvasHeight) {
		return (point.x >= 0 
					&& point.y >= 0 
					&& point.x <= canvasWidth 
					&& point.y <= canvasHeight);
	}
}
