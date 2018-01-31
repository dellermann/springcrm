/*
 * OrganizationController.groovy
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

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.bson.types.ObjectId


/**
 * The class {@code OrganizationController} contains actions which manage
 * organizations.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured(['ROLE_ADMIN', 'ROLE_CONTACT'])
class OrganizationController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']


    //-- Fields -------------------------------------

    ConfigService configService
    OrganizationService organizationService


    //-- Public methods -------------------------

    def copy(Organization organization) {
        respond new Organization(organization), view: 'create'
    }

    def create() {
        respond new Organization(params)
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        Organization organization = organizationService.delete new ObjectId(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.deleted.message',
                    args: [message(code: 'organization.label'), organization]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def edit(String id) {
        respond id == null ? null : organizationService.get(new ObjectId(id))
    }

    def find(Byte type) {
        List<Organization> list
        if (type) {
            list = organizationService.findAllByRecTypeInListAndNameLike(
                [type, 3 as byte] as Set<Byte>, "%${params.name}%",
                [sort: 'name']
            )
        } else {
            list = organizationService.findAllByNameLike(
                "%${params.name}%", [sort: 'name']
            )
        }

        respond list
    }

    def get(String id) {
        respond id == null ? null : organizationService.get(new ObjectId(id))
    }

    def getPhoneNumbers(String id) {
        Map res = null
        Organization organization = id == null ? null
            : organizationService.get(new ObjectId(id))
        if (organization != null) {
            List<String> phoneNumbers = [
                    organization.phone,
                    organization.phoneOther,
                    organization.fax
                ]
                .findAll()
                .unique()
            res = [phoneNumbers: phoneNumbers]
        }

        respond res
    }

    def getTermOfPayment(String id) {
        int termOfPayment = configService.getInteger('termOfPayment', 14i)

        Organization organization = id == null ? null
            : organizationService.get(new ObjectId(id))
        if (organization?.termOfPayment != null) {
            termOfPayment = organization.termOfPayment
        }

        respond termOfPayment: termOfPayment
    }

    def index(Byte listType) {
        Set<Byte> types = [listType, 3 as byte] as Set<Byte>
        String letter = params.letter?.toString()
        if (letter) {
            int max = params.int('max')
            int num
            if (listType) {
                num = organizationService.countByNameLessThanAndRecTypeInList(
                    letter, types
                )
            } else {
                num = organizationService.countByNameLessThan(letter)
            }
            params.sort = 'name'
            params.offset = (int) (Math.floor(num.toDouble() / max) * max)
        }

        List<Organization> list
        int count
        if (listType) {
            list = organizationService.findAllByRecTypeInList(types, params)
            count = organizationService.countByRecTypeInList(types)
        } else {
            list = organizationService.list(params)
            count = organizationService.count()
        }

        respond list, model: [organizationCount: count]
    }

    def save(Organization organization) {
        if (organization == null) {
            notFound()
            return
        }

        try {
            organizationService.save organization
        } catch (ValidationException ignored) {
            respond organization.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.created.message',
                    args: [message(code: 'organization.label'), organization]
                ) as Object
                redirect organization
            }
            '*' { respond organization, [status: CREATED] }
        }
    }

    def show(String id) {
        respond id == null ? null : organizationService.get(new ObjectId(id))
    }

    def update(Organization organization) {
        if (organization == null) {
            notFound()
            return
        }

        try {
            organizationService.save organization
        } catch (ValidationException ignored) {
            respond organization.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.updated.message',
                    args: [message(code: 'organization.label'), organization]
                ) as Object
                redirect organization
            }
            '*' { respond organization, [status: OK] }
        }
    }


    //-- Non-public methods ---------------------

    private void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'organization.label'), params.id]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
