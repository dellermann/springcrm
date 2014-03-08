/*
 * AutocompleteExModule.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


package org.amcworld.springcrm.module

import org.openqa.selenium.Keys


class AutocompleteExModule extends geb.Module {

    //-- Class variables ------------------------

    static base = { $('span.springcrm-autocompleteex-combobox') }
    static content = {
        body { $('#content') }
        dropdownBtn { $('a') }
        input { $('input[type=text]') }
    }


    //-- Public methods -------------------------

    /**
     * Set the value and selects the item with the given index.
     *
     * @param value the value to enter into the input control
     * @param idx   the zero-based position of the item which is to select
     * @return      the selected text
     */
    String select(String value, int idx = 0) {
        input << value
        Thread.sleep 1000
        input << Keys.ARROW_DOWN * (idx + 1) << Keys.TAB
        input.value()
    }
}
