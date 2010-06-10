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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.webbisar.svc.WebbisService;
import se.vgregion.webbisar.types.BirthTime;
import se.vgregion.webbisar.types.Hospital;
import se.vgregion.webbisar.types.MultimediaFile;
import se.vgregion.webbisar.types.Name;
import se.vgregion.webbisar.types.Sex;
import se.vgregion.webbisar.types.Webbis;

/**
 * Spring 2.5 POJO Test Cases
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class })
@ContextConfiguration(locations = "/applicationContext*.xml")
@Transactional
public class WebbisServiceImplTest {

    private final static String TEMP_DIR = "C:/";

    @Autowired
    private WebbisService webbisService;

    public WebbisService getWebbisService() {
        return webbisService;
    }

    public void setWebbisService(WebbisService webbisService) {
        this.webbisService = webbisService;
    }

    @Before
    public void setup() throws Exception {
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));

        webbisService.save(TEMP_DIR, new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33),
                2345, 55, Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma"));
    }

    @Test
    public void testInsert() throws Exception {
        // We want to check that webbis changes are logged in trace log
        Logger logger = Logger.getLogger("tracelog");
        final StringWriter writer = new StringWriter();
        Appender appender = new WriterAppender(new SimpleLayout(), writer);
        logger.addAppender(appender);

        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Kalle", "Anka"));
        parents.add(new Name("Kajsa", "Anka"));

        webbisService.save(TEMP_DIR, new Webbis("Pelle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33),
                2345, 55, Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma"));

        // Ensure trace log was called
        assertTrue(writer.toString().contains("CREATED : null"));
        assertTrue(writer.toString().contains("authorId=someId,name=Pelle"));
    }

    @Test
    public void testGetAllAndDisable() throws Exception {
        // We want to check that webbis changes are logged in trace log
        Logger logger = Logger.getLogger("tracelog");
        final StringWriter writer = new StringWriter();
        Appender appender = new WriterAppender(new SimpleLayout(), writer);
        logger.addAppender(appender);

        List<Webbis> list = webbisService.getAllWebbisar();
        Webbis toUpdate = list.get(0);
        assertEquals("Kalle", toUpdate.getName());

        toUpdate.toggleEnableDisable();
        webbisService.save(TEMP_DIR, toUpdate);

        toUpdate = webbisService.getById(toUpdate.getId());
        assertEquals(Boolean.TRUE, toUpdate.isDisabled());

        // Ensure trace log was called
        assertTrue(writer.toString().contains("UPDATED : null"));
        assertTrue(writer.toString().contains("authorId=someId,name=Kalle"));
    }

    @Test
    public void testDelete() throws Exception {
        // We want to check that webbis changes are logged in trace log
        Logger logger = Logger.getLogger("tracelog");
        final StringWriter writer = new StringWriter();
        Appender appender = new WriterAppender(new SimpleLayout(), writer);
        logger.addAppender(appender);

        List<Webbis> list = webbisService.getAllWebbisar();
        Webbis toUpdate = list.get(0);

        webbisService.delete(toUpdate.getId());

        list = webbisService.getAllWebbisar();
        assertEquals(0, list.size());

        // Ensure trace log was called
        assertTrue(writer.toString().contains("DELETED : null"));
        assertTrue(writer.toString().contains("authorId=someId,name=Kalle"));
    }

    @Test
    public void testGetNumberOfWebbisar() throws Exception {
        assertEquals(1, webbisService.getNumberOfWebbisar());
    }
}
