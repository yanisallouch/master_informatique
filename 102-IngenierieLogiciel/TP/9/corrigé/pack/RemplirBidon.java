package pack;

/**
 * 
 * @author ahmad
 *
 */
public class RemplirBidon extends CommandeBidon {
	//ConcreteCommand

	private Bidon b;
	//receiver dans la description du pattern
	
	public RemplirBidon(Bidon b){
		this.b = b;
	}
	
	@Override
	public void execute() {
		//pour préparer un éventuel undo
		volDeplace = b.getVolumeMax() - b.getVolume();
		//exécution de la commande proprement dite
		b.remplir();
	}

	@Override
	public void undo() {
		//question ouverte pour plus tard ... 
		//undo pourrait-il être une commande ? peut-on "undo" un undo?
		
		b.setVolume(b.getVolume() - volDeplace);
	}

}
