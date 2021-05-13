package pack;

/**
 * @author ahmad
 */

public class TransvaserBidon extends CommandeBidon {

	protected Bidon source , destination;
	
	public TransvaserBidon(Bidon src,Bidon dst) {
		source = src;
		destination = dst;
	}
	
	@Override
	public void execute() {
		volDeplace = 
				source.transvaser(destination);
	}

	@Override
	public void undo() {
		source.setVolume(
				source.getVolume() + volDeplace);
		destination.setVolume(
				destination.getVolume() - volDeplace);
	}

}
