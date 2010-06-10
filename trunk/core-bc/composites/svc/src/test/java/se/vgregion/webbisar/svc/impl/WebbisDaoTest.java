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

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.webbisar.svc.WebbisDao;
import se.vgregion.webbisar.types.BirthTime;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;
import se.vgregion.webbisar.types.MultimediaFile.MediaType;

/**
 * Spring 2.5 POJO Test Cases
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@ContextConfiguration(locations = "/applicationContext*.xml")
@Transactional
public class WebbisDaoTest {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    private WebbisDao webbisDao;

    public WebbisDao getWebbisDao() {
        return webbisDao;
    }

    public void setWebbisDao(WebbisDao resumeDao) {
        this.webbisDao = resumeDao;
    }

    @Before
    public void before() {
        System.out.println("-------");
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddZ");

    @Test
    @Rollback(false)
    public void testInsert() throws Exception {
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        webbisDao.save(new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma"));

        parents = new ArrayList<Name>();
        images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Kalle", "Bohlin"));
        parents.add(new Name("Jenny", "Nilsson"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        webbisDao.save(new Webbis("Gunnar", "someId", Sex.Male, new BirthTime(2009, 1, 4, 0, 0), 3453, 49,
                Hospital.OSTRA, "Göteborg", parents, images, "Lisa och Nisse", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma"));

        parents = new ArrayList<Name>();
        images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Janne", "Johansson"));
        parents.add(new Name("Emma", "Svensson"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        webbisDao.save(new Webbis("Kalle", "someOtherId", Sex.Male, new BirthTime(2009, 1, 2, 14, 34), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma"));

        parents = new ArrayList<Name>();
        images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Kalle", "Bohlin"));
        parents.add(new Name("Jenny", "Nilsson"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        Webbis webbis = new Webbis("Gunnar", "someId", Sex.Male, new BirthTime(2009, 1, 4, 0, 23), 3453, 49,
                Hospital.NAL, "Göteborg", parents, images, "Lisa och Nisse", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma");
        webbis.disable();
        webbisDao.save(webbis);

        // Test add twins
        parents = new ArrayList<Name>();
        images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Urban", "Svensson"));
        parents.add(new Name("Anna", "Örland"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("http://somewhere.se/images/12343.jpg", "Detta är en fin bild",
                MediaType.IMAGE, "image/jpeg"));

        List<Webbis> webbisList = new ArrayList<Webbis>();
        webbis = new Webbis("Berra", "someOtherId", Sex.Male, new BirthTime(2009, 1, 5, 0, 24), 3453, 49,
                Hospital.MOLNDAL, "Göteborg", parents, images, "Lisa och Nisse", "Ett meddelande",
                "email@email.se", "http://www.blog.se/mamma");
        Webbis twinWebbis = new Webbis("Perra", "someOtherId", Sex.Male, new BirthTime(2009, 1, 5, 0, 14), 3400,
                48, Hospital.MOLNDAL, "Göteborg", parents, images, "Lisa och Nisse", "Ett meddelande",
                "email@email.se", "http://www.blog.se/mamma");
        twinWebbis.setMainMultipleBirthWebbis(webbis);
        webbisList.add(twinWebbis);
        webbis.setMultipleBirthSiblings(webbisList);

        webbisDao.save(webbis);
    }

    @Test
    public void testReIndex() throws Exception {
        webbisDao.reindex();
    }

    @Test
    public void testFindKalle() throws Exception {
        List<Webbis> l = webbisDao.searchWebbis("kalle", 0, 10, false);
        assertEquals(3, l.size());
        assertEquals("Kalle", l.get(0).getName());
        assertEquals(new BirthTime(2009, 1, 2, 14, 33), l.get(0).getBirthTime());
    }

    @Test
    public void testFindGunnar() throws Exception {
        List<Webbis> l = webbisDao.searchWebbis("gunnar", 0, 10, false);
        assertEquals(2, l.size());
        assertEquals("Gunnar", l.get(0).getName());
    }

    @Test
    public void testFindGunnarCapital() throws Exception {
        List<Webbis> l = webbisDao.searchWebbis("Gunnar", 0, 10, false);
        assertEquals(2, l.size());
        assertEquals("Gunnar", l.get(0).getName());
    }

    @Test
    public void testFindC() throws Exception {
        List<Webbis> l = webbisDao.searchWebbis("nilsson", 0, 10, false);
        assertEquals(1, l.size());
        assertEquals("Gunnar", l.get(0).getName());
    }

    @Test
    public void testFindD() throws Exception {
        List<Webbis> l = webbisDao.searchWebbis("20090104", 0, 10, false);
        assertEquals(1, l.size());
        assertEquals("Gunnar", l.get(0).getName());
    }

    @Test
    public void testGetWebbisarForAuthorId() throws Exception {
        List<Webbis> l = webbisDao.getWebbisarForAuthorId("someId");
        assertEquals(2, l.size());
    }

    @Test
    public void testGetLatestWebbis() {
        List<Webbis> w = webbisDao.getLastestWebbis(1);
        assertEquals("Expected one webbis", 1, w.size());
        assertEquals("Berra", w.get(0).getName());
    }

    @Test
    public void testGetLatestWebbisFromHospital() {
        List<Webbis> w = webbisDao.getLastestWebbis(Hospital.OSTRA, 1);
        assertEquals("Expected one webbis", 1, w.size());
        assertEquals("Gunnar", w.get(0).getName());

        w = webbisDao.getLastestWebbis(Hospital.NAL, 1);
        assertEquals("Expected no webbis", 0, w.size());
    }

    @Test
    public void testFindTwins() throws Exception {
        List<Webbis> twin1List = webbisDao.searchWebbis("Perra", 0, 10, false);
        List<Webbis> twin2List = webbisDao.searchWebbis("Berra", 0, 10, false);
        assertEquals(1, twin1List.size());
        assertEquals(1, twin2List.size());
        assertEquals(twin2List.get(0), twin1List.get(0).getMainMultipleBirthWebbis());
        assertEquals(twin1List.get(0), twin2List.get(0).getMultipleBirthSiblings().get(0));
    }

    @Test
    @Rollback(false)
    public void testFindAll() throws Exception {
        List<Webbis> l = webbisDao.findAllWebbis();
        for (Webbis w : l) {
            webbisDao.delete(w);
        }
        System.out.println(l);
    }
}
