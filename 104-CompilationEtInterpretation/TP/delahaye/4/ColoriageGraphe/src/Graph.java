import java.util.ArrayList;

public class Graph {
	ArrayList<Sommet> listesSommets = new ArrayList<>(5);
	ArrayList<Arete> listesAretes = new ArrayList<>();
	private int couleur = 0;
	// l'entier correspond a l'ordre de desactivation
	ArrayList<Pair<Sommet, Integer>> listesSommetsDesactive = new ArrayList<Pair<Sommet, Integer>>(listesSommets.size());
	
	public Graph(ArrayList<Sommet> listesSommets, ArrayList<Arete> listesAretes) {
		super();
		this.listesSommets = listesSommets;
		this.listesAretes = listesAretes;
		for(Sommet elem : listesSommets) {
			listesSommetsDesactive.add(new Pair<Sommet, Integer>(elem, -1));
		}
	}

	public Graph() {
		for(Sommet elem : listesSommets) {
			this.listesSommetsDesactive.add(new Pair<Sommet, Integer>(elem, -1));
		}
	}

	public ArrayList<Sommet> getlistesSommets() {
		return listesSommets;
	}

	public void listesSommets(ArrayList<Sommet> listesSommets) {
		this.listesSommets = listesSommets;
	}

	public ArrayList<Arete> getListesAretes() {
		return listesAretes;
	}

	public void setListesAretes(ArrayList<Arete> listesAretes) {
		this.listesAretes = listesAretes;
	}	

	public void initGraphExemple1() {
		ArrayList<Integer> integerToSommet = new ArrayList<>(6);
		integerToSommet.add((int)'T'); // 0
		integerToSommet.add((int)'U'); // 1
		integerToSommet.add((int)'V'); // 2
		integerToSommet.add((int)'X'); // 3
		integerToSommet.add((int)'Y'); // 4
		integerToSommet.add((int)'Z'); // 5
		for(int elem : integerToSommet) {
			listesSommets.add(new Sommet(""+(char)elem));
		}
		listesAretes.add(new Arete(listesSommets.get(0), listesSommets.get(1), true));
		listesAretes.add(new Arete(listesSommets.get(0), listesSommets.get(4)));
		listesAretes.add(new Arete(listesSommets.get(0), listesSommets.get(2)));
		listesAretes.add(new Arete(listesSommets.get(1), listesSommets.get(0), true));
		listesAretes.add(new Arete(listesSommets.get(1), listesSommets.get(4)));
		listesAretes.add(new Arete(listesSommets.get(1), listesSommets.get(3)));
		listesAretes.add(new Arete(listesSommets.get(3), listesSommets.get(2)));
		listesAretes.add(new Arete(listesSommets.get(3), listesSommets.get(4)));
		listesAretes.add(new Arete(listesSommets.get(3), listesSommets.get(1)));
		listesAretes.add(new Arete(listesSommets.get(2), listesSommets.get(0)));
		listesAretes.add(new Arete(listesSommets.get(2), listesSommets.get(3)));
		listesAretes.add(new Arete(listesSommets.get(2), listesSommets.get(5)));
		listesAretes.add(new Arete(listesSommets.get(4), listesSommets.get(0)));
		listesAretes.add(new Arete(listesSommets.get(4), listesSommets.get(3)));
		listesAretes.add(new Arete(listesSommets.get(4), listesSommets.get(1)));
		listesAretes.add(new Arete(listesSommets.get(5), listesSommets.get(2)));
		for(Sommet elem : listesSommets) {
			this.listesSommetsDesactive.add(new Pair<Sommet, Integer>(elem, -1));
		}
		
	}

	public void initGraphExemple2() {
		ArrayList<Integer> integerToSommet = new ArrayList<>(6);
		integerToSommet.add((int)'T'); // 0
		integerToSommet.add((int)'X'); // 1
		integerToSommet.add((int)'Y'); // 2
		integerToSommet.add((int)'Z'); // 3
		for(int elem : integerToSommet) {
			listesSommets.add(new Sommet(""+(char)elem));
		}
		listesAretes.add(new Arete(listesSommets.get(0), listesSommets.get(2)));
		listesAretes.add(new Arete(listesSommets.get(0), listesSommets.get(3)));
		listesAretes.add(new Arete(listesSommets.get(2), listesSommets.get(0)));
		listesAretes.add(new Arete(listesSommets.get(2), listesSommets.get(1)));
		listesAretes.add(new Arete(listesSommets.get(1), listesSommets.get(2)));
		listesAretes.add(new Arete(listesSommets.get(1), listesSommets.get(3)));
		listesAretes.add(new Arete(listesSommets.get(3), listesSommets.get(0)));
		listesAretes.add(new Arete(listesSommets.get(3), listesSommets.get(1)));
		for(Sommet elem : listesSommets) {
			this.listesSommetsDesactive.add(new Pair<Sommet, Integer>(elem, -1));
		}
	}
	
