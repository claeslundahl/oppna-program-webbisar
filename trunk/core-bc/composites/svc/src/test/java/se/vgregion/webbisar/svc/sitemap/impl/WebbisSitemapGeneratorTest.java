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

/**
 * 
 */
package se.vgregion.webbisar.svc.sitemap.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.vgregion.sitemap.model.SitemapEntry;
import se.vgregion.sitemap.util.W3CDateTimeUtil;
import se.vgregion.webbisar.types.BirthTime;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.MultimediaFile.MediaType;
import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;
import se.vgregion.webbisar.util.DateTimeUtil;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class WebbisSitemapGeneratorTest {
    private WebbisSitemapGenerator sitemapGenerator;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        sitemapGenerator = new WebbisSitemapGenerator();
    }

    /**
     * Test method for
     * {@link se.vgregion.webbisar.svc.sitemap.impl.WebbisSitemapGenerator#generate(java.util.List)}.
     */
    @Test
    public final void testGenerate() {
        String sitemapContent = sitemapGenerator.generate(poulateEntries());

        assertTrue("Should contain loc", sitemapContent.contains("<loc>http://base.com?webbisId=1</loc>"));
        assertTrue("Should contain lastmod",
                sitemapContent.contains("<lastmod>2010-01-11T01:00:00+01:00</lastmod>"));
        assertTrue("Should contain changefreq", sitemapContent.contains("<changefreq>daily</changefreq>"));

        assertTrue("Should contain parent1",
                sitemapContent.contains("<webbis:parent1>Gunnar Bohlin</webbis:parent1>"));
        assertTrue("Should contain parent2",
                sitemapContent.contains("<webbis:parent2>Jenny Lind</webbis:parent2>"));
        assertTrue("Should contain weight", sitemapContent.contains("<webbis:weight>2345</webbis:weight>"));
        assertTrue("Should contain imageLink",
                sitemapContent
                        .contains("<webbis:imageLink>http://base.com/images/today/12343.jpg</webbis:imageLink>"));
        assertTrue("Should contain hospital", sitemapContent.contains("<webbis:hostpital>KSS</webbis:hostpital>"));
        assertTrue("Should contain name", sitemapContent.contains("<webbis:name>Kalle</webbis:name>"));
        assertTrue("Should contain birthDate",
                sitemapContent.contains("<webbis:birthdate>2009-01-02 14:33</webbis:birthdate>"));
        assertTrue("Should contain length", sitemapContent.contains("<webbis:length>55</webbis:length>"));
        assertTrue("Should contain locality",
                sitemapContent.contains("<webbis:locality>Mölndal</webbis:locality>"));
    }

    private List<SitemapEntry> poulateEntries() {
        List<SitemapEntry> sitemapEntries = new ArrayList<SitemapEntry>();

        Webbis webbis = createWebbis();

        String lastmod = W3CDateTimeUtil.getLastModifiedW3CDateTime(webbis.getLastModified(), webbis.getCreated());
        SitemapEntry entry = new SitemapEntry("http://base.com?webbisId=" + webbis.getId(), lastmod, "daily");

        // Additional information, not sitemap standard
        entry.addExtraInformation("name", webbis.getName());
        entry.addExtraInformation("weight", String.valueOf(webbis.getWeight()));
        entry.addExtraInformation("length", String.valueOf(webbis.getLength()));
        entry.addExtraInformation("hostpital", webbis.getHospital() != null ? webbis.getHospital().getLongName()
                : "");
        entry.addExtraInformation("locality", webbis.getHome());
        entry.addExtraInformation("parent1", webbis.getParents().size() > 0 ? webbis.getParents().get(0)
                .getFullName() : "");
        entry.addExtraInformation("parent2", webbis.getParents().size() > 1 ? webbis.getParents().get(1)
                .getFullName() : "");
        entry.addExtraInformation("birthdate",
                DateTimeUtil.formatDateSwedish(webbis.getBirthTime().getTimeAsDate()));
        if (webbis.getMediaFiles() != null && webbis.getMediaFiles().size() > 0) {
            MultimediaFile image = webbis.getMediaFiles().get(0);
            entry.addExtraInformation("imageLink", "http://base.com/images" + image.getLocation());
        }

        sitemapEntries.add(entry);

        return sitemapEntries;
    }

    private Webbis createWebbis() {
        Webbis webbis = null;
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();
        Date created = new BirthTime(2010, 1, 1, 01, 00).getTimeAsDate();
        Date modified = new BirthTime(2010, 1, 11, 01, 00).getTimeAsDate();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("/today/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("/today/56789.jpg", "Detta är också en fin bild", MediaType.IMAGE,
                "image/jpeg"));

        webbis = new Webbis(new Long(1), "Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma", created);

        webbis.setLastModified(modified);

        return webbis;
    }
}
