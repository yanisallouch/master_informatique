import java.util.Scanner;	
import java.util.Stack;

public class Client {
	Stack<ICommande> listesCommandes;
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// boucle interactive
		Scanner sc = new Scanner(System.in);
		int b1;
		int b2;
		int action;
		int qte;
		int flagContinue = 1;
		while (flagContinue == 1) {
			System.out.println("remplir:0 vider:1 transvaser:2 undo:3 quitter:-1 ?");
			action = sc.nextInt();
			switch (action) {
			case 0: {
				// TODO les commandes execute + lanceur dans lequel je push les "commandes"
				System.out.println("bidon ?");
				b1 = sc.nextInt();
				qte = sc.nextInt();
				break;
			}
			case 1: {
				// TODO
				System.out.println("bidon ?");
				b1 = sc.nextInt();
				break;
			}
			case 2: {
				// TODO
				System.out.println("indice bidon 1 ?");
				b1 = sc.nextInt();
				System.out.println("indice bidon 2 ?");
				b2 = sc.nextInt();
				break;
			}
			case 3: {
				// TODO
				System.out.println("undo dernière action");
				break;
			}
			case -1: {
				flagContinue = -1;
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + action);
			}
		}
		sc.close();
	}
}
