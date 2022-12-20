package game;

import java.awt.Rectangle;


//Justin Watts ~ Dec. 13, 2022

public class SpaceShip extends Rectangle {
	int speed = 5;
	int shootChance;

	SpaceShip(int x, int y){
		
		shootChance =  (int) (Math.random()*50+1);
		width = 60;
		height = 25;
		this.x = x;
		this.y = y;
	}
}
