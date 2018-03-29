package BattleCity;

import java.awt.Color;

public class StandardTank extends EnemyTank {
	public StandardTank(Color color, int x,int y) {
		point.x = x;
		point.y = y;
		bodyColor = color;
		shootDelay = 20;
		hp = 1;
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
