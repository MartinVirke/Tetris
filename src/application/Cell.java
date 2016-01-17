package application;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class Cell implements Serializable {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	private int x, y, imageId;
	private boolean alive;
	private double r, g, b, a;

	private transient Color bgColor, bgShade;

	public Cell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public void setColors() {
		this.bgColor = new Color(r, g, b, 1.0f);
		this.bgShade = new Color(0.0f, 0.0f, 0.0f, this.a);
	}

	public void setColors(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f - a / 25;
		setColors();
	}

	public boolean isAlive() {
		return alive;
	}

	public Color getBgShade() {
		return bgShade;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getImageId() {
		return imageId;
	}

	public void setBgShade(Color bgShade) {
		this.bgShade = bgShade;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

}
