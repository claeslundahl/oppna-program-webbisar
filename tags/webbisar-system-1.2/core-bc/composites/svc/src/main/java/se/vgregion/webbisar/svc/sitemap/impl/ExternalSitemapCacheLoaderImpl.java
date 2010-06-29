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

import java.util.List;

import se.vgregion.webbisar.svc.sitemap.CacheLoader;
import se.vgregion.webbisar.svc.sitemap.SitemapCache;
import se.vgregion.webbisar.svc.sitemap.SitemapEntry;
import se.vgregion.webbisar.types.Webbis;
import se.vgregion.webbisar.util.DateTimeUtil;

/**
 * Implementation of the CacheLoader interface which populates a SitemapCache by using the
 * {@link WebbisCacheServiceImpl}.
 */
public class ExternalSitemapCacheLoaderImpl implements CacheLoader<SitemapCache> {

    private final WebbisCacheServiceImpl webbisCacheService;
    private final String externalApplicationBaseURL;

    /**
     * Constructs a new {@link ExternalSitemapCacheLoaderImpl}.
     * 
     * @param webbisCacheService
     *            The {@link WebbisCacheServiceImpl} implementation to use to fetch units.
     * @param externalApplicationBaseURL
     *            The external URL to the application.
     */
    public ExternalSitemapCacheLoaderImpl(final WebbisCacheServiceImpl webbisCacheService,
            String externalApplicationBaseURL) {
        this.webbisCacheService = webbisCacheService;
        this.externalApplicationBaseURL = externalApplicationBaseURL;
    }

    /**
     * {@inheritDoc}
     */
    public SitemapCache createEmptyCache() {
        return new SitemapCache();
    }

    public SitemapCache loadCache() {
        SitemapCache cache = new SitemapCache();

        populateWebbisar(cache);

        return cache;
    }

    private void populateWebbisar(SitemapCache cache) {
        List<Webbis> webbisar = webbisCacheService.getCache().getWebbisar();
        // Check if list of webbisar is populated, otherwise we fill it up!
        if (webbisar.size() < 1) {
            webbisCacheService.reloadCache();
            webbisar = webbisCacheService.getCache().getWebbisar();
        }

        for (Webbis webbis : webbisar) {
            String lastmod = DateTimeUtil
                    .getLastModifiedW3CDateTime(webbis.getLastModified(), webbis.getCreated());
            SitemapEntry entry = new SitemapEntry(externalApplicationBaseURL + "?webbisId=" + webbis.getId(),
                    lastmod, "daily");
            cache.add(entry);
        }
    }
}
