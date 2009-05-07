package se.vgr.webbisar.presentation;

import java.io.Serializable;

public class SearchCriteriaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("text=").append(text);
		return sb.toString();
	}
}
