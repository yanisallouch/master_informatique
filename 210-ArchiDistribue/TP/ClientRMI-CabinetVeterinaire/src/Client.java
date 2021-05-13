

import java.io.File;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	private Client() {}

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		String host = (args.length < 1) ? null : args[0];
		
		try {
			Registry registry = LocateRegistry.getRegistry(1199);

			String unixPath = "absoluteUnixPathToPolicy.policy";
			String windowsPath = "absoluteWindowsPathToPolicy.policy";
			
			System.setProperty( "java.security.policy",windowsPath);
			SecurityManager securityManager = new SecurityManager();
        	System.setSecurityManager(securityManager);

        	String classPath = "aboslutePathToCompiledImplementation/bin";

			// Voici comment j'ai formater mes paths
//			String unixPath = "/mnt/e/gitlab.com/master_informatique/210-ArchiDistribue/TP/ClientRMI-CabinetVeterinaire/src/Client.policy";
//			String windowsPath = "E:\\gitlab.com\\master_informatique\\210-ArchiDistribue\\TP\\ClientRMI-CabinetVeterinaire\\src\\Client.policy";
//			
//		
//			System.setProperty( "java.security.policy",windowsPath);
//			SecurityManager securityManager = new SecurityManager();
//        	System.setSecurityManager(securityManager);
//
//        	String classPath = "E:\\gitlab.com\\master_informatique\\210-ArchiDistribue\\TP\\ServerRMI-CabinetVeterinaire\\bin";

        	
        	URL uri = new File(classPath).toURI().toURL();
        	
        	System.setProperty("java.rmi.server.codebase", uri.toString());

			CabinetVeterinaire stub = (CabinetVeterinaire) registry.lookup("Cabinet");
			
			Connexion client = new ConnexionImplementation();
			client.setUsername("Gregoire");
			
			stub.connexion(client);
			
			Bundle animalInfo = new BundleImplementation();
			animalInfo.setDossierSuivi("this is absolutely not a tracking file");
			animalInfo.setEspece("Chien");
			animalInfo.setEspeceClass("ChienEspece");
			animalInfo.setNom("Leo");
			animalInfo.setNomMaitre("Martin");
			animalInfo.setRace("Labrador");
			
			stub.add(animalInfo);
			Animal stubAnimal  = (Animal) stub.find("Leo");
			System.out.println(stubAnimal.getNom() );
			// output : Leo
			System.out.println("est une espece de "+ stubAnimal.getEspeceClass());
			// output : est une espece de ChienEspece et a pour duree de vie, 0.0
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}