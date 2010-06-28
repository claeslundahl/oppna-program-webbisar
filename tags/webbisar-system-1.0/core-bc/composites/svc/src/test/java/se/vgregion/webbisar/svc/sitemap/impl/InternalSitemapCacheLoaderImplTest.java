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

package se.vgregion.webbisar.svc.sitemap.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import se.vgregion.webbisar.svc.sitemap.SitemapCache;
import se.vgregion.webbisar.svc.sitemap.SitemapEntry;

public class InternalSitemapCacheLoaderImplTest {
    private WebbisCacheServiceImpl unitCacheService = new WebbisCacheServiceImpl(new WebbisCacheLoaderMock());
    private InternalSitemapCacheLoaderImpl loader = new InternalSitemapCacheLoaderImpl(unitCacheService,
            "http://internal.com", "weekly");

    @Test
    public void createEmptyCacheReturnEmptyCache() {
        SitemapCache emptyCache = loader.createEmptyCache();
        assertNotNull(emptyCache);
        assertEquals(0, emptyCache.getEntries().size());
    }

    @Test
    public void loadCacheReloadsUnitCacheIfNoUnitsAreFound() {
        SitemapCache cache = loader.loadCache();
        assertNotNull(cache);
        assertEquals(3, cache.getEntries().size());
    }

    @Test
    public void locationUsesInternalBaseUrlForUnits() {
        SitemapCache cache = loader.loadCache();
        assertEquals("http://internal.com?webbisId=123", cache.getEntries().get(0).getLocation());
    }

    @Test
    public void loadCacheUsesCreateTimestampForLastmodIfUnitIsNotModified() {
        SitemapCache cache = loader.loadCache();
        assertEquals("2010-01-01T01:00:00+01:00", cache.getEntries().get(0).getLastModified());
    }

    @Test
    public void loadCacheUsesModifyTimestampForLastmodIfUnitIsModified() {
        SitemapCache cache = loader.loadCache();
        assertEquals("2010-01-11T01:00:00+01:00", cache.getEntries().get(1).getLastModified());
    }

    @Test
    public void verifyExtraInformation() {
        SitemapCache cache = loader.loadCache();
        for (SitemapEntry.ExtraInformation extraInformation : cache.getEntries().get(2)) {
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
