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

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import se.vgregion.sitemap.service.SitemapService;
import se.vgregion.sitemap.servlet.DefaultSitemapServlet;

/**
 * Generates an ExtraInfo (internal) sitemap for Webbisar.
 */
public class WebbisExtraInfoSitemapServlet extends DefaultSitemapServlet {
    private static final long serialVersionUID = 4782818023575166386L;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadSitemapService() {
        WebApplicationContext springContext = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());

        setSitemapService((SitemapService<?>) springContext.getBean("internalSitemapService"));
    }
}
