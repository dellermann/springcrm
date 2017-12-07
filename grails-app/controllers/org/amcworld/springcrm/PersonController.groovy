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
class PersonController extends GenericDomainController<Person> {

    //-- Fields ---------------------------------

    GoogleContactSync googleContactSync
    LdapService ldapService
    OrganizationService organizationService
    PersonService personService


    //-- Constructors ---------------------------

    PersonController() {
        super(Person)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def find(Long organization) {
        Organization organizationInstance =
            organizationService.get(organization)
        List<Person> list = personService.searchPersons(
            organizationInstance, "${params.name}%".toString(),
            [sort: 'lastName', order: 'asc']
        )

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
            int num = personService.countByLastNameLessThan(
                params.letter.toString()
            )
            params.sort = 'lastName'
            int max = params.int('max')
            params.offset = Math.floor(num / max) * max
        }

        getIndexModel personService.list(params), personService.count()
    }

    def ldapdelete(Long id) {
        if (ldapService && id) {
            Person personInstance = personService.get(id)
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
                Person personInstance = personService.get(id)
                if (personInstance != null
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

            List<Person> personInstanceList = personService.list()
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
        Map linkParams = null

        if (organization) {
            def organizationInstance = organizationService.get(organization)
            if (organizationInstance) {
                list = personService.findAllByOrganization(
                    organizationInstance, params
                )
                count =
                    personService.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        }

        getListEmbeddedModel list, count, linkParams
    }

    def save() {
        Person personInstance = doSave()

        if (ldapService != null && !isExcludeFromSync(personInstance)) {
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
     * @param ids   a list of IDs of {@code Rating} instances that should be
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
    protected void lowLevelDelete(Person instance) {
        personService.delete instance.id

        if (ldapService) {
            ldapService.delete instance
        }
    }

    @Override
    protected Person lowLevelGet(Long id) {
        personService.get id
    }

    @Override
    protected Person lowLevelSave(Person instance) {
        personService.save instance
    }

    @Override
    protected Person updateInstance(Person instance) {
        byte [] picture = instance.picture
        bindData instance, params

        if (params.pictureRemove == '1') {
            instance.picture = null
        } else if (params.picture?.isEmpty()) {
            instance.picture = picture
        }

        lowLevelSave instance
    }
}
