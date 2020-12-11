package mockito;

//import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Dice.class)
public class TestDice {

	@Test
	public void testDiceException() throws Exception {
		Dice mockDice = mock(Dice.class);
		
		when(mockDice.roll()).thenReturn(-1);
		System.out.println(mockDice.roll() + " is");
		PowerMockito.whenNew(Dice.class).withAnyArguments().thenReturn(mockDice);
		assertThrows(DiceException.class, ()->{mockDice.roll();});
	}
}