/**
*	@author	Guilhem	Blanchard
*	@author	Yanis	Allouch
*	@date	25 octobre 2020
*	@attribute HMIN104
*/

package colorGrap;

import java.util.ArrayList;

public class Graph {
	ArrayList<Sommet> sommets = new ArrayList<>();
	ArrayList<Arete> aretes = new ArrayList<>();
	
	public Graph(ArrayList<Sommet> sommets, ArrayList<Arete> arretes) {
		this.sommets = sommets;
		this.aretes = arretes;
		this.calculeDegre();
	}

	public Graph() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Sommet> getSommets() {
		return sommets;
	}

	public void setSommets(ArrayList<Sommet> sommets) {
		this.sommets = sommets;
	}

	public ArrayList<Arete> getAretes() {
		return aretes;
	}

	public void setAretes(ArrayList<Arete> aretes) {
		this.aretes = aretes;
	}

	void init(int example) {
		switch (example) {
		case 1:
			Sommet v1 = new Sommet("v");
			Sommet x1 = new Sommet("x");
			Sommet y1 = new Sommet("y");
			Sommet z1 = new Sommet("z");
			Sommet u1 = new Sommet("u");
			Sommet t1 = new Sommet("t");
			
			Arete vx1 = new Arete(v1,x1);
			Arete xy1 = new Arete(x1,y1);
			Arete yu1 = new Arete(y1,u1);
			Arete ut1 = new Arete(u1,t1,true);
			Arete tv1 = new Arete(t1,v1);
			Arete vz1 = new Arete(v1,z1);
			Arete ty1 = new Arete(t1,y1);
			Arete ux1 = new Arete(u1,x1);

			ArrayList<Sommet> sommetsG1=new ArrayList<>();
			sommetsG1.add(v1);
			sommetsG1.add(x1);
			sommetsG1.add(y1);
			sommetsG1.add(z1);
			sommetsG1.add(u1);
			sommetsG1.add(t1);
			
			ArrayList<Arete> aretesG1=new ArrayList<>();
			aretesG1.add(vx1);
			aretesG1.add(xy1);
			aretesG1.add(yu1);
			aretesG1.add(ut1);
			aretesG1.add(tv1);
			aretesG1.add(vz1);
			aretesG1.add(ty1);
			aretesG1.add(ux1);
			
			this.setSommets(sommetsG1);
			this.setAretes(aretesG1);
			this.calculeDegre();
			break;
		case 2:
			Sommet x2 = new Sommet("x");
			Sommet y2 = new Sommet("y");
			Sommet z2 = new Sommet("z");
			Sommet t2 = new Sommet("t");
			
			Arete xy2 = new Arete(x2,y2);
			Arete ty2 = new Arete(t2,y2);
			Arete tz2 = new Arete(t2,z2);
			Arete zx2 = new Arete(z2,x2);

			ArrayList<Sommet> sommetsG2=new ArrayList<>();
			sommetsG2.add(x2);
			sommetsG2.add(y2);
			sommetsG2.add(z2);
			sommetsG2.add(t2);
			
			ArrayList<Arete> aretesG2=new ArrayList<>();
			aretesG2.add(xy2);
			aretesG2.add(ty2);
			aretesG2.add(tz2);
			aretesG2.add(zx2);
			
			this.setSommets(sommetsG2);
			this.setAretes(aretesG2);
			this.calculeDegre();
			break;
		case 666: //Peterson avec un bras
			System.out.println("Vous avez invoquer satan ! Advienne que pourra de ce programme !");
			Sommet pa = new Sommet("a");
			Sommet pb = new Sommet("b");
			Sommet pc = new Sommet("c");
			Sommet pd = new Sommet("d");
			Sommet pe = new Sommet("e");
			Sommet pf = new Sommet("f");
			Sommet pg = new Sommet("g");
			Sommet ph = new Sommet("h");
			Sommet pi = new Sommet("i");
			Sommet pj = new Sommet("j");
			Sommet pz = new Sommet("z");
			
			Arete ab = new Arete(pa,pb);
			Arete bc = new Arete(pb,pc);
			Arete cd = new Arete(pc,pd);
			Arete de = new Arete(pd,pe);
			Arete ea = new Arete(pe,pa); //pentagone ext
			Arete fh = new Arete(pf,ph);
			Arete hj = new Arete(ph,pj);
			Arete jg = new Arete(pj,pg);
			Arete gi = new Arete(pg,pi);
			Arete fi = new Arete(pf,pi);//pentacle int
			Arete af = new Arete(pa,pf);
			Arete gb = new Arete(pg,pb);
			Arete hc = new Arete(ph,pc);
			Arete id = new Arete(pi,pd);
			Arete ej = new Arete(pe,pj);//lien ext int
			Arete az = new Arete(pa,pz);//bras

			ArrayList<Sommet> sommetsP=new ArrayList<>();
			sommetsP.add(pa);
			sommetsP.add(pb);
			sommetsP.add(pc);
			sommetsP.add(pd);
			sommetsP.add(pe);
			sommetsP.add(pf);
			sommetsP.add(pg);
			sommetsP.add(ph);
			sommetsP.add(pi);
			sommetsP.add(pj);
			sommetsP.add(pz);

			ArrayList<Arete> aretesP=new ArrayList<>();
			aretesP.add(ab);
			aretesP.add(bc);
			aretesP.add(cd);
			aretesP.add(de);
			aretesP.add(ea);
			aretesP.add(fh);
			aretesP.add(hj);
			aretesP.add(jg);
			aretesP.add(gi);
			aretesP.add(fi);
			aretesP.add(af);
			aretesP.add(gb);
			aretesP.add(hc);
			aretesP.add(id);
			aretesP.add(ej);
			aretesP.add(az);
			
			
			this.setSommets(sommetsP);
			this.setAretes(aretesP);
			this.calculeDegre();
			break;
		default:
			throw new RuntimeException("Numero d'exemple de graph inconnue");
		}
		
	}
	
