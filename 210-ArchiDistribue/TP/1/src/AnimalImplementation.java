package versionSimple;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AnimalImplementation extends UnicastRemoteObject implements Animal {

	private static final long serialVersionUID = 1L;
	private String nom;
	private String nomDuMaitre;
	private String especeAnimal;
	private String raceAnimal;
	DossierSuivi dossierSuivi;
	private Espece especeClass;

	public AnimalImplementation() throws RemoteException {
		super();
		nom = "nomFoobar";
		nomDuMaitre = "nomMaitreFoobar";
		especeAnimal = "especeFoobar";
		raceAnimal = "raceFoobar";
		dossierSuivi = new DossierSuiviImplementation();
		especeClass = new EspeceImplementation();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom2) {
		this.nom = nom2;
	}
	
	public String getNomDuMaitre() {
		return nomDuMaitre;
	}
	
	public void setNomDuMaitre(String nomMaitre) {
		this.nomDuMaitre = nomMaitre;
		
	}	

	public String getNomWithNomDuMaitre() {
		return nom + " appartient a " + nomDuMaitre;
	}
	
	public void setNomWithNomDuMaitre(String nom2, String nomMaitre) {
		this.nom = nom2;
		this.nomDuMaitre = nomMaitre;
	}

	public String getEspece() {
		return especeAnimal;
	}
	
	public void setEspece(String espece) {
		this.especeAnimal = espece;
	}
	
	public String getRace() {
		return raceAnimal;
	}
	
	public void setRace(String race) {
		raceAnimal = race;
	}
	
	public String getEspeceWithRace() {
		return especeAnimal + " de " + raceAnimal;
	}

	public void setEspeceWithRace(String espece, String race) {
		this.especeAnimal = espece;
		this.raceAnimal = race;
	}
	
	public void setDossierSuivi(DossierSuivi dossierSuivi) throws RemoteException {
		this.dossierSuivi = dossierSuivi;
	}

	public DossierSuivi getDossierSuivi() throws RemoteException {
		return dossierSuivi;
	}

	public void setSuivi(String suivi) throws RemoteException {
		dossierSuivi.setSuivi(suivi);
	}

	public String getSuivi() throws RemoteException {
		return dossierSuivi.getSuivi();
	}

	public Espece getEspeceClass()  throws RemoteException {
		return especeClass;
	}
	
	public void setEspeceClass(String espece)  throws RemoteException {
		this.especeClass.setNom(espece);
	}
	
	public Boolean isNom(String nom) {
			return getNom().equals(nom);
	}

}