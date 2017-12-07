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
class CallController extends GenericDomainController<Call> {

    //-- Fields -------------------------------------

    CallService callService
    OrganizationService organizationService
    PersonService personService


    //-- Constructors ---------------------------

    CallController() {
        super(Call)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        Call callInstance = newInstance(params)
        if (callInstance.person != null) {
            callInstance.phone = callInstance.person.phone
            callInstance.organization = callInstance.person.organization
        } else if (callInstance.organization != null) {
            callInstance.phone = callInstance.organization.phone
        }

        getCreateModel callInstance
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
            handleLetter 'subject', callService.countBySubjectLessThan(letter)
            params.search = null
        }

        List<Call> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = callService.findAllBySubjectLike(searchFilter, params)
            count = callService.countBySubjectLike(searchFilter)
        } else {
            list = callService.list(params)
            count = callService.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person) {
        List<Call> list = []
        int count = 0
        Map linkParams = [: ]

        if (organization) {
            Organization organizationInstance =
                organizationService.get(organization)
            if (organizationInstance != null) {
                list = callService.findAllByOrganization(
                    organizationInstance, params
                )
                count = callService.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        } else if (person) {
            Person personInstance = personService.get(person)
            if (personInstance != null) {
                list = callService.findAllByPerson(personInstance, params)
                count = callService.countByPerson(personInstance)
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


    //-- Non-public methods -------------------------

    @Override
    protected void lowLevelDelete(Call instance) {
        callService.delete instance.id
    }

    @Override
    protected Call lowLevelSave(Call instance) {
        callService.save instance
    }

    @Override
    protected Call lowLevelGet(Long id) {
        callService.get id
    }
}
