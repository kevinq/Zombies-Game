package robertv.GameTest;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Animation;
import java.util.*;

public class Zombie extends Entity {

	public Image[] sprites;
	public Animation[] anim;
	public int facing;
	
	public Zombie() {
		xCoord = 0;
		yCoord = 0;
		speed = 0.5;
		position = new Vector(0,0);
		velocity = new Vector(0,0);
		
		aabb = new Rectangle(0,0,32,32);
		
	}
	
	public Zombie spawnZombie(int xc, int yc, Image[] spr, Animation[]  anArray) {
		xCoord = xc;
		yCoord = yc;
		aabb.setBounds(xc, yc, 32, 32);
		sprites = spr;
		anim = anArray;
		return this;
	}

	public void update() {
		/*
		 * zombie velocity is constrained to only go in
		 * one direction at once.
		 * it also makes sure it's facing in the right direction.
		 */
		if(Math.abs(this.velocity.x) > Math.abs(this.velocity.y)) {
			this.velocity.y = 0;
			if(this.velocity.x > 0) {
				this.velocity.x = 1;
				this.facing = Player.WEST;
			}
			if(this.velocity.x < 0) {
				this.velocity.x = -1;
				this.facing = Player.EAST;
			}
		}else {
			if(this.velocity.y > 0) {
				this.velocity.y = 1;
				this.facing = Player.NORTH;
			}
			if(this.velocity.y < 0) {
				this.velocity.y = -1;
				this.facing = Player.SOUTH;
			}
			this.velocity.x = 0;
		}
		// bad way to do collision detection
//		if ((Math.abs(SimpleTest.rachel.xCoord - this.xCoord) < .2) && (Math.abs(SimpleTest.rachel.yCoord - this.yCoord) < .2)){
//			System.out.println("ZOMBIE PLAYER COLLISION.");
//			System.out.println("player coordinates " + SimpleTest.rachel.xCoord + "," + SimpleTest.rachel.yCoord);
//			System.out.println("zombie coordinates " + this.xCoord + "," + this.yCoord);
//			SimpleTest.rachel.decreaseHealth();
//		}
		super.update();
	}
	
	/*
	 * currently this is the same as its parent collision check,
	 * but I might want it to be different eventually
	 * 
	 */
	public  Entity collisionCheck(ArrayList<? extends Entity> checklist) {
		
		if(velocity.x > speed || velocity.x < -speed) {
			velocity.x /= Math.abs(velocity.x);
			velocity.x *= speed;
		}
		if(velocity.y > speed || velocity.y < -speed) {
			velocity.y /= Math.abs(velocity.y);
			velocity.y *= speed;
		}

		
		for(int i=0;i<checklist.size();i++) {
			if(this.aabb.intersects(checklist.get(i).aabb)) {
				Entity that = checklist.get(i);
				if(this == that) {
					return null;
				}
				int dx = (int)(Math.abs(this.aabb.getCenterX() - that.aabb.getCenterX()) - (this.aabb.getWidth()/2 + that.aabb.getWidth()/2));
				int dy = (int)(Math.abs(this.aabb.getCenterY() - that.aabb.getCenterY()) - (this.aabb.getHeight()/2 + that.aabb.getHeight()/2));
				
				dx = Math.abs(dx);
				dy = Math.abs(dy);

				if(facing == Player.WEST && dx <= dy) {
					position.x -= dx;
				}
				if(facing == Player.EAST && dx <= dy) {
					position.x += dx;
				}
				if(facing == Player.NORTH && dy <= dx) {
					position.y -= dy;
				}
				if(facing == Player.SOUTH && dy <= dx) {
					position.y += dy;
				}		
				return that;
			}
		}
		return null;
	}
	
	@Override
	public boolean collisionTrue(Entity en, ArrayList<Entity> elist) {
		if (en instanceof Player){
			System.out.println("Player collision detected.");
			return true;
		}
		if (en instanceof Zombie){
			return true;
		}
		return false;
	}
	
	public void render(int xp, int yp) {
			anim[facing].draw(xp+(int)position.x, yp-(int)position.y);
	}

	
}
