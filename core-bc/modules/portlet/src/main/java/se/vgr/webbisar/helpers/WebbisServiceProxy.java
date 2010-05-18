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
package se.vgr.webbisar.helpers;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.vgr.webbisar.svc.WebbisImageService;
import se.vgr.webbisar.svc.WebbisService;
import se.vgr.webbisar.types.Webbis;

public class WebbisServiceProxy {
    private WebbisService webbisService;
    private WebbisImageService webbisImageService;

    public WebbisServiceProxy() {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "services-config.xml" });
        webbisService = (WebbisService) context.getBean("webbisServiceProxy");
        webbisImageService = (WebbisImageService) context.getBean("webbisImageServiceProxy");

    }

    public void saveWebbis(String sessionId, Webbis webbis) {
        webbisService.save(sessionId, webbis);
    }

    public List<Webbis> getWebbisarForAuthorId(String userId) {
        return webbisService.getWebbisarForAuthorId(userId);
    }

    public void deleteWebbis(String webbisId) {
        webbisService.delete(Long.parseLong(webbisId));
    }

    public List<Webbis> searchWebbisIncludeDisabled(String searchString, int firstResult, int maxResults) {
        return webbisService.searchWebbisarIncludeDisabled(searchString, firstResult, maxResults);
    }

    public int getNumberOfMatchesForIncludeDisabled(String searchString) {
        return webbisService.getNumberOfMatchesForIncludeDisabled(searchString);
    }

    public void toggleEnableDisable(String webbisId) {
        webbisService.toggleEnableDisable(webbisId);
    }

    public void resize(List<String> imagePaths) {
        webbisImageService.resize(imagePaths);
    }

    public void deleteImages(List<String> toBeDeletedList) {
        webbisImageService.deleteImages(toBeDeletedList);
    }

    public void cleanUpTempDir(String dir) {
        webbisImageService.cleanUpTempDir(dir);
    }

    public Webbis prepareForEditing(String sessionId, String webbisId) {
        return webbisService.prepareForEditing(sessionId, Long.parseLong(webbisId));
    }

    public String getImageBaseUrl() {
        return webbisService.getImageBaseUrl();
    }

    public String getFtpConfig() {
        return webbisService.getFtpConfiguration();
    }

    public void reindex() {
        webbisService.reindex();
    }
}