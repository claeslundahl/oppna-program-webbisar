package se.vgr.webbisar.svc;

import java.util.List;

public interface WebbisImageService {
	
	public void resize(List<String> images);

	public void deleteImages(List<String> toBeDeletedList);

	public void cleanUpTempDir(String dir);
	
}
