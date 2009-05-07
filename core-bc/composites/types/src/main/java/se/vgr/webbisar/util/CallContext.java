package se.vgr.webbisar.util;

import java.io.Serializable;

public class CallContext implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	public CallContext(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("usedId=").append(userId).toString();
	}
}
