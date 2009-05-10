/**
 * Copyright 2009 Vastra Gotalandsregionen
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
 */
package se.vgr.webbisar.types;


import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Time implements Serializable{

	private static final long serialVersionUID = 1L;
	private int hour;
	private int minute;
	
	protected Time() { /*empty*/ }

	public Time(int hour, int minute) {
		if(hour < 0 || hour > 23) throw new IllegalArgumentException("Hour out of range " + hour);
		if(minute < 0 || minute > 59) throw new IllegalArgumentException("Minute out of range " + minute);
		
		this.hour = hour;
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(hour).append(":").append(minute);
		return sb.toString();
	}
	
}
