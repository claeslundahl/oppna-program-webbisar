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
