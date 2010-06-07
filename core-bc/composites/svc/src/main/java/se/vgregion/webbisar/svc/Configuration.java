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

package se.vgregion.webbisar.svc;

public interface Configuration {
    String getMultimediaFileBaseDir();

    String getMultimediaFileTempDir();

    String getMultimediaFileBaseUrl();

    ImageSize getImageSize();

    float getImageQuality();

    int getMaxVideoFileSize();

    String getFtpConfiguration();

    String getBaseUrl();

    String getRemoteEditUrl();

    String getMailHost();

    String getMailFromAddress();

    String getMailFromAddressName();

    String getMailLogo();

    String getMailTemplate();

    Boolean isTestMode();
}
