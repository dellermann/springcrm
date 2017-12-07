/*
 * WorkController.groovy
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

import org.grails.datastore.mapping.query.api.BuildableCriteria


/**
 * The class {@code WorkController} contains actions which manage works
 * (services).
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class WorkController extends SalesItemController<Work> {

    //-- Fields -------------------------------------

    WorkService workService


    //-- Constructors ---------------------------

    WorkController() {
        super(Work)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def find(String name) {
        Integer number = null
        try {
            number = name as Integer
        } catch (NumberFormatException ignored) { /* ignored */ }

        BuildableCriteria c = Work.createCriteria()
        List<Work> list = workService.findAllByNumberOrNameIlike(
            number, "%${name}%", [sort: 'number', order: 'asc']
        )

        [(getDomainInstanceName('List')): list]
    }

    def get(Long id) {
        super.get id
    }

    def index() {
        if (params.letter) {
            int num = workService.countByNameLessThan(params.letter.toString())
            params.sort = 'name'
            int max = params.int('max')
            params.offset = Math.floor(num / max) * max
        }

        getIndexModel workService.list(params), workService.count()
    }

    def save() {
        super.save()
    }

    def selectorList() {
        String searchFilter =
            params.search ? "%${params.search}%".toString() : ''
        String letter = params.letter?.toString()
        if (letter) {
            int num
            if (params.search) {
                num = workService.countByNameLessThanAndNameLike(
                    letter, searchFilter
                )
            } else {
                num = workService.countByNameLessThan(letter)
            }
            params.sort = 'name'
            int max = params.int('max')
            params.offset = Math.floor(num / max) * max
        }

        List<Product> list
        int count
        if (params.search) {
            list = workService.findAllByNameLike(searchFilter, params)
            count = workService.countByNameLike(searchFilter)
        } else {
            list = workService.list(params)
            count = workService.count()
        }

        getIndexModel list, count
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods -------------------------

    @Override
    protected Work lowLevelGet(Long id) {
        workService.get id
    }

    @Override
    protected Work lowLevelSave(Work instance) {
        workService.save instance
    }
}
