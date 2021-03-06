package robertv.GameTest;

import java.util.*;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;


/*
 * this is the default class that lets things move
 * and collide with each other. It provides Vectors
 * for movement, bounding boxes, and an Image to 
 * draw to the screen.
 */
public class Entity {
	public int xCoord;
	public int yCoord;
	
	//speed is actually a multiplier
	//so objects can move faster than others
	public double speed;
	//position is separate from coordinates.
	//instead position holds a pixel offset 
	//so that objects can move in increments
	//and not directly from tile to tile.
	//in update, when position exceeds 32,
	//the coordinate is incremented and the position
	//brought back down.
	public Vector position;
	public Vector velocity;
	public Vector acceleration;
	
	public Rectangle aabb;
	
	public Image sprite;
	
	public  void update() {
		if(velocity.x > speed || velocity.x < -speed) {
			velocity.x /= Math.abs(velocity.x);
			velocity.x *= speed;
		}
		if(velocity.y > speed || velocity.y < -speed) {
			velocity.y /= Math.abs(velocity.y);
			velocity.y *= speed;
		}
		position.add(velocity);
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
	
	public boolean isHere(int x, int y) {
		return (x == xCoord && y == yCoord);
	}
	
	public void setSprite(Image i) {
		sprite = i;
	}
	
	public  void render(int xp, int yp) {
		sprite.draw(xp, yp);
	}
	
	public Rectangle makeBoundingBox(int x, int y, int width, int height) {
		aabb.setBounds(x, y, width, height);
		return aabb;
	}
	
/*	public  Entity collisionCheck(ArrayList<? extends Entity> checklist) {
		for(int i=0;i<checklist.size();i++) {
			if(this.aabb.intersects(checklist.get(i).aabb)) {
				if(this == checklist.get(i)) {
					return null;
				}
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

				if(velocity.x > 0 && dx <= dy) {
					position.x -= dx;
				}else
				if(velocity.x < 0 && dx <= dy) {
					position.x += dx;
				}else
				if(velocity.y > 0 && dy <= dx) {
					position.y -= dy;
				}else
				if(velocity.y < 0 && dy <= dx) {
					position.y += dy;
				}				
				return that;
			}
		}
		return null;
	}

	
	public boolean collisionTrue(Entity en, ArrayList<Entity> elist) {
		return false;
	}
}
