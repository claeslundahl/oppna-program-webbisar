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
import se.vgr.webbisar.types.Image;
import se.vgr.webbisar.types.Webbis;
import se.vgr.webbisar.util.DateTimeUtil;

/**
 * Implementation of the CacheLoader interface which populates a SitemapCache by using the
 * {@link WebbisCacheServiceImpl}.
 */
public class InternalSitemapCacheLoaderImpl implements CacheLoader<SitemapCache> {
    private final Log log = LogFactory.getLog(getClass());
    private final WebbisCacheServiceImpl webbisCacheService;
    private final String internalApplicationBaseURL;
    private final String internalImageBaseURL;

    /**
     * Constructs a new {@link InternalSitemapCacheLoaderImpl}.
     * 
     * @param webbisCacheService
     *            The {@link WebbisCacheServiceImpl} implementation to use to fetch units.
     * @param internalApplicationBaseURL
     *            The internal URL to the application.
     * @param internalImageBaseURL
     *            The internal URL to the image servlet.
     */
    public InternalSitemapCacheLoaderImpl(final WebbisCacheServiceImpl webbisCacheService,
            final String internalApplicationURL, final String internalImageBaseURL) {
        this.webbisCacheService = webbisCacheService;
        this.internalApplicationBaseURL = internalApplicationURL;
        this.internalImageBaseURL = internalImageBaseURL;
    }

    /**
     * {@inheritDoc}
     */
    public SitemapCache createEmptyCache() {
        return new SitemapCache();
    }

    /**
     * {@inheritDoc}
     */
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
            SitemapEntry entry = new SitemapEntry(internalApplicationBaseURL + "?webbisId=" + webbis.getId(),
                    lastmod, "daily");

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
            if (webbis.getImages() != null && webbis.getImages().size() > 0) {
                Image image = webbis.getImages().get(0);
                entry.addExtraInformation("imageLink", internalImageBaseURL + image.getLocation());
            }

            cache.add(entry);
        }
    }
}
