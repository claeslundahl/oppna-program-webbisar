package se.vgr.webbisar.svc.impl;

import org.junit.Test;

import se.vgr.webbisar.svc.ImageSize;


public class TestImageUtil {

	@Test
	public void testCalculate() throws Exception {
		ImageSize src = new ImageSize(2048,1024);
		ImageSize dest = new ImageSize(1024,768);
		
		ImageSize res = ImageUtil.calculateImageSize(src, dest);
		System.out.println(res);
	}
}
