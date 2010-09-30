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

import java.util.List;

import se.vgregion.sitemap.SitemapEntryLoader;
import se.vgregion.sitemap.impl.DefaultSitemapCacheLoader;
import se.vgregion.sitemap.model.SitemapCache;
import se.vgregion.sitemap.model.SitemapEntry;
import se.vgregion.sitemap.util.W3CDateTimeUtil;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.Webbis;
import se.vgregion.webbisar.util.DateTimeUtil;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class WebbisExtraInfoSitemapCacheLoader extends DefaultSitemapCacheLoader {

    private final String imageBaseURL;

    /**
     * @param sitemapEntryLoader
     * @param applicationBaseURL
     */
    public WebbisExtraInfoSitemapCacheLoader(SitemapEntryLoader sitemapEntryLoader, String applicationBaseURL,
            String imageBaseURL) {
        super(sitemapEntryLoader, applicationBaseURL);
        this.imageBaseURL = imageBaseURL;
    }

    @Override
    public void populateSitemapEntryCache(SitemapCache cache) {

        List<Webbis> webbisar = getSitemapEntryLoader().loadSitemapEntrySourceData();

        for (Webbis webbis : webbisar) {
            String lastmod = W3CDateTimeUtil.getLastModifiedW3CDateTime(webbis.getLastModified(),
                    webbis.getCreated());
            SitemapEntry entry = new SitemapEntry(getApplicationBaseURL() + "?webbisId=" + webbis.getId(),
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
            entry.addExtraInformation("birthdate",
                    DateTimeUtil.formatDateSwedish(webbis.getBirthTime().getTimeAsDate()));
            if (webbis.getMediaFiles() != null && webbis.getMediaFiles().size() > 0) {
                MultimediaFile image = webbis.getMediaFiles().get(0);
                entry.addExtraInformation("imageLink", imageBaseURL + image.getLocation());
            }

            cache.add(entry);
        }
    }
}
