

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Animal  extends Remote {
	
	public String getNom() throws RemoteException;
	public void setNom(String nom2) throws RemoteException;
	
	public String getNomDuMaitre() throws RemoteException;
	public void setNomDuMaitre(String nomMaitre) throws RemoteException;

	public String getNomWithNomDuMaitre() throws RemoteException;
	public void setNomWithNomDuMaitre(String nom2, String nomMaitre) throws RemoteException;

	public String getEspece() throws RemoteException;
	public void setEspece(String espece) throws RemoteException;
	
	public String getRace() throws RemoteException;
	public void setRace(String race) throws RemoteException;
	
	public String getEspeceWithRace() throws RemoteException;
	public void setEspeceWithRace(String espece, String race) throws RemoteException;

	public DossierSuivi getDossierSuivi() throws RemoteException;
	public void setDossierSuivi(DossierSuivi dossierSuivi) throws RemoteException;

	public String getSuivi() throws RemoteException;
	public void setSuivi(String suivi) throws RemoteException;

	public Espece getEspeceClass() throws RemoteException;
	public void setEspeceClass(String espece) throws RemoteException;
	
	public Boolean isNom(String nom) throws RemoteException;
	
}
