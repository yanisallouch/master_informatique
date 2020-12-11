package testDictionary;
import dico.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SuperClassTest {

	public IDictionary dico;

	@Test
	void testAddOneElementToEmptyDico() {
		assertEquals(5, dico.size());
		dico.put("probity", "integrity and uprightness; honesty.");
		assertEquals(1, dico.size());
		assertTrue(dico.containsKey("probity"));
		assertEquals("integrity and uprightness; honesty.", dico.get("probity"));
	}
}