package robertv.GameTest;

import java.util.*;
import java.lang.Math;
import java.lang.reflect.Array;



public class Map {
	/*
	 * shelves is a list of all the Bookshelves on this map.
	 * each Bookshelf object stores coordinates.
	 * when you want a specific shelf, you must loop through shelves
	 * and check if the coordinate you want is that of the Bookshelf.
	 */
	private ArrayList<Bookshelf> shelves;
	private Random generator;
	/*
	 * tiles is essentially a 2D array with data on what type of tile
	 * exists on this map.
	 * '1' is a bookshelf. Either the top or bottom, since bookshelves
	 * are two tiles high.
	 * '0' is the floor. AKA empty.
	 * '2' is a zombie. Zombies spawn here but after that the data is
	 * changed back to 0.
	 */
	private ArrayList<int[]> tiles;
		
	public Map() {
		generator = new Random();
		shelves = new ArrayList<Bookshelf>();
		
		tiles = new ArrayList<int[]>();
		for(int[] f : tiles) {
			f = new int[31];
		}
		
	}
	
	public int height() {
		return tiles.size();
	}
	
	
	/*
	 * row generation is built-in to getRow
	 * when it tries to access a row that doesn't exist
	 * it generates it.
	 * since all accessing methods use getRow(),
	 * this ensures that no null pointer exceptions occur
	 */
	public int[] getRow(int index) {
		if(index < 0) {
			int[] temp = new int[31];
			for(int i = 0; i < 31; i++) {
				temp[i] = -1;
			}
			return temp;
		}
		if(index >= tiles.size()) {
			this.generateRows((index - tiles.size()) + 1);
		}
		return tiles.get(index);
	}
	
	/*
	 * Deprecated?
	 * It was useful before but it's not anymore
	 */
	public String getRowAsBinary(int index) {
		int[] thing = this.getRow(index);
		String s = "";
		for(int i = 0; i < Array.getLength(getRow(index)); i++) {
			s += thing[i];
		}
		return s;
	}
	
	public int getBit(int row, int col) {
		int[] f = getRow(row);
		if(col < 0 || col >= Array.getLength(f)) {
			return -1;
		}
			return f[col];
	}
	
	public int[][] getSurroundings(int row, int col) {
		int[][] surroundings = new int[3][3];
		
		surroundings[0][0] = getBit(row + 1, col - 1);
		surroundings[0][1] = getBit(row, col - 1);
		surroundings[0][2] = getBit(row - 1, col - 1);

		surroundings[1][0] = getBit(row + 1, col);
		surroundings[1][1] = getBit(row, col);
		surroundings[1][2] = getBit(row - 1, col);

		surroundings[2][0] = getBit(row + 1, col + 1);
		surroundings[2][1] = getBit(row, col + 1);
		surroundings[2][2] = getBit(row - 1, col + 1);

		
		return surroundings;
	}
	
	
	/*
	 * getSurroundingShelves searches an area slightly larger
	 * than 3x3, which may or may not be necessary.
	 */
	public ArrayList<Bookshelf> getSurroundingShelves(int row, int col) {
		ArrayList<Bookshelf> returnarray = new ArrayList<Bookshelf>();
		for(int i=0; i < shelves.size(); i++) {
			Bookshelf thisone = shelves.get(i);
			if(
					thisone.isHere(col-2, row-2) ||
					   thisone.isHere(col-2, row-1) ||
					   thisone.isHere(col-2, row) ||
					   thisone.isHere(col-2, row+1) ||

				thisone.isHere(col-1, row-2) ||
			   thisone.isHere(col-1, row-1) ||
			   thisone.isHere(col-1, row) ||
			   thisone.isHere(col-1, row+1) ||
			   
			   thisone.isHere(col, row-2) ||
			   thisone.isHere(col, row-1) ||
			   thisone.isHere(col, row) ||
			   thisone.isHere(col, row+1) ||
			   
			   thisone.isHere(col+1, row-2) ||
			   thisone.isHere(col+1, row-1) ||
			   thisone.isHere(col+1, row) ||
			   thisone.isHere(col+1, row+1) ||
			   
			   thisone.isHere(col+2, row-2) ||
			   thisone.isHere(col+2, row-1) ||
			   thisone.isHere(col+2, row) ||
			   thisone.isHere(col+2, row+1)
			   ) {
				returnarray.add(thisone);
			}
		}
		return returnarray;
	}
	
	public boolean playerNear(int x, int y){
		
		
		
		return false;
	}
	
	/*
	 * similar to get surroundings. xc and yc are coordinates,
	 * relX is the coordinate you want to check.
	 * +1 is to your right, -1 to your left.
	 * etc.
	 * 
	 */
	public Bookshelf getSpecificShelf(int xc, int yc, int relX, int relY) {
		for(int i = 0; i < shelves.size(); i++) {
			if(shelves.get(i).isHere(xc+relX, yc+relY)) {
				return shelves.get(i);
			}
		}
		return null;
	}
	
	/*
	 * generates a random 32-bit int,
	 * then uses the bits as map data.
	 * it generates 3 rows at a time.
	 * the reason for this is because I want a row with no shelves
	 * between all the shelves.
	 * Also because bookshelves are two tiles high,
	 * a row idential to the first has to be added on top.
	 */
	private int generateRows(int num) {
		int times = (int)Math.ceil((float)num / 3.0);
		for(int i = 0; i < times; i++) {
			
			int genInt = generator.nextInt();
			
			//first we add a row of all zeroes
			int[] rowOne = new int[31];
			for(int one = 0; one < 31; one++) {
				rowOne[one] = 0;
				if( tiles.size() > 5 && generator.nextInt(10) == 5) {
					rowOne[one] = 2;
					//one in ten chance of spawning a zombie
				}
			}
			tiles.add(rowOne);
			
			//then we add data based on the random int...
			int[] rowTwo = new int[31];
			for(int two = 30; two >= 0; two--) {
				if((genInt & (1 << two)) == 0) {
					rowTwo[two] = 0;
				} else {
					rowTwo[two] = 1;
					shelves.add(new Bookshelf(two, tiles.size()));
				}
			}
			tiles.add(rowTwo);
			
			//..and we add that row again.
			tiles.add(rowTwo);
			
			
		}
		return tiles.size();
	}
	
	public Bookshelf getShelfHere(int x, int y) {
		for(int i = 0; i < shelves.size(); i++) {
			if(shelves.get(i).isHere(x, y)) {
				return shelves.get(i);
			}
		}
		return null;
	}
	
}
