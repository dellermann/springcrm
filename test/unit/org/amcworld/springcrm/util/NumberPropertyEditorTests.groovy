/*
 * NumberPropertyEditorTests.groovy
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

import org.junit.Test


/**
 * The class {@code NumberPropertyEditorTests} contains test cases for the
 * class {@code NumberPropertyEditor} which converts strings to numbers and
 * vice versa in a localized way.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class NumberPropertyEditorTests {

    //-- Public methods -------------------------

	@Test
	void testGetAsText() {
        Locale.default = Locale.US
        def editor = new NumberPropertyEditor(Double)
        editor.value = 4.5d
		assert '4.5' == editor.asText
        editor.value = 0.089d
        assert '0.089' == editor.asText
        editor.value = 0.0d
        assert '0' == editor.asText

        Locale.default = Locale.GERMANY
        editor.value = 4.5d
        assert '4,5' == editor.asText
        editor.value = 0.089d
        assert '0,089' == editor.asText
        editor.value = 0.0d
        assert '0' == editor.asText
	}

	@Test
	void testSetAsText() {
        Locale.default = Locale.US
        def editor = new NumberPropertyEditor(Double)
        editor.asText = '4.5'
        assert 4.5d == editor.value
        editor.asText = '4.500'
        assert 4.5d == editor.value
        editor.asText = '0.089'
        assert 0.089d == editor.value
        editor.asText = '0'
        assert 0.0d == editor.value
        editor.asText = ''
        assert 0.0d == editor.value
        editor.asText = null
        assert 0.0d == editor.value

        Locale.default = Locale.GERMANY
        editor.asText = '4,5'
        assert 4.5d == editor.value
        editor.asText = '4,500'
        assert 4.5d == editor.value
        editor.asText = '0,089'
        assert 0.089d == editor.value
        editor.asText = '0'
        assert 0.0d == editor.value
        editor.asText = ''
        assert 0.0d == editor.value
        editor.asText = null
        assert 0.0d == editor.value
	}

    @Test
    void testSetValue() {
        def editor = new NumberPropertyEditor(Double)
        editor.value = 4.5
        assert 4.5d == editor.value
        editor.value = 4.5f
        assert 4.5d == editor.value
        editor.value = 4.5d
        assert 4.5d == editor.value
        editor.value = 4
        assert 4.0d == editor.value
        editor.value = 4l
        assert 4.0d == editor.value
        editor.value = 4i
        assert 4.0d == editor.value
        editor.value = (short) 4
        assert 4.0d == editor.value
        editor.value = (byte) 4
        assert 4.0d == editor.value
    }
}
