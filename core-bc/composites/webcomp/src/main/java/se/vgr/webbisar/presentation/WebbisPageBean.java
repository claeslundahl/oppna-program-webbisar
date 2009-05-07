package se.vgr.webbisar.presentation;

import java.io.Serializable;
import java.util.List;

public class WebbisPageBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int pageNumber;
	private boolean lastPage;
	private boolean firstPage;
	private List<WebbisBean> webbisar;
	
	
	public WebbisPageBean(int pageNumber, boolean firstPage, boolean lastPage, List<WebbisBean> webbisar) {
		this.firstPage = firstPage;
		this.lastPage = lastPage;
		this.pageNumber = pageNumber;
		this.webbisar = webbisar;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public List<WebbisBean> getWebbisar() {
		return webbisar;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public boolean isFirstPage() {
		return firstPage;
	}
	
	
}
