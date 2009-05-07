package se.vgr.webbisar.web;

import java.util.List;

import se.vgr.webbisar.types.Webbis;

public class WebbisBean extends se.vgr.webbisar.presentation.WebbisBean {

	private static final long serialVersionUID = 1L;

	private List<WebbisBean> list;
	
	public WebbisBean(String imageBaseUrl, Webbis webbis, List<WebbisBean> list) {
		super(imageBaseUrl, webbis);
		this.list = list;
	}

	public boolean isLast() {
		return  this == list.get(list.size() - 1);
	}

	
}
