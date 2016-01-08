package application;

public class TBlock extends Block{

	public TBlock(int x, int y, int rot) {
		super(x, y, rot);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public Block makeCopy() {
		return new TBlock(this.getX(), this.getY(), this.getRot());
	}

}
