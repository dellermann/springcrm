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

import static org.springframework.http.HttpStatus.*

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
class WorkController {

    //-- Class fields -------------------------------

    WorkService workService


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

        Work work = workService.delete new ObjectId(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.deleted.message',
                    args: [message(code: 'work.label'), work]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def edit(String id) {
        respond id == null ? null : workService.get(new ObjectId(id))
    }

    def find(String name) {
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
        respond workService.get(new ObjectId(id))
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

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.created.message',
                    args: [message(code: 'work.label'), work]
                ) as Object
                redirect work
            }
            '*' { respond work, [status: CREATED] }
        }
    }

    def selectorList() {
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

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.updated.message',
                    args: [message(code: 'work.label'), work]
                ) as Object
                redirect work
            }
            '*' { respond work, [status: OK] }
        }
    }


    //-- Non-public methods ---------------------

    private void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'work.label'), params.id]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
