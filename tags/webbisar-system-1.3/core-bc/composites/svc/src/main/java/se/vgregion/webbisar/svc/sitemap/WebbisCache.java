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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.vgregion.webbisar.types.Webbis;

/**
 * A cache for units.
 */
public class WebbisCache {
    private final List<Webbis> webbisar = new ArrayList<Webbis>();

    public List<Webbis> getWebbisar() {
        return Collections.unmodifiableList(webbisar);
    }

    /**
     * Adds a new unit to the cache.
     * 
     * @param unit
     *            The unit to add to the cache.
     */
    public void add(Webbis webbis) {
        if (webbis == null) {
            throw new IllegalArgumentException("Parameter webbis is null.");
        }

        if (!this.webbisar.contains(webbis)) {
            this.webbisar.add(webbis);
        }
    }
}
