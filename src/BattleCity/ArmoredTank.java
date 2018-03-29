package BattleCity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public class ArmoredTank extends EnemyTank {
	public ArmoredTank(int a,int x,int y) {
		point = new Point(x,y);
		dimension = new Dimension(40,40);
		direction = DIR_UP;
		bodyColor = Color.gray;
		shootDelay = 20;
		lastShoot = 0;
		killScore = 0;
		hp = a;
		live = 0;
	}
	
long frame = 0;
	
	@Override
	public void think() {

		if (frame < 10)
			moveRight();
		if (frame >= 10 && frame < 20)
			moveDown();
		if (frame >= 20 && frame < 30)
			moveLeft();
		if (frame >= 30)
			moveUp();
		
		frame = (frame+1) % 40;
	}
}
