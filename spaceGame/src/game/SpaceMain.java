package game;
//Justin Watts ~ Dec. 12, 2022
//Making an asteroid style space game

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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class SpaceMain {


	Timer paintTimer = new Timer(10, new PTimer());
	SpaceShip player = new SpaceShip(50,50);
	
	ArrayList<Laser> laserList = new ArrayList<Laser>();
	int MAXLASERS = 6;

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
		window.addKeyListener(new MKeyListener());
		window.pack();
		window.setLocationRelativeTo(null); // Center on the screen
		window.setVisible(true);
		paintTimer.start();
	}

	//method that moves the lasers
	void calculateLaser() {
		for (int i = 0; i < laserList.size(); i++) {
			Laser b = laserList.get(i);
			b.x += b.speed;
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
			g2.fillRect(player.x, player.y, player.length, player.width);

			//Drawing the player lasers
			g2.setColor(Color.BLUE);
			for (Laser b : laserList) {
				g2.fillRect(b.x, b.y, Laser.length, Laser.width);
			}
		}
	}


	//Timer that repaints the screen and moves the laser
	class PTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			calculateLaser();
			pan.repaint();
		}		
	}


	//Checks what keys are pressed
	//TODO: not optimal way of checked key presses, use some other utility but keep interior methods
	class MKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent event) {
			char pressedKey = event.getKeyChar();

			//Moving the ship
			if (pressedKey == 'd') { player.x += player.speed; }
			if (pressedKey == 'a') { player.x -= player.speed; }
			if (pressedKey == 'w') { player.y -= player.speed; }
			if (pressedKey == 's') { player.y += player.speed; }

			//Making the laser
			if (pressedKey == ' ' && laserList.size() < MAXLASERS) {
				int laserX = (player.x + player.length + Laser.length);
				int laserY = (player.y + player.width/2 - Laser.width/2);
				Laser b = new Laser(laserX,laserY);

				laserList.add(b);
			}
		}
	}
}