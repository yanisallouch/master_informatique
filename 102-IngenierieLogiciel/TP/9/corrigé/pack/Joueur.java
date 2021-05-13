package pack;

//le client, incluant le joueur, est représenté par le main.

public class Joueur {

	//fonction de debug pour verifier l'etat de bidons
	public static String etatBidon(Bidon b1,Bidon b2,Bidon b3){
		return b1.getVolume() + " "+b2.getVolume()+" "+b3.getVolume();
	}
	
	public static void main(String[] args) {
		
		Bidon b3 = new Bidon(3);
		Bidon b4 = new Bidon(4);
		Bidon b6 = new Bidon(6);

		//lanceur == invoker
		Lanceur lanceur = new Lanceur();
		
		//ici la création de la commande est concomittante à son invocation
		//(on crée la commande en même temps qu'on la passe au lanceur
		//ce n'est pas forcément toujours le cas.
		//Il peut y avoir plein de variantes, par exemple on crée les commandes une fois
		//et on les passe ensuite au lanceur quand besoin.
		//c'est d'ailleurs une optimisation possible de cet exercice, si on doit remplir
		//3 fois le bidon b3, est-il utile de créer 3 instances de RemplirBidon ...
		
		lanceur.pushCommand(new RemplirBidon(b3));
		//lc.pushCommand(new RemplirBidon(b4));
		lanceur.pushCommand(new RemplirBidon(b6));
		System.out.println(etatBidon(b3, b4, b6));
		
		lanceur.pushCommand(new TransvaserBidon(b3,b4));
		System.out.println(etatBidon(b3, b4, b6));
		
		lanceur.pushCommand(new TransvaserBidon(b6,b3));
		System.out.println(etatBidon(b3, b4, b6));
		
		lanceur.pushCommand(new TransvaserBidon(b4,b6));
		System.out.println(etatBidon(b3, b4, b6));
		
		lanceur.pushCommand(new ViderBidon(b6));
		System.out.println(etatBidon(b3, b4, b6));
		
		System.out.println("maintenant on annule tout");
		while(lanceur.hasNextUndo()){ 
			lanceur.popLastCommand();
			System.out.println(etatBidon(b3, b4, b6));
		}
		
	}
}
