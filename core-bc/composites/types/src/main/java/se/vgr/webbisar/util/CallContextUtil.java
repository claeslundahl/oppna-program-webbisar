package se.vgr.webbisar.util;

public class CallContextUtil {
	private static ThreadLocal<CallContext> tc = new ThreadLocal<CallContext>();
	
	public static void setContext(CallContext ctx) {
		tc.set(ctx);
	}
	
	public static CallContext getContext() {
		return tc.get();
	}
	
	public static CallContext clear() {
		CallContext ctx = tc.get();
		tc.set(null);
		return ctx;
	}

}
