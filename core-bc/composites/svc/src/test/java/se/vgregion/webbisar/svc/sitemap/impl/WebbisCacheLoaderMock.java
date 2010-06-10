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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.vgregion.webbisar.svc.sitemap.CacheLoader;
import se.vgregion.webbisar.svc.sitemap.WebbisCache;
import se.vgregion.webbisar.types.BirthTime;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;
import se.vgregion.webbisar.types.MultimediaFile.MediaType;

class WebbisCacheLoaderMock implements CacheLoader<WebbisCache> {

    public WebbisCache createEmptyCache() {
        return new WebbisCache();
    }

    public WebbisCache loadCache() {
        WebbisCache webbisCache = new WebbisCache();
        Date created = new BirthTime(2010, 1, 1, 01, 00).getTimeAsDate();
        Date modified = new BirthTime(2010, 1, 11, 01, 00).getTimeAsDate();

        webbisCache.add(createWebbis(123, "Kalle", created, created));
        webbisCache.add(createWebbis(456, "Pelle", created, modified));
        webbisCache.add(createWebbis(789, "Olle", created, created));

        return webbisCache;
    }

    private Webbis createWebbis(long id, String name, Date created, Date modified) {
        Webbis webbis = null;
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));

        webbis = new Webbis(id, name, "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma", created);

        webbis.setLastModified(modified);

        return webbis;
    }
}