	void calculeDegre() {
		int cpt;
		for(Sommet ittSmt : sommets) {
			cpt=0;
			for(Arete ittArete: aretes) {
				if(ittArete.contient(ittSmt) && ittArete.isPreference() == false) {
					cpt++;
				}
			}
			ittSmt.setDegre(cpt);
		}
	}
	
	public int degreEffectif(Sommet smt) {
		int cpt = 0;
		for(Arete ittArete : aretes) {
			if(ittArete.contient(smt) && ittArete.isPreference() == false) {
				cpt++;
			}
		}
		return cpt;
	}

	boolean resteSommetAColorier() {
		for(Sommet ittSmt: sommets) {
			if (ittSmt.getCouleur()==0) {
				return true;
			}
		}
		return false;
    }
	
	boolean resteSommetADesactiver() {
		for(Sommet ittSmt : sommets) {
			if(ittSmt.isDesactiver()) {
				return true;
			}
		}
		return false;
	}

	Sommet getSommetDegreMin(int k) {
		int degMin = sommets.get(0).getDegre();
		Sommet smtMin = null;
		for(Sommet ittSmt : sommets) {
			int ittDeg = ittSmt.getDegre();
			if(degMin > ittDeg && ittDeg < k) {
				degMin = ittDeg;
				smtMin = ittSmt;
			}
		}
		return smtMin;
	}
	
	Sommet getSommetDegreMax(int k) {
		this.calculeDegre();
		int degMax = 0;
		Sommet smtMax = null;
		for (Sommet ittSmt : sommets) {
			int ittDeg = ittSmt.getDegre();
			if(degMax < ittDeg ) {
				degMax = ittDeg;
				smtMax = ittSmt;
			}
		}
		return smtMax;
	}
	
	Graph retirerSommet(Sommet smt) {

		Graph graphRes = new Graph();
		
		ArrayList<Sommet> sommetsRes = new ArrayList<>();
		for(Sommet ittSmt : sommets) {
			if(ittSmt.equals(smt) == false) {
				sommetsRes.add(ittSmt);
			}
		}
		graphRes.setSommets(sommetsRes);
		
		ArrayList<Arete> areteRes = new ArrayList<>();
		for(Arete ittArete: aretes) {
			if(ittArete.contient(smt) == false) {
				areteRes.add(ittArete);
			}
		}
		graphRes.setAretes(areteRes);
		
		return graphRes; // sous graphe priver de smt du graph courant
	}
	
	public Sommet sommetTrivialementColorable(int k) {
		for(Sommet ittSmt : sommets) {
			if(degreEffectif(ittSmt) < k) {
				return ittSmt;
			}
		}
		return null;
	}
	
