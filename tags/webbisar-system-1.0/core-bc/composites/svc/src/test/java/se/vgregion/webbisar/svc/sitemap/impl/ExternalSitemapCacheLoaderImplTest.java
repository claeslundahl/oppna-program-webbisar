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

public class ExternalSitemapCacheLoaderImplTest {
    private WebbisCacheServiceImpl unitCacheService = new WebbisCacheServiceImpl(new WebbisCacheLoaderMock());
    private ExternalSitemapCacheLoaderImpl loader = new ExternalSitemapCacheLoaderImpl(unitCacheService,
            "http://external.com");

    @Test
    public void createEmptyCacheReturnEmptyCache() {
        SitemapCache emptyCache = loader.createEmptyCache();
        assertNotNull(emptyCache);
        assertEquals(0, emptyCache.getEntries().size());
    }

    @Test
    public void loadCacheReloadsWebbisCacheIfNoUnitsAreFound() {
        SitemapCache cache = loader.loadCache();
        assertNotNull(cache);
        assertEquals(3, cache.getEntries().size());
    }

    @Test
    public void locationUsesExternalBaseUrl() {
        SitemapCache cache = loader.loadCache();
        assertEquals("http://external.com?webbisId=123", cache.getEntries().get(0).getLocation());
    }

    @Test
    public void loadCacheUsesCreateTimestampForLastmodIfEntryIsNotModified() {
        SitemapCache cache = loader.loadCache();
        assertEquals("2010-01-01T01:00:00+01:00", cache.getEntries().get(0).getLastModified());
    }

    @Test
    public void loadCacheUsesModifyTimestampForLastmodIfEntryIsModified() {
        SitemapCache cache = loader.loadCache();
        assertEquals("2010-01-11T01:00:00+01:00", cache.getEntries().get(1).getLastModified());
    }

    @Test
    public void verifyNoExtraInformation() {
        SitemapCache cache = loader.loadCache();
        SitemapEntry entry = cache.getEntries().get(2);
        assertFalse("ExtraInformation should be empty for external", entry.iterator().hasNext());
    }
}
