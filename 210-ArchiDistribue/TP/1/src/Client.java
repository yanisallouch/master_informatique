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
			
			CabinetVeterinaire stub = (CabinetVeterinaire) registry.lookup("Cabinet");
			
			Bundle animalInfo = new BundleImplementation();
			animalInfo.setDossierSuivi("this is absolutely not a tracking file");
			animalInfo.setEspece("Chien");
			animalInfo.setEspeceClass("ChienEspece");
			animalInfo.setNom("Leo");
			animalInfo.setNomMaitre("Martin");
			animalInfo.setRace("Labrador");
			
			stub.add(animalInfo);
			Animal stubAnimal  = (Animal) stub.find("Leo");
			System.out.println(stubAnimal.getNom());
			
			
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}
}