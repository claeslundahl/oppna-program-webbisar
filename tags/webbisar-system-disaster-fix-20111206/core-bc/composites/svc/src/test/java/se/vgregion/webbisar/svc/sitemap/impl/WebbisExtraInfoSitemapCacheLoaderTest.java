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

import org.junit.Before;
import org.junit.Test;

import se.vgregion.sitemap.model.SitemapCache;
import se.vgregion.sitemap.model.SitemapEntry;
import se.vgregion.webbisar.svc.WebbisServiceMock;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class WebbisExtraInfoSitemapCacheLoaderTest {

    private WebbisExtraInfoSitemapCacheLoader sitemapCacheLoader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        WebbisServiceMock webbisService = new WebbisServiceMock();
        WebbisSitemapEntryLoader sitemapEntryLoader = new WebbisSitemapEntryLoader(webbisService);
        sitemapCacheLoader = new WebbisExtraInfoSitemapCacheLoader(sitemapEntryLoader, "http://base.com",
                "http://base.com/images");
    }

    /**
     * Test method for
     * {@link se.vgregion.webbisar.svc.sitemap.impl.WebbisExtraInfoSitemapCacheLoader#populateSitemapEntryCache(se.vgregion.sitemap.model.SitemapCache)}
     * .
     */
    @Test
    public final void testPopulateSitemapEntryCacheSitemapCache() {
        SitemapCache cache = new SitemapCache();
        sitemapCacheLoader.populateSitemapEntryCache(cache);
        assertEquals(1, cache.getEntries().size());

        SitemapEntry entry = cache.getEntries().get(0);
        assertEquals("http://base.com?webbisId=1", entry.getLocation());
        assertEquals("2010-01-11T01:00:00+01:00", entry.getLastModified());
        assertEquals("daily", entry.getChangeFrequency());

        for (SitemapEntry.ExtraInformation extraInformation : entry) {
            if ("parent1".equals(extraInformation.getName())) {
                assertEquals("Gunnar Bohlin", extraInformation.getValue());
            } else if ("name".equals(extraInformation.getName()) || "weight".equals(extraInformation.getName())
                    || "length".equals(extraInformation.getName())
                    || "hostpital".equals(extraInformation.getName())
                    || "locality".equals(extraInformation.getName())
                    || "parent2".equals(extraInformation.getName())
                    || "birthdate".equals(extraInformation.getName())
                    || "imageLink".equals(extraInformation.getName())) {

            } else {
                fail("Unexpected extra information found");
            }
        }
    }
}
