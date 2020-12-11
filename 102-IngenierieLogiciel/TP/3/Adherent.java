package tpmocks;

import java.util.GregorianCalendar;

public class Adherent {
private String nom;
private static int nbAdherents=0;
private boolean cotisationPayee=true;
private int derniereAnneeCotisation;

private final int  numero;

public Adherent(String nom){
	nbAdherents++;
	this.nom=nom;
	this.numero=nbAdherents;
	derniereAnneeCotisation=new GregorianCalendar().get(GregorianCalendar.YEAR);
}

public String toString(){
	return nom+" "+numero+" derniereAnneeCotisation="+derniereAnneeCotisation;
}

// méthode appelée à la fin de chaque année. Retourne vrai ssi il faut radier l'abonné
public boolean finAnnee(){
	setCotisationPayee(false);
	if (new GregorianCalendar().get(GregorianCalendar.YEAR)-derniereAnneeCotisation>5){
		System.out.println("il faudrait radier l'abonné");
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
}
