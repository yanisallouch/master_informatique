package pack;

/**
 * @author ahmad
 * @modified cd
 */

public class ViderBidon extends CommandeBidon {

	private Bidon bid;
	
	public ViderBidon(Bidon b) {
		this.bid = b;
	}
	
	@Override
	public void execute() {
		volDeplace = bid.getVolume();
		bid.vider();

	}

	@Override
	public void undo() {
		bid.setVolume(volDeplace);
	}

}
