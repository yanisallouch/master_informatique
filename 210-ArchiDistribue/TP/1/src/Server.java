package versionSimple;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Server {
	
	public Server() {}

	public static void main(String args[]) {

		try {
			
			CabinetVeterinaire objCabinet = new CabinetVeterinaireImplementation();
			Registry registry = LocateRegistry.createRegistry(1099);

			String path = "/mnt/e/gitlab.com/master_informatique/210-ArchiDistribue/TD/TP/1/versionSimple/AnimalServer.policy";
			String pathAlt = "E:\\gitlab.com\\master_informatique\\210-ArchiDistribue\\TD\\TP\\1\\versionSimple\\AnimalServer.policy";
			System.setProperty( "java.security.policy", pathAlt);
			SecurityManager securityManager = new SecurityManager();
        	System.setSecurityManager(securityManager);

			if (registry==null){
				System.err.println("RmiRegistry not found");
			}else{
				registry.bind("Cabinet", objCabinet);
				System.err.println("Server ready");
			}
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}