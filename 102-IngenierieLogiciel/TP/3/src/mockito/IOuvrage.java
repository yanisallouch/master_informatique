package mockito;

import java.util.ArrayList;

public interface IOuvrage {
	
	public int nbExemplairesDisponible();
	public ArrayList<Pair<String,Integer>> listeDesMediathequesAvecNbOuvrageDisponible();
}