	public int deg(Sommet smt) {
		int count = 0;
		for(int i=0; i < listesAretes.size() ; i++) {
			Arete tmp = listesAretes.get(i);
			if (tmp.elem1.equals(smt) && !(tmp.isPreference()) && isNotDesactiver(tmp.elem2)) {
				count++;
			}
		}
		return count;
	}
	
	public boolean isOneHigherDegPresent() {
		ArrayList<Integer> nbDeg = new ArrayList<>(listesSommets.size()); // stocker le nombre de deg pour chaque sommet

		for(Sommet elem : listesSommets) {
			if(isNotDesactiver(elem)) {
				nbDeg.add(deg(elem));
			}
		}

		int count = 0; // count le nb de degre equivalent
		int max = nbDeg.get(0);

		for(Integer i : nbDeg) {
			if(max < i) {
				max = i;
				count = 1;
			}else if (i == max) {
				count++;
			}
		}

		return count>=1;
	}
	
	private boolean isDesactiver(Sommet smt) {
		boolean isDesactiver = false;
		for(Pair<?,?> elem : listesSommetsDesactive) {
			if(elem.sommet.equals(smt)) {
				isDesactiver = !elem.entier.equals(-1);
				return isDesactiver;
			}
		}
		return isDesactiver; // default
	}
	
	private boolean isNotDesactiver(Sommet smt) {
		return !isDesactiver(smt);
	}

	public Sommet getSommetWithHigherDeg() {
		if(isOneHigherDegPresent()) {
			ArrayList<Integer> nbDeg = new ArrayList<>(listesSommets.size());
			for(Sommet elem : listesSommets) {
				if(isNotDesactiver(elem)) {
					nbDeg.add(deg(elem));	
				}else {
					nbDeg.add(0);
				}
			}
			int max = nbDeg.get(0);
			int indice = 0;
			
			for(int i = 0; i < nbDeg.size(); i++) {
				if(max < nbDeg.get(i)) {
					max = nbDeg.get(i);
					indice = i;
				}
			}
			return listesSommets.get(indice);
		}else {
			return null;
		}
	}
	
	public Sommet getSommetWithDegLessThan(int k) {
		for (Sommet elem : listesSommets) {
			if(isNotDesactiver(elem)) {
				if(deg(elem) < k) {
					return elem;
				}
			}
		}
		return null;
	}
	
	public boolean isNonActif(Pair<?,?> elem) {
		return !elem.getEntier().equals(-1);
	}
	
	public int nbSommetNonActif() {
		int count = 0;
		for(Pair<?, ?> e : listesSommetsDesactive) {
			if(isNonActif(e)) {
				count++;
			}
		}	
		return count;
	}
	
	public int nbSommetActif() {
		return Math.abs(listesSommets.size() - nbSommetNonActif());
	}
	
	public void desactiverSommets(int k, int ordre) {
		if(nbSommetActif() > 0 && k > 0) {
			Sommet aDesactiver = getSommetWithDegLessThan(k);
			if(aDesactiver != null) {
				System.out.println("J'ai trouver le sommet " + aDesactiver.getLabel() + " ayant un degre " + deg(aDesactiver) + " inferieur a  K = " + k );
			}else {
				System.out.println("Il n'y a pas de petit degre inferieur a K = " + k);
				aDesactiver = getSommetWithHigherDeg();
				if(aDesactiver != null) {
					System.out.println("J'ai trouve le sommet " + aDesactiver.getLabel() + " ayant un degre superieur a  K = " + k);
				}else {
					System.out.println("Il y a un bug dans la matrice");
				}
			}
			int indice = 0;
			for(int i = 0; i < listesSommetsDesactive.size(); i++) {
				if(listesSommetsDesactive.get(i).getSommet().equals(aDesactiver)) {
					indice = i;
					break;
				}
			}
			// petit test au cas ou la liste est vide 
			if(listesSommetsDesactive.size() > 0) {
				listesSommetsDesactive.set(indice, new Pair<Sommet,Integer>(aDesactiver, ordre));
			}else {
				listesSommetsDesactive.add(indice, new Pair<Sommet,Integer>(aDesactiver, ordre));
			}
			desactiverSommets(k, ordre+1);
		}else {
			System.out.println("Il n'y a plus de sommet a traiter !");
		}
	}
	
