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
