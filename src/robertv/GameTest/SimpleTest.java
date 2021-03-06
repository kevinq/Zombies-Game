package robertv.GameTest;

import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Animation;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.*;

public class SimpleTest extends BasicGame {

	
	/*
	 * Data Structures
	 */
	int frame;
	//frame is currently unused
	Map gameSpace;
	ArrayList<Entity> entities;
	//for non Bookshelf entities (Books and Zombies)
	static Player rachel;

	/*    
	 * Sprites and Images
	 */
	Image spritesheet;
	Image floortile;
	Image[] topShelves = new Image[7];
	Image[] bottomShelves = new Image[7];
	Image[] playerSprites = new Image[4];
	Image[] zombieSprites = new Image[4];
	Animation[] wc;
	Animation[] zwc; //zombie's walking animation
	Image GUI; //the ribbon
	
	UnicodeFont ufont; //for printing info to the GUI
	
	UnicodeFont uFont2; // For messages
	
	Input keyboard;
	
	/*
	 * Scoring Variables
	 */
	int maxHeight;
	public static int ZombiesKilled;
	private final int SCORE_VAR_HEIGHT = 672-(4*32)+3;
	
	static GameContainer main_container; // the gamecontainer object
	
    public SimpleTest() {
        super("Zombies Game.");
        frame = 0;
        ZombiesKilled = 0;
    }
    
    @Override
    public void init(GameContainer container) throws SlickException {
    	gameSpace = new Map();
    	entities = new ArrayList<Entity>(); 	
    	rachel = new Player(gameSpace);
    	
    	spritesheet = new Image("/assets/terrain.png");
    	wc = new Animation[4];
    	zwc = new Animation[4];
    	for(int i = 0; i < 4; i++) {
    		wc[i] = new Animation();
    		wc[i].setPingPong(true);
    		zwc[i] = new Animation();
    		zwc[i].setPingPong(true);

    	}
    	int fd = 120; //frame duration
    	
    	
    	/*
    	 * for getting the GUI info to display
    	 */
    	ufont = new UnicodeFont("/assets/mensch.ttf", 22, false, false);
    	ufont.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
    	ufont.addGlyphs("0123456789");
    	ufont.loadGlyphs();
    	
    	//For custom message test
    	uFont2 = new UnicodeFont("/assets/mensch.ttf", 22, false, false);
    	uFont2.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
    	uFont2.addGlyphs("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.'!");
    	uFont2.loadGlyphs();
    	
    	
    	/*
    	 * set up the sprites
    	 */
    	floortile = spritesheet.getSubImage(0, 64, 32, 32);
    	GUI = spritesheet.getSubImage(0,32*12,480,32*3);
    	rachel.bookimg = spritesheet.getSubImage(32*2, 32*2, 32, 32);
    	    	
    	for(int i = 0; i < 7; i++) {
    		topShelves[i] = spritesheet.getSubImage(32*i, 0, 32, 32);
    		bottomShelves[i] = spritesheet.getSubImage(32*i, 32, 32, 32);
    	}
    	for(int i = 0; i < 4; i++) {
    		wc[i].addFrame(spritesheet.getSubImage(32*i,128-32,32,32), fd);
    		wc[i].addFrame(spritesheet.getSubImage(32*i,128,32,32), fd);
    		wc[i].addFrame(spritesheet.getSubImage(32*i,128+32,32,32), fd);
    		
    		zwc[i].addFrame(spritesheet.getSubImage(32*i,224-32,32,32), fd);
    		zwc[i].addFrame(spritesheet.getSubImage(32*i,224,32,32), fd);
    		zwc[i].addFrame(spritesheet.getSubImage(32*i,224+32,32,32), fd);

    		playerSprites[i] = spritesheet.getSubImage(32*i, 128, 32, 32);
    		zombieSprites[i] = spritesheet.getSubImage(32*i, 224, 32, 32);
    	}
    	
    	rachel.setSprites(playerSprites);
    	rachel.setSpeed(3);
    	rachel.setWalkCycle(wc);
    	
    	keyboard = new Input(21*32);
    	
    	maxHeight = 0;

    }

