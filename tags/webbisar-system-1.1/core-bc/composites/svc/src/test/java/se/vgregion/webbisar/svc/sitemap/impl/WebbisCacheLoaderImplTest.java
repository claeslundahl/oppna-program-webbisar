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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import se.vgregion.webbisar.svc.WebbisService;
import se.vgregion.webbisar.svc.sitemap.WebbisCache;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.Webbis;

public class WebbisCacheLoaderImplTest {
    private final WebbisServiceMock webbisService = new WebbisServiceMock();
    private final WebbisCacheLoaderImpl webbisCacheLoader = new WebbisCacheLoaderImpl(webbisService);

    @Test
    public void cacheIsEmptyIfSearchServiceDoesNotReturnAnyHsaIdentities() {
        WebbisCache webbisCache = webbisCacheLoader.loadCache();
        assertNotNull(webbisCache);
        assertEquals(0, webbisCache.getWebbisar().size());
    }

    @Test
    public void createEmptyCacheReturnNewEmptyCacheEachTime() {
        WebbisCache emptyCache1 = webbisCacheLoader.createEmptyCache();
        WebbisCache emptyCache2 = webbisCacheLoader.createEmptyCache();
        assertEquals(0, emptyCache1.getWebbisar().size());
        assertEquals(emptyCache1.getWebbisar(), emptyCache2.getWebbisar());
        assertNotSame(emptyCache1, emptyCache2);
    }

    private static class WebbisServiceMock implements WebbisService {

        /**
         * {@inheritDoc}
         */
        public void cleanUp(String tempDir) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public void delete(Long webbisId) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public List<Webbis> getAllWebbisar() {
            return new ArrayList<Webbis>();
        }

        /**
         * {@inheritDoc}
         */
        public Webbis getById(Long webbisId) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public String getFtpConfiguration() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public String getImageBaseUrl() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public List<Webbis> getLatestWebbisar(int maxResult) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public List<Webbis> getLatestWebbisar(Hospital hospital, int maxResult) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public int getMaxVideoFileSize() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public int getMaxNoOfVideoFiles() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public Integer getNumberOfMatchesFor(String criteria) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public Integer getNumberOfMatchesForIncludeDisabled(String criteria) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public long getNumberOfWebbisar() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public List<Webbis> getWebbisar(int firstResult, int maxResult) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public List<Webbis> getWebbisarForAuthorId(String userId) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public Boolean isTestMode() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public Webbis prepareForEditing(String tempDir, Long webbisId) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public void reindex() {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public void save(String tempDir, Webbis webbis) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public void saveAll(Set<Webbis> webbisar) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public List<Webbis> searchWebbisar(String criteria, int firstResult, int maxResults) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public List<Webbis> searchWebbisarIncludeDisabled(String criteria, int firstResult, int maxResults) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        /**
         * {@inheritDoc}
         */
        public void toggleEnableDisable(String webbisId) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

    }
}
