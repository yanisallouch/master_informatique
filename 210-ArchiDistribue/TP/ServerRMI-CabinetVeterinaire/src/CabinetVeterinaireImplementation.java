

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class CabinetVeterinaireImplementation extends UnicastRemoteObject implements CabinetVeterinaire {

	private static final int MILESTONE_TEST = 5;
	private static final int MILESTONE_HIGH = 1000;
	private static final int MILESTONE_MEDIUM = 500;
	private static final int MILESTONE_LOW = 100;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3504199374528969864L;

	private List<Animal> listAnimals;
	private List<Connexion> listClients;
	
	protected CabinetVeterinaireImplementation() throws RemoteException {
		super();
		this.listAnimals = new ArrayList<Animal>();
		this.listClients = new ArrayList<>();
	}

	@Override
	public Animal find(String nomAnimal) throws RemoteException {
		for (Animal animal : listAnimals) {
			if (animal.isNom(nomAnimal)) {
				System.out.println("find(" + nomAnimal + ") have a match !");
				// output : find(Leo) have a match !

				return animal;
			}
		}
		System.out.println("find(..) found nothing");
		return new AnimalImplementation();
	}

	@Override
	public void add(Bundle bundleInfo) throws RemoteException {
		Animal animal = new AnimalImplementation();
		animal.setNom(bundleInfo.getNom());
		animal.setNomDuMaitre(bundleInfo.getNomMaitre());
		animal.setRace(bundleInfo.getRace());
		animal.setEspece(bundleInfo.getEspece());
		animal.setSuivi(bundleInfo.getDossierSuivi());
		animal.setEspeceClass(bundleInfo.getEspeceClass());
		listAnimals.add(animal);
		System.out.println("I've added the animal " + animal.getNom() + " to the database !");
		// output : I've added the animal Leo to the database !
		switch (listAnimals.size()) {
		case MILESTONE_TEST :
			alert(5);
			break;
		case MILESTONE_LOW :
			alert(100);
			break;
		case MILESTONE_MEDIUM :
			alert(500);
			break;
		case MILESTONE_HIGH :
			alert(1000);
			break;
		default:
			
		}
	}

	private void alert(int i) throws RemoteException {
		assert(listClients.size() != 0);
		
		System.out.println("We've reached a new milestone of " + i + " !");
		System.out.println("I need to tell this to EVERYONE !!");
		
		for ( Connexion client : listClients ) {
			client.alert(i);
		}
		
		System.out.println("All my clients ("+ listClients.size() + ") have been warned !");
	}

	@Override
	public void connexion(Connexion client) throws RemoteException {
		this.listClients.add(client);
	}
}
