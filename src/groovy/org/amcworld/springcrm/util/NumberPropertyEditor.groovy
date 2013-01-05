/*
 * NumberPropertyEditor.groovy
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

import java.beans.PropertyEditorSupport
import java.text.NumberFormat
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.util.NumberUtils
import org.springframework.util.StringUtils


/**
 * The class {@code NumberPropertyEditor} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class NumberPropertyEditor extends PropertyEditorSupport {

    //-- Instance variables ---------------------

    Class<? extends Number> numberClass


    //-- Constructors ---------------------------

    NumberPropertyEditor(Class<? extends Number> numberClass) {
        if (numberClass == null) {
            throw new IllegalArgumentException(
                'Property class must not be null.'
            )
        }
        this.numberClass = numberClass
    }


    //-- Public methods -------------------------

    @Override
    public String getAsText() {
        def numberFormat = NumberFormat.getNumberInstance(LCH.locale)
        return (value == null) ? '' : numberFormat.format(value)
    }

    @Override
    void setValue(Object value) {
        if (value instanceof Number) {
            super.setValue(
                NumberUtils.convertNumberToTargetClass(value, numberClass)
            )
        } else {
            super.setValue(value)
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        def numberFormat = NumberFormat.getNumberInstance(LCH.locale)
        value = StringUtils.hasText(text) \
            ? NumberUtils.parseNumber(text, numberClass, numberFormat)
            : 0
    }
}
