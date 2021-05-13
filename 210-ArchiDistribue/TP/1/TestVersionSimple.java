package jUnitTestVersionSimple;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.RemoteException;

import org.junit.jupiter.api.*;

import versionSimple.Animal;
import versionSimple.AnimalImplementation;
import versionSimple.DossierSuivi;
import versionSimple.DossierSuiviImplementation;
import versionSimple.EspeceImplementation;

public class TestVersionSimple {
	Animal unAnimal;
	DossierSuivi dossierSuivi;
	EspeceImplementation especeClass;
	
	String empty = "";
	double zeroDureeDeVide = 0.0;
	
	String nom = "Leo";
	String nomMaitre = "Lucio";
	String espece = "uneEspeceDeChien";
	String race = "uneRaceDeChien";
	String concat = empty;
	String suiviNonEmpty = "Suivi foobar";

	@BeforeEach
	void setup() throws RemoteException {
		dossierSuivi = new DossierSuiviImplementation();
		especeClass = new EspeceImplementation();
		unAnimal = new AnimalImplementation();
		unAnimal.setDossierSuivi(dossierSuivi);
	}
	
	@Test
	void testNom() throws RemoteException {
		unAnimal.setNom(nom);
		assertEquals(nom, unAnimal.getNom());
	}

	@Test
	void testNomDuMaitre() throws RemoteException {
		unAnimal.setNomDuMaitre(nomMaitre);
		assertEquals(nomMaitre, unAnimal.getNomDuMaitre());
	}

	@Test
	void testNomWithNomDuMaitre() throws RemoteException {
		unAnimal.setNomWithNomDuMaitre(nom,nomMaitre);
		concat = nom + " appartient a " + nomMaitre;
		assertEquals(concat, unAnimal.getNomWithNomDuMaitre());
	}
		
	@Test
	void testEspeceAnimal() throws RemoteException {
		unAnimal.setEspece(espece);
		assertEquals(espece, unAnimal.getEspece());
	}
	
	@Test
	void testRaceAnimal() throws RemoteException {
		unAnimal.setRace(race);
		assertEquals(race, unAnimal.getRace());
	}
	
	@Test
	void testEspeceWithRaceAnimal() throws RemoteException {
		unAnimal.setEspeceWithRace(espece,race);
		concat = espece + " de " + race;
		assertEquals(concat, unAnimal.getEspeceWithRace());
	}

	@Test
	void testSuiviAnimalWithNonEmptyDossierSuivi() throws RemoteException {
		dossierSuivi.setSuivi(suiviNonEmpty);
		unAnimal.setDossierSuivi(dossierSuivi);
		assertEquals(dossierSuivi, unAnimal.getDossierSuivi());
	}

	@Test
	void testSuiviAnimalWithEmptyDossierSuivi() throws RemoteException {
		dossierSuivi.setSuivi(empty);
		unAnimal.setDossierSuivi(dossierSuivi);
		assertEquals(dossierSuivi, unAnimal.getDossierSuivi());
	}

	@Test
	void testSuiviAnimalWithEmptySuivi () throws RemoteException {
		unAnimal.setDossierSuivi(new DossierSuiviImplementation());
		assertEquals(empty, unAnimal.getSuivi());
	}
	
	@Test
	void testSuiviAnimalWithNonEmptySuivi () throws RemoteException {
		unAnimal.setDossierSuivi(new DossierSuiviImplementation());
		unAnimal.setSuivi(suiviNonEmpty);
		assertEquals(suiviNonEmpty, unAnimal.getSuivi());
	}

	@Test
	void testEspeceWithEmptyNom() {
		assertEquals(empty, especeClass.getNom());
	}
	
	@Test
	void testEspeceWithNonEmptyNom() {
		especeClass.setNom(nom);
		assertEquals(nom, especeClass.getNom());
	}
	
	@Test
	void testEspeceWithZeroDureeDeVide() {
		assertEquals(zeroDureeDeVide, especeClass.getDureeDeVie());
	}
	
	@Test
	void testAnimalGetEspeceNotNull() throws RemoteException {
		assertTrue(unAnimal.getEspeceClass() != null);
	}
	
}