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
