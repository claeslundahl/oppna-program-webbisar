package se.vgr.webbisar.helpers;

import org.junit.Test;


public class TestParseMessage {

	@Test
	public void testParse() throws Exception {
		WebbisPortletHelper h = new WebbisPortletHelper("",null);
		String r = h.parseMessage("hello world testsaasASalkjsdsczxaaaanvcdsjhfdyriuweyskfgdhsgfhsdgfjhsdgfjshdgfjhsdgfjsdfghds");	
		
		System.out.println(r);
	}
	
}
