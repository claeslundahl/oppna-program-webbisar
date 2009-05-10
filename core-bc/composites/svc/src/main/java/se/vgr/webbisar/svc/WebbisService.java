/**
 * Copyright 2009 Vastra Gotalandsregionen
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
 */
package se.vgr.webbisar.svc;

import java.util.List;
import java.util.Set;

import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Webbis;

public interface WebbisService {

	long getNumberOfWebbisar();
	
	Webbis getById(final Long webbisId);
	
	List<Webbis> getWebbisar(final int firstResult, final int maxResult);

	void save(final String tempDir, final Webbis webbis);

	Webbis prepareForEditing(final String tempDir, final Long webbisId);
	
	void cleanUp(final String tempDir);

	void saveAll(final Set<Webbis> webbisar);
	
	public void delete(final Long webbisId);
	
	List<Webbis> getWebbisarForAuthorId(final String userId);
	
	Integer getNumberOfMatchesFor(final String criteria);
	
	Integer getNumberOfMatchesForIncludeDisabled(final String criteria);

	List<Webbis> searchWebbisar(final String criteria, final int firstResult, final int maxResults);
	
	List<Webbis> searchWebbisarIncludeDisabled(final String criteria, final int firstResult, final int maxResults);
	
	List<Webbis> getLatestWebbisar(int maxResult);
	
	List<Webbis> getLatestWebbisar(Hospital hospital, int maxResult);
	
	void reindex();

	void toggleEnableDisable(String webbisId);

	String getImageBaseUrl();

	String getFtpConfiguration();
	
}
