import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Connexion extends Remote {

	public String getUsername() throws RemoteException;
	public void setUsername(String user) throws RemoteException;
	public void alert(int n) throws RemoteException;
}
