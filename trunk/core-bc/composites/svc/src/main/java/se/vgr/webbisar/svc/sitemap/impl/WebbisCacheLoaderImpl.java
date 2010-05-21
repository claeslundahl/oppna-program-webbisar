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

import se.vgr.webbisar.svc.WebbisService;
import se.vgr.webbisar.svc.sitemap.CacheLoader;
import se.vgr.webbisar.svc.sitemap.WebbisCache;
import se.vgr.webbisar.types.Webbis;

/**
 * Implementation of the CacheLoader interface which populates a UnitCache by using the {@link SearchService}.
 */
public class WebbisCacheLoaderImpl implements CacheLoader<WebbisCache> {
    private final Log log = LogFactory.getLog(getClass());
    private final WebbisService webbisService;

    /**
     * Constructs a new {@link WebbisCacheLoaderImpl}.
     * 
     * @param webbisService
     *            The {@link SearchService} implementation to use to fetch units.
     */
    public WebbisCacheLoaderImpl(final WebbisService webbisService) {
        this.webbisService = webbisService;
    }

    /**
     * {@inheritDoc}
     */
    public WebbisCache loadCache() {
        WebbisCache cache = new WebbisCache();

        List<Webbis> webbisar = webbisService.getAllWebbisar();
        for (Webbis webbis : webbisar) {
            cache.add(webbis);
        }

        return cache;
    }

    /**
     * {@inheritDoc}
     */
    public WebbisCache createEmptyCache() {
        return new WebbisCache();
    }
}
