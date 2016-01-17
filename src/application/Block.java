package application;

import java.io.Serializable;

/**
 * This class is an abstract class containing the necessary variables and logic
 * for creating different shapes of blocks.
 * 
 * The class is serializable so that the state of the game can be saved and
 * loaded for later use.
 * 
 * @author Martin Virke
 */

public abstract class Block implements Serializable {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	private int x, y, rot;
	private Color color;

	// Explanation of the pattern system can be found in the LBlock class.
	protected int[][] pattern;

	protected enum Color {
		BLUE, GREEN, LIGHTBLUE, PURPLE, RED, TURQUOISE, YELLOW
	}

	/**
	 * Stores the parameters in the new instance.
	 * 
	 * @param x X position of the new block.
	 * @param y Y position of the new block.
	 * @param rot Rotation of the new block.
	 * @param color The color of the new block.
	 */
	
	public Block(int x, int y, int rot, Color color) {
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.color = color;
	}
	
	/**
	 * Abstract method needed for copying the blocks.
	 * 
	 * @return returns the new copy.
	 */

	public abstract Block makeCopy();

	/**
	 * Decrements the rotation of the block, so that the block rotates clockwise.
	 */
	
	public void incRot() {
		this.rot = rot - 1 < 0 ? 3 : rot - 1;
	}

	/**
	 * @return color The color of the block.
	 */
	
	public Color getColor() {
		return color;
	}

	/**
	 * @return x The current x position of the block.
	 */
	
	public int getX() {
		return x;
	}
	
	/**
	 * @return y The current y position of the block.
	 */

	public int getY() {
		return y;
	}
	
	/**
	 * @return rot The current rotation of the block.
	 */

	public int getRot() {
		return this.rot;
	}
	
	/**
	 * Returns rotation incremented by 1, and resets it to 0 when above the given threshold.
	 * 
	 * @return rot The current rotation of the block incremented by 1.
	 */

	public int getYRot() {
		return this.rot + 1 > 3 ? 0 : this.rot + 1;
	}

	/**
	 * @return pattern The pattern for the block.
	 */
	
	public int[][] getPattern() {
		return pattern;
	}

	/**
	 * @param x The desired x position of the block.
	 */
	
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * @param y The desired y position of the block.
	 */

	public void setY(int y) {
		this.y = y;
	}

}
