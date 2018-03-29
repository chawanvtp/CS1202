package BattleCity;

import java.awt.*;       
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;    

/** Custom Drawing Code Template */
public class Game extends JFrame implements KeyListener {
	public static final int CANVAS_WIDTH  = 640;
	public static final int CANVAS_HEIGHT = 480;

	public static final int STATE_TITLE = 100;
	public static final int STATE_PLAY = 101;
	public static final int STATE_WIN = 102;
	public static final int STATE_LOSE = 103;
	public static final int STATE_DYING = 104;
	public static final int STATE_RESULT = 105;
	
	public static boolean enter = false;
	
	public static int stage;
	protected int expectedScore;
	private int state = STATE_TITLE;

	private DrawCanvas canvas;

	private Tank player;
	private ArrayList<Tank> enemies;
	private ArrayList<Bullet> bullets;
	private ArrayList<BulletEnemy> enemyBullets;

	public Game() {
		canvas = new DrawCanvas();    
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		Container cp = getContentPane();
		cp.add(canvas);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setTitle("Battle-City");
		setVisible(true);
		addKeyListener(this);

		player = new PlayerTank();
		enemies = new ArrayList<Tank>();
		bullets = new ArrayList<Bullet>();
		enemyBullets = new ArrayList<BulletEnemy>();
	}
	
	

	private class DrawCanvas extends JPanel {
		// Override paintComponent to perform your own painting
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);     // paint parent's background

