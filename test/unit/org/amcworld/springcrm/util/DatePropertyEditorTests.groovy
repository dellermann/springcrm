package org.amcworld.springcrm.util;

import static org.junit.Assert.*;
import static java.util.Calendar.*;

import org.junit.Test;

class DatePropertyEditorTests {

	@Test
	void testGetAsText() {
		Date d = new Date(
			date:13, month:MAY, year:2011 - 1900, hours:15, minutes:24
		)
		DatePropertyEditor editor = new DatePropertyEditor()
		editor.value = d
		Locale.setDefault(Locale.GERMANY)
		assertEquals "13.05.11 15:24", editor.asText
		Locale.setDefault(Locale.US)
		assertEquals "5/13/11 3:24 PM", editor.asText
		Locale.setDefault(Locale.UK)
		assertEquals "13/05/11 15:24", editor.asText
	}

	@Test
	void testSetAsTextDate() {
		DatePropertyEditor editor = new DatePropertyEditor()
		Locale.setDefault(Locale.GERMANY)
		editor.setAsText "13.05.11"
		Date d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13.5.11"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13.5.2011"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
		assertEquals 0, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]

		Locale.setDefault(Locale.US)
		editor.setAsText "5/13/11"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "05/13/11"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
		assertEquals 0, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "05/13/2011"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
		assertEquals 0, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]

		Locale.setDefault(Locale.UK)
		editor.setAsText "13/05/11"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13/5/11"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 0, d[HOUR_OF_DAY]
        assertEquals 0, d[MINUTE]
        assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13/5/2011"
		d = editor.value
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
		DatePropertyEditor editor = new DatePropertyEditor()
		Locale.setDefault(Locale.GERMANY)
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
		editor.setAsText "13.05.11 15:24"
		Date d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 24, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13.5.11 5:4"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 5, d[HOUR_OF_DAY]
		assertEquals 4, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13.5.2011 15:24:45"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 24, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]

		Locale.setDefault(Locale.US)
		editor.setAsText "5/13/11 3:24 PM"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 24, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "05/13/11 3:4 PM"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 4, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "05/13/2011 8:56 AM"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 8, d[HOUR_OF_DAY]
		assertEquals 56, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]

		Locale.setDefault(Locale.UK)
		editor.setAsText "13/05/11 15:24"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 15, d[HOUR_OF_DAY]
		assertEquals 24, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13/5/11 5:4"
		d = editor.value
		assertEquals 13, d[DATE]
		assertEquals MAY, d[MONTH]
		assertEquals 2011, d[YEAR]
		assertEquals 5, d[HOUR_OF_DAY]
		assertEquals 4, d[MINUTE]
		assertEquals 0, d[SECOND]
		assertEquals 0, d[MILLISECOND]
		editor.setAsText "13/5/2011 15:24"
		d = editor.value
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
		DatePropertyEditor editor = new DatePropertyEditor()
		Locale.setDefault(Locale.GERMANY)
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
		DatePropertyEditor editor = new DatePropertyEditor()
		Locale.setDefault(Locale.GERMANY)
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
		DatePropertyEditor editor = new DatePropertyEditor()
		editor.value = null
		assertNull editor.getAsText()
		
		editor = new DatePropertyEditor()
		editor.setAsText null
		assertNull editor.value
	}
	
	@Test(expected=IllegalArgumentException.class)
	void testInvalidValue() {
		DatePropertyEditor editor = new DatePropertyEditor()
		Locale.setDefault(Locale.GERMANY)
		editor.setAsText("foobar")
	}

	@Test(expected=IllegalArgumentException.class)
	void testInvalidFormat() {
		DatePropertyEditor editor = new DatePropertyEditor()
		Locale.setDefault(Locale.GERMANY)
		editor.setAsText("13/05/1975")
	}
}
