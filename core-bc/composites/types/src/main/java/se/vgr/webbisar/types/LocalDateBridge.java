package se.vgr.webbisar.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.search.bridge.ParameterizedBridge;
import org.hibernate.search.bridge.TwoWayStringBridge;
import org.hibernate.util.StringHelper;

public class LocalDateBridge implements TwoWayStringBridge, ParameterizedBridge {

	private Resolution resolution;

	static final SimpleDateFormat sdfDate     = new SimpleDateFormat("yyyyMMdd");
	static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMddHHmm");
	
	public LocalDateBridge() {
	}

	public LocalDateBridge(Resolution resolution) {
		setResolution( resolution );
	}

	public Object stringToObject(String stringValue) {
		if ( StringHelper.isEmpty( stringValue ) ) return null;
		try {
			if(stringValue.length() == 8) {
				return sdfDate.parseObject(stringValue);
			} else if(stringValue.length() == 12) {
				return sdfDateTime.parseObject(stringValue);
			} else {
				throw new ParseException(stringValue,0);
			}
		}
		catch (ParseException e) {
			throw new HibernateException( "Unable to parse into date: " + stringValue, e );
		}
	}

	public String objectToString(Object object) {
		if(object == null) return null;
		if(resolution == Resolution.DATE) {
			return sdfDate.format((Date) object);
		} else if(resolution == Resolution.DATETIME) {
			return sdfDateTime.format((Date) object);
		}
		throw new HibernateException("Unable to convert object date to stringValue " + object);
	}

	@SuppressWarnings("unchecked")
	public void setParameterValues(Map parameters) {
		Object resolution = parameters.get( "resolution" );
		Resolution hibResolution;
		if ( resolution instanceof String ) {
			hibResolution = Resolution.valueOf( ( (String) resolution ).toUpperCase( Locale.ENGLISH ) );
		}
		else {
			hibResolution = (Resolution) resolution;
		}
		setResolution( hibResolution );
	}
	
	private void setResolution(Resolution r){
		this.resolution = r;
	}
	
	static enum Resolution {
		DATE, DATETIME
	}
}
