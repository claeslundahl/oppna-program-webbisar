/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.webbisar.tool;

import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.vgregion.webbisar.svc.WebbisService;

public class WebbisIndexer {

    public static void main(String[] args) {
        AbstractRefreshableApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
                "services-config.xml", "applicationContext-hibernate.xml" });

        WebbisService svc = (WebbisService) ctx.getBean("webbisService");

        svc.reindex();

        ctx.close();
    }

}
