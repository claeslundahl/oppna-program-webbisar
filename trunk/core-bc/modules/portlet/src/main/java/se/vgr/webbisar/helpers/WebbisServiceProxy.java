package se.vgr.webbisar.helpers;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.vgr.webbisar.svc.WebbisImageService;
import se.vgr.webbisar.svc.WebbisService;
import se.vgr.webbisar.types.Webbis;

public class WebbisServiceProxy {
	private WebbisService webbisService;
	private WebbisImageService webbisImageService;

	public WebbisServiceProxy() {
		
		ClassPathXmlApplicationContext context 
			= new ClassPathXmlApplicationContext(new String[] {"services-config.xml"});
		webbisService = (WebbisService)context.getBean("webbisServiceProxy");
		webbisImageService = (WebbisImageService) context.getBean("webbisImageServiceProxy");
		
	}

	public void saveWebbis(String sessionId, Webbis webbis) {
		webbisService.save(sessionId, webbis);
	}
	
	public List<Webbis> getWebbisarForAuthorId(String userId){
		return webbisService.getWebbisarForAuthorId(userId);
	}

	public void deleteWebbis(String webbisId) {
		webbisService.delete(Long.parseLong(webbisId));
	}
	
	public List<Webbis> searchWebbisIncludeDisabled(String searchString, int firstResult, int maxResults){
		return webbisService.searchWebbisarIncludeDisabled(searchString, firstResult, maxResults);
	}
	
	public int getNumberOfMatchesForIncludeDisabled(String searchString){
		return webbisService.getNumberOfMatchesForIncludeDisabled(searchString);
	}

	public void toggleEnableDisable(String webbisId) {
		webbisService.toggleEnableDisable(webbisId);		
	}

	public void resize(List<String> imagePaths) {
		webbisImageService.resize(imagePaths);
	}

	public void deleteImages(List<String> toBeDeletedList) {
		webbisImageService.deleteImages(toBeDeletedList);
	}

	public void cleanUpTempDir(String dir) {
		webbisImageService.cleanUpTempDir(dir);
	}

	public Webbis prepareForEditing(String sessionId, String webbisId) {
		return webbisService.prepareForEditing(sessionId, Long.parseLong(webbisId));	
	}

	public String getImageBaseUrl() {
		return webbisService.getImageBaseUrl();
	}

	public String getFtpConfig() {
		return webbisService.getFtpConfiguration();
	}

	public void reindex() {
		webbisService.reindex();
	}
}
