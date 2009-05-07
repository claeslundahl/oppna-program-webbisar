package se.vgr.webbisar.util;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocation;

public class RemoteInvocationFactory extends DefaultRemoteInvocationFactory {

	@Override
	public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
		RemoteInvocation ri = super.createRemoteInvocation(methodInvocation);
		ri.addAttribute("callContext", CallContextUtil.getContext());
		return ri;
	}

}
