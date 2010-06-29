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

package se.vgregion.webbisar.types;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.junit.Test;

import se.vgregion.webbisar.types.MultimediaFile.MediaType;

public class TestWebbis {

    ClassValidator<Webbis> validator = new ClassValidator<Webbis>(Webbis.class, ResourceBundle.getBundle(
            "se.vgregion.webbisar.types.ValidatorMessages", new Locale("sv")));

    @Test
    public void testOk() throws Exception {

        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));

        Webbis w = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma");

        InvalidValue[] invalidValues = validator.getInvalidValues(w);
        assertEquals(getInvalidValues(invalidValues), 0, invalidValues.length);
        assertNull(w.getMainMultipleBirthWebbis());
        assertNull(w.getMultipleBirthSiblings());
    }

    @Test
    public void testEmailFail() throws Exception {

        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));

        Webbis w = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email",
                "http://www.blog.se/mamma");

        InvalidValue[] invalidValues = validator.getInvalidValues(w);
        assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
        assertEquals("Expected failing email", "email", invalidValues[0].getPropertyName());
    }

    @Test
    public void testFailUrl() throws Exception {

        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));

        Webbis w = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "www.blog.se/mamma");

        InvalidValue[] invalidValues = validator.getInvalidValues(w);
        assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
        assertEquals("Expected failing homePage", "homePage", invalidValues[0].getPropertyName());

        w = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55, Hospital.KSS,
                "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http:/www.blog.se/mamma");

        invalidValues = validator.getInvalidValues(w);
        assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
        assertEquals("Expected failing homePage", "homePage", invalidValues[0].getPropertyName());

        w = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55, Hospital.KSS,
                "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma?hello=2&id=88%3D");

        invalidValues = validator.getInvalidValues(w);
        assertEquals("Unexpected " + getInvalidValues(invalidValues), 0, invalidValues.length);

        w = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 55, Hospital.KSS,
                "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se", "");

        invalidValues = validator.getInvalidValues(w);
        assertEquals("Unexpected " + getInvalidValues(invalidValues), 0, invalidValues.length);

    }

    @Test
    public void testBirthDate() throws Exception {
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));
        images.add(new MultimediaFile("images/12343.jpg", "Detta är en fin bild", MediaType.IMAGE, "image/jpeg"));

        Webbis w = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2018, 1, 2, 14, 33), 2345, 55,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma");

        InvalidValue[] invalidValues = validator.getInvalidValues(w);
        assertEquals("Unexpected " + getInvalidValues(invalidValues), 1, invalidValues.length);
        assertEquals("Expected failing birthTime", "birthTime", invalidValues[0].getPropertyName());
    }

    @Test
    public void testTriplets() {
        List<Name> parents = new ArrayList<Name>();
        List<MultimediaFile> images = new ArrayList<MultimediaFile>();
        List<Webbis> siblings = new ArrayList<Webbis>();

        parents.add(new Name("Gunnar", "Bohlin"));
        parents.add(new Name("Jenny", "Lind"));

        Webbis main = new Webbis("Kalle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 33), 2345, 47,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma");
        Webbis sibling1 = new Webbis("Pelle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 43), 2346, 48,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma");
        sibling1.setMainMultipleBirthWebbis(main);
        siblings.add(sibling1);
        Webbis sibling2 = new Webbis("Olle", "someId", Sex.Male, new BirthTime(2009, 1, 2, 14, 53), 2347, 49,
                Hospital.KSS, "Mölndal", parents, images, "Johanna", "Ett meddelande", "email@email.se",
                "http://www.blog.se/mamma");
        sibling2.setMainMultipleBirthWebbis(main);
        siblings.add(sibling2);

        main.setMultipleBirthSiblings(siblings);

        assertEquals(2, main.getMultipleBirthSiblings().size());
        assertNull(main.getMainMultipleBirthWebbis());
        assertEquals(main, main.getMultipleBirthSiblings().get(0).getMainMultipleBirthWebbis());
        assertEquals("Olle", main.getMultipleBirthSiblings().get(1).getName());
    }

    private String getInvalidValues(InvalidValue[] values) {
        String ret = "";
        for (InvalidValue v : values) {
            ret += "[" + v.getPropertyName() + ":" + v.getMessage() + "]";
        }
        return ret;
    }
}
