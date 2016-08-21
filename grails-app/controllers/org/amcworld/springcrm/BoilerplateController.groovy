/*
 * BoilerplateController.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code BoilerplateController} contains actions which manage
 * boilerplates which are used in text fields.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
class BoilerplateController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        int max = params.max =
            Math.min(params.max ? params.int('max') : 10, 100)

        if (params.letter) {
            int num = Boilerplate.countByNameLessThan(params.letter.toString())
            params.sort = 'name'
            params.offset = (Math.floor(num / max) * max) as Integer
        }

        List<Boilerplate> list = Boilerplate.list(params)
        int count = Boilerplate.count()

        [boilerplateInstanceList: list, boilerplateInstanceTotal: count]
    }

    def create() {
        [boilerplateInstance: new Boilerplate(params)]
    }

    def copy(Long id) {
        Boilerplate boilerplateInstance = Boilerplate.get(id)
        if (!boilerplateInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'boilerplate.label'), id]
            )
            redirect action: 'index'
            return
        }

        boilerplateInstance = new Boilerplate(boilerplateInstance)
        render view: 'create', model: [boilerplateInstance: boilerplateInstance]
    }

    def save() {
        Boilerplate boilerplateInstance = new Boilerplate(params)
        if (!boilerplateInstance.save(flush: true)) {
            render(
                view: 'create',
                model: [boilerplateInstance: boilerplateInstance]
            )
            return
        }

        request.boilerplateInstance = boilerplateInstance
        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'boilerplate.label'),
                boilerplateInstance.toString()
            ]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: boilerplateInstance.id
        }
    }

    def show(Long id) {
        Boilerplate boilerplateInstance = Boilerplate.get(id)
        if (!boilerplateInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'boilerplate.label'), id]
            )
            redirect action: 'index'
            return
        }

        [boilerplateInstance: boilerplateInstance]
    }

    def edit(Long id) {
        Boilerplate boilerplateInstance = Boilerplate.get(id)
        if (!boilerplateInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'boilerplate.label'), id]
            )
            redirect action: 'index'
            return
        }

        [boilerplateInstance: boilerplateInstance]
    }

    def update(Long id) {
        Boilerplate boilerplateInstance = Boilerplate.get(id)
        if (!boilerplateInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'boilerplate.label'), id]
            )
            redirect action: 'index', id: id
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (boilerplateInstance.version > version) {
                boilerplateInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [
                        message(
                            code: 'boilerplate.label', default: 'Boilerplate'
                        )
                    ] as Object[],
                    'Another user has updated this Boilerplate while you were editing'
                )
                render(
                    view: 'edit',
                    model: [boilerplateInstance: boilerplateInstance]
                )
                return
            }
        }
        boilerplateInstance.properties = params
        if (!boilerplateInstance.save(flush: true)) {
            render(
                view: 'edit', model: [boilerplateInstance: boilerplateInstance]
            )
            return
        }

        request.boilerplateInstance = boilerplateInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'boilerplate.label'),
                boilerplateInstance.toString()
            ]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: boilerplateInstance.id
        }
    }

    def delete(Long id) {
        Boilerplate boilerplateInstance = Boilerplate.get(id)
        if (!boilerplateInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'boilerplate.label'), id]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        try {
            boilerplateInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'boilerplate.label')]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (DataIntegrityViolationException ignored) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'boilerplate.label')]
            )
            redirect action: 'show', id: id
        }
    }

    def find(String name) {
        List<Boilerplate> boilerplateInstanceList =
            Boilerplate.findAllByNameIlike("%${name}%")

        [boilerplateInstanceList: boilerplateInstanceList]
    }

    def get(Long id) {
        [boilerplateInstance: Boilerplate.get(id)]
    }
}
