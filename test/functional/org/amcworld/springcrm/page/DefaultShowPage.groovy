/*
 * DefaultShowPage.groovy
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

import org.amcworld.springcrm.module.ShowFieldsetModule


class DefaultShowPage extends DefaultContentPage {

    //-- Class variables ------------------------

    static content = {
        createBtn { toolbarBtn(1) }
        copyBtn { toolbarBtn(3) }
        dataSheet { $('div#content > div.data-sheet') }
        deleteBtn { toolbarBtn(4) }
        editBtn { toolbarBtn(2) }
        fieldset { moduleList ShowFieldsetModule, dataSheet.find('section.fieldset') }
        listBtn { toolbarBtn(0) }
        timestamps { $('p.record-timestamps').text() }
    }


    //-- Public methods -------------------------

    /**
     * Checks the toolbar of the show view.
     *
     * @param controller    the name of the controller
     * @param id            the ID of the item
     * @since               1.4
     */
    void checkToolbar(String controller, long id) {
        def pg = this
        checkToolbar controller, [
            [
                action: 'list',
                color: 'white',
                icon: 'list',
                label: 'Liste'
            ],
            [
                action: 'create',
                color: 'green',
                icon: 'plus',
                label: 'Anlegen'
            ],
            [
                action: 'edit',
                color: 'green',
                icon: 'pencil-square-o',
                id: id,
                label: 'Bearbeiten'
            ],
            [
                action: 'copy',
                color: 'blue',
                icon: 'files-o',
                id: id,
                label: 'Kopieren'
            ],
            [
                action: 'delete',
                check: { btn ->
                    assert 'Sind Sie sicher?' == pg.withConfirm(false) { btn.click() }
                    assert browser.isAt(pg.class)
                },
                color: 'red',
                cssClasses: ['delete-btn'],
                icon: 'trash-o',
                id: id,
                label: 'Löschen'
            ]
        ]
    }
}
