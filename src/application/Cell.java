package application;

import java.io.Serializable;

import javafx.scene.paint.Color;

/**
 * This class stores the information for the states of each cell on the playing
 * field. It is serialized so that it can be stored for later use.
 * 
 * @author Martin Virke
 */

public class Cell implements Serializable {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	private int x, y, imageId;
	private boolean alive;
	private double r, g, b, a;

	private transient Color bgColor, bgShade;

	/**
	 * @param x
	 *            The x position of the cell.
	 * @param y
	 *            The y position of the cell.
	 */

	public Cell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the color of the cell based on stored variables. Used to easily
	 * create new colors after loading the serialized class from a file.
	 */

	public void setColors() {
		this.bgColor = new Color(r, g, b, 1.0f);
		this.bgShade = new Color(0.0f, 0.0f, 0.0f, this.a);
	}

	/**
	 * Stores the red, green, blue, alpha/opacity for the cell and calls the
	 * overloaded version of itself in order to set the colors with those
	 * values.
	 * 
	 * @param r
	 *            The red value.
	 * @param g
	 *            The green value.
	 * @param b
	 *            The blue value.
	 * @param a
	 *            The alpha/opacity value.
	 */

	public void setColors(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f - a / 25;
		setColors();
	}

	/**
	 * @return alive Whether or not the cell is alive.
	 */
	
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * @return bgShade The shading to cast on the cell.
	 */

	public Color getBgShade() {
		return bgShade;
	}
	
	/**
	 * @return bgColor The background color of the cell.
	 */

	public Color getBgColor() {
		return bgColor;
	}

	/**
	 * @return x The x position of the cell.
	 */
	
	public int getX() {
		return x;
	}
	
	/**
	 * @return y The y position of the cell.
	 */

	public int getY() {
		return y;
	}
	
	/**
	 * Gets the imageId which corresponds to the correct index of the array of images in GameLogic.
	 * 
	 * @return imageId The image ID.
	 */

	public int getImageId() {
		return imageId;
	}
	
	/**
	 * @param bgShade The shading to cast on the cell.
	 */

	public void setBgShade(Color bgShade) {
		this.bgShade = bgShade;
	}
	
	/**
	 * @param bgColor The background color of the cell.
	 */

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	
	/**
	 * @param x The x position of the cell.
	 */

	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * @param y The y position of the cell.
	 */

	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * @param alive Whether the cell is alive or not.
	 */

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * @param imageId Set the id that corresponds to the correct image in the array of images.
	 */
	
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

}
