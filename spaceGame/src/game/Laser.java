package game;

import java.awt.Rectangle;

//Justin Watts ~ Dec. 13, 2022

public class Laser extends Rectangle {
	int x, y;
	int speed = 3;
	static int width = 6;
	static int height = 16;

	Laser(int x, int y){
		this.x = x;
		this.y = y;
	}
}
