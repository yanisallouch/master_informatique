import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
	public String nomGraphe;
	public ArrayList<Node> nodes;
	public ArrayList<Edge> edges;
	
	public Graph() {
		this.nomGraphe = "Generique";
	}

	public void createNewGraph(String nomGraphe) {
		this.setNomGraphe(nomGraphe);
		this.createNewGraph();
	}
	
	public void createNewGraph() {
		Scanner input = new Scanner(System.in);
		
		int nbSommets = -1;
		int nbAretes = -1;
		
		while( nbSommets < 0) {
			System.out.print("Indiquer le nombre de sommets > 0 : ");
			nbSommets = input.nextInt();
		}
		this.nodes = new ArrayList<>(nbSommets);
		
		while( nbAretes <= 0) {
			System.out.print("Indiquer le nombre d'aretes > 0 : ");
			nbAretes = input.nextInt();
		}
		this.edges = new ArrayList<>(nbAretes);
		
		this.setNomGraphe(nomGraphe);
		for(int i = 0; i < nbSommets; i++) {	
			input = new Scanner(System.in);
			Node tmpNode = new Node();
			System.out.print("Entrer le label du sommet " + i + " : ");
			tmpNode.setLabel(input.next());
			

			
			
			tmpNode.setShape("rect");
			
			
			nodes.add(tmpNode);	
		}
		
		System.out.println("-----------------");
		System.out.println("Saisi des sommets aux clavier terminer !");
		
		
		for(int i = 0; i < nbAretes; i++) {
			input = new Scanner(System.in);
			Edge tmpEdge = new Edge();

			System.out.println("-----------------");
			System.out.println("Recapitulatif des noeuds : ");
			int k = 0;
			for(Node ittNode : nodes) {
				System.out.println( "\t" + "indice = " + k + ", " + ittNode);
				k++;
			}
			
			System.out.print("Entrer l'indice du SOMMET GAUCHE de l'arete " + i + " : ");
			tmpEdge.setNodeLeft(nodes.get(input.nextInt()));
			System.out.print("Entrer l'indice du SOMMET DROIT de l'arete " + i + " : ");
			tmpEdge.setNodeRight(nodes.get(input.nextInt()));
			

			
			
			tmpEdge.setHead("none");
			
			
			
			edges.add(tmpEdge);
		}
		input.close();
		System.out.println("-----------------");
		System.out.println("Saisi des aretes aux clavier terminer !");
		
		
	}
	
	public String printlnGraphviz() {
		String resultat = "digraph " + this.nomGraphe + " {\t\n";
		
		for(Node ittNode : nodes) {
			resultat += "\t" + ittNode + "\n";
		}
		for(Edge ittEdge : edges) {
			resultat +=  "\t" + ittEdge + "\n";
		}
		
		resultat += "}";
		System.out.print(resultat);
		
		return resultat;
	}
	
	
	
	public String getNomGraphe() {
		return nomGraphe;
	}

	public void setNomGraphe(String nomGraphe) {
		this.nomGraphe = nomGraphe;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "Graph [nodes=" + nodes + ", edges=" + edges + "]";
	}
}
