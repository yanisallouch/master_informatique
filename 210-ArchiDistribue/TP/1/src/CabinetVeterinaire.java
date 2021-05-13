package versionSimple;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CabinetVeterinaire extends Remote {

	public Animal find(String nomAnimal) throws RemoteException;
	public void add(Bundle bundleInfo) throws RemoteException;
	
}
