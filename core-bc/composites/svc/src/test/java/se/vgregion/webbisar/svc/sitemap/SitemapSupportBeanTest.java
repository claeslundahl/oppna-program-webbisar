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

package se.vgregion.webbisar.svc.sitemap;

import static org.junit.Assert.*;

import org.junit.Test;

import se.vgregion.webbisar.svc.sitemap.impl.SitemapCacheServiceImpl;

public class SitemapSupportBeanTest {
    private SitemapCacheLoaderMock sitemapCacheLoader = new SitemapCacheLoaderMock();
    private SitemapCacheServiceImpl sitemapCacheService = new SitemapCacheServiceImpl(sitemapCacheLoader);
    private SitemapGenerator internalSitemapGenerator = new InternalSitemapGenerator();
    private SitemapGenerator externalSitemapGenerator = new ExternalSitemapGenerator();
    private SitemapSupportBean internalSitemapSupportBean = new SitemapSupportBean(internalSitemapGenerator,
            sitemapCacheService);
    private SitemapSupportBean externalSitemapSupportBean = new SitemapSupportBean(externalSitemapGenerator,
            sitemapCacheService);

    @Test
    public void cacheIsReloadedIfEmpty() {
        internalSitemapSupportBean.getSitemapContent();
        sitemapCacheLoader.assertCacheLoaded();
    }

    @Test
    public void locAndLastmodUsesLocationAndLastModified() {
        sitemapCacheLoader.setSitemapCache(new SitemapCacheBuilder()
                .withWebbis("123", "2010-02-01T01:00:00+01:00").buildSitemapCache());
        String sitemapContent = internalSitemapSupportBean.getSitemapContent();
        String loc = getTagContent(sitemapContent, "loc");
        assertEquals("http://external.com/webbisar?webbisId=123", loc);
        String lastmod = getTagContent(sitemapContent, "lastmod");
        assertEquals("2010-02-01T01:00:00+01:00", lastmod);
    }

    @Test
    public void changeFrequencyIsUsedForChangefreqTag() {
        sitemapCacheLoader.setSitemapCache(new SitemapCacheBuilder()
                .withWebbis("123", "2010-02-01T01:00:00+01:00").buildSitemapCache());
        String sitemapContent = internalSitemapSupportBean.getSitemapContent();
        String changefreq = getTagContent(sitemapContent, "changefreq");
        assertEquals("daily", changefreq);
    }

    @Test
    public void extraInformationIsAddedIfAvailable() {
        sitemapCacheLoader.setSitemapCache(new SitemapCacheBuilder().withWebbis("123",
                "2010-02-01T01:00:00+01:00", "webbisId", "123", "name", "Kalle").buildSitemapCache());
        String sitemapContent = internalSitemapSupportBean.getSitemapContent();

        String hsaIdentity = getTagContent(sitemapContent, "webbis:name");
        assertEquals("Kalle", hsaIdentity);
    }

    @Test
    public void noExtraInformationIsAddedForExternalSitemap() {
        sitemapCacheLoader.setSitemapCache(new SitemapCacheBuilder().withWebbis("123",
                "2010-02-01T01:00:00+01:00", "webbisId", "123").buildSitemapCache());
        String sitemapContent = externalSitemapSupportBean.getSitemapContent();

        assertEquals("webbis namespace found in sitemap content", -1, sitemapContent.indexOf("webbis:"));
    }

    @Test
    public void bothUnitsAndPersonsArePresentInFileIfNoSpecificTypeIsRequested() {
        sitemapCacheLoader.setSitemapCache(new SitemapCacheBuilder()
                .withWebbis("123", "2010-02-01T01:00:00+01:00").buildSitemapCache());
        String sitemapContent = externalSitemapSupportBean.getSitemapContent();

        assertTrue("webbis not present", sitemapContent.contains("webbis"));
    }

    private String getTagContent(String content, String tag) {
        int startIndex = content.indexOf("<" + tag + ">");
        int endIndex = content.indexOf("</" + tag + ">");

        String tagContent = "";

        if (endIndex > startIndex && startIndex > -1) {
            tagContent = content.substring(startIndex + 2 + tag.length(), endIndex);
        }

        return tagContent;
    }

    private static class SitemapCacheBuilder {
        private final SitemapCache sitemapCache = new SitemapCache();

        public SitemapCache buildSitemapCache() {
            return this.sitemapCache;
        }

        public SitemapCacheBuilder withWebbis(final String webbisId, final String modifyTimestamp,
                final String... extraInformation) {
            createAndAddEntry(webbisId, "http://external.com/webbisar?webbisId=" + webbisId, modifyTimestamp,
                    extraInformation);
            return this;
        }

        private void createAndAddEntry(String hsaIdentity, String url, String modifyTimestamp,
                String... extraInformation) {
            SitemapEntry entry = new SitemapEntry(url, modifyTimestamp, "daily");
            for (int i = 0; extraInformation != null && i < extraInformation.length; i += 2) {
                entry.addExtraInformation(extraInformation[i], extraInformation[i + 1]);
            }
            sitemapCache.add(entry);
        }
    }

    private static class SitemapCacheLoaderMock implements CacheLoader<SitemapCache> {
        private boolean cacheLoaded;
        private SitemapCache sitemapCache = new SitemapCache();

        public void setSitemapCache(SitemapCache sitemapCache) {
            this.sitemapCache = sitemapCache;
        }

        public void assertCacheLoaded() {
            assertTrue("Cache was not loaded", cacheLoaded);
        }

        public SitemapCache createEmptyCache() {
            return new SitemapCache();
        }

        public SitemapCache loadCache() {
            this.cacheLoaded = true;
            return sitemapCache;
        }
    }
}
