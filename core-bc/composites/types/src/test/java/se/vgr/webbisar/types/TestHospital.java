package se.vgr.webbisar.types;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestHospital {
	
	@Test
	public void testToString() throws Exception {
		assertEquals("KSS", Hospital.KSS.toString());
		assertEquals("KSS", Hospital.KSS.toLongString());
		assertEquals("MOLNDAL", Hospital.MOLNDAL.toString());
		assertEquals("Mölndals sjukhus", Hospital.MOLNDAL.toLongString());
		assertEquals("NAL", Hospital.NAL.toString());
		assertEquals("NÄL", Hospital.NAL.toLongString());
		assertEquals("SAS", Hospital.SAS.toString());
		assertEquals("SÄS Borås", Hospital.SAS.toLongString());
		assertEquals("OSTRA", Hospital.OSTRA.toString());
		assertEquals("Östra sjukhuset", Hospital.OSTRA.toLongString());
		assertEquals("OVRIGT", Hospital.OVRIGT.toString());
		assertEquals("Övrigt", Hospital.OVRIGT.toLongString());
	}
	
	@Test
	public void testFromString() throws Exception {
		assertEquals(Hospital.KSS, Hospital.fromLongString("KSS"));
		assertEquals(Hospital.MOLNDAL, Hospital.fromLongString("Mölndals sjukhus"));
		assertEquals(Hospital.NAL, Hospital.fromLongString("NÄL"));
		assertEquals(Hospital.SAS, Hospital.fromLongString("SÄS Borås"));
		assertEquals(Hospital.OSTRA, Hospital.fromLongString("Östra sjukhuset"));
		assertEquals(Hospital.OVRIGT, Hospital.fromLongString("Övrigt"));
	}
}
