package se.vgr.webbisar.svc.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.vgr.webbisar.svc.Configuration;
import se.vgr.webbisar.svc.WebbisImageService;

@Service("webbisImageService")
public class WebbisImageServiceImpl implements WebbisImageService {

	private Configuration cfg;
	
	@Autowired
	void setConfiguration(Configuration cfg) {
		this.cfg = cfg;
	} 
		
	public void resize(List<String> images) {
		for(String imagePath : images) {
			File imageFile = new File(cfg.getImageBaseDir(), imagePath);
			try {
				System.out.println(imageFile.getAbsolutePath());
				ImageUtil.scaleImage(imageFile, cfg.getImageSize(), cfg.getImageQuality());
			} catch (IOException e) {
				// TODO: Do proper logging here!
				e.printStackTrace();
			}
		}
	}

	public void deleteImages(List<String> toBeDeletedList) {
		for(String imagePath: toBeDeletedList) {
			File imageFile = new File(cfg.getImageBaseDir(), imagePath);
			imageFile.delete();
		}
	}

	public void cleanUpTempDir(String dir) {
		File tempDir = new File(new File(cfg.getImageBaseDir(),"temp"), dir);
		ImageUtil.removeDir(tempDir);
	}

}
