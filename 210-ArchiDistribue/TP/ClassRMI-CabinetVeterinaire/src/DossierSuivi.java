

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DossierSuivi extends Remote {
	public String getSuivi() throws RemoteException;
	public void setSuivi(String suivi) throws RemoteException;
}
