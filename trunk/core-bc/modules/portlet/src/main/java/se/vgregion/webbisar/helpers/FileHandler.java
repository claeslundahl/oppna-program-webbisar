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

package se.vgregion.webbisar.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FileHandler {

    private static final Logger logger = Logger.getLogger(FileHandler.class);

    private String host;
    private int port;
    private String userName;
    private String password;

    public FileHandler(String ftpConfig) {
        logger.info("FTP CONFIGURATION IS: " + ftpConfig);

        StringTokenizer t = new StringTokenizer(ftpConfig, ";");
        try {
            this.host = t.nextToken();
            this.port = Integer.parseInt(t.nextToken());
            this.userName = t.nextToken();
            this.password = t.nextToken();
        } catch (NumberFormatException e) {
            logger.fatal("FTP CONFIGURATION: Failed to parse!!");
        }
    }

    public void writeTempFile(String fileName, String sessionId, InputStream is) throws FTPException {
        FTPClient ftp = connect();
        try {
            ftp.makeDirectory("temp");
            ftp.changeWorkingDirectory("temp");
            ftp.makeDirectory(sessionId);
            ftp.changeWorkingDirectory(sessionId);
            ftp.storeFile(fileName, is);
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ftp.disconnect();
            } catch (IOException e) {
                // Empty...
            }
        }

    }

    private FTPClient connect() throws FTPException {
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect(host, port);
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
            }
            ftp.login(userName, password);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            return ftp;
        } catch (SocketException e) {
            throw new FTPException("Failed to connect to server", e);
        } catch (IOException e) {
            throw new FTPException("Failed to connect to server", e);
        }
    }
}
