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

import static org.apache.commons.io.FilenameUtils.separatorsToUnix;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.vgr.webbisar.svc.Configuration;
import se.vgr.webbisar.svc.WebbisDao;
import se.vgr.webbisar.svc.WebbisService;
import se.vgr.webbisar.types.Hospital;
import se.vgr.webbisar.types.Image;
import se.vgr.webbisar.types.Webbis;
import se.vgr.webbisar.util.CallContextUtil;

@Service("webbisService")
@Transactional
public class WebbisServiceImpl implements WebbisService {

    private WebbisDao webbisDao;
    private Configuration cfg;

    @Autowired
    void setWebbisDao(WebbisDao webbisDao) {
        this.webbisDao = webbisDao;
    }

    @Autowired
    void setConfiguration(Configuration cfg) {
        this.cfg = cfg;
    }

    @Transactional(readOnly = true)
    public Webbis getById(Long webbisId) {
        return this.webbisDao.get(webbisId);
    }

    @Transactional(readOnly = true)
    public long getNumberOfWebbisar() {
        return this.webbisDao.getNumberOfWebbisar();
    }

    @Transactional(readOnly = true)
    public List<Webbis> getWebbisar(final int firstResult, final int maxResult) {
        return this.webbisDao.getWebbisar(firstResult, maxResult);
    }

    @Transactional(readOnly = true)
    public List<Webbis> getWebbisarForAuthorId(String userId) {
        return this.webbisDao.getWebbisarForAuthorId(userId);
    }

    public void save(String tempDir, Webbis w) {

        // We expect the webbis to come with images in the temp folder.
        copyWebbisImagesToDir(ImageUtil.getDirForTodaysDate(cfg.getImageBaseDir()), w);

        if (w.isPersisted()) {
            // Load the previous version of the Webbis.
            Webbis previousWebbis = this.webbisDao.get(w.getId());

            // Remove any unused images
            removeImageFiles(w.getUnusedImages(previousWebbis));

            // Save it.
            w = this.webbisDao.merge(w);
            TraceLog.log("UPDATED", CallContextUtil.getContext(), w);
        }
        else {
            this.webbisDao.save(w);
            TraceLog.log("CREATED", CallContextUtil.getContext(), w);
        }

        File tmpDir = getFullTempDir(tempDir);
        if (tmpDir.exists()) {
            removeDir(tmpDir);
        }
    }

    private File getFullTempDir(String tempDir) {
        File tmpDir = new File(cfg.getImageTempDir(), tempDir);
        return tmpDir;
    }

    private void removeDir(File dir) {
        ImageUtil.removeDir(dir);
    }

    private void copyWebbisImagesToDir(File newDir, Webbis webbis) {
        List<Image> images = webbis.getImages();
        for (Image image : images) {
            File fromImageFile = new File(cfg.getImageBaseDir(), image.getLocation());// the location of the image
            // is relative
            File toImageFile = new File(newDir.getPath(), fromImageFile.getName());
            ImageUtil.copyFile(fromImageFile, toImageFile);

            image.setLocation(separatorsToUnix(toImageFile.getPath()).replace(
                    separatorsToUnix(cfg.getImageBaseDir()), ""));
        }
    }

    private void removeImageFiles(List<Image> images) {
        for (Image image : images) {
            File imageFile = new File(cfg.getImageBaseDir(), image.getLocation());
            imageFile.delete();
        }
    }

    public void saveAll(Set<Webbis> webbisar) {
        for (Webbis webbis : webbisar) {
            this.webbisDao.save(webbis);
        }
    }

    public void delete(Long webbisId) {
        Webbis webbis = this.webbisDao.get(webbisId);
        removeImageFiles(webbis.getImages());
        this.webbisDao.delete(webbis);
        TraceLog.log("DELETED", CallContextUtil.getContext(), webbis);
    }

    @Transactional(readOnly = true)
    public List<Webbis> searchWebbisar(String criteria, int firstResult, int maxResults) {
        return this.webbisDao.searchWebbis(criteria, firstResult, maxResults, false);
    }

    @Transactional(readOnly = true)
    public List<Webbis> searchWebbisarIncludeDisabled(String criteria, int firstResult, int maxResults) {
        return this.webbisDao.searchWebbis(criteria, firstResult, maxResults, true);
    }

    /**
     * Get latest active webbis on a specific hospital
     * 
     * @inheritDoc
     */
    @Transactional(readOnly = true)
    public List<Webbis> getLatestWebbisar(Hospital hospital, int maxResult) {
        return this.webbisDao.getLastestWebbis(hospital, maxResult);
    }

    /**
     * Get latest active webbis
     * 
     * @inheritDoc
     */
    @Transactional(readOnly = true)
    public List<Webbis> getLatestWebbisar(int maxResult) {
        return this.webbisDao.getLastestWebbis(maxResult);
    }

    @Transactional(readOnly = true)
    public Integer getNumberOfMatchesFor(String criteria) {
        return this.webbisDao.getNumberOfMatchesFor(criteria, false);
    }

    @Transactional(readOnly = true)
    public Integer getNumberOfMatchesForIncludeDisabled(String criteria) {
        return this.webbisDao.getNumberOfMatchesFor(criteria, true);
    }

    public void reindex() {
        this.webbisDao.reindex();
    }

    public void toggleEnableDisable(String webbisId) {
        Webbis w = this.webbisDao.get(Long.parseLong(webbisId));
        w.toggleEnableDisable();
    }

    public Webbis prepareForEditing(String tempDir, Long webbisId) {
        Webbis copy = this.webbisDao.getDetached(webbisId);
        copyWebbisImagesToDir(getFullTempDir(tempDir), copy);
        return copy;
    }

    public void cleanUp(String tempDir) {
        ImageUtil.removeDir(getFullTempDir(tempDir));
    }

    public String getImageBaseUrl() {
        return cfg.getImageBaseUrl();
    }

    public String getFtpConfiguration() {
        return cfg.getFtpConfiguration();
    }

}
