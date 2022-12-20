package game;
//Watts ~ Dec. 12, 2022
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
	Timer enemySpawn = new Timer(2000, new SpawnTimer());

	SpaceShip player = new SpaceShip(50,50);
	BetterKeyListener bkl = new BetterKeyListener();

	ArrayList<Laser> laserList = new ArrayList<Laser>();
	final int MAXLASERS = 6;
	int playerLaserAmt = 0;
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
		enemySpawn.start();

	}

	//method that moves the lasers
	void calculateLaser() {
		for (int i = 0; i < laserList.size(); i++) {
			Laser tempLaser = laserList.get(i);
			tempLaser.x += tempLaser.speed;


			//Removing laser when off the screen
			if (tempLaser.x > PANW) {
				if (tempLaser.playerLaser) playerLaserAmt--;
				laserList.remove(i);
				i --;
			}

			//Removing the enemy ship and laser if the laser touches it
			for (int k = 0; k < shipList.size(); k++) {
				SpaceShip tempShip = shipList.get(k);

				if (tempLaser.intersects(tempShip)) {

					if (tempLaser.playerLaser) playerLaserAmt--;
					laserList.remove(i);
					shipList.remove(k);
				}
			}
		}
	}

	void calculateEnemies() {
		int shootChance =  (int) (Math.random()*50+1);

		for (int i = 0; i < shipList.size(); i++) {

			//Moving the ship
			SpaceShip tempShip = shipList.get(i);
			if (tempShip.y <= 0) tempShip.speed *= -1;
			if (tempShip.y >= PANH) tempShip.speed *= -1;

			tempShip.y += tempShip.speed;

			//Shooting lasers
			if (tempShip.shootChance == shootChance ) {
				int laserX = (tempShip.x  - Laser.LASERWIDTH);
				int laserY = (tempShip.y + tempShip.height/2 - Laser.LASERHEIGHT/2);
				Laser tempLaser = new Laser(laserX,laserY, false);
				tempLaser.speed *= -1;

				laserList.add(tempLaser);
			}
		}
	}

	void moveShip() {
		if (bkl.isKeyDown('D')) { player.x += player.speed; }
		if (bkl.isKeyDown('A')) { player.x -= player.speed; }
		if (bkl.isKeyDown('W')) { player.y -= player.speed; }
		if (bkl.isKeyDown('S')) { player.y += player.speed; }

		//Making the laser
		if (bkl.isKeyDown(' ') && playerLaserAmt < MAXLASERS) {

			if ((System.currentTimeMillis() - lastShot) >= cooldown ) {
				int laserX = (player.x + player.width + Laser.LASERWIDTH);
				int laserY = (player.y + player.height/2 - Laser.LASERHEIGHT/2);
				Laser b = new Laser(laserX,laserY, true);

				playerLaserAmt++;
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
			g2.fillRect(player.x, player.y, player.width, player.height);

			//Drawing the lasers
			for (Laser b : laserList) {
				if (b.playerLaser) g2.setColor(Color.RED);
				else g2.setColor(Color.MAGENTA);
				g2.fillRect(b.x, b.y, Laser.LASERWIDTH, Laser.LASERHEIGHT);
			}

			//Drawing the enemy ship
			g2.setColor(Color.MAGENTA);
			for (SpaceShip b : shipList) {
				g2.fillRect(b.x, b.y, b.width, b.height);
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
			if (MAXSHIPS > shipList.size()) shipList.add(new SpaceShip (600, (int) (Math.random()*PANH+1)));
		}		
	}
}

