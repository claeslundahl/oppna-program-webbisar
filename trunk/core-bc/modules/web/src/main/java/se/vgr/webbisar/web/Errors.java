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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Errors implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Date> errors = new ArrayList<Date>();
	private int seconds;
	
	public Errors(int seconds) {
		this.seconds = seconds;
	}
	
	public void addError() {
		truncateErrorList();
		errors.add(new Date());
	}
	
	public int getNumOfErrors() {
		return errors.size();
	}
	
	private synchronized void truncateErrorList() {
		Date treshold = new Date(System.currentTimeMillis() - seconds);
		Iterator<Date> iter = errors.iterator();
		while(iter.hasNext()) {
			Date e = iter.next();
			if(e.before(treshold)) {
				iter.remove();
			}
		}
	}
}
