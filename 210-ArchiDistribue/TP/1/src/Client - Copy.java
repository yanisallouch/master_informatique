package versionSimple;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	private Client() {}

	public static void main(String[] args) {

		String host = (args.length < 1) ? null : args[0];
		
		try {
			Registry registry = LocateRegistry.getRegistry(host);

			String path = "/mnt/e/gitlab.com/master_informatique/210-ArchiDistribue/TD/TP/1/versionSimple/AnimalServer.policy";
			String pathAlt = "E:\\gitlab.com\\master_informatique\\210-ArchiDistribue\\TD\\TP\\1\\versionSimple\\AnimalServer.policy";
			
			System.setProperty( "java.security.policy",pathAlt);
			SecurityManager securityManager = new SecurityManager();
        	System.setSecurityManager(securityManager);
			
			Animal stub = (Animal) registry.lookup("Animal");
			
			stub.setEspeceClass("EspeceN°1");
			Espece stubEspece = (Espece) stub.getEspeceClass();
			System.out.println("espece name : " + stubEspece.getNom() );
			
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}