/*
 * EmptyListModule.groovy
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

import org.amcworld.springcrm.page.DefaultFormPage


class EmptyListModule extends geb.Module {

    //-- Class variables ------------------------

    static base = { $('div.empty-list') }
    static content = {
        buttons { moduleList ButtonModule, $('div.buttons') }
        message { $('p').text() }
    }


    //-- Public methods -------------------------

    void check(Class<DefaultFormPage> createPage, String createLinkText) {
        assert 'Diese Liste enthält keine Einträge.' == message
        assert 1 == buttons.size()
        buttons[0].checkLinkToPage createPage
        assert createLinkText == buttons[0].text()
    }
}