    @Override
    public void update(GameContainer container, int delta)
            throws SlickException {
    	frame++;
    	
    	if((rachel.yCoord+1) > maxHeight) {
    		maxHeight = rachel.yCoord;
    	}
//    	if (rachel.getHealth() == 0){
//    		System.out.println("You suck.");
//    	}
    }
   
    /**
     * This method captures key presses from the keyboard
     */
    @Override
    public void keyPressed(int key, char c) {
    	switch (key) {
    	case Input.KEY_UP : rachel.startMoving(Player.NORTH);
    	break;
    	case Input.KEY_DOWN : rachel.startMoving(Player.SOUTH);
    	break;
    	case Input.KEY_LEFT : rachel.startMoving(Player.EAST);
    	break;
    	case Input.KEY_RIGHT : rachel.startMoving(Player.WEST);
    	break;
    	// 'A' key used for taking a book
    	case Input.KEY_A :
    		Bookshelf check = gameSpace.getSpecificShelf(rachel.xCoord - 1, rachel.yCoord, 0, 1);
    		if(rachel.isFacing(Player.NORTH) && (check != null)) {
    			rachel.takeBook(check);
    		}
    	break;
    	// 'S' key used for throwing book
    	case Input.KEY_S :
    		rachel.fireBook(entities);
    	}
    }
    //*/
    
    @Override
    public void keyReleased(int key, char c) {
    	switch(key) {
    	case Input.KEY_UP:
    		rachel.stopMoving(Player.NORTH);
    		break;
    	case Input.KEY_DOWN:
    		rachel.stopMoving(Player.SOUTH);
    		break;
    	case Input.KEY_LEFT:
    		rachel.stopMoving(Player.EAST);
    		break;
    	case Input.KEY_RIGHT:
    		rachel.stopMoving(Player.WEST);
    		break;
    	case Input.KEY_Q:
    		// for quitting the game 
    		main_container.exit();
    		break;
    	}
    }
    

