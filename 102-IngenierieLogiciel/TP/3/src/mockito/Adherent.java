package mockito;

import java.util.GregorianCalendar;

public class Adherent {
	
	private static int nbAdherents=0;
	
	private String nom;
	private boolean cotisationPayee = true;
	private int derniereAnneeCotisation;
	
	private final int  numero;
	
	public Adherent(String nom){
		Adherent.nbAdherents++;
		this.nom = nom;
		this.numero = nbAdherents;
		derniereAnneeCotisation = new GregorianCalendar().get(GregorianCalendar.YEAR);
	}
	
	public String toString(){
		return nom+" "+numero+" derniereAnneeCotisation="+derniereAnneeCotisation;
	}
	
	// méthode appelée à la fin de chaque année. Retourne vrai ssi il faut radier l'abonné
	public boolean finAnnee(){
		setCotisationPayee(false);
		System.out.println("derniereAnnée : " + derniereAnneeCotisation);
		if (new GregorianCalendar().get(GregorianCalendar.YEAR)-derniereAnneeCotisation > 5){
			System.out.println("il faut radier l'abonné");
			return true;
		}
		return false;
	}
	
	public void reAdhesion(){
		setCotisationPayee(true);
		derniereAnneeCotisation=new GregorianCalendar().get(GregorianCalendar.YEAR);
	}

	public boolean isCotisationPayee() {
		return cotisationPayee;
	}

	private void setCotisationPayee(boolean cotisationPayee) {
		this.cotisationPayee = cotisationPayee;
	}
	public void setYear(int annee) {
		this.derniereAnneeCotisation = annee;
	}
}
