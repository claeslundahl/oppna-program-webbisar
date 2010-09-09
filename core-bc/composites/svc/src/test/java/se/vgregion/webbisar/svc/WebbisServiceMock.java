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
package se.vgregion.webbisar.svc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import se.vgregion.webbisar.types.BirthTime;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.MultimediaFile.MediaType;
import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class WebbisServiceMock implements WebbisService {

    public void cleanUp(String tempDir) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void delete(Long webbisId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<Webbis> getAllEnabledWebbisar() {
        List<Webbis> list = new ArrayList<Webbis>();
        list.add(createWebbis());
        return list;
    }

    public Webbis getById(Long webbisId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String getFtpConfiguration() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String getImageBaseUrl() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<Webbis> getLatestWebbisar(int maxResult) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<Webbis> getLatestWebbisar(Hospital hospital, int maxResult) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getMaxVideoFileSize() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getMaxNoOfVideoFiles() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Integer getNumberOfMatchesFor(String criteria) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Integer getNumberOfMatchesForIncludeDisabled(String criteria) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public long getNumberOfWebbisar() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<Webbis> getWebbisar(int firstResult, int maxResult) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<Webbis> getWebbisarForAuthorId(String userId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Boolean isTestMode() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Webbis prepareForEditing(String tempDir, Long webbisId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void reindex() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void save(String tempDir, Webbis webbis) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void saveAll(Set<Webbis> webbisar) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<Webbis> searchWebbisar(String criteria, int firstResult, int maxResults) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<Webbis> searchWebbisarIncludeDisabled(String criteria, int firstResult, int maxResults) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void toggleEnableDisable(String webbisId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private Webbis createWebbis() {
        Webbis webbis = null;
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();
        Date created = new BirthTime(2010, 1, 1, 01, 00).getTimeAsDate();
        Date modified = new BirthTime(2010, 1, 11, 01, 00).getTimeAsDate();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("/today/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("/today/56789.jpg", "Detta är också en fin bild", MediaType.IMAGE,
                "image/jpeg"));

        webbis = new Webbis(new Long(1), "Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma", created);

        webbis.setLastModified(modified);

        return webbis;
    }
}
