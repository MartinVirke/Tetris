package application;

public class RSBlock extends Block{

	public RSBlock(int x, int y, int rot) {
		super(x, y, rot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Block makeCopy() {
		return new RSBlock(this.getX(), this.getY(), this.getRot());
	}

}
