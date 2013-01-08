/*
 * DatePropertyEditorTests.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm.util

import static java.util.Calendar.*
import org.junit.Before
import org.junit.Test
import org.springframework.context.support.ResourceBundleMessageSource


/**
 * The class {@code DatePropertyEditorTests} contains test cases for the class
 * {@code DatePropertyEditor} which converts strings to dates and vice versa
 * in a localized way.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.0
 */
class DatePropertyEditorTests {

    //-- Instance variables ---------------------

	ResourceBundleMessageSource messageSource


    //-- Public methods -------------------------

	@Before
    void setUp() {
		messageSource = new ResourceBundleMessageSource()
		messageSource.basename = 'org.amcworld.springcrm'
    }

	@Test
	void testGetAsText() {
		Date d = new Date(
			date: 13, month: MAY, year: 2011 - 1900, hours: 15, minutes: 24
		)
		def editor = new DatePropertyEditor(messageSource)
		editor.value = d
		assert '2011-05-13 15:24' == editor.asText
	}

	@Test
	void testSetAsTextDate() {
		def editor = new DatePropertyEditor(messageSource)
		editor.asText = '2011-05-13'
		Date d = editor.value
		assert 13 == d[DATE]
		assert MAY == d[MONTH]
		assert 2011 == d[YEAR]
		assert 0 == d[HOUR_OF_DAY]
        assert 0 == d[MINUTE]
        assert 0 == d[SECOND]
		assert 0 == d[MILLISECOND]
	}

	@Test
	void testSetAsTextDateTime() {
		def editor = new DatePropertyEditor(messageSource)
		editor.asText = '2011-05-13 15:24'
		Date d = editor.value
		assert 13 == d[DATE]
		assert MAY == d[MONTH]
		assert 2011 == d[YEAR]
		assert 15 == d[HOUR_OF_DAY]
		assert 24 == d[MINUTE]
		assert 0 == d[SECOND]
		assert 0 == d[MILLISECOND]
	}

	@Test
	void testCompactDateFormat() {
		def editor = new DatePropertyEditor(messageSource)
		editor.asText = '130575'
		Date d = editor.value
		assert 13 == d[DATE]
		assert MAY == d[MONTH]
		assert 1975 == d[YEAR]
		assert 0 == d[HOUR_OF_DAY]
        assert 0 == d[MINUTE]
        assert 0 == d[SECOND]
		assert 0 == d[MILLISECOND]
		editor.asText = '13051975'
		d = editor.value
		assert 13 == d[DATE]
		assert MAY == d[MONTH]
		assert 1975 == d[YEAR]
		assert 0 == d[HOUR_OF_DAY]
        assert 0 == d[MINUTE]
        assert 0 == d[SECOND]
		assert 0 == d[MILLISECOND]
	}

	@Test
	void testCompactDateTimeFormat() {
		def editor = new DatePropertyEditor(messageSource)
		editor.asText = '130575 1524'
		Date d = editor.value
		assert 13 == d[DATE]
		assert MAY == d[MONTH]
		assert 1975 == d[YEAR]
		assert 15 == d[HOUR_OF_DAY]
		assert 24 == d[MINUTE]
		assert 0 == d[SECOND]
		assert 0 == d[MILLISECOND]
		editor.asText = '13051975 1524'
		d = editor.value
		assert 13 == d[DATE]
		assert MAY == d[MONTH]
		assert 1975 == d[YEAR]
		assert 15 == d[HOUR_OF_DAY]
		assert 24 == d[MINUTE]
		assert 0 == d[SECOND]
		assert 0 == d[MILLISECOND]
	}

    @Test
    void testEmptyValues() {
        def editor = new DatePropertyEditor(messageSource)
        editor.asText = ''
        assert null == editor.value
    }

	@Test
	void testNullValues() {
		def editor = new DatePropertyEditor(messageSource)
		editor.value = null
		assert null == editor.asText

		editor = new DatePropertyEditor(messageSource)
		editor.asText = null
		assert null == editor.value
	}

	@Test(expected = IllegalArgumentException.class)
	void testInvalidValue() {
		def editor = new DatePropertyEditor(messageSource)
		editor.asText = 'foobar'
	}

	@Test(expected=IllegalArgumentException.class)
	void testInvalidFormat() {
		def editor = new DatePropertyEditor(messageSource)
		editor.asText = '13/05/1975'
	}
}
