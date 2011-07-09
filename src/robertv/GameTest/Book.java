package robertv.GameTest;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;
import java.util.*;

public class Book extends Entity{
	
	public Book(int x, int y, Vector p, Vector v) {
		//inherited
		xCoord = x;
		yCoord = y;
		position = p;
		velocity = v;
		aabb = new Rectangle(0,0,32,32);
		speed = 5;
	}
	
	//spawn book given information about the player
	public Book(int x, int y, int direction, Vector p) {
		aabb = new Rectangle(0,0,32,32);
		speed = 5;
		switch(direction) {
		case Player.NORTH:
			xCoord = x-1;
			yCoord = y;
			velocity = new Vector(0,1);
		break;
		case Player.SOUTH:
			xCoord = x-1;
			yCoord = y;
			velocity = new Vector(0,-1);
		break;
		case Player.WEST:
			xCoord = x-1;
			yCoord = y;
			velocity = new Vector(1, 0);
		break;
		case Player.EAST:
			xCoord = x-1;
			yCoord = y;
			velocity = new Vector(-1, 0);
		break;
		}
		position = new Vector(p.x, p.y);
		velocity.scalarMultiply(speed);
	} //end constructor
	
	
	/*
	 * what to do if it has collided with something.
	 * This method returns true if this object needs to be removed
	 * from elist.
	 * This is because if it was removed, the for loop iterating through elist
	 * needs to be told to break. If not a crash occurs.
	 */
	public boolean collisionTrue(Entity collider, ArrayList<Entity> elist) {
		if(collider instanceof Zombie) {
			elist.remove(this);
			elist.remove(collider);
			return true;
		}
		if(collider instanceof Bookshelf) {
			elist.remove(this);
			return true;
		}
		return false;
	}
	
	
	public  void render(int xp, int yp) {
		sprite.draw(xp+(int)position.x, yp-(int)position.y);
	}
}
