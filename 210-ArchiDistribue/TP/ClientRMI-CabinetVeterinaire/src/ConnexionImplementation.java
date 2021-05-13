import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConnexionImplementation extends UnicastRemoteObject implements Connexion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1257631648384174585L;

	protected ConnexionImplementation() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	String username;
	
	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public void alert(int n) {
		System.out.println("We have registered " + n + " clients");
	}

}