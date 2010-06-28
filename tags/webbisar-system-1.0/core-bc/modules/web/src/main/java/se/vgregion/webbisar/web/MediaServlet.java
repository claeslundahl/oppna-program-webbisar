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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import se.vgregion.webbisar.svc.Configuration;

/**
 * Servlet implementation class MediaServlet
 */
public class MediaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Log LOGGER = LogFactory.getLog(MediaServlet.class);

    private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

    private String baseFilePath;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MediaServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        Configuration config = (Configuration) ctx.getBean("configuration");
        baseFilePath = config.getMultimediaFileBaseDir();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // Get requested image by path info.
        String requestedFile = request.getPathInfo();
        LOGGER.info("requestedFile: " + requestedFile);

        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        File file = new File(baseFilePath, URLDecoder.decode(requestedFile, "UTF-8"));
        LOGGER.info("filePath: " + file.getAbsolutePath());
        LOGGER.info("fileName: " + file.getName());

        // Check if file exists
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Get content type by filename.
        String contentType = getServletContext().getMimeType(file.getName().replace(".JPG", ".jpg"));
        if (contentType == null && file.getName().endsWith("3gp")) {
            contentType = "video/3gpp";
        }

        if (contentType == null || !(contentType.startsWith("image") || contentType.startsWith("video"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Init servlet response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.addHeader("Content-Type", contentType);
        response.addHeader("Content-Length", String.valueOf(file.length()));

        response.addHeader("Expires", "Sun, 17 Jan 2038 19:14:07 GMT");
        response.addHeader("Cache-Control", "public");

        response.addHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

        // Prepare streams.
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            // Open streams.
            input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Finalize task.
            output.flush();
        } finally {
            // Gently close streams.
            close(output);
            close(input);
        }
    }

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                LOGGER.error("Could not close resource", e);
            }
        }
    }
}
