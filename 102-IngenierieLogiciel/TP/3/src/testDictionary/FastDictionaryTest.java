package testDictionary;

import org.junit.jupiter.api.BeforeEach;

import implementation.FastDictionary;

class FastDictionaryTest extends SuperClassTest {

	@BeforeEach
	void setUp() throws Exception {
		dico = new FastDictionary();
	}
}