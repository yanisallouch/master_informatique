package helloWorld;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Hello extends Remote{
	String sayHello() throws RemoteException;
	void printHello() throws RemoteException;
}


