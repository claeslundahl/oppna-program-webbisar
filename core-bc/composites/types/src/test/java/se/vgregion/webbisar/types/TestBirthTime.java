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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import se.vgregion.webbisar.types.BirthTime;

public class TestBirthTime {

	@Test
	public void testSmartTime() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		
		BirthTime bt = new BirthTime(2009,1,12,0,12);
		assertEquals("Idag", bt.getSmartTime(sdf.parse("2009-01-12 10:10")));
		assertEquals("Igår", bt.getSmartTime(sdf.parse("2009-01-13 10:10")));
		assertEquals("i måndags", bt.getSmartTime(sdf.parse("2009-01-14 10:10")));
		assertEquals("i måndags", bt.getSmartTime(sdf.parse("2009-01-15 10:10")));
		assertEquals("i måndags", bt.getSmartTime(sdf.parse("2009-01-16 10:10")));
		assertEquals("i måndags", bt.getSmartTime(sdf.parse("2009-01-17 10:10")));
		assertEquals("i måndags", bt.getSmartTime(sdf.parse("2009-01-18 10:10")));
		assertEquals("12 januari", bt.getSmartTime(sdf.parse("2009-01-19 10:10")));
	}
	
	@Test
	public void testInvalidDate() throws Exception {
		BirthTime bt = new BirthTime(2009,2,31,0,0);
		//Date d = bt.getTimeAsDate();
	}
	
	@Test
	public void testSmartTime2() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		
		BirthTime bt = new BirthTime(2009,1,12,0,12);
		assertEquals("i måndags", bt.getSmartTime(sdf.parse("2009-01-18 10:10")));
		bt = new BirthTime(2009,1,13,0,12);
		assertEquals("i tisdags", bt.getSmartTime(sdf.parse("2009-01-19 10:10")));
		bt = new BirthTime(2009,1,14,0,12);
		assertEquals("i onsdags", bt.getSmartTime(sdf.parse("2009-01-20 10:10")));
		bt = new BirthTime(2009,1,15,0,12);
		assertEquals("i torsdags", bt.getSmartTime(sdf.parse("2009-01-21 10:10")));
		bt = new BirthTime(2009,1,16,0,12);
		assertEquals("i fredags", bt.getSmartTime(sdf.parse("2009-01-22 10:10")));
		bt = new BirthTime(2009,1,17,0,12);
		assertEquals("i lördags", bt.getSmartTime(sdf.parse("2009-01-23 10:10")));
		bt = new BirthTime(2009,1,18,0,12);
		assertEquals("i söndags", bt.getSmartTime(sdf.parse("2009-01-24 10:10")));
	}
	
	@Test
	public void testEquals() throws Exception {
		BirthTime bt1 = new BirthTime(2009,10,10,0,12);
		BirthTime bt2 = new BirthTime(bt1.getTimeAsDate());
		assertEquals(bt1,bt2);
	}
	
	@Test
	public void testMonthLimits() {
		for(int i = 1 ; i < 12; i++) {
			new BirthTime(2009,i,1,10,0);
		}
		BirthTime bt = new BirthTime(2009,0,1,10,0);
		assertEquals(2009, bt.getYear());
		assertEquals(1, bt.getMonth());
		assertEquals(1, bt.getDay());
		assertEquals(10, bt.getHour());
		assertEquals(0, bt.getMinutes());
		bt = new BirthTime(2009,13,1,10,0);
		assertEquals(2009, bt.getYear());
		assertEquals(12, bt.getMonth());
		assertEquals(1, bt.getDay());
		assertEquals(10, bt.getHour());
		assertEquals(0, bt.getMinutes());
	}
	@Test
	public void testDayLimits() {
		for(int i = 1 ; i <= 31; i++) {
			new BirthTime(2009,10,i,10,0);
		}
		BirthTime bt = new BirthTime(2009,10,0,10,0);
		assertEquals(2009, bt.getYear());
		assertEquals(10, bt.getMonth());
		assertEquals(1, bt.getDay());
		assertEquals(10, bt.getHour());
		assertEquals(0, bt.getMinutes());
		bt = new BirthTime(2009,10,32,10,0);
		assertEquals(2009, bt.getYear());
		assertEquals(10, bt.getMonth());
		assertEquals(31, bt.getDay());
		assertEquals(10, bt.getHour());
		assertEquals(0, bt.getMinutes());
	}
	
	@Test
	public void testHourLimits() {
		for(int i = 0 ; i <= 23; i++) {
			new BirthTime(2009,10,1,i,0);
		}
		BirthTime bt = new BirthTime(2009,10,1,-1,0);
		assertEquals(2009, bt.getYear());
		assertEquals(10, bt.getMonth());
		assertEquals(1, bt.getDay());
		assertEquals(0, bt.getHour());
		assertEquals(0, bt.getMinutes());
		bt = new BirthTime(2009,10,1,24,0);
		assertEquals(2009, bt.getYear());
		assertEquals(10, bt.getMonth());
		assertEquals(1, bt.getDay());
		assertEquals(23, bt.getHour());
		assertEquals(0, bt.getMinutes());
	}
	
	@Test
	public void testMinutesLimits() {
		for(int i = 0 ; i <= 59; i++) {
			new BirthTime(2009,10,1,1,i);
		}
		BirthTime bt =new BirthTime(2009,10,1,1,-1);
		assertEquals(2009, bt.getYear());
		assertEquals(10, bt.getMonth());
		assertEquals(1, bt.getDay());
		assertEquals(1, bt.getHour());
		assertEquals(0, bt.getMinutes());
		bt = new BirthTime(2009,10,1,1,60);
		assertEquals(2009, bt.getYear());
		assertEquals(10, bt.getMonth());
		assertEquals(1, bt.getDay());
		assertEquals(1, bt.getHour());
		assertEquals(59, bt.getMinutes());
	}
}