			switch(state) {
			case STATE_DYING:
				setBackground(Color.BLACK);  // set background color for this JPanel
				g.setColor(Color.white);
				g.setFont(getFont().deriveFont(70.0f));
				g.drawString("Enter to continue", 60, 150);
				g.drawString("Life : "+player.live, 60, 280);
				g.setFont(getFont().deriveFont(18.0f));
				g.drawString("Press [ENTER} to start", 150, 360);
				
				enemies.removeAll(enemies);
				bullets.removeAll(bullets);
				enemyBullets.removeAll(enemyBullets);
				
				break;
			case STATE_TITLE:
				setBackground(Color.BLACK);  // set background color for this JPanel
				g.setColor(Color.white);
				g.setFont(getFont().deriveFont(180.0f));
				g.drawString("Battle", 100, 150);
				g.drawString("City", 100, 280);

				g.setFont(getFont().deriveFont(18.0f));
				g.drawString("Press [ENTER} to start", 150, 360);
				break;
			case STATE_PLAY:
				setBackground(Color.BLACK);  // set background color for this JPanel

				player.draw(g);
				
				for(Tank t : enemies) {
	
					t.think();
					t.draw(g);
					if((frame-t.lastShoot)>=t.shootDelay){
						enemyBullets.add( new BulletEnemy(Color.red, 
								t.getX(), t.getY(), 
								t.getDirection())
								);
						t.setLastShoot(frame);
					}

					// Collision detection				
					if( player.collides(t) ) {
						player.die();
						t.die();
						g.setColor(Color.white);
						g.drawString("Boom!", player.getX(), player.getY());
						if(player.state==111){state = STATE_DYING;}
						else if(player.state==112){state = STATE_LOSE;}
					}
				}

				ArrayList<Bullet> toBeRemoved = new ArrayList<Bullet>();
				ArrayList<Bullet> tempBullets = new ArrayList<Bullet>();
				ArrayList<BulletEnemy> EtoBeRemoved = new ArrayList<BulletEnemy>();
				ArrayList<BulletEnemy> EtempBullets = new ArrayList<BulletEnemy>();
				tempBullets.addAll(bullets);
				EtempBullets.addAll(enemyBullets);
				
				for(Bullet bullet : tempBullets) {
					// Draw bullet
					if (! bullet.isOnScreen(CANVAS_WIDTH,CANVAS_HEIGHT)) {
						// this causes a problem, mutual exclusive
						// bullets.remove(bullet);
						// FIX: delay the removal.
						toBeRemoved.add( bullet );
					}
					bullet.update();
					// Detect wheter the bullet hits a tank
					for(Tank t : enemies) {
						if( t.collides( bullet ) ) {
							t.killedBy(player,t);
							toBeRemoved.add( bullet );
							
							break;
						}
					}
					bullet.draw(g);
				}		
				
				bullets.removeAll( toBeRemoved );
				
				for(BulletEnemy enemyBullets : EtempBullets) {
					// Draw bullet
					if (! enemyBullets.isOnScreen(CANVAS_WIDTH,CANVAS_HEIGHT)) {
						// this causes a problem, mutual exclusive
						// bullets.remove(bullet);
						// FIX: delay the removal.
						EtoBeRemoved.add( enemyBullets );
					}
					enemyBullets.update();
					// Detect wheter the bullet hits a tank
					 
						if( player.collides( enemyBullets ) ) {
							player.die();
							EtoBeRemoved.add( enemyBullets );
							if(player.state==111){state = STATE_DYING;}
							else if(player.state==112){state = STATE_LOSE;}
							break;
						}
					
					enemyBullets.draw(g);
				}			
				bullets.removeAll( EtoBeRemoved );


				// Print the frame number
				g.setColor(Color.white);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
				g.drawString("Stage : "+stage, 10, 40);
				g.drawString("Score : " + player.killScore + " / "+expectedScore, 10, 85);
				break;
			case STATE_WIN:
				setBackground(Color.BLACK);  // set background color for this JPanel
				g.setColor(Color.white);
				g.setFont(getFont().deriveFont(80.0f));
				g.drawString("Victory", 100, 150);
				g.setFont(getFont().deriveFont(18.0f));
				g.drawString("Press [ENTER} to start", 150, 360);
				enemies.removeAll(enemies);
				bullets.removeAll(bullets);
				enemyBullets.removeAll(enemyBullets);
				break;
			case STATE_LOSE:
				setBackground(Color.BLACK);  // set background color for this JPanel
				g.setColor(Color.white);
				g.setFont(getFont().deriveFont(80.0f));
				g.drawString("Defeated", 100, 150);
				g.drawString("Score : ", 100, 240);
				enemies.removeAll(enemies);
				bullets.removeAll(bullets);
				enemyBullets.removeAll(enemyBullets);
				break;
			}
			

		}
	}

	long frame = 0;
	public void update() {
		frame++;
		if(player.killScore>=expectedScore&&stage>=1){
			state = STATE_WIN;
		}else if(player.killScore<expectedScore){
			if (frame%50==0) {
				int px = (int) (Math.random()*(CANVAS_WIDTH-50))+1;
				int py = (int) (Math.random()*(CANVAS_HEIGHT-50))+1;
				if(enemies.size()%4==0){
					enemies.add( new ArmoredTank(2+(stage/2),px,py) );
					expectedScore += 2+(stage/2);
				}else if(enemies.size()%9==0){
					enemies.add( new SpeedTank((20/stage),px,py) );
				}else{
					enemies.add( new StandardTank(Color.red, px,py) );
				}
			}
		}

		repaint();
	}

	static class GameLoop implements Runnable {
		Game gg;
		public GameLoop(Game gg) {
			this.gg = gg;
		}

		@Override
		public void run() {
			try {
				while(true) {
					gg.update();
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// Run the GUI codes on the Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				Game gg = new Game(); // Let the constructor do the job				
				Thread t = new Thread(new GameLoop(gg));
				t.start();
			}
		});
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if (state == STATE_TITLE||state == STATE_WIN) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				stage += 1;
				expectedScore += stage;
				state = STATE_PLAY;
				break;
			}
		}
		
		if (state == STATE_LOSE) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				stage = 1;
				player.hp = 1;
				player.live = 5;
				player.killScore = 0;
				expectedScore = stage;
				state = STATE_TITLE;
				break;
			}
		}
		
		if((player.state == STATE_DYING||state == STATE_DYING)&&enter == false){
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				player.respawn();
				expectedScore -= 2+(stage/2);
				state = STATE_PLAY;
				enter = true;
				break;
			}
		}
		
		if (state == STATE_PLAY) {

			switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				player.moveUp();
				break;
			case KeyEvent.VK_DOWN:
				player.moveDown();
				break;
			case KeyEvent.VK_LEFT:
				player.moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				player.moveRight();
				break;
			case KeyEvent.VK_SPACE:
				if((frame-player.lastShoot)>=player.shootDelay||player.lastShoot==0){
				bullets.add( new Bullet(Color.green, 
						player.getX(), player.getY(), 
						player.getDirection())
						);
				player.setLastShoot(frame);
				}
				break;
			case KeyEvent.VK_ESCAPE:
				player.die();
				break;
			}
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e){ 
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			enter = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e){
	}
	
}