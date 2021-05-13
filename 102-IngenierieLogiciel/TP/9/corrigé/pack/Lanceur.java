package pack;

import java.util.Stack;

/**
 * @author Ahmad BEDJA BOANA
 */

public class Lanceur {
	//invoqueur ou "caller" ou lanceur 
	//reçoit les demandes d'execution de commandes envoyées par le client joueur
	//gère l'historique
	
	protected Stack<ICommande> history;
	
	public boolean hasNextUndo(){
		return (!history.isEmpty());
	}
	
	public Lanceur(){
		this.history = new Stack<ICommande>();
	}
	
	/**
	 * execute la commande c et la sauve dans l'historique
	 * @param c
	 */
	public void pushCommand(ICommande c){
		//gérer les exceptions ...
		c.execute();
		history.push(c);
	}
	
	/**
	 * annule la commande la plus recente et la retire de l'historique
	 */
	public void popLastCommand(){
		history.lastElement().undo();
		history.pop();
	}
	

}
