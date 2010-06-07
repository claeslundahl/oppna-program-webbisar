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

package se.vgregion.webbisar.svc.impl;

import java.io.File;

import se.vgregion.webbisar.svc.Configuration;
import se.vgregion.webbisar.svc.ImageSize;

public class ConfigurationImpl implements Configuration {

    Boolean testMode;

    String multimediaFileBaseDir;
    String multimediaFileBaseUrl;
    ImageSize imageSize;
    float imageQuality;
    int maxVideoFileSize;

    String baseUrl;
    String remoteEditUrl;
    String ftpConfiguration;

    String mailHost;
    String mailFromAddress;
    String mailFromAddressName;
    String mailLogo;
    String mailTemplate;

    public Boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(Boolean testMode) {
        this.testMode = testMode;
    }

    public String getFtpConfiguration() {
        return ftpConfiguration;
    }

    public void setFtpConfiguration(String ftpConfiguration) {
        this.ftpConfiguration = ftpConfiguration;
    }

    public String getMultimediaFileBaseUrl() {
        return multimediaFileBaseUrl;
    }

    public void setMultimediaFileBaseUrl(String multimediaFileBaseUrl) {
        this.multimediaFileBaseUrl = multimediaFileBaseUrl;
    }

    public String getMultimediaFileBaseDir() {
        return multimediaFileBaseDir;
    }

    public String getMultimediaFileTempDir() {
        return new File(getMultimediaFileBaseDir(), "temp").getAbsolutePath();
    }

    public void setMultimediaFileBaseDir(String dir) {
        multimediaFileBaseDir = dir;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public float getImageQuality() {
        return imageQuality;
    }

    public void setImageQuality(float imageQuality) {
        this.imageQuality = imageQuality;
    }

    public int getMaxVideoFileSize() {
        return maxVideoFileSize;
    }

    public void setMaxVideoFileSize(int maxVideoFileSize) {
        this.maxVideoFileSize = maxVideoFileSize;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRemoteEditUrl() {
        return remoteEditUrl;
    }

    public void setRemoteEditUrl(String remoteEditUrl) {
        this.remoteEditUrl = remoteEditUrl;
    }

    public String getMailFromAddress() {
        return mailFromAddress;
    }

    public void setMailFromAddress(String mailFromAddress) {
        this.mailFromAddress = mailFromAddress;
    }

    public String getMailFromAddressName() {
        return mailFromAddressName;
    }

    public void setMailFromAddressName(String mailFromAddressName) {
        this.mailFromAddressName = mailFromAddressName;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public String getMailLogo() {
        return mailLogo;
    }

    public void setMailLogo(String mailLogo) {
        this.mailLogo = mailLogo;
    }

    public String getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(String mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

}
