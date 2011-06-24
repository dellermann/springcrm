/**
 * DatePropertyEditor
 * 
 * Copyright (c) 2011, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


package org.amcworld.springcrm.util

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * @author Daniel Ellermann
 */
class DatePropertyEditor extends PropertyEditorSupport {
	
	@Override
	String getAsText() {
		if (value == null) {
			return null
		} else {
			DateFormat format = DateFormat.getDateTimeInstance(
				DateFormat.SHORT, DateFormat.SHORT
			)
			return format.format(value)
		}
	}

	@Override
	void setAsText(String text) throws IllegalArgumentException {
		if (text == null) {
			value = null
		} else {
			int pos = text.indexOf(' ')
			if (pos < 0) {
				value = parseDate(text)
			} else {
				value = parseDate(text.substring(0, pos))
				parseTime(value, text.substring(pos + 1))
			}
		}
	}
	
	protected Date parseDate(String text) throws IllegalArgumentException {
		DateFormat format;
		if (text.isLong()) {
			format = new SimpleDateFormat(
				(text.length() > 6) ? 'ddMMyyyy' : 'ddMMyy'
			)
		} else {
			format = DateFormat.getDateInstance(DateFormat.SHORT)
		}
		try {
			value = format.parse(text)
		} catch (ParseException) {
			throw new IllegalArgumentException()
		}
	}
	
	protected void parseTime(Date d, String text) {
		DateFormat format;
		if (text.isLong()) {
			format = new SimpleDateFormat('HHmm')
		} else {
			format = DateFormat.getTimeInstance(DateFormat.SHORT)
		}
		try {
			value = new Date((value as Date).time + format.parse(text).time)
		} catch (ParseException) {
			throw new IllegalArgumentException()
		}
	}
}
