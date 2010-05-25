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
