package mockito;

import java.util.ArrayList;

public class Requete {
	public static ArrayList<String> listesMediatheques (IOuvrage ouvrage){
		ArrayList<Pair<String, Integer>> liste = ouvrage.listeDesMediathequesAvecNbOuvrageDisponible();
		ArrayList<String> res = new ArrayList<>();
		for(Pair<String, Integer> elem : liste) {
			if(elem.getValue() > 0) {
				res.add(elem.getKey());
			}
		}
		return res;
	}
	
}
