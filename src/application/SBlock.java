package application;

public class SBlock extends Block{

	public SBlock(int x, int y, int rot) {
		super(x, y, rot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Block makeCopy() {
		return new SBlock(this.getX(), this.getY(), this.getRot());
	}

}
