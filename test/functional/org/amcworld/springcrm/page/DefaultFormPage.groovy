/*
 * DefaultFormPage.groovy
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


package org.amcworld.springcrm.page

import geb.navigator.Navigator


class DefaultFormPage extends DefaultContentPage {

    //-- Class variables ------------------------

    static content = {
        cancelBtn { toolbarButtons[1] }
        errorFields { form.find('.field.error') }
        fieldset { form.find('fieldset', it) }
        form { $('div#content > form') }
        submitBtn { toolbarButtons[0] }
    }


    //-- Public methods -------------------------

    /**
     * Checks whether exactly the given list of fields are marked as error.
     *
     * @param fieldNames    the names of the field which must be marked as
     *                      error
     * @return              {@code true} if exactly the fields with the given
     *                      names and not more are marked as error;
     *                      {@code false} otherwise
     */
    void checkErrorFields(String... fieldNames) {
        List<String> shouldBeErrors = new ArrayList<String>()
        shouldBeErrors.addAll fieldNames
        List<String> shouldNotBeErrors = []

        for (Navigator errorField in errorFields) {
            Navigator inputs = errorField.find('input') + errorField.find('textarea')
            for (Navigator input in inputs) {
                String name = input.@name
                if (name in fieldNames) {
                    shouldBeErrors.remove name
                } else if (name) {
                    shouldNotBeErrors << name
                }
            }
        }
        if (shouldBeErrors) {
            println "Fields ${shouldBeErrors.toListString()} not marked as error, but should."
        }
        if (shouldNotBeErrors) {
            println "Fields ${shouldNotBeErrors.toListString()} marked as error, but shouldn't."
        }
        assert shouldBeErrors.empty && shouldNotBeErrors.empty
    }
}
