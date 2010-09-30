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

import se.vgregion.sitemap.SitemapEntryLoader;
import se.vgregion.sitemap.impl.DefaultSitemapCacheLoader;
import se.vgregion.sitemap.model.SitemapCache;
import se.vgregion.sitemap.model.SitemapEntry;
import se.vgregion.sitemap.util.W3CDateTimeUtil;
import se.vgregion.webbisar.types.Webbis;

/**
 * Implementation of the CacheLoader interface which populates a SitemapCache by using the
 * {@link WebbisCacheServiceImpl}.
 */
public class WebbisSitemapCacheLoader extends DefaultSitemapCacheLoader {

    /**
     * @param sitemapEntryLoader
     * @param applicationBaseURL
     */
    public WebbisSitemapCacheLoader(SitemapEntryLoader sitemapEntryLoader, String applicationBaseURL) {
        super(sitemapEntryLoader, applicationBaseURL);
    }

    @Override
    public void populateSitemapEntryCache(SitemapCache cache) {

        List<Webbis> webbisar = getSitemapEntryLoader().loadSitemapEntrySourceData();

        for (Webbis webbis : webbisar) {
            String lastmod = W3CDateTimeUtil.getLastModifiedW3CDateTime(webbis.getLastModified(),
                    webbis.getCreated());
            SitemapEntry entry = new SitemapEntry(getApplicationBaseURL() + "?webbisId=" + webbis.getId(),
                    lastmod, "daily");
            cache.add(entry);
        }
    }
}
