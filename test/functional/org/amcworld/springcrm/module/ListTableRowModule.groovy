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

    static base = { $('tr') }
    static content = {

        /*
         * XXX this doesn't work although it is valid code: it only returns
         * on button, the edit button
         */
//        actionButtons { moduleList ButtonModule, $('td.action-buttons') }
        deleteButton { $('td.action-buttons > a', 1) }
        editButton { $('td.action-buttons > a', 0) }
        td { $('td', it) }
    }


    //-- Public methods -------------------------

    void checkActionButtons(Class<DefaultFormPage> editPage, String controller,
                            long id)
    {

        /*
         * XXX the following code depends on a working content "actionButtons"
         */
//        2 == row.actionButtons.size()
//        def editBtn = row.actionButtons[0]
//        editBtn.checkLinkToPage editPage, id
//        editBtn.checkColor 'green'
//        assert 'Bearbeiten' == editBtn.text()
//        def deleteBtn = row.actionButtons[1]
//        assert Page.makeUrl('call', 'delete', call.id) == deleteBtn.@href
//        deleteBtn.checkColor 'red'
//        deleteBtn.checkCssClasses 'delete-btn'
//        assert 'Löschen' == deleteBtn.text()
//        assert 'Sind Sie sicher?' == withConfirm(false) { deleteBtn.click() }

        assert editPage.getAbsUrl(id) == editButton.@href
        assert ['button', 'green', 'small'] == editButton.classes()
        assert 'Bearbeiten' == editButton.text()

        assert Page.makeUrl(controller, 'delete', id) == deleteButton.@href
        assert ['button', 'delete-btn', 'red', 'small'] == deleteButton.classes()
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
