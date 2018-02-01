/*
 * PhoneCallController.groovy
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
import grails.web.RequestParameter
import org.bson.types.ObjectId


/**
 * The class {@code PhoneCallController} contains actions which manage phone
 * calls associated to an organization or person.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured(['ROLE_ADMIN', 'ROLE_CALL'])
class PhoneCallController {

    //-- Fields ---------------------------------

    OrganizationService organizationService
    PersonService personService
    PhoneCallService phoneCallService


    //-- Public methods -------------------------

    def copy(PhoneCall call) {
        respond new PhoneCall(call), view: 'create'
    }

    def create() {
        respond new PhoneCall(params)
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        PhoneCall call = phoneCallService.delete new ObjectId(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.deleted.message',
                    args: [message(code: 'phoneCall.label'), call]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def edit(String id) {
        respond id == null ? null : phoneCallService.get(new ObjectId(id))
    }

    def index() {
        String letter = params.letter?.toString()
        if (letter) {
            int max = params.int('max')
            int num = phoneCallService.countBySubjectLessThan(letter)
            params.sort = 'subject'
            params.offset = (int) (Math.floor(num.toDouble() / max) * max)
            params.remove 'search'
        }

        List<PhoneCall> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = phoneCallService.findAllBySubjectLike(searchFilter, params)
            count = phoneCallService.countBySubjectLike(searchFilter)
        } else {
            list = phoneCallService.list(params)
            count = phoneCallService.count()
        }

        respond list, model: [phoneCallCount: count]
    }

    def listEmbedded(@RequestParameter('organization') String organizationId,
                     @RequestParameter('person') String personId) {
        List<PhoneCall> list = null
        Map model = null

        if (organizationId != null) {
            Organization organization =
                organizationService.get(new ObjectId(organizationId))
            if (organization != null) {
                list = phoneCallService.findAllByOrganization(
                    organization, params
                )
                model = [
                    phoneCallCount:
                        phoneCallService.countByOrganization(organization),
                    linkParams: [organization: organization.id.toString()]
                ]
            }
        } else if (personId != null) {
            Person person = personService.get(new ObjectId(personId))
            if (person != null) {
                list = phoneCallService.findAllByPerson(person, params)
                model = [
                    phoneCallCount: phoneCallService.countByPerson(person),
                    linkParams: [person: person.id.toString()]
                ]
            }
        }

        respond list, model: model
    }

    def save(PhoneCall call) {
        if (call == null) {
            notFound()
            return
        }

        try {
            phoneCallService.save call
        } catch (ValidationException ignored) {
            respond call.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.created.message',
                    args: [message(code: 'phoneCall.label'), call]
                ) as Object
                redirect call
            }
            '*' { respond call, [status: CREATED] }
        }
    }

    def show(String id) {
        respond id == null ? null : phoneCallService.get(new ObjectId(id))
    }

    def update(PhoneCall call) {
        if (call == null) {
            notFound()
            return
        }

        try {
            phoneCallService.save call
        } catch (ValidationException ignored) {
            respond call.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.updated.message',
                    args: [message(code: 'phoneCall.label'), call]
                ) as Object
                redirect call
            }
            '*' { respond call, [status: OK] }
        }
    }


    //-- Non-public methods ---------------------

    private void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'phoneCall.label'), params.id]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
