
public abstract class Commande implements ICommande {
	public int volumeDeplacer;
	public Bidon bidonSource;
	
	public Commande() {
		super();
	}

	public Commande(int volumeDeplacer) {
		super();
		this.volumeDeplacer = volumeDeplacer;
	}

	public Commande(Bidon bidonSource) {
		super();
		this.bidonSource = bidonSource;
	}

	public Commande(int volumeDeplacer, Bidon bidonSource) {
		super();
		this.volumeDeplacer = volumeDeplacer;
		this.bidonSource = bidonSource;
	}

	public Bidon getBidonSource() {
		return bidonSource;
	}

	public void setBidonSource(Bidon bidonSource) {
		this.bidonSource = bidonSource;
	}

	public int getVolumeDeplacer() {
		return volumeDeplacer;
	}

	public void setVolumeDeplacer(int volumeDeplacer) {
		this.volumeDeplacer = volumeDeplacer;
	}

}
