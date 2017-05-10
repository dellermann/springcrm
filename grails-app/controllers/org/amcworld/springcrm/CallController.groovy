/*
 * CallController.groovy
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
 * The class {@code CallController} contains actions which manage phone calls
 * associated to an organization or person.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class CallController extends GeneralController<Call> {

    //-- Constructors ---------------------------

    CallController() {
        super(Call)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        Map<String, Object> model = super.create()

        Call callInstance = model[domainInstanceName] as Call
        if (callInstance.person) {
            callInstance.phone = callInstance.person.phone
            callInstance.organization = callInstance.person.organization
        } else if (callInstance.organization) {
            callInstance.phone = callInstance.organization.phone
        }

        model
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def index() {
        String letter = params.letter?.toString()
        if (letter) {
            int max = params.int('max')
            int num = Call.countBySubjectLessThan(letter)
            params.sort = 'subject'
            params.offset = (Math.floor(num / max) * max) as int
            params.search = null
        }

        List<Call> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Call.findAllBySubjectLike(searchFilter, params)
            count = Call.countBySubjectLike(searchFilter)
        } else {
            list = Call.list(params)
            count = Call.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person) {
        List<Call> list = []
        int count = 0
        Map<String, Object> linkParams = [: ]

        if (organization) {
            def organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                list = Call.findAllByOrganization(organizationInstance, params)
                count = Call.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        } else if (person) {
            def personInstance = Person.get(person)
            if (personInstance) {
                list = Call.findAllByPerson(personInstance, params)
                count = Call.countByPerson(personInstance)
                linkParams = [person: personInstance.id]
            }
        }

        getListEmbeddedModel list, count, linkParams
    }

    def save() {
        super.save()
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }
}
