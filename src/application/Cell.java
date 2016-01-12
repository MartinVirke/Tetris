package application;

import javafx.scene.paint.Color;

public class Cell {

	private int x, y, imageId;
	private boolean alive;
	private Color bgColor, bgShade;

	public Cell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public void setColors(double r, double g, double b, double a) {
		this.bgColor = new Color(r, g, b, 1.0f);
		this.bgShade = new Color(0.0f, 0.0f, 0.0f, 1.0f - a / 25);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setBgShade(Color bgShade) {
		this.bgShade = bgShade;
	}

	public Color getBgShade() {
		return bgShade;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public Color getBgColor() {
		return bgColor;
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

	public void setColorId(int colorId) {
		this.imageId = colorId;
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

}
