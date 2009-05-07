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
