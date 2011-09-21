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

package se.vgregion.webbisar.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ftpserver.FtpServer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class FtpServerListener implements ServletContextListener {

    private static final Log LOGGER = LogFactory.getLog(FtpServerListener.class);

    public static final String FTPSERVER_CONTEXT_NAME = "org.apache.ftpserver";

    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Stopping FtpServer");

        FtpServer server = (FtpServer) sce.getServletContext().getAttribute(FTPSERVER_CONTEXT_NAME);

        if (server != null) {
            server.stop();

            sce.getServletContext().removeAttribute(FTPSERVER_CONTEXT_NAME);

            LOGGER.info("FtpServer stopped");
        } else {
            LOGGER.info("No running FtpServer found");
        }

    }

    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Starting FtpServer");

        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());

        FtpServer server = (FtpServer) ctx.getBean("myServer");

        sce.getServletContext().setAttribute(FTPSERVER_CONTEXT_NAME, server);

        try {
            server.start();
            LOGGER.info("FtpServer started");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start FtpServer", e);
        }
    }

}
