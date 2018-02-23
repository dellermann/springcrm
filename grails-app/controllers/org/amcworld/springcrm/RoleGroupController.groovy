/*
 * RoleGroupController.groovy
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

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.bson.types.ObjectId


/**
 * The class {@code RoleGroupController} manages the user groups of the
 * application.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Secured(['ROLE_ADMIN', 'ROLE_USER'])
class RoleGroupController extends GeneralController<RoleGroup> {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']


    //-- Fields ---------------------------------

    RoleGroupService roleGroupService


    //-- Constructors ---------------------------

    RoleGroupController() {
        super(RoleGroup)
    }


    //-- Public methods -------------------------

    def copy(RoleGroup roleGroup) {
        respond new RoleGroup(roleGroup), view: 'create'
    }

    def create() {
        respond new RoleGroup(params)
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        redirectAfterDelete roleGroupService.delete(new ObjectId(id))
    }

    def edit(String id) {
        respond id == null ? null : roleGroupService.get(new ObjectId(id))
    }

    def index() {
        respond(
            roleGroupService.list(params),
            model: [roleGroupCount: roleGroupService.count()]
        )
    }

    def save(RoleGroup roleGroup) {
        if (roleGroup == null) {
            notFound()
            return
        }

        try {
            redirectAfterStorage roleGroupService.save(roleGroup)
        } catch (ValidationException ignored) {
            respond roleGroup.errors, view: 'create'
        }
    }

    def show(String id) {
        respond id == null ? null : roleGroupService.get(new ObjectId(id))
    }

    def update(RoleGroup roleGroup) {
        if (roleGroup == null) {
            notFound()
            return
        }

        roleGroup.markDirty 'authorities'
        try {
            redirectAfterStorage roleGroupService.save(roleGroup)
        } catch (ValidationException ignored) {
            respond roleGroup.errors, view: 'edit'
        }
    }
}
