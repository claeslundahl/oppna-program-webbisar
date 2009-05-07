package se.vgr.webbisar.presentation;

import java.util.ArrayList;
import java.util.List;

import se.vgr.webbisar.svc.Configuration;
import se.vgr.webbisar.svc.WebbisService;
import se.vgr.webbisar.types.Webbis;

public class WebbisarFlowSupportBean {

	private static final int NUMBER_OF_WEBBIS_ON_BROWSE_PAGE = 6;
	private static final int NUMBER_OF_WEBBIS_ON_SEARCH_PAGE = 8;
	private WebbisService webbisService;
	private Configuration cfg;
	
	public WebbisService getAddressService() {
		return webbisService;
	}

	public void setWebbisService(WebbisService webbisService) {
		this.webbisService = webbisService;
	}
	
	public void setConfiguration(Configuration cfg) {
		this.cfg = cfg;
	}
	
	public boolean shouldShowWebbis(String webbisId) {
		return webbisId != null;
	}
	
	public WebbisPageBean loadPage(WebbisPageBean o) {
		int pageNumber = (o == null) ? 0  : ((WebbisPageBean)o).getPageNumber();
		return internalLoadPage(pageNumber);
	}

	public WebbisPageBean loadNextPage(WebbisPageBean o) {
		int pageNumber = (o == null) ? 0  : ((WebbisPageBean)o).getPageNumber() + 1;
		return internalLoadPage(pageNumber);
	}
	
	public WebbisPageBean loadPrevPage(WebbisPageBean o) {
		int pageNumber = (o == null) ? 
				0  : 
				(((WebbisPageBean)o).getPageNumber() == 0) ? 
						0 : 
						((WebbisPageBean)o).getPageNumber() - 1;
		
		return internalLoadPage(pageNumber);
	}
	
	protected WebbisPageBean internalLoadPage(int pageNumber) {
		long numberOfWebbisar = webbisService.getNumberOfWebbisar();
		
		long numberOfPages = numberOfWebbisar / NUMBER_OF_WEBBIS_ON_BROWSE_PAGE + ( ( numberOfWebbisar % NUMBER_OF_WEBBIS_ON_BROWSE_PAGE  ) != 0 ? 1 : 0 );
		
		List<Webbis> webbisar = webbisService.getWebbisar(pageNumber * NUMBER_OF_WEBBIS_ON_BROWSE_PAGE,NUMBER_OF_WEBBIS_ON_BROWSE_PAGE);
		
		List<WebbisBean> list = new ArrayList<WebbisBean>();
		for(Webbis webbis : webbisar) {
			list.add(new WebbisBean(cfg.getImageBaseUrl(), webbis, 0));
		}
		return new WebbisPageBean(pageNumber, pageNumber == 0, pageNumber == (numberOfPages - 1 ) ,list);
	}
	
		
	public WebbisBean getWebbis(Long webbisId, Object selectedImage) {
		int imageId = (selectedImage == null) ? 0 : (Integer) selectedImage;
			
		Webbis webbis = webbisService.getById(webbisId);
		
		return new WebbisBean(cfg.getImageBaseUrl(), webbis, imageId);
	}
	
	public SearchResultPageBean search(SearchCriteriaBean searchCriteria) {
		int numberOfHits = webbisService.getNumberOfMatchesFor(searchCriteria.getText());
		
		int numberOfPages = numberOfHits / NUMBER_OF_WEBBIS_ON_SEARCH_PAGE + ( ( numberOfHits % NUMBER_OF_WEBBIS_ON_SEARCH_PAGE  ) != 0 ? 1 : 0 );	
		
		List<Webbis> resultList = webbisService.searchWebbisar(searchCriteria.getText(), 0, NUMBER_OF_WEBBIS_ON_SEARCH_PAGE);
		List<SearchResultBean> list = new ArrayList<SearchResultBean>();
		for(Webbis webbis : resultList) {
			list.add(new SearchResultBean(webbis));
		}
		return new SearchResultPageBean(searchCriteria, numberOfHits, 0, true, numberOfPages < 2, list);
	} 
	
	private SearchResultPageBean internalLoadSearchPage(SearchCriteriaBean searchCriteria, int pageNumber){
		int numberOfHits = webbisService.getNumberOfMatchesFor(searchCriteria.getText());
		int numberOfPages = numberOfHits / NUMBER_OF_WEBBIS_ON_SEARCH_PAGE + ( ( numberOfHits % NUMBER_OF_WEBBIS_ON_SEARCH_PAGE ) != 0 ? 1 : 0 );		
		
		List<Webbis> resultList = webbisService.searchWebbisar(searchCriteria.getText(), pageNumber*NUMBER_OF_WEBBIS_ON_SEARCH_PAGE, NUMBER_OF_WEBBIS_ON_SEARCH_PAGE);
		List<SearchResultBean> list = new ArrayList<SearchResultBean>();
		for(Webbis webbis : resultList) {
			list.add(new SearchResultBean(webbis));
		}
		return new SearchResultPageBean(searchCriteria, numberOfHits, pageNumber, pageNumber == 0, pageNumber == (numberOfPages - 1), list);		
	}
	
	public SearchResultPageBean loadNextSearchPage(SearchResultPageBean o) {	
		int pageNumber = o.getPageNumber() + 1;
		return internalLoadSearchPage(o.getSearchCriteria(), pageNumber);
	}
	
	public SearchResultPageBean loadPrevSearchPage(SearchResultPageBean o) {		
		int pageNumber = o.getPageNumber() - 1;
		return internalLoadSearchPage(o.getSearchCriteria(), pageNumber);
	}
	
}
