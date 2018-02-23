/*
 * WorkController.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.bson.types.ObjectId
import org.grails.datastore.mapping.query.api.BuildableCriteria


/**
 * The class {@code WorkController} contains actions which manage works
 * (services).
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured(['ROLE_ADMIN', 'ROLE_WORK'])
class WorkController extends GeneralController<Work> {

    //-- Class fields -------------------------------

    WorkService workService


    //-- Constructors ---------------------------

    WorkController() {
        super(Work)
    }


    //-- Public methods -------------------------

    def copy(Work work) {
        respond new Work(work), view: 'create'
    }

    def create() {
        respond new Work(params)
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        redirectAfterDelete workService.delete(new ObjectId(id))
    }

    def edit(String id) {
        respond id == null ? null : workService.get(new ObjectId(id))
    }

    def find(String name) {
        // TODO revise this method!
        Integer number = null
        try {
            number = name as Integer
        } catch (NumberFormatException ignored) { /* ignored */ }

        BuildableCriteria c = Work.createCriteria()
        List<Work> list = (List<Work>) c.list {
            or {
                eq 'number', number
                ilike 'name', "%${name}%"
            }
            order 'number', 'asc'
        }

        respond list
    }

    def get(String id) {
        respond id == null ? null : workService.get(new ObjectId(id))
    }

    def index() {
        respond(
            workService.list(params), model: [workCount: workService.count()]
        )
    }

    def save(Work work) {
        if (work == null) {
            notFound()
            return
        }

        try {
            workService.save work
        } catch (ValidationException ignored) {
            respond work.errors, view: 'create'
            return
        }

        redirectAfterStorage work
    }

    def selectorList() {
        // TODO implement this method!
//        super.selectorList()
    }

    def show(String id) {
        respond id == null ? null : workService.get(new ObjectId(id))
    }

    def update(Work work) {
        if (work == null) {
            notFound()
            return
        }

        try {
            workService.save work
        } catch (ValidationException ignored) {
            respond work.errors, view: 'edit'
            return
        }

        redirectAfterStorage work
    }
}
