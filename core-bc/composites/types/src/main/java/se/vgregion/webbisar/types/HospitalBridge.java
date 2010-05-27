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

package se.vgregion.webbisar.types;

import java.text.ParseException;

import org.hibernate.HibernateException;
import org.hibernate.search.bridge.TwoWayStringBridge;
import org.hibernate.util.StringHelper;

public class HospitalBridge implements TwoWayStringBridge {
	
	public HospitalBridge() { /* Empty */ }

	public Object stringToObject(String stringValue) {
		if ( StringHelper.isEmpty( stringValue ) ) return null;
		
		try {
			return Hospital.fromLongString(stringValue);
		}
		catch (ParseException e) {
			throw new HibernateException( "Unable to parse into date: " + stringValue, e );
		}
	}

	public String objectToString(Object object) {
		if(object == null) return null;
		try {
			return ( (Hospital) object).getLongName();			
		} catch(Exception e) {
			throw new HibernateException("Unable to convert object date to stringValue " + object);			
		}
	}

}
