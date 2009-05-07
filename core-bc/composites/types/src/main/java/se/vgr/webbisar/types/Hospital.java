package se.vgr.webbisar.types;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.Serializable;
import java.text.ParseException;
import java.util.EnumSet;

public enum Hospital implements Serializable{
	
	KSS ("KSS"), 
	MOLNDAL ("Mölndals sjukhus"), 
	NAL ("NÄL"), 
	SAS ("SÄS Borås"), 
	OSTRA ("Östra sjukhuset"), 
	OVRIGT ("Övrigt");
	
	private String longName;
	
	Hospital(String longName) {
		this.longName = longName;
	}
	
	public String getLongName() {
		return longName;
	}
	
	public static Hospital fromLongString(String str) throws ParseException {
		if(isEmpty(str)) throw new ParseException("String empty or null in Hospital enum!",0);
		
		for(Hospital h : EnumSet.allOf(Hospital.class)) {
			if(h.getLongName().equals(str)) return h;
		}
		throw new ParseException(str + " not in range in Hospital enum!",0);
	}
	
	public String toLongString() {
		return this.getLongName();
	}
	
}
