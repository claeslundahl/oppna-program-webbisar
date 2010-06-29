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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ErrorServlet
 */
public class ErrorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static final Logger log = Logger.getLogger(ErrorServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ErrorServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        HttpSession session = request.getSession();
        Errors errors = (Errors) session.getAttribute("internalErrors");
        if (errors == null) {
            log.debug("Received first error");
            errors = new Errors(10000);
            session.setAttribute("internalErrors", errors);
        }

        errors.addError();
        log.debug("Number of errors " + errors.getNumOfErrors());
        if (errors.getNumOfErrors() > 3) {
            log.debug("Number of errors exceeded, redirect to " + request.getContextPath() + "/error.jsp");
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        } else {
            log.debug("Redirect to " + request.getContextPath() + "/index.jsp");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}
