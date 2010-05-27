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

package se.vgregion.webbisar.svc;

import java.util.List;

import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.Webbis;

public interface WebbisDao {

	public void save(Webbis webbis);

	public Webbis merge(Webbis webbis);

	public Webbis get(Long id);

	public void delete(Webbis webbis);

	public Integer getNumberOfMatchesFor(final String criteria, final boolean includeDisabled);
	
	public List<Webbis> searchWebbis(final String criteria, int firstResult, int maxResults, final boolean includeDisabled);

	public List<Webbis> getWebbisar(final int firstResult, final int maxResult);

	public long getNumberOfWebbisar();
	
	public List<Webbis> findAllWebbis();
	
	public void reindex();

	public List<Webbis> getWebbisarForAuthorId(final String authorId);

	public Webbis getDetached(Long webbisId);

	public List<Webbis> getLastestWebbis(Hospital hospital, int maxResult);
	
	public List<Webbis> getLastestWebbis(int maxResult);
}
