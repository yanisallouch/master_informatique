package mockito;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class TestAdherent {
	
	Adherent myMock = mock(Adherent.class, "Michel");

	@Test
	public void testFinAnnee() {
		//assertFalse(myMock.finAnnee()); // 0 > 5 => false
		//Ne reponds pas au test poser.
		//doAnswer((invocation) -> {return true;}).when(myMock).finAnnee();
		myMock.setYear(2009);
		myMock.finAnnee();
		assertEquals(true,myMock.finAnnee()); // 0 > 5 => false
	}
}