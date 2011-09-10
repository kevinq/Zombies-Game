package robertv.GameTest;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.Animation;
import java.util.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

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
	private int health;
	
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
		
		health = 100;
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
	
	public int getHealth() {
		return health;
	}
	
	
	///*
	public void startMoving(int direction) {
		switch(setFacing(direction)) {
		case NORTH :
    		this.velocity = ((new Vector(0, 1)).scalarMultiply(this.speed));
		break;
		case SOUTH :
    		this.velocity = ((new Vector(0, -1)).scalarMultiply(this.speed));
		break;
		case EAST :
    		this.velocity = ((new Vector(-1, 0)).scalarMultiply(this.speed));
		break;
		case WEST :
    		this.velocity = ((new Vector(1, 0)).scalarMultiply(this.speed));
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
		try {
			Sound whoosh = new Sound("/assets/whoosh.wav");
			whoosh.play();
		} catch (SlickException e) {
			e.printStackTrace();
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
		
		ArrayList<Bookshelf> collisions = map.getSurroundingShelves(yCoord, xCoord);
		if(collisionCheck(collisions) != null) {
			return;
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
	
	public  Entity collisionCheck(ArrayList<? extends Entity> checklist) {
		
		Rectangle oldaabb = new Rectangle(0,0,0,0);
		oldaabb.setBounds(aabb);
		aabb.setBounds(aabb.getX()+(float)velocity.x, aabb.getY()-(float)velocity.y, aabb.getWidth(), aabb.getHeight());
		
		for(int i=0;i<checklist.size();i++) {
			if(this.aabb.intersects(checklist.get(i).aabb)) {
				Entity that = checklist.get(i);
				int dx = (int)(Math.abs(this.aabb.getCenterX() - that.aabb.getCenterX()) - (this.aabb.getWidth()/2 + that.aabb.getWidth()/2));
				int dy = (int)(Math.abs(this.aabb.getCenterY() - that.aabb.getCenterY()) - (this.aabb.getHeight()/2 + that.aabb.getHeight()/2));
				
				dx = Math.abs(dx);
				dy = Math.abs(dy);
				
				dx+=2;
				dy+=2;
				
				if(velocity.x > 0) {
					position.x = position.x + velocity.x - dx;
					aabb.setX(aabb.getX()-dx);
				}
				if(velocity.x < 0) {
					position.x = position.x + velocity.x + dx;
					aabb.setX(aabb.getX()+dx);
				}
				if(velocity.y > 0) {
					position.y = position.y + velocity.y - dy;
					aabb.setY(aabb.getY()+dy);
				}
				if(velocity.y < 0) {
					position.y = position.y + velocity.y + dy;
					aabb.setY(aabb.getY()-dy);
				}
			
				return that;
			}
		}
		aabb.setBounds(oldaabb);
		return null;
	}

	
	public void render(int xp, int yp) {
		if(velocity.magnitude() == 0) {
			sprites[facing].draw(480, 336);
		}
		else {
			walkcycle[facing].draw(480, 336);
		}
	}
	
	@Override
	public boolean collisionTrue(Entity en, ArrayList<Entity> elist) {
		if (en instanceof Zombie){
			health = health - 10;
			System.out.println("Collision with zombie detected!");
			return true;
		}
		System.out.println("player collision true reached!");
		return false;
	}
}
