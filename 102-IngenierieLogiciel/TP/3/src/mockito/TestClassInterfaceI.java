package mockito;

import static org.mockito.AdditionalMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestClassInterfaceI {	
	I myMock1;
	
	@BeforeEach
	public void setup() throws Exception {
	}
	
	@Test
	public void testAssert() throws Exception {
		myMock1 = mock(I.class);
		// il n'est pas nécéssaire de mock une instance vide sur 0 car c'est son comportement par défaut. 
		// when(myMock1.methodeInt()).thenReturn(0);
		assertEquals(0,myMock1.methodeInt());
	}
	@Test
	public void testVerify() throws Exception {
		myMock1 = mock(I.class);
		when(myMock1.methodeInt()).thenReturn(1, 2, 3, 4);
		for(int i = 1; i <= 4 ; i++) {
			assertEquals(i, myMock1.methodeInt());
		}
		verify(myMock1, times(4)).methodeInt();
		assertEquals(4, myMock1.methodeInt());
		assertEquals(4, myMock1.methodeInt());
	}
	@Test
	public void testExceptionInt() throws Exception {
		myMock1 = mock(I.class);
		when(myMock1.methodeInt()).thenThrow(Exception.class);
		assertThrows(Exception.class, () -> myMock1.methodeInt());	
	}
	@Test
	public void testExceptionVoid() throws Exception {
		myMock1 = mock(I.class);
		doThrow(new Exception()).when(myMock1).methodeVoid();
		assertThrows(Exception.class, () -> myMock1.methodeVoid());	
	}
	@Test
	public void testParam() throws Exception {
		myMock1 = mock(I.class);
		when(myMock1.methodeParam(3)).thenReturn(3);
		when(myMock1.methodeParam(5)).thenReturn(10);
		assertEquals(0,myMock1.methodeParam(1));
		assertEquals(3,myMock1.methodeParam(3));
		assertEquals(10,myMock1.methodeParam(5));
	}
	@Test
	public void testMatchers() throws Exception {
		myMock1 = mock(I.class);
		when(myMock1.methodeParam(gt(10))).thenReturn(42);
		when(myMock1.methodeParam(leq(10))).thenReturn(0);
		assertEquals(0,myMock1.methodeParam(1));
		assertEquals(42,myMock1.methodeParam(99));
		assertEquals(0,myMock1.methodeParam(10));
	}
	@Test
	public void testMatchersListe() throws Exception {
		myMock1 = mock(I.class);
		when(myMock1.methodeParamArrayList(argThat((ArrayList<String> l) -> (l.contains("42")) || (l.size() == 1)) )).thenReturn(42);
		ArrayList<String> tmp  = new ArrayList<String>();
		tmp.add("TOTO");
		tmp.add("42");
		assertEquals(42,myMock1.methodeParamArrayList(tmp));
	}
}