package game;
//Justin Watts ~ Dec. 12, 2022
//Making a Galaga style space game

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class SpaceMain {


	Timer paintTimer = new Timer(10, new PTimer());
	//Timer enemySpawn = new Timer(2000, new SpawnTimer());
	
	SpaceShip player = new SpaceShip(50,50);
	BetterKeyListener bkl = new BetterKeyListener();

	ArrayList<Laser> laserList = new ArrayList<Laser>();
	final int MAXLASERS = 6;
	long cooldown = 400;
	long lastShot;

	ArrayList<SpaceShip> shipList = new ArrayList<SpaceShip>();
	final int MAXSHIPS = 4;

	static final int PANW = 700;
	static final int PANH = 700;
	JFrame window;
	DrawingPanel pan;


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SpaceMain(); 
			}
		});
	}


	// Sets up the panel and window objects as well as starts the timers
	SpaceMain(){
		window = new JFrame();
		window.setTitle("PEW PEW SPACE PEW PEW");
		pan = new DrawingPanel();

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(pan);
		window.addKeyListener(bkl);
		window.pack();
		window.setLocationRelativeTo(null); // Center on the screen
		window.setVisible(true);
		paintTimer.start();
		//enemySpawn.start();
		shipList.add(new SpaceShip(600,600));
	}

	//method that moves the lasers
	void calculateLaser() {
		for (int i = 0; i < laserList.size(); i++) {
			Laser tempLaser = laserList.get(i);
			tempLaser.x += tempLaser.speed;


			//Removing laser when off the screen
			if (tempLaser.x > PANW) {
				laserList.remove(i);
				i --;
			}

			//Removing the enemy ship and laser if the laser touches it
			for (int k = 0; k < shipList.size(); k++) {
				SpaceShip tempShip = shipList.get(k);
				if (tempLaser.intersects(tempShip)) {
					laserList.remove(i);
					shipList.remove(k);
					i--;
					k--;
				}
			}
		}
	}

	void calculateEnemies() {
		for (int i = 0; i < shipList.size(); i++) {
			SpaceShip b = shipList.get(i);
			if (b.y <= 0) b.speed = 1;
			if (b.y >= PANH) b.speed = -1;
			if (b.y == 600) b.speed = 0;
			
			b.y += b.speed;
		}
	}

	void moveShip() {
		if (bkl.isKeyDown('D')) { player.x += player.speed; }
		if (bkl.isKeyDown('A')) { player.x -= player.speed; }
		if (bkl.isKeyDown('W')) { player.y -= player.speed; }
		if (bkl.isKeyDown('S')) { player.y += player.speed; }

		//Making the laser
		if (bkl.isKeyDown(' ') && laserList.size() < MAXLASERS) {

			if ((System.currentTimeMillis() - lastShot) >= cooldown ) {
				int laserX = (player.x + player.height + Laser.height);
				int laserY = (player.y + player.width/2 - Laser.width/2);
				Laser b = new Laser(laserX,laserY);

				laserList.add(b);
				lastShot = System.currentTimeMillis();
			}
		}
	}



	@SuppressWarnings("serial")
	class DrawingPanel extends JPanel {
		DrawingPanel(){
			this.setBackground(new Color(0,0,0));
			this.setPreferredSize(new Dimension(PANW, PANH));
		}	

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //turn on antiAliasing

			//Drawing the player ship
			g2.setColor(Color.RED);
			g2.fillRect(player.x, player.y, player.height, player.width);

			//Drawing the player lasers
			g2.setColor(Color.BLUE);
			for (Laser b : laserList) {
				g2.fillRect(b.x, b.y, Laser.height, Laser.width);
			}
			
			//Drawing the enemy ship
			g2.setColor(Color.MAGENTA);
			for (SpaceShip b : shipList) {
				g2.fillRect(b.x, b.y, b.height, b.width);
			}
		}
	}


	//Timer that repaints the screen and moves the laser
	class PTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			moveShip();
			calculateEnemies();
			calculateLaser();
			pan.repaint();
		}		
	}

	//Timer that spawns the enemies
	class SpawnTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (MAXSHIPS > shipList.size()) shipList.add(new SpaceShip (600,400));
		}		
	}
}

