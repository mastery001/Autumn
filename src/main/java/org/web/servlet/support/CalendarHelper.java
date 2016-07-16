package org.web.servlet.support;

import java.util.Calendar;

/**
 * 日期帮助pojo
 * @author mastery
 * @Time 2015-4-17 下午12:10:37
 * 
 */
public class CalendarHelper {

	private int year;

	private int month;

	private int date;

	public CalendarHelper() {
		Calendar cal = Calendar.getInstance();
		month = cal.get(Calendar.MONTH) + 1;
		year = cal.get(Calendar.YEAR);
		date = cal.get(Calendar.DATE);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
	
	public String toFileFormat() {
		return this.year + "/" + this.month + "/" + this.date;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CalendarHelper) {
			CalendarHelper ch = (CalendarHelper) obj;
			return ch.getYear() == this.year && ch.getMonth() == this.month
					&& ch.getDate() == this.date;
		}
		return super.equals(obj);
	}

}
