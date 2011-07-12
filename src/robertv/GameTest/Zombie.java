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
	
	/*
	 * currently this is the same as its parent collision check,
	 * but I might want it to be different eventually
	 * 
	 */
/*	public  Entity collisionCheck(ArrayList<? extends Entity> checklist) {
		for(int i=0;i<checklist.size();i++) {
			if(this.aabb.intersects(checklist.get(i).aabb)) {
				//Selective stop method
				int dx = this.xCoord - checklist.get(i).xCoord;
				int dy = this.yCoord - checklist.get(i).yCoord;
				
					if(dx == 0 && dy == 0) {
						dx -= 1;
					}

					if((dy > 0 && velocity.y < 0) || (dy < 0 && velocity.y > 0)) { 
						velocity.y = 0;
					} else 
					if((dx > 0 && velocity.x < 0) || (dx < 0 && velocity.x > 0)) { 
						velocity.x = 0;
					}

				return checklist.get(i);
			}
		}
		return null;
	}
*/	
	
	public void render(int xp, int yp) {
			anim[facing].draw(xp+(int)position.x, yp-(int)position.y);
	}

	
}
