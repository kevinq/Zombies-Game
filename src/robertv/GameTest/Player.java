package robertv.GameTest;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.Animation;
import java.util.*;

public class Player extends Entity {
		
	public static final int NORTH = 1;
	public static final int SOUTH = 0;
	public static final int EAST = 2;
	public static final int WEST = 3;
	
	private int facing;
	private Image[] sprites;
	public Image bookimg;
	private Animation[] walkcycle;
	
	private Map map;
	
	//private Book[] inventory;
	private int ammo;
	
	public Player(Map m) {
		xCoord = 16;
		yCoord = 0;
		speed = 1;
		facing = NORTH;
		sprites = new Image[4];
		
		aabb = new Rectangle(480+10, 336+19, 12, 11);
		
		position = new Vector(0,0);
		velocity = new Vector(0);
		acceleration = new Vector(0);
		
		map = m;
		
		ammo = 0;
		//inventory = new Book[10];
	}
	
	public int setFacing(int direction) {
		facing = direction;
		return facing;
	}
	
	public boolean isFacing(int direction) {
		return direction == facing;
	}
	
	public void setSprites(Image[] in) {
		sprites = in;
	}
	
	public void setWalkCycle(Animation[] wc) {
		walkcycle = wc;
	}
	
	public void setSpeed(int sp) {
		speed = sp;
	}
	
	public int getAmmo() {
		return ammo;
	}
	
	
	///*
	public void startMoving(int direction) {
		switch(setFacing(direction)) {
		case NORTH :
    		this.velocity.add((new Vector(0, 1)).scalarMultiply(this.speed));
		break;
		case SOUTH :
    		this.velocity.add((new Vector(0, -1)).scalarMultiply(this.speed));
		break;
		case EAST :
    		this.velocity.add((new Vector(-1, 0)).scalarMultiply(this.speed));
		break;
		case WEST :
    		this.velocity.add((new Vector(1, 0)).scalarMultiply(this.speed));
		break;
		}
	}
	//*/

	
	public void stopMoving(int direction) {
		switch (direction) {
		case NORTH:
		case SOUTH:
			velocity.y = 0;
		break;
		case EAST:
		case WEST:
			velocity.x = 0;
		break;
		}
	}
	
	
	public void takeBook(Bookshelf source) {
		if(ammo < 10 && source.takeBook()) {
			ammo++;
		}
	}
	
	public void fireBook(ArrayList<Entity> entityList) {
		if(ammo == 0) {
			return;
		}
		Book bk = new Book(xCoord, yCoord, facing, position);
		bk.setSprite(bookimg);
		entityList.add(bk);
		ammo--;
	}
	
	/*
	 * this update is near identical to its parent update()
	 * except it has a collision check built in
	 * 
	 */
	public void update() {
		ArrayList<Bookshelf> collisions = map.getSurroundingShelves(yCoord, xCoord);
		collisionCheck(collisions);
		
		//clamp the velocity so as not to exceed speed
		//not certain how necessary this is.
		if(velocity.x > speed || velocity.x < -speed) {
			velocity.x /= Math.abs(velocity.x);
			velocity.x *= speed;
		}
		if(velocity.y > speed || velocity.y < -speed) {
			velocity.y /= Math.abs(velocity.y);
			velocity.y *= speed;
		}
				
		position.add(velocity);
		
		if((xCoord < 1 && velocity.x < 0) || 
			(xCoord > 30 && velocity.x > 0) || 
			(yCoord < 0) && velocity.y < 0) {
			position.subtract(velocity);
			//went off screen
		}
		
		if(position.x > 32.0) {
			this.xCoord++;
			position.x -= 32.0;
		}
		if(position.x < 0.0) {
			this.xCoord--;
			position.x += 32.0;
		}
		if(position.y > 32.0) {
			this.yCoord++;
			position.y -= 32.0;
		}
		if(position.y < 0.0) {
			this.yCoord--;
			position.y += 32.0;
		}

	}
	
	public void render(int xp, int yp) {
		if(velocity.magnitude() == 0) {
			sprites[facing].draw(480, 336);
		}
		else {
			walkcycle[facing].draw(480, 336);
		}
	}
	
	
}