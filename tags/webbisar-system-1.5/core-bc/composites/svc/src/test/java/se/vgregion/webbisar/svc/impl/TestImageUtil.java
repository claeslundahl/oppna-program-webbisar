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

import org.junit.Test;

import se.vgregion.webbisar.svc.ImageSize;

public class TestImageUtil {

    @Test
    public void testCalculate() throws Exception {
        ImageSize src = new ImageSize(2048, 1024);
        ImageSize dest = new ImageSize(1024, 768);

        ImageSize res = ImageUtil.calculateImageSize(src, dest);
        assertEquals(1024, res.getWidth(), 0);
        assertEquals(512, res.getHeight(), 0);
    }

    @Test
    public void testMax() throws Exception {
        ImageSize src = new ImageSize(2048, 1024);
        assertEquals(2048, src.max(), 0);

        src = new ImageSize(1024, 2048);
        assertEquals(2048, src.max(), 0);
    }
}
