package se.vgr.webbisar.util;

import java.lang.reflect.InvocationTargetException;

import org.springframework.remoting.support.DefaultRemoteInvocationExecutor;
import org.springframework.remoting.support.RemoteInvocation;

public class RemoteInvocationExecutor extends DefaultRemoteInvocationExecutor {

	@Override
	public Object invoke(RemoteInvocation invocation, Object targetObject)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		try {
			CallContext ctx = (CallContext) invocation.getAttribute("callContext");
			if(ctx != null) CallContextUtil.setContext(ctx);
			return super.invoke(invocation, targetObject);
		} finally {
			CallContextUtil.clear();
		}
	}

}
