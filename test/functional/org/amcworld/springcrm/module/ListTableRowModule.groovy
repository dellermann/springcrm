/*
 * ListTableRowModule.groovy
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

import geb.Page
import org.amcworld.springcrm.page.DefaultFormPage


class ListTableRowModule extends geb.Module {

    //-- Class variables ------------------------

    static content = {
        actionButtons { moduleList ButtonModule, $('td.action-buttons .button') }
        deleteButton { actionButtons[1] }
        editButton { actionButtons[0] }
        td { $('td', it) }
    }


    //-- Public methods -------------------------

    void checkActionButtons(String controller, long id) {
        2 == actionButtons.size()

        assert Page.makeUrl(controller, 'edit', id) == editButton.@href
        editButton.checkColor 'green'
        editButton.checkSize 'small'
        assert 'Bearbeiten' == editButton.text()

        assert Page.makeUrl(controller, 'delete', id) == deleteButton.@href
        deleteButton.checkColor 'red'
        deleteButton.checkSize 'small'
        deleteButton.checkCssClasses 'delete-btn'
        assert 'Löschen' == deleteButton.text()
        assert 'Sind Sie sicher?' == withConfirm(false) { deleteButton.click() }
        assert browser.isAt(page.class)
    }

    void checkTdClasses(String... classes) {
        for (int i = 0; i < classes.length; i++) {
            assert classes[i] in td(i).classes()
        }
    }
}
