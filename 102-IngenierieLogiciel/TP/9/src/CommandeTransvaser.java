
public class CommandeTransvaser extends Commande {
	public Bidon bidonDestination;
	
	public CommandeTransvaser(Bidon bidonDestination) {
		super();
		this.bidonDestination = bidonDestination;
	}

	public Bidon getBidonDestination() {
		return bidonDestination;
	}

	public void setBidonDestination(Bidon bidonDestination) {
		this.bidonDestination = bidonDestination;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

}
