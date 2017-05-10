/*
 * PersonController.groovy
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

import javax.naming.AuthenticationException
import javax.naming.CommunicationException
import javax.naming.NameNotFoundException
import net.sf.jmimemagic.Magic
import org.amcworld.springcrm.google.GoogleContactSync
import org.grails.datastore.mapping.query.api.BuildableCriteria


/**
 * The class {@code PersonController} contains actions which manage persons
 * that belong to an organization.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class PersonController extends GeneralController<Person> {

    //-- Fields ---------------------------------

    GoogleContactSync googleContactSync
    LdapService ldapService


    //-- Constructors ---------------------------

    PersonController() {
        super(Person)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def find(Long organization) {
        Organization organizationInstance = Organization.get(organization)
        BuildableCriteria c = Person.createCriteria()
        List<Person> list = (List<Person>) c.list {
            eq('organization', organizationInstance)
            and {
                or {
                    ilike('lastName', "${params.name}%")
                    ilike('firstName', "${params.name}%")
                }
            }
            order('lastName', 'asc')
        }

        [(getDomainInstanceName('List')): list]
    }

    def gdatasync() {
        if (googleContactSync) {
            googleContactSync.sync credential.loadUser()
            flash.message = message(
                code: 'default.gdata.allsync.success', args: [plural]
            ) as Object
        }

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'index'
        }
    }

    def getPhoneNumbers(Long id) {
        Person personInstance = getDomainInstanceWithStatus(id)
        if (personInstance == null) {
            return
        }

        List<String> phoneNumbers = [
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

        [phoneNumbers: phoneNumbers]
    }

    def getPicture(Long id) {
        Person personInstance = getDomainInstanceWithStatus(id)
        if (personInstance != null) {
            response.contentType =
                Magic.getMagicMatch(personInstance.picture).mimeType
            response.contentLength = personInstance.picture.length
            response.outputStream << personInstance.picture
        }

        null
    }

    def handleAuthenticationException(AuthenticationException ignore) {
        handleLdapException 'authentication'
    }

    def handleConnectException(CommunicationException ignore) {
        handleLdapException 'communication'
    }

    def handleNameNotFoundException(NameNotFoundException ignore) {
        handleLdapException 'nameNotFound'
    }

    def index() {
        if (params.letter) {
            int max = params.int('max')
            int num = Person.countByLastNameLessThan(params.letter.toString())
            params.sort = 'lastName'
            params.offset = Math.floor(num / max) * max
        }

        super.index()
    }

    def ldapdelete(Long id) {
        if (ldapService && id) {
            Person personInstance = Person.get(id)
            if (personInstance) {
                ldapService.delete personInstance
            }
        }

        redirect action: 'index'
    }

    def ldapexport(Long id) {
        if (ldapService) {
            List<Long> excludeIds = excludeFromSyncValues
            if (id) {
                Person personInstance = Person.get(id)
                if (personInstance
                    && !isExcludeFromSync(personInstance, excludeIds))
                {
                    ldapService.save personInstance
                    flash.message = message(
                        code: 'default.ldap.export.success',
                        args: [label, personInstance.toString()]
                    ) as Object
                } else {
                    flash.message = message(
                        code: 'default.not.found.message', args: [label, id]
                    ) as Object
                }
                redirect action: 'show', id: id
                return
            }

            List<Person> personInstanceList = Person.list()
            for (Person personInstance : personInstanceList) {
                if (!isExcludeFromSync(personInstance, excludeIds)) {
                    ldapService.save personInstance
                }
            }
            flash.message = message(
                code: 'default.ldap.allexport.success', args: [plural]
            ) as Object
        }

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'index'
        }
    }

    def listEmbedded(Long organization) {
        List<Person> list = null
        int count = 0
        Map<String, Object> linkParams = null

        if (organization) {
            def organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                list =
                    Person.findAllByOrganization(organizationInstance, params)
                count = Person.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        }

        getListEmbeddedModel list, count, linkParams
    }

    def save() {
        Person personInstance = saveInstance()

        if (ldapService && !isExcludeFromSync(personInstance)) {
            ldapService.save personInstance
        }
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the IDs of the {@code Rating} instances which should be exclude
     * from synchronization.
     *
     * @return  a list of IDs of {@code Rating} instance that should be
     *          excluded from synchronization
     * @since   2.0
     */
    private List<Long> getExcludeFromSyncValues() {
        List<Long> ids = credential.settings.excludeFromSync?.split(/,/)?.
            collect { it as Long }

        ids ?: []
    }

    /**
     * Handles the given type of exception that occurred while accessing the
     * LDAP service.  The method collects all necessary information and
     * redirects to an error page.
     *
     * @param type  the type of exception that has been occurred
     */
    private def handleLdapException(String type) {
        Long origId
        if (actionName == 'save') {
            origId = ((Person) request['personInstance']).id
        } else {
            origId = params.id
        }

        redirect(
            controller: 'error', action: 'ldapPerson',
            params: [type: type, origAction: actionName, personId: origId]
        )
    }

    /**
     * Checks whether or not the given person should be excluded from
     * synchronization.
     *
     * @param p     the given person
     * @param ids   a list of IDs of {@code Rating} instance that should be
     *              excluded from synchronization
     * @return      {@code true} if the given person should be excluded from
     *              synchronization; {@code false} otherwise
     * @since       2.0
     */
    private boolean isExcludeFromSync(Person p,
                                      List<Long> ids = excludeFromSyncValues)
    {
        ids.contains p.organization.rating?.id
    }

    @Override
    protected void lowLevelDelete(Person personInstance) {
        super.lowLevelDelete personInstance

        if (ldapService) {
            ldapService.delete personInstance
        }
    }

    @Override
    protected Person lowLevelUpdate(Person personInstance) {
        byte [] picture = personInstance.picture
        personInstance.properties = params

        if (params.pictureRemove == '1') {
            personInstance.picture = null
        } else if (params.picture?.isEmpty()) {
            personInstance.picture = picture
        }

        personInstance.save failOnError: true, flush: true
    }
}
