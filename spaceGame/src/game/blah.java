import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

				if ((System.currentTimeMillis() - lastShot) >= cooldown ) {
					int laserX = (player.x + player.length + Laser.length);
					int laserY = (player.y + player.width/2 - Laser.width/2);
					Laser b = new Laser(laserX,laserY);

					laserList.add(b);
					lastShot = System.currentTimeMillis();
				}
			}
		}
	}