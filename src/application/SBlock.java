package application;

/**
 * Class for creating a S-block.
 * 
 * @author Martin Virke
 */

public class SBlock extends Block{

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
	
	public SBlock(int x, int y, int rot) {
		super(x, y, rot, Color.PURPLE);
		this.pattern = new int[][] { { -1, 0, 0, 1 }, { 1, 1, 0, 0 }, { 1, 0, 0, -1 }, { -1, -1, 0, 0 } };
	}

	// makeCopy() overridar den abstrakta metoden i Block.
	
	/**
	 * Returns a copy of the block.
	 */
	
	@Override
	public Block makeCopy() {
		return new SBlock(this.getX(), this.getY(), this.getRot());
	}

}
