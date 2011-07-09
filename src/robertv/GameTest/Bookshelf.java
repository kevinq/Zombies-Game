package robertv.GameTest;

import org.newdawn.slick.geom.*;

public class Bookshelf extends Entity {
	public static final int MIN_BOOKS = 5;
	public static final int MAX_BOOKS = 10;
	
	private int numBooks;
	private boolean isEmpty;
		
	public Bookshelf() {
		numBooks = (int)(Math.random() * MAX_BOOKS);
		xCoord = -1;
		yCoord = -1;
		isEmpty = false;
		aabb = new Rectangle(0,0,32,64);
	}
	
	public Bookshelf(int x, int y) {
		numBooks = (int)(Math.random() * MAX_BOOKS);
		xCoord = x;
		yCoord = y;
		isEmpty = false;
		aabb = new Rectangle(0,0,32,64);
	}
	
	public Bookshelf(int x, int y, int nB) {
		if(nB > MAX_BOOKS) {
			numBooks = MAX_BOOKS;
		}else if(nB < MIN_BOOKS && nB > 0) {
			numBooks = MIN_BOOKS;
		}else {
			numBooks = nB;
		}
		xCoord = x;
		yCoord = y;
		if(nB <= 0) {
			nB = 0;
			isEmpty = true;
		} else {
			isEmpty = false;
		}
	}
	
	public boolean takeBook() {
		numBooks--;
		if(numBooks < 0) {
			isEmpty = true;
			return false;
		}else if (numBooks == 0) {
			isEmpty = true;
			return true;
		}
		return true;
	}
	
	
	/* the following method finds the appropriate
	 * sprite to use depending on how full the shelf
	 * is in relation to MAX_BOOKS.
	 * 0 is full, 6 is empty.
	 */
	public static int getAptSprite(int nB) {
		
		double interval = (double)MAX_BOOKS / 7.0;
		
		if(nB==0) {
			return 6;
		}
		
		for(int i = 0; i < 6; i++) {
			if( nB > i*interval && nB <= (i+1)*interval ) {
				return 5 - i;
			}
		}
		
		return 6;
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
		
	public int holds() {
		return numBooks;
	}
		
	
	public void update() {
		if (numBooks == 0) {
			isEmpty = true;
		}
	}
	
}
