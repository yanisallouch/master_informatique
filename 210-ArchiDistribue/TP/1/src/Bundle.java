package versionSimple;

import java.io.Serializable;

public interface Bundle extends Serializable {
	
	public void setNom(String nom);
	public void setNomMaitre(String nom);
	public void setEspece(String nom);
	public void setRace(String nom);
	public void setDossierSuivi(String nom);
	public void setEspeceClass(String nom);
	
	public String getNom();
	public String getNomMaitre();
	public String getEspece();
	public String getRace();
	public String getDossierSuivi();
	public String getEspeceClass();

}