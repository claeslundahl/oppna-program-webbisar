package se.vgr.webbisar.presentation;

import java.io.Serializable;
import java.util.List;

public class SearchResultPageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private SearchCriteriaBean searchCriteria;
	private int pageNumber;
	private boolean lastPage;
	private boolean firstPage;
	private int numberOfHits;
	private List<SearchResultBean> searchResults;
	
	
	public SearchResultPageBean(SearchCriteriaBean searchCriteria, int numberOfHits, int pageNumber, boolean firstPage, 
			                    boolean lastPage, List<SearchResultBean> webbisar) {
		this.searchCriteria = searchCriteria;
		this.numberOfHits = numberOfHits;
		this.firstPage = firstPage;
		this.lastPage = lastPage;
		this.pageNumber = pageNumber;
		this.searchResults = webbisar;
	}
	
	public SearchCriteriaBean getSearchCriteria() {
		return searchCriteria;
	}
	
	public int getNumberOfHits() {
		return numberOfHits;
	}

	public int getPageNumber() {
		return pageNumber;
	}
	
	public List<SearchResultBean> getSearchResults() {
		return searchResults;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public boolean isFirstPage() {
		return firstPage;
	}

	
}
