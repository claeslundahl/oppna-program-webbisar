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

package se.vgr.webbisar.svc.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.vgr.webbisar.svc.Configuration;
import se.vgr.webbisar.svc.WebbisImageService;

@Service("webbisImageService")
@Transactional
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
