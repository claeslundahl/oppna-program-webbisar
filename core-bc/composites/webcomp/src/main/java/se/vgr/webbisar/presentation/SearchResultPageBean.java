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
