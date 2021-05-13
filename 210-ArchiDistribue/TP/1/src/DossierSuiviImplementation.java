package versionSimple;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DossierSuiviImplementation extends UnicastRemoteObject implements DossierSuivi {


	/**
	 * 
	 */
	
	public DossierSuiviImplementation() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private static final long serialVersionUID = 2776767144751729888L;
	String suivi = "";
	
	@Override
	public String getSuivi() throws RemoteException {
		return suivi;
	}

	@Override
	public void setSuivi(String suivi) throws RemoteException {
		this.suivi = suivi;
	}
}