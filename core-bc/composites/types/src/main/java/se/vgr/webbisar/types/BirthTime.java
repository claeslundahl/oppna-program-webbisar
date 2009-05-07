package se.vgr.webbisar.types;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class BirthTime implements Comparable<BirthTime>, Serializable {


	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter fullDateFormatter = new DateTimeFormatterBuilder()
			.appendYearOfEra(4, 4).appendLiteral('-').appendMonthOfYear(2)
			.appendLiteral('-').appendDayOfMonth(2)
			.appendLiteral(' ').appendHourOfDay(2).appendLiteral(':')
			.appendMinuteOfHour(2).toFormatter().withLocale(
					new Locale("sv", "swe"));

	private static final DateTimeFormatter weekDayFormatter = new DateTimeFormatterBuilder()
			.appendDayOfWeekText().toFormatter().withLocale(
					new Locale("sv", "swe"));

	private static final DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
			.appendDayOfMonth(1).appendLiteral(' ').appendMonthOfYearText()
			.toFormatter().withLocale(new Locale("sv", "swe"));
	
	private static final DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
		.appendHourOfDay(2).appendLiteral(':').appendMinuteOfHour(2)
		.toFormatter().withLocale(new Locale("sv", "swe"));

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minutes;
	
	public BirthTime(){
		Date date = new Date();
		DateTime dt = new DateTime(date);

		this.year = dt.get(DateTimeFieldType.year());
		this.month = dt.get(DateTimeFieldType.monthOfYear());
		this.day = dt.get(DateTimeFieldType.dayOfMonth());
		this.hour = dt.get(DateTimeFieldType.hourOfDay());
		this.minutes = dt.get(DateTimeFieldType.minuteOfHour());
	}

	public BirthTime(int year, int month, int day, int hour, int minutes) {
		boolean error = true;
		int cnt = 0;
		// FIX: This is kind of an hack to make this class kind of "fail safe"
		while(error && cnt++ < 10) {
			try {
				new DateTime(year, month, day, hour, minutes, 0, 0);
				error = false;
			} catch(IllegalFieldValueException e) {
				String s = e.getFieldName();
				System.out.println(e);
				if("dayOfMonth".equals(s)) {
					day = (day > e.getUpperBound().intValue()) ? e.getUpperBound().intValue() : e.getLowerBound().intValue();
				} else if ("monthOfYear".equals(s)) {
					month = (month > e.getUpperBound().intValue()) ? e.getUpperBound().intValue() : e.getLowerBound().intValue();
				} else if ("hourOfDay".equals(s)) {
					hour = (hour > e.getUpperBound().intValue()) ? e.getUpperBound().intValue() : e.getLowerBound().intValue();
				} else if ("minuteOfHour".equals(s)) {
					minutes = (minutes > e.getUpperBound().intValue()) ? e.getUpperBound().intValue() : e.getLowerBound().intValue();
				}
			}			
		}
		this.day = day;
		this.hour = hour;
		this.minutes = minutes;
		this.month = month;
		this.year = year;
	}

	public BirthTime(Date date) {
		DateTime dt = new DateTime(date);

		this.year = dt.get(DateTimeFieldType.year());
		this.month = dt.get(DateTimeFieldType.monthOfYear());
		this.day = dt.get(DateTimeFieldType.dayOfMonth());
		this.hour = dt.get(DateTimeFieldType.hourOfDay());
		this.minutes = dt.get(DateTimeFieldType.minuteOfHour());
	}

	public String getTime() {
		return timeFormatter.print(asDateTime());
	}
	
	public Date getTimeAsDate() {
		DateTime dt = asDateTime();
		return dt.toDate();
	}

	public String getSmartTime(Date n) {

		DateTime now = new DateTime(n);
		DateTime lastMidnight = new DateTime(now.toDateMidnight());
		DateTime lastTwoMidnight = new DateTime(lastMidnight).minusDays(1);
		DateTime lastWeek = new DateTime(lastMidnight).minusDays(6);

		DateTime birthTime = asDateTime();

		if (birthTime.isBefore(now) && birthTime.isAfter(lastMidnight)) {
			return "Idag";
		} else if (birthTime.isBefore(lastMidnight)
				&& birthTime.isAfter(lastTwoMidnight)) {
			return "Igår";
		} else if (birthTime.isAfter(lastWeek)) {
			return "i " + weekDayFormatter.print(birthTime) + "s";
		}
		return dateFormatter.print(birthTime);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + hour;
		result = prime * result + minutes;
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BirthTime other = (BirthTime) obj;
		if (day != other.day)
			return false;
		if (hour != other.hour)
			return false;
		if (minutes != other.minutes)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return fullDateFormatter.print(asDateTime());
	}

	private DateTime asDateTime() {
		return new DateTime(year, month, day, hour, minutes, 0, 0);
	}

	public int compareTo(BirthTime o) {
		return this.toString().compareTo(o.toString());
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}

}
