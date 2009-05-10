/**
 * Copyright 2009 Vastra Gotalandsregionen
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
 */
package se.vgr.webbisar.svc.impl;

import java.io.File;

import se.vgr.webbisar.svc.Configuration;
import se.vgr.webbisar.svc.ImageSize;


public class ConfigurationImpl implements Configuration {

	String imageBaseDir;
	String imageBaseUrl;
	ImageSize imageSize;
	float imageQuality;
	String ftpConfiguration;
	
	public String getFtpConfiguration() {
		return ftpConfiguration;
	}

	public void setFtpConfiguration(String ftpConfiguration) {
		this.ftpConfiguration = ftpConfiguration;
	}

	public String getImageBaseDir() {
		return imageBaseDir;
	}

	public String getImageTempDir() {
		return new File(getImageBaseDir(),"temp").getAbsolutePath();
	}
	
	public void setImageBaseDir(String dir) {
		imageBaseDir = dir;
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

	public String getImageBaseUrl() {
		return imageBaseUrl;
	}

	public void setImageBaseUrl(String imageBaseUrl) {
		this.imageBaseUrl = imageBaseUrl;
	}
	
}
