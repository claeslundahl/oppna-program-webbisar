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

package se.vgregion.webbisar.svc.sitemap;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.vgregion.webbisar.types.BirthTime;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;
import se.vgregion.webbisar.types.MultimediaFile.MediaType;

public class WebbisCacheTest {
    private WebbisCache webbisCache = new WebbisCache();

    @Before
    public void setUp() throws Exception {
        Webbis duplicate = createWebbis("Kalle");
        webbisCache.add(duplicate);
        webbisCache.add(createWebbis("Pelle"));
        // Add same again to make sure that duplicates are removed.
        webbisCache.add(duplicate);
    }

    @Test
    public void testInstantiation() {
        WebbisCache webbisCache = new WebbisCache();
        assertNotNull(webbisCache);
    }

    @Test
    public void testGetUnits() {
        List<Webbis> units = webbisCache.getWebbisar();
        assertEquals(2, units.size());
    }

    private Webbis createWebbis(String name) {
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        return new Webbis(name, "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55, Hospital.KSS,
                "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma");
    }
}
