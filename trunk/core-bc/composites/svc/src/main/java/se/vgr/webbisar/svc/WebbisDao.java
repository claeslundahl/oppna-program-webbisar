package se.vgr.webbisar.svc;

import java.util.List;

import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Webbis;

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
