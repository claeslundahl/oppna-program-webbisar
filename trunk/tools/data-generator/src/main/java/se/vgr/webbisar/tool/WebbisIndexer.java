package se.vgr.webbisar.tool;

import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.vgr.webbisar.svc.WebbisService;

public class WebbisIndexer {

	public static void main(String[] args) {
		AbstractRefreshableApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"services-config.xml","applicationContext-hibernate.xml"});
		
		WebbisService svc = (WebbisService) ctx.getBean("webbisService");
		
		svc.reindex();
		
		ctx.close();
	}

}
