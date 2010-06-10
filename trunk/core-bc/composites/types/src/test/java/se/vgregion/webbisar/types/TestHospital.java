/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.webbisar.types;

import static org.junit.Assert.*;

import org.junit.Test;

import se.vgregion.webbisar.types.Hospital;

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
