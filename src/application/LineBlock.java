package application;

/**
 * Class for creating a line block.
 * 
 * @author Martin Virke
 */

public class LineBlock extends Block{

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	/**
	 * @param x
	 *            X position of the new block.
	 * @param y
	 *            Y position of the new block.
	 * @param rot
	 *            Rotation of the new block.
	 */
	
	public LineBlock(int x, int y, int rot) {
		super(x, y, rot, Color.LIGHTBLUE);

		this.pattern = new int[][] { { 0, 0, 0, 0 }, { -2, -1, 0, 1 }, { 0, 0, 0, 0 }, { 2, 1, 0, -1 } };
	}

	/**
	 * Returns a copy of the block.
	 */
	
	@Override
	public Block makeCopy() {
		return new LineBlock(this.getX(), this.getY(), this.getRot());
	}

}
