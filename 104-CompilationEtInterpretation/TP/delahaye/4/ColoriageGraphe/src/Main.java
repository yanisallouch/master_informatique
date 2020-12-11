public class Main {

	public static void main(String[] args) {
		int k = 3;
		
		Graph graphExemple1 = new Graph();

		
//		
//		System.out.println("-------------------------");
//		System.out.println("Initialisation de l'exemple 1 du TD avec K = " + k);
//		graphExemple1.initGraphExemple1();
//		System.out.println("Affichage du graphe :");
//		graphExemple1.affichageGraphe();
//		System.out.println("Debut algorithme coloriage sur l'exemple 1");
//		
//		
//		
//		graphExemple1.desactiverSommets(k, 0);
//		graphExemple1.coloriageSommets();
//
//		
//		
//		System.out.println("Affichage du resultat :");
//		graphExemple1.affichageGraphe();
//		System.out.println("-------------------------");
		
		k = 2;
		
		System.out.println("-------------------------");
		System.out.println("Initialisation de l'exemple 1 du TD avec K = " + k);
		graphExemple1 = new Graph(); 
		graphExemple1.initGraphExemple1();
		System.out.println("Affichage du graphe :");
		graphExemple1.affichageGraphe();
		System.out.println("Debut algorithme coloriage sur l'exemple 1");
		
		
		
		graphExemple1.desactiverSommets(k, 0);
		graphExemple1.coloriageSommets();
		
		
		
		System.out.println("Affichage du resultat :");
		graphExemple1.affichageGraphe();
		System.out.println("-------------------------");
		
		
		System.out.println("-------------------------");
		System.out.println("Initialisation de l'exemple 2 du TD avec K = " + k);
		Graph graphExemple2 = new Graph();
		graphExemple2.initGraphExemple2();
		System.out.println("Affichage du graphe :");
		graphExemple2.affichageGraphe();
		System.out.println("Debut algorithme coloriage sur l'exemple 2");
		
		
		
		graphExemple2.desactiverSommets(k, 0);
		graphExemple2.coloriageSommets();
		
		
		
		System.out.println("Affichage du resultat :");
		graphExemple2.affichageGraphe();
		System.out.println("-------------------------");
		
		System.out.println("Fin du programme");
	}
}