    @Override
    public void render(GameContainer container, Graphics g)
            throws SlickException {
    	
    	/*
    	 * draws the tiles relative to the player position
    	 * set up bounding boxes appropriately
    	 * based off a screen size of 960x672
    	 * with 32x32 pixel tiles
    	 * showing 15 tiles on either side left/right
    	 * and 10 tiles on either side up/down
    	 * 
    	 */
    	int xP = 960 - (int)rachel.position.x;
    	//x coordinate of top-left corner of current tile
    	int yP = (int)rachel.position.y-16;
    	//y coordinate of top-left corner of current tile
    	
    	for(int i = 11; i >= -10; i--) {
    		
    		int[] curRow = gameSpace.getRow(rachel.yCoord + i);
    		if(curRow == null) {
    			continue;
    		}
    		for(int j = 14; j >= -16; j--) {
    			if((rachel.xCoord + j) < 0 || (rachel.xCoord + j) >= 31) {
    				xP-=32;
    				continue;
    			}
    			if(curRow[rachel.xCoord + j] == 0) {
    				floortile.draw(xP,yP);
    			}
    			if(curRow[rachel.xCoord + j] == 2) {
    				floortile.draw(xP,yP);
    				Zombie zed = new Zombie();
    				zed.spawnZombie(rachel.xCoord+j, rachel.yCoord+i, zombieSprites, zwc);
    				entities.add(zed);
    				curRow[rachel.xCoord+j] = 0;
    			}
    			if(curRow[rachel.xCoord + j] == 1) {
    				if(gameSpace.getRow(rachel.yCoord + i - 1)[rachel.xCoord + j] == 1) {
        				Bookshelf thisShelf = gameSpace.getShelfHere(rachel.xCoord + j, rachel.yCoord + i - 1);
    					topShelves[Bookshelf.getAptSprite(thisShelf.holds())].draw(xP,yP);
    				} else {
        				Bookshelf thisShelf = gameSpace.getShelfHere(rachel.xCoord + j, rachel.yCoord + i);
        				thisShelf.makeBoundingBox(xP, yP-32, 32, 64);
    					bottomShelves[Bookshelf.getAptSprite(thisShelf.holds())].draw(xP,yP);
    				}
    			}
    			
        		//draw non-bookshelf entities
    			for(Entity e : entities) {
    				if(e.isHere(rachel.xCoord + j, rachel.yCoord+i)) {
    					if(e instanceof Zombie) {
    						e.makeBoundingBox(xP+(int)e.position.x+10, (yP-(int)e.position.y)+19,12,11);
    					}else if(e instanceof Book) {
    						e.makeBoundingBox(xP+(int)e.position.x+12, (yP-(int)e.position.y)+10, 11, 15);
    					}
    					e.render(xP, yP);
    				}
    			}
    			
    			
    			
    			xP -= 32;
    		}//ends the inner for loop
    		xP = 960 - (int)rachel.position.x;
    		yP += 32;
    		
    		
    	}//ends the outer for loop
    	    	
    	rachel.update();
    	rachel.render(480, 336);
    	
    	GUI.draw(0,672-(32*5));
    	ufont.drawString(96+21, SCORE_VAR_HEIGHT, "" + rachel.getAmmo());
    	ufont.drawString(96+101, SCORE_VAR_HEIGHT, "" + ZombiesKilled);
    	ufont.drawString(96+180, SCORE_VAR_HEIGHT, "" + rachel.getHealth());
    	ufont.drawString((32*12)+30, SCORE_VAR_HEIGHT, "" + maxHeight);
    	uFont2.drawString(500, SCORE_VAR_HEIGHT, "Don't get hit.");

    	// check player zombie collision
    	ArrayList<Zombie> nearbyZombies = gameSpace.getSurroundingZombie(rachel.yCoord, rachel.xCoord, entities);
    	ArrayList<Bookshelf> nearbyShelves = gameSpace.getSurroundingShelves(rachel.yCoord, rachel.xCoord);
    	ArrayList<Entity> nearbyPlayerStuff = new ArrayList<Entity>();
    	nearbyPlayerStuff.addAll(nearbyZombies);
    	nearbyPlayerStuff.addAll(nearbyShelves);
    	for (Entity e : nearbyPlayerStuff){
    		Entity possibleCollision = rachel.collisionCheck(nearbyPlayerStuff);
    		if (possibleCollision != null){
    			if (rachel.collisionTrue(possibleCollision, nearbyPlayerStuff)){
    				break;
    			}
    		}
    	}
    	//check collisions
    	for(Entity e : entities) {
    		
    		ArrayList<Bookshelf> colliders = gameSpace.getSurroundingShelves(e.yCoord, e.xCoord);
    		ArrayList<Entity> collidersE = nearbyEntities(e.xCoord, e.yCoord);
    		Entity en = e.collisionCheck(colliders);
    		Entity entwo = e.collisionCheck(collidersE);
    		if(en != null) {
    			if(e.collisionTrue(en, entities)) {
    				break;
    			}
    		}
    		if(entwo != null) {
    			if(e.collisionTrue(entwo, entities)) {
    				break;
    			}
    		}
    		if(e.xCoord > 33 || e.xCoord < -1 || e.yCoord < -1 || e.yCoord > (rachel.yCoord + 45)) {
    			entities.remove(e); // went off screen
    			break;
    		}
    		if(e instanceof Zombie) {
    			e.velocity = Vector.getDirectionVector(e.xCoord, e.yCoord, rachel.xCoord, rachel.yCoord);
    		}    		
			e.update();

    	}

    }//end of render method
    
    public ArrayList<Entity> nearbyEntities(int x, int y) {
    	ArrayList<Entity> elsubset = new ArrayList<Entity>();
    	for(Entity e : entities) {
    		if(e.isHere(x,y)) {
    			elsubset.add(e);
    		}
    	}
    	return elsubset;
    }

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new SimpleTest());
            main_container = app;
            app.setShowFPS(false);
            app.setVSync(true);
            app.setDisplayMode(960, 672, false); 
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