	public int couleurTrivial(int k, Sommet smt) {
		Sommet autreSommet = null;
		boolean couleurDispo[] = new boolean[k+1];
		for(int i = 0; i < k+1; i++) {
			//initialisation
			couleurDispo[i] = true;
		}
		for(Arete ittArete: aretes) {
			//on cherche une arete voisine au smt, 
			if(ittArete.contient(smt)) {
				autreSommet = ittArete.autreExtremiterDe(smt);
				if (autreSommet == null) {
					throw new RuntimeException("J'ai rencontrer une erreur innatendu : je n'ai pas reussi a obtenir l'extremiter du sommet "+ ittArete);
				}
				for(int col=1; col<=k; col++) {
					//et on false la couleur prise par l'autre sommet voisin au smt
					if(autreSommet.getCouleur() == col && ittArete.isPreference() == false){
						couleurDispo[col] = false;
					}
				}
			}						
		}
		for(int i = 1; i < k+1 ; i++){
			if(couleurDispo[i] ) {
				return i; // premiere couleur disponible retourner
			}
		}
		return -1;
	}
	
	public ArrayList<Integer> couleursUtiliserTrier(int k){
		ArrayList<Integer> couleursUtiliser = new ArrayList<>();
		int min = 1;
		for(int i = 0; i < sommets.size(); i++) {
			if(sommets.get(i).getCouleur() == min) {
				couleursUtiliser.add(min);
				min++;
				i=-1;// on recommence la boucle --> goto
			}
		}
		return couleursUtiliser;
	}
	
	public void colorier(int k) {
		if(resteSommetAColorier()) {
			Sommet smtAColorier = sommetTrivialementColorable(k); 
			if (smtAColorier != null) { // si il existe un sommet smt trivialement colorable
				Graph graphRes = retirerSommet(smtAColorier); 
				graphRes.colorier(k);
				// colorier(k) prive de smt
				for(Sommet ittSmtGraphRes : graphRes.sommets) {
					for(Sommet ittSmt : sommets) {
						if(ittSmt.memeEtiquette(ittSmtGraphRes)) {
							ittSmt.setCouleur(ittSmtGraphRes.getCouleur());
						}
					}
				}//prendre les couleurs de graphRes et les mettre sur this
				
				smtAColorier.setCouleur(couleurTrivial(k, smtAColorier));
				// attribuer une couleur disponible à s
			}else { 
				smtAColorier = getSommetDegreMax(k);
				if (smtAColorier == null) {
					throw new RuntimeException("Situation innatendu : je n'ai pas reussi a obtenir un sommet trivial\n ni a spill (degrer maximum), peut-être que vous avez choisi un k == 0 ?");
				}else {
					// sinon s'il existe un sommet smt
					Graph graphRes = retirerSommet(smtAColorier); 
					graphRes.colorier(k);
					// colorier(k) prive de smt
					for(Sommet ittSmtGraphRes : graphRes.sommets) {
						for(Sommet ittSmt : sommets) {
							if(ittSmt.memeEtiquette(ittSmtGraphRes)) {
								ittSmt.setCouleur(ittSmtGraphRes.getCouleur());
							}
						}
					}//prendre les couleurs de graphRes et les mettre sur this
					
					smtAColorier.setCouleur(couleurTrivial(k, smtAColorier));
					// spiller(smt)
//					int couleurOptimiste = -1;
//					
//					for(Arete ittArete : aretes) {
//						if(ittArete.contient(smtAColorier)) {
//							
//						}
//					}
					// VERIFIER LES VOISINS SI ON POURRAIT REUTILISER UNE COULEUR PRECDENTE
					// VERSION OPTIMISTE
				}
			}
		}
	}
				
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Graph other = (Graph) obj;
		if (aretes == null) {
			if (other.aretes != null)
				return false;
		} else if (!aretes.equals(other.aretes))
			return false;
		if (sommets == null) {
			if (other.sommets != null)
				return false;
		} else if (!sommets.equals(other.sommets))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String res = "";
		for(Sommet ittSmt : sommets) {
			res += ittSmt;
			for(Arete ittArete : aretes) {
				if(ittArete.position(ittSmt) == 1) {
					res +=  "\t" + ittArete;
				}
			}
			res += "\n";
		}
		return res;
	}	
}