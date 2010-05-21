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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import se.vgr.webbisar.svc.sitemap.CacheLoader;
import se.vgr.webbisar.svc.sitemap.SitemapCache;
import se.vgr.webbisar.svc.sitemap.SitemapEntry;
import se.vgr.webbisar.types.Webbis;
import se.vgr.webbisar.util.DateTimeUtil;

/**
 * Implementation of the CacheLoader interface which populates a SitemapCache by using the
 * {@link WebbisCacheServiceImpl}.
 */
public class InternalSitemapCacheLoaderImpl implements CacheLoader<SitemapCache> {
    private final Log log = LogFactory.getLog(getClass());
    private final WebbisCacheServiceImpl webbisCacheService;
    private final String internalApplicationURL;

    /**
     * Constructs a new {@link InternalSitemapCacheLoaderImpl}.
     * 
     * @param webbisCacheService
     *            The {@link WebbisCacheServiceImpl} implementation to use to fetch units.
     * @param searchService
     *            The {@link SearchService} implementation to use to fetch persons.
     * @param internalApplicationURL
     *            The internal URL to the application.
     * @param changeFrequency
     *            The change frequency of the sitemap entries.
     */
    public InternalSitemapCacheLoaderImpl(final WebbisCacheServiceImpl webbisCacheService,
            final String internalApplicationURL) {
        this.webbisCacheService = webbisCacheService;
        this.internalApplicationURL = internalApplicationURL;
    }

    /**
     * {@inheritDoc}
     */
    public SitemapCache loadCache() {
        SitemapCache cache = new SitemapCache();

        populateWebbisars(cache);

        return cache;
    }

    private void populateWebbisars(SitemapCache cache) {
        List<Webbis> webbisar = webbisCacheService.getCache().getWebbisar();

        // Check if list of units is populated, otherwise we fill it up!
        if (webbisar.size() < 1) {
            webbisCacheService.reloadCache();
            webbisar = webbisCacheService.getCache().getWebbisar();
        }

        for (Webbis webbis : webbisar) {
            String lastmod = DateTimeUtil
                    .getLastModifiedW3CDateTime(webbis.getLastModified(), webbis.getCreated());
            SitemapEntry entry = new SitemapEntry(internalApplicationURL + "?webbisId=" + webbis.getId(), lastmod,
                    "daily");

            // TODO: HRIV har en egen dtd för denna info!

            // Additional information, not sitemap standard
            entry.addExtraInformation("name", webbis.getName());
            entry.addExtraInformation("weight", String.valueOf(webbis.getWeight()));
            entry.addExtraInformation("length", String.valueOf(webbis.getLength()));
            entry.addExtraInformation("hostpital", webbis.getHospital() != null ? webbis.getHospital()
                    .getLongName() : "");
            entry.addExtraInformation("locality", webbis.getHome());
            entry.addExtraInformation("parent1", webbis.getParents().size() > 0 ? webbis.getParents().get(0)
                    .getFullName() : "");
            entry.addExtraInformation("parent2", webbis.getParents().size() > 1 ? webbis.getParents().get(1)
                    .getFullName() : "");
            entry.addExtraInformation("birthdate", DateTimeUtil.formatDateSwedish(webbis.getBirthTime()
                    .getTimeAsDate()));

            cache.add(entry);
        }
    }

    /**
     * {@inheritDoc}
     */
    public SitemapCache createEmptyCache() {
        return new SitemapCache();
    }

}
