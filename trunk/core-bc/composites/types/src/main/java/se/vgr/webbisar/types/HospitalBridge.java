package se.vgr.webbisar.types;

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
