/**
*	@author	Guilhem	Blanchard
*	@author	Yanis	Allouch
*	@date	25 octobre 2020
*	@attribute HMIN104
*/

package colorGrap;
import java.util.Scanner;  // Import the Scanner class

public class Main {

	public static void main(String[] args) {
		Graph graphExemple = new Graph();
		
	    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
	    System.out.print("Choix de k : ");
	    int k = myObj.nextInt();  // Read user input

	    System.out.print("Choix du graphe, 1: pentagone, 2 diament : ");
	    int numeroExemple = myObj.nextInt();  // Read user input
		myObj.close(); //fermeture de scanner
		
		
		System.out.println("-------------------------");
		System.out.println("Initialisation de l'exemple" + numeroExemple + " du TD avec K = " + k);
		graphExemple.init(numeroExemple);
		System.out.println("Affichage du graphe :");
		System.out.println(graphExemple);
		System.out.println("Debut algorithme coloriage sur l'exemple " + numeroExemple);
		graphExemple.colorier(k);
		System.out.println("Affichage du resultat :");
		System.out.println(graphExemple);
		System.out.println("-------------------------");
		
//		System.out.println("-------------------------");
//		System.out.println("Initialisation de l'exemple 1 du TD avec K = " + k);
//		graphExemple.init(1);
//		System.out.println("Affichage du graphe :");
//		System.out.println(graphExemple);
//		System.out.println("Debut algorithme coloriage sur l'exemple 1");
//		graphExemple.colorier(k);
//		System.out.println("Affichage du resultat :");
//		System.out.println(graphExemple);
//		System.out.println("-------------------------");

//		System.out.println("-------------------------");
//		System.out.println("Initialisation de l'exemple 2 du TD avec K = " + k);
//		graphExemple = new Graph();
//		graphExemple.init(2);
//		System.out.println("Affichage du graphe :");
//		System.out.println(graphExemple);
//		System.out.println("Debut algorithme coloriage sur l'exemple 2");
//		graphExemple.colorier(k);
//		System.out.println("Affichage du resultat :");
//		System.out.println(graphExemple);
//		System.out.println("-------------------------");

//		System.out.println("-------------------------");
//		System.out.println("Initialisation de l'exemple ??? du TD avec K = " + k);
//		graphExemple = new Graph();
//		graphExemple.init(666);
//		System.out.println("Affichage du graphe :");
//		System.out.println(graphExemple);
//		System.out.println("Debut algorithme coloriage sur l'exemple ???");
//		graphExemple.colorier(k);
//		System.out.println("Affichage du resultat :");
//		System.out.println(graphExemple);
//		System.out.println("-------------------------");
	}
}