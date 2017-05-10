/*
 * BoilerplateController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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


package org.amcworld.springcrm


/**
 * The class {@code BoilerplateController} contains actions which manage
 * boilerplates which are used in text fields.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.1
 */
class BoilerplateController extends GeneralController<Boilerplate> {

    //-- Constructors ---------------------------

    BoilerplateController() {
        super(Boilerplate)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def get(Long id) {
        super.get id
    }

    def find(String name) {
        List<Boilerplate> boilerplateInstanceList =
            Boilerplate.findAllByNameIlike("%${name}%")

        [(getDomainInstanceName('List')): boilerplateInstanceList]
    }

    def index() {
        if (params.letter) {
            int max = params.int('max')
            int num = Boilerplate.countByNameLessThan(params.letter.toString())
            params.sort = 'name'
            params.offset = (Math.floor(num / max) * max) as Integer
        }

        super.index()
    }

    def save() {
        super.save()
        request['redirectParams'] = [noLruRecord: params.noLruRecord]
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }
}
