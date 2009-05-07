package se.vgr.webbisar.beans;

import java.io.Serializable;
import java.util.List;

public class SearchResultPageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int numberOfHits;
	private List<SearchResultBean> searchResults;
	
	
	public SearchResultPageBean(int numberOfHits, List<SearchResultBean> webbisar) {
		this.numberOfHits = numberOfHits;
		
		this.searchResults = webbisar;
	}
	
	
	public int getNumberOfHits() {
		return numberOfHits;
	}

	
	
	public List<SearchResultBean> getSearchResults() {
		return searchResults;
	}
	
}
