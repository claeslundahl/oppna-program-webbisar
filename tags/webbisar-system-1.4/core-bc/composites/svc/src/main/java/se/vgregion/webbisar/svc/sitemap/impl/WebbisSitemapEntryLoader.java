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

import org.springframework.beans.factory.annotation.Autowired;

import se.vgregion.sitemap.SitemapEntryLoader;
import se.vgregion.webbisar.svc.WebbisService;
import se.vgregion.webbisar.types.Webbis;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class WebbisSitemapEntryLoader implements SitemapEntryLoader<Webbis> {

    private final WebbisService webbisService;

    @Autowired
    public WebbisSitemapEntryLoader(final WebbisService webbisService) {
        this.webbisService = webbisService;
    }

    /**
     * {@inheritDoc}
     */
    public List<Webbis> loadSitemapEntrySourceData() {
        return webbisService.getAllEnabledWebbisar();
    }
}
