package testDictionary;
import implementation.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderedDictionaryTest extends SuperClassTest {

	@BeforeEach
	void setUp() throws Exception {
		dico = new OrderedDictionary(5);
	}
	
	@Test
	void testOrdreObject() {
		dico.put("probity", "integrity and uprightness; honesty.");
		dico.put("mellifluous", "flowing with honey; sweetened with or as if with honey.");
		dico.put("festinate", "to hurry; hasten.");
		dico.put("quondam", "former; onetime: his quondam partner.");
		assertEquals("integrity and uprightness; honesty.", dico.get(0));
		assertEquals("flowing with honey; sweetened with or as if with honey.", dico.get(1));
		assertEquals("to hurry; hasten.", dico.get(2));
		assertEquals("former; onetime: his quondam partner.", dico.get(3));
	}
}