package se.vgr.webbisar.svc.impl;

import org.apache.log4j.Logger;

import se.vgr.webbisar.types.Webbis;
import se.vgr.webbisar.util.CallContext;

public class TraceLog {
	private static Logger tracelog = Logger.getLogger("tracelog");

	public static void log(String event, CallContext cc, Webbis w) {
		tracelog.info(event + " : " + cc + " - " + w);
	}
}
