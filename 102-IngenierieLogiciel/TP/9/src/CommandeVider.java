
public class CommandeVider extends Commande {
	
	@Override
	public void execute() {
		this.setVolumeDeplacer(this.getBidonSource().getQuantite());
		this.bidonSource.vider();
	}

	@Override
	public void cancel() {
		this.bidonSource.remplir();
		
	}

}
