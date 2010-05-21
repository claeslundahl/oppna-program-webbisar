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

package se.vgr.webbisar.svc.sitemap.impl;

import java.util.List;

import se.vgr.webbisar.svc.sitemap.CacheLoader;
import se.vgr.webbisar.svc.sitemap.SitemapCache;
import se.vgr.webbisar.svc.sitemap.SitemapEntry;
import se.vgr.webbisar.types.Webbis;
import se.vgr.webbisar.util.DateTimeUtil;

/**
 * Implementation of the CacheLoader interface which populates a SitemapCache by using the
 * {@link WebbisCacheServiceImpl}.
 */
public class PublicSitemapCacheLoaderImpl implements CacheLoader<SitemapCache> {

    private final WebbisCacheServiceImpl webbisCacheService;
    private final String externalApplicationURL;

    /**
     * Constructs a new {@link PublicSitemapCacheLoaderImpl}.
     * 
     * @param webbisCacheService
     *            The {@link WebbisCacheServiceImpl} implementation to use to fetch units.
     * @param externalApplicationURL
     *            The external URL to the application.
     */
    public PublicSitemapCacheLoaderImpl(final WebbisCacheServiceImpl webbisCacheService,
            String externalApplicationURL) {
        this.webbisCacheService = webbisCacheService;
        this.externalApplicationURL = externalApplicationURL;
    }

    public SitemapCache loadCache() {
        SitemapCache cache = new SitemapCache();

        List<Webbis> webbisar = webbisCacheService.getCache().getWebbisar();
        // Check if list of units is populated, otherwise we fill it up!
        if (webbisar.size() < 1) {
            webbisCacheService.reloadCache();
            webbisar = webbisCacheService.getCache().getWebbisar();
        }

        for (Webbis webbis : webbisar) {
            String lastmod = DateTimeUtil
                    .getLastModifiedW3CDateTime(webbis.getLastModified(), webbis.getCreated());
            SitemapEntry entry = new SitemapEntry(externalApplicationURL + "?webbisId=" + webbis.getId(), lastmod,
                    "daily");
            cache.add(entry);
        }

        return cache;
    }

    /**
     * {@inheritDoc}
     */
    public SitemapCache createEmptyCache() {
        return new SitemapCache();
    }

}
