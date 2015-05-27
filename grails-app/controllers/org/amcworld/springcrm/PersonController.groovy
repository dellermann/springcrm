/*
 * PersonController.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import com.google.gdata.data.extensions.*
import grails.converters.JSON
import javax.naming.AuthenticationException
import javax.naming.CommunicationException
import javax.naming.NameNotFoundException
import javax.servlet.http.HttpServletResponse
import net.sf.jmimemagic.Magic


/**
 * The class {@code PersonController} contains actions which manage persons
 * that belong to an organization.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class PersonController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    GoogleContactSyncService googleContactSyncService
    LdapService ldapService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (params.letter) {
            int num = Person.countByLastNameLessThan(params.letter)
            params.sort = 'lastName'
            params.offset = Math.floor(num / params.max) * params.max
        }

        [
            personInstanceList: Person.list(params),
            personInstanceTotal: Person.count()
        ]
    }

    def listEmbedded(Long organization) {
        List<Person> l
        int count
        Map<String, Object> linkParams

        if (organization) {
            def organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                l = Person.findAllByOrganization(organizationInstance, params)
                count = Person.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        }
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        [
            personInstanceList: l, personInstanceTotal: count,
            linkParams: linkParams
        ]
    }

    def create() {
        def personInstance = new Person()
        personInstance.properties = params
        [personInstance: personInstance]
    }

    def copy(Long id) {
        def personInstance = Person.get(id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), id])
            redirect action: 'index'
            return
        }

        personInstance = new Person(personInstance)
        render view: 'create', model: [personInstance: personInstance]
    }

    def save() {
        def personInstance = new Person(params)
        if (!params.picture?.size) {
            personInstance.picture = null
        }
        if (!personInstance.save(flush: true)) {
            render view: 'create', model: [personInstance: personInstance]
            return
        }

        request.personInstance = personInstance
        if (ldapService) {
            ldapService.save personInstance
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: personInstance.id
        }
    }

    def show(Long id) {
        def personInstance = Person.get(id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), id])
            redirect action: 'index'
            return
        }

        [personInstance: personInstance]
    }

    def edit(Long id) {
        def personInstance = Person.get(id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), id])
            redirect action: 'index'
            return
        }

        [personInstance: personInstance]
    }

    def update(Long id) {
        def personInstance = Person.get(id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), id])
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (personInstance.version > version) {
                personInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'person.label', default: 'Person')] as Object[], 'Another user has updated this Person while you were editing')
                render view: 'edit', model: [personInstance: personInstance]
                return
            }
        }

        if (params.autoNumber) {
            params.number = personInstance.number
        }
        byte [] picture = personInstance.picture
        personInstance.properties = params
        if (params.pictureRemove == '1') {
            personInstance.picture = null
        } else if (params.picture?.isEmpty()) {
            personInstance.picture = picture
        }
        if (!personInstance.save(flush: true)) {
            render view: 'edit', model: [personInstance: personInstance]
            return
        }

        request.personInstance = personInstance
        if (ldapService) {
            ldapService.save personInstance
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: personInstance.id
        }
    }

    def delete(Long id) {
        def personInstance = Person.get(id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        try {
            personInstance.delete flush: true
            if (ldapService) {
                ldapService.delete personInstance
            }

            flash.message = message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'person.label', default: 'Person')])
            redirect action: 'show', id: id
        }
    }

    def getPicture(Long id) {
        def personInstance = Person.get(id)
        if (!personInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        response.contentType = Magic.getMagicMatch(personInstance.picture).mimeType
        response.contentLength = personInstance.picture.length
        response.outputStream << personInstance.picture
        null
    }

    def getPhoneNumbers(Long id) {
        def personInstance = Person.get(id)
        if (!personInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        def phoneNumbers = [
                personInstance.phone,
                personInstance.phoneHome,
                personInstance.mobile,
                personInstance.fax,
                personInstance.phoneAssistant,
                personInstance.phoneOther,
                personInstance.organization.phone,
                personInstance.organization.phoneOther,
                personInstance.organization.fax
            ]
            .findAll({ it != '' })
            .unique()
        render phoneNumbers as JSON
    }

    def find(Long organization) {
        def organizationInstance = Organization.get(organization)
        def c = Person.createCriteria()
        def list = c.list {
            eq('organization', organizationInstance)
            and {
                or {
                    ilike('lastName', "${params.name}%")
                    ilike('firstName', "${params.name}%")
                }
            }
            order('lastName', 'asc')
        }
        render(contentType: 'text/json') {
            array {
                for (p in list) {
                    person id: p.id, name: p.fullName
                }
            }
        }
    }

    def gdatasync() {
        if (googleContactSyncService) {
            googleContactSyncService.sync()
            flash.message = message(code: 'default.gdata.allsync.success', args: [message(code: 'person.plural', default: 'persons')])
        }
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'index'
        }
    }

    def ldapexport(Long id) {
        if (ldapService) {
            if (id) {
                def personInstance = Person.get(id)
                if (personInstance) {
                    ldapService.save personInstance
                    flash.message = message(code: 'default.ldap.export.success', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])
                } else {
                    flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), id])
                }
                redirect action: 'show', id: id
                return
            }

            def personInstanceList = Person.list()
            personInstanceList.each { ldapService.save it }
            flash.message = message(code: 'default.ldap.allexport.success', args: [message(code: 'person.plural', default: 'persons')])
        }
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'index'
        }
    }

    def ldapdelete(Long id) {
        if (ldapService && id) {
            def personInstance = Person.get(id)
            if (personInstance) {
                ldapService.delete personInstance
            }
        }
        redirect action: 'index'
    }

    def handleAuthenticationException(AuthenticationException e) {
        handleLdapException 'authentication'
    }

    def handleConnectException(CommunicationException e) {
        handleLdapException 'communication'
    }

    def handleNameNotFoundException(NameNotFoundException e) {
        handleLdapException 'nameNotFound'
    }


    //-- Non-public methods ---------------------

    /**
     * Handles the given type of exception that occurred while accessing the
     * LDAP service.  The method collects all necessary information and
     * redirects to an error page.
     *
     * @param type  the type of exception that has been occurred
     */
    protected def handleLdapException(String type) {
        def origId = (actionName == 'save') ? request.personInstance.id \
            : params.id
        redirect controller: 'error', action: 'ldapPerson', params: [
                type: type, origAction: actionName, personId: origId
            ]
    }
}
