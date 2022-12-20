package game;

import java.awt.Rectangle;

//Justin Watts ~ Dec. 13, 2022

public class Laser extends Rectangle {
	int speed = 3;
	static int LASERWIDTH = 16;
	static int LASERHEIGHT = 6;
	boolean playerLaser;


	Laser(int x, int y, boolean player){

		width = LASERWIDTH;
		height = LASERHEIGHT;

		this.x = x;
		this.y = y;
		
		this.playerLaser = player;
	}
}
