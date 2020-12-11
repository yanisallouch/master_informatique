package mockito;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestRequete {
	ArrayList<Pair<String, Integer>> liste1;
	
	@BeforeEach
	void setUp() {
		liste1 = new ArrayList<>();
		liste1.add(new Pair<String, Integer>("A", 1));
		liste1.add(new Pair<String, Integer>("B", 1));
		liste1.add(new Pair<String, Integer>("C", 0));
	}
	
	@Test
	void test1() {
		IOuvrage ouvrage = mock(IOuvrage.class);
		ArrayList<String> res = Requete.listesMediatheques(ouvrage);
		assertTrue(res.isEmpty());
	}
	
	@Test
	void test2() {
		IOuvrage ouvrage = mock(IOuvrage.class);
		when(ouvrage.nbExemplairesDisponible()).thenReturn(2);
		assertEquals(2, ouvrage.nbExemplairesDisponible());
		when(ouvrage.listeDesMediathequesAvecNbOuvrageDisponible()).thenReturn(liste1);
		assertEquals(2, ouvrage.nbExemplairesDisponible());
	}
}
