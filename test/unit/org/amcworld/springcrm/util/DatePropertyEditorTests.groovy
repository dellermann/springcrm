package org.amcworld.springcrm.util;

import static java.util.Calendar.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.springframework.context.support.ResourceBundleMessageSource


class DatePropertyEditorTests {

	private ResourceBundleMessageSource messageSource

	@Before
    void setUp() {
		messageSource = new ResourceBundleMessageSource()
		messageSource.setBasename('org.amcworld.springcrm')
    }

	@Test
	void testGetAsText() {
		Date d = new Date(
			date:13, month:MAY, year:2011 - 1900, hours:15, minutes:24
		)
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.value = d
		assertEquals "2011-05-13 15:24", editor.asText
	}

	@Test
	void testSetAsTextDate() {
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.setAsText "2011-05-13"
		Date d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
	}
	
	@Test
	void testSetAsTextDateTime() {
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.setAsText "2011-05-13 15:24"
		Date d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 24, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
	}

	@Test
	void testCompactDateFormat() {
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.setAsText "130575"
		Date d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 1975, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13051975"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 1975, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
	}
	
	@Test
	void testCompactDateTimeFormat() {
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.setAsText "130575 1524"
		Date d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 1975, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 24, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13051975 1524"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 1975, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 24, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
	}

	@Test
	void testNullValues() {
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.value = null
		assertNull editor.getAsText()
		
		editor = new DatePropertyEditor(messageSource)
		editor.setAsText null
		assertNull editor.value
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testInvalidValue() {
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.setAsText("foobar")
	}

	@Test(expected=IllegalArgumentException.class)
	void testInvalidFormat() {
		DatePropertyEditor editor = new DatePropertyEditor(messageSource)
		editor.setAsText("13/05/1975")
	}
}
