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

import java.text.ParseException;

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
			value = (text.indexOf(' ') < 0) ? parseDate(text) : parseDateTime(text)
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
			return new SimpleDateFormat(fmt).parse(text)
		} catch (ParseException) {
			throw new IllegalArgumentException()
		}
	}
	
	protected Date parseDateTime(String text) throws IllegalArgumentException {
		String fmt
		int pos = text.indexOf(' ')
		if (pos >= 0) {
			String s = text.substring(0, pos)
			if (s.isLong()) {
				fmt = (s.length() > 6) ? 'ddMMyyyy' : 'ddMMyy'
			} else {
				fmt = dateFormat
			}
			s = text.substring(pos + 1).trim()
			fmt += ' ' + (s.isLong() ? 'HHmm' : timeFormat)
		} else {
			fmt += dateTimeFormat
		}
		try {
			return new SimpleDateFormat(fmt).parse(text)
		} catch (ParseException) {
			throw new IllegalArgumentException()
		}
	}
}
