package versionSimple;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class CabinetVeterinaireImplementation extends UnicastRemoteObject implements CabinetVeterinaire {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3504199374528969864L;

	private List<Animal> listAnimals;
	
	protected CabinetVeterinaireImplementation() throws RemoteException {
		super();
		this.listAnimals = new ArrayList<Animal>();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Animal find(String nomAnimal) throws RemoteException {
		for (Animal animal : listAnimals) {
			if (animal.isNom(nomAnimal)) {
				System.out.println("find(" + nomAnimal + ") have a match !");
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
	}
}
