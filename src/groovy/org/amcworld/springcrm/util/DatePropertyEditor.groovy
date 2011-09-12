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

import java.beans.PropertyEditorSupport
import java.text.SimpleDateFormat
import org.springframework.context.i18n.LocaleContextHolder


/**
 * @author Daniel Ellermann
 */
class DatePropertyEditor extends PropertyEditorSupport {

	//-- Instance variables ---------------------

	String dateFormat
	String dateTimeFormat
	String timeFormat


	//-- Constructors ---------------------------

	DatePropertyEditor(messageSource) {
		dateFormat = messageSource.getMessage(
			'default.format.date', null, 'yyyy-MM-dd',
			LocaleContextHolder.locale
		)
		dateTimeFormat = messageSource.getMessage(
			'default.format.datetime', null, 'yyyy-MM-dd HH:mm',
			LocaleContextHolder.locale
		)
		timeFormat = messageSource.getMessage(
			'default.format.time', null, 'HH:mm', LocaleContextHolder.locale
		)
	}


	//-- Public methods -------------------------

	@Override
	String getAsText() {
		if (value == null) {
			return null
		} else {
			SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat)
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


	//-- Non-public methods ---------------------

	protected Date parseDate(String text) throws IllegalArgumentException {
		String fmt
		if (text.isLong()) {
			fmt = (text.length() > 6) ? 'ddMMyyyy' : 'ddMMyy'
		} else {
			fmt = dateFormat
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat(fmt)
			format.timeZone = TimeZone.getTimeZone('UTC')
			value = format.parse(text)
		} catch (ParseException) {
			throw new IllegalArgumentException()
		}
	}
	
	protected void parseTime(Date d, String text) {
		String fmt = text.isLong() ? 'HHmm' : timeFormat
		try {
			SimpleDateFormat format = new SimpleDateFormat(fmt)
			format.timeZone = TimeZone.getTimeZone('UTC')
			value = new Date((value as Date).time + format.parse(text).time)
		} catch (ParseException) {
			throw new IllegalArgumentException()
		}
	}
}
