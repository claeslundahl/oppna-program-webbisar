package se.vgr.webbisar.svc;

import java.beans.PropertyEditorSupport;
import java.util.StringTokenizer;

public class ImageSizeEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		StringTokenizer t = new StringTokenizer(text, "x");
		int width = Integer.parseInt(t.nextToken());
		int height = Integer.parseInt(t.nextToken());

		setValue(new ImageSize(width, height));
	}
}
