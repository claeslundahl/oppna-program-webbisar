package se.vgr.webbisar.svc;

public interface Configuration {
	String getImageBaseDir();
	String getImageTempDir();
	ImageSize getImageSize();
	float getImageQuality();
	String getImageBaseUrl();
	String getFtpConfiguration();
}
