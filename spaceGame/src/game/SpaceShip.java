package game;

import java.awt.Rectangle;

//Justin Watts ~ Dec. 13, 2022

public class SpaceShip extends Rectangle {
	int x,y;
	int speed = 5;
	int width = 25;
	int length = 75;

	SpaceShip(int x, int y){
	 this.x = x;
	 this.y = y;
	}
}
