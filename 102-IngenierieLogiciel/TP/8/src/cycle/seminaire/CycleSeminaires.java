package cycle.seminaire;

import cycle.seminaire.etat.abstrait.EtatSeminaire;
import cycle.seminaire.etat.concret.EtatAbandonne;
import cycle.seminaire.etat.concret.EtatAnnule;
import cycle.seminaire.etat.concret.EtatArchive;
import cycle.seminaire.etat.concret.EtatComplet;
import cycle.seminaire.etat.concret.EtatEnCours;
import cycle.seminaire.etat.concret.EtatPlacesDisponibles;
import cycle.seminaire.etat.concret.EtatPlanification;
import cycle.seminaire.etat.concret.EtatPreparationSeminaire;
import cycle.seminaire.etat.concret.EtatTerminer;

public class CycleSeminaires {
	public EtatSeminaire etatCourant;
	public EtatSeminaire[] etatsCourants = new EtatSeminaire[9];
	public String titre;
	public String resume;
	public int nbInscrits;
	public int capacite;
	
	public CycleSeminaires(String titre, String resume, int nbInscrits, int capacite) {
		// Cadre annulable
		this.etatsCourants[0] = new EtatPlanification();
		this.etatsCourants[1] = new EtatPlacesDisponibles();
		this.etatsCourants[2] = new EtatComplet();
		this.etatsCourants[3] = new EtatPreparationSeminaire();
		// Cadre annuler
		this.etatsCourants[4] = new EtatEnCours();
		this.etatsCourants[5] = new EtatTerminer();
		this.etatsCourants[6] = new EtatAbandonne();
		this.etatsCourants[8] = new EtatAnnule();
		// Hors cadre
		this.etatsCourants[7] = new EtatArchive();
		
		this.etatCourant = etatsCourants[0];
		this.titre = titre;
		this.resume = resume;
		this.nbInscrits = nbInscrits;
		this.capacite = capacite;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public int getNbInscrits() {
		return nbInscrits;
	}

	public void setNbInscrits(int nbInscrits) {
		this.nbInscrits = nbInscrits;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}
	
	
}