	public int getIndiceFromOrdreDesactiver(int ordre) {
		/*
		 * <ordre> doit correspondre a l'ordre de desactivation dans la liste
		 * des sommets desactiver. La fonction retourne l'indice du sommet dans 
		 * la liste des sommets associer a l'entier passer en argument.
 		 * 
 		 * Exemple si je passe 4, je rends l'indice du sommet qui a ete retirer du graphe
 		 * a la quatrieme iteration lors de l'application de l'algorithme.
 		 * 
 		 * En cas d'echec, -1 est retourner.
 		 * 
		 */
		
		for(int i=0; i < listesSommets.size(); i++) {
			if(listesSommetsDesactive.get(i).getEntier().equals(ordre)) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	public ArrayList<Sommet> getListeSommetVoisin(Sommet smt){
		ArrayList<Sommet> listeVoisin = new ArrayList<>();
		for (Arete a1 : listesAretes) {
			if(a1.getElem1().equals(smt)) { // si l'arete correspond a celle du smt passe en param
				if(isNotDesactiver(a1.getElem2())) {
					listeVoisin.add(a1.getElem2()); // j'ajoute le sommet a son extremiter dans la liste des voisins du smt
				}
			}
		}
		return listeVoisin;
	}
	
	public void coloriageSommets() {
		if(nbSommetNonActif() > 0) {
			Sommet aTraiter = null;
			System.out.println("Je sais qu'il reste au moins un sommet a colorier.");
			int indice = 0;
			for(int i = listesSommets.size() - 1; i >= 0 ; i--) {
				indice = getIndiceFromOrdreDesactiver(i);
				if(indice != -1) {
					if(isDesactiver(listesSommets.get(indice))) {
						aTraiter =  listesSommets.get(indice);

						couleurReset();

						aTraiter.setCouleur(this.getCouleur());

						ArrayList<Sommet> lesVoisinsCourant = getListeSommetVoisin(aTraiter);
						Sommet tmp = null;
						int j = 0;
						for(j = 0; j < lesVoisinsCourant.size(); j++) {
							tmp = lesVoisinsCourant.get(j);
							if(tmp.getCouleur() == aTraiter.getCouleur()) {
								couleurSuivante();
								aTraiter.setCouleur(this.getCouleur());
								j=-1; 
								// je dois reverifier que mon indice ne rentre pas en colision avec un autre sommet,
								// ca ne boucle pas l'infini parce que la couleur ce fait incrementer constamment,
								// dans le pire des cas, jusqu'a arriver a un etat stable et on pourra sortir de la boucle.
							}
						}

						listesSommets.set(indice, aTraiter); 
						System.out.println("J'ai trouver le sommet " + aTraiter.getLabel());
						System.out.println("Je lui ai assigne la couleur : " + aTraiter.getCouleur());
						break;
					}
				}
			}
//			for(int i =0; i < listesSommetsDesactive.size(); i++) {
//				if(listesSommetsDesactive.get(i).getSommet().equals(aTraiter)) {
//					Pair<Sommet, Integer> tmp = listesSommetsDesactive.get(i);
//					tmp.setEntier(-1);
//					listesSommetsDesactive.set(i, tmp);
//				}
//			}
			/*
			 *  je peux me passer de chercher etant donne que j'utilise une propriete intraseque a ma structure
			 *  qui fait que ma liste des sommets desactiver a le meme ordre que celle des sommets enregistrer 
			 *  dans mon graphe.
			 */
			Pair<Sommet, Integer> tmp = listesSommetsDesactive.get(indice);
			tmp.setEntier(-1); // je le reactive pour pouvoir le compter au tour suivant 
			listesSommetsDesactive.set(indice, tmp); // je l'ecrase dans la liste
			
			
			
			coloriageSommets();
		}else {
			System.out.println("Il n'y a plus de sommet a colorier");
		}
	}

	public ArrayList<Sommet> getListesSommets() {
		return listesSommets;
	}

	public void setListesSommets(ArrayList<Sommet> listesSommets) {
		this.listesSommets = listesSommets;
	}

	public int getCouleur() {
		return couleur;
	}

	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}

	public ArrayList<Pair<Sommet, Integer>> getListesSommetsDesactive() {
		return listesSommetsDesactive;
	}

	public void setListesSommetsDesactive(ArrayList<Pair<Sommet, Integer>> listesSommetsDesactive) {
		this.listesSommetsDesactive = listesSommetsDesactive;
	}

	private void couleurSuivante() {
		this.couleur++;
	}
	
	private void couleurReset() {
		this.couleur = 0;
	}
	
	public void affichageGraphe() {
		for(Sommet e : listesSommets) {
			if( isNotDesactiver(e)) {
				System.out.println("" + e);
				for(Arete e2 : listesAretes) {
					if(e2.getElem1().equals(e)) {
						System.out.println("	" + e2);
					}
				}
			}
		}
	}
	
}
