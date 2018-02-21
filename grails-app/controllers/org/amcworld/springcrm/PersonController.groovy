/*
 * PersonController.groovy
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
import grails.web.http.HttpHeaders
import javax.naming.AuthenticationException
import javax.naming.CommunicationException
import javax.naming.NameNotFoundException
import net.sf.jmimemagic.Magic
import org.amcworld.springcrm.google.GoogleContactSync
import org.bson.types.ObjectId


/**
 * The class {@code PersonController} contains actions which manage persons
 * that belong to an organization.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured(['ROLE_ADMIN', 'ROLE_CONTACT'])
class PersonController {

    //-- Fields ---------------------------------

    GoogleContactSync googleContactSync
    LdapService ldapService
    OrganizationService organizationService
    PersonService personService
    UserService userService
    UserSettingService userSettingService


    //-- Public methods -------------------------

    def copy(Person person) {
        respond new Person(person), view: 'create'
    }

    def create() {
        respond new Person(params)
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        Person person = personService.delete(new ObjectId(id))
        if (ldapService != null) {
            ldapService.delete person
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.deleted.message',
                    args: [message(code: 'person.label'), person]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def edit(String id) {
        respond id == null ? null : personService.get(new ObjectId(id))
    }

    def find(@RequestParameter('organization') String id) {
        Organization org =
            id == null ? null : organizationService.get(new ObjectId(id))

        respond personService.search(org, params.name?.toString())
    }

    def gdatasync() {
        if (googleContactSync != null) {
            googleContactSync.sync currentUser
            flash.message = message(
                code: 'default.gdata.allsync.success', args: ['person.plural']
            ) as Object
        }

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'index'
        }
    }

    def getPhoneNumbers(String id) {
        Map res = null
        Person person = id == null ? null : personService.get(new ObjectId(id))
        if (person != null) {
            List<String> phoneNumbers = [
                    person.phone,
                    person.phoneHome,
                    person.mobile,
                    person.fax,
                    person.phoneAssistant,
                    person.phoneOther,
                    person.organization.phone,
                    person.organization.phoneOther,
                    person.organization.fax
                ]
                .findAll()
                .unique()
            res = [phoneNumbers: phoneNumbers]
        }

        respond res
    }

    def getPicture(String id) {
        Person person = id == null ? null : personService.get(new ObjectId(id))
        byte [] picture = person?.picture
        if (picture == null) {
            render status: NOT_FOUND
            return
        }

        /* Content-Length is not set by render() 😒 */
        response.setHeader HttpHeaders.CONTENT_LENGTH, picture.length.toString()
        render(
            contentType: Magic.getMagicMatch(picture).mimeType, file: picture
        )
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
            int num =
                personService.countByLastNameLessThan(params.letter.toString())
            params.sort = 'lastName'
            params.offset = (int) (Math.floor(num.toDouble() / max) * max)
        }

        respond(
            personService.list(params),
            model: [personCount: personService.count()]
        )
    }

    def ldapdelete(String id) {
        if (ldapService && id) {
            Person person = personService.get(new ObjectId(id))
            if (person != null) {
                ldapService.delete person
            }
        }

        redirect action: 'index'
    }

    def ldapexport(String id) {
        if (ldapService) {
            List<String> excludeIds = excludeFromSyncValues
            if (id) {
                Person person = personService.get(new ObjectId(id))
                if (person && !isExcludeFromSync(person, excludeIds)) {
                    ldapService.save person
                    flash.message = message(
                        code: 'default.ldap.export.success',
                        args: ['person.label', person.toString()]
                    ) as Object
                } else {
                    flash.message = message(
                        code: 'default.not.found.message',
                        args: ['person.label', id]
                    ) as Object
                }
                redirect action: 'show', id: id
                return
            }

            List<Person> personList = personService.list()
            for (Person personInstance : personList) {
                if (!isExcludeFromSync(personInstance, excludeIds)) {
                    ldapService.save personInstance
                }
            }
            flash.message = message(
                code: 'default.ldap.allexport.success', args: ['person.plural']
            ) as Object
        }

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'index'
        }
    }

    def listEmbedded(@RequestParameter('organization') String id) {
        List<Person> list = null
        Map model = null

        Organization organization =
            id == null ? null : organizationService.get(new ObjectId(id))
        if (organization != null) {
            list = personService.findAllByOrganization(organization, params)
            model = [
                personCount: personService.countByOrganization(organization),
                linkParams: [organization: id]
            ]
        }

        respond list, model: model
    }

    def save(Person person) {
        if (person == null) {
            notFound()
            return
        }

        try {
            personService.save person
        } catch (ValidationException ignored) {
            respond person.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.created.message',
                    args: [message(code: 'person.label'), person]
                ) as Object
                redirect person
            }
            '*' { respond person, [status: CREATED] }
        }
    }

    def show(String id) {
        respond id == null ? null : personService.get(new ObjectId(id))
    }

    def update(String id) {
        Person person = id ? personService.get(new ObjectId(id)) : null
        if (person == null) {
            notFound()
            return
        }

        byte [] picture = person.picture
        bindData person, params, [exclude: 'picture']

        if (params.pictureRemove == '1') {
            person.picture = null
        } else if (params.picture?.isEmpty()) {
            person.picture = picture
        }

        try {
            personService.save person
        } catch (ValidationException ignored) {
            respond person.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.updated.message',
                    args: [message(code: 'person.label'), person]
                ) as Object
                redirect person
            }
            '*' { respond person, [status: OK] }
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the currently logged in user.
     *
     * @return  the currently logged in user
     * @since   3.0
     */
    private User getCurrentUser() {
        userService.getCurrentUser()
    }

    /**
     * Gets the IDs of the {@code Rating} instances which should be exclude
     * from synchronization.
     *
     * @return  a list of IDs of {@code Rating} instance that should be
     *          excluded from synchronization
     * @since   2.0
     */
    private List<String> getExcludeFromSyncValues() {
        List<String> ids =
            userSettingService.getString(currentUser, 'excludeFromSync')
                ?.split(/,/)

        ids ?: []
    }

    /**
     * Handles the given type of exception that occurred while accessing the
     * LDAP service.  The method collects all necessary information and
     * redirects to an error page.
     *
     * @param type  the type of exception that has been occurred
     */
    private void handleLdapException(String type) {
        String id = actionName == 'save' ? ((Person) request['person']).id
            : params.id

        redirect(
            controller: 'error', action: 'ldapPerson',
            params: [type: type, origAction: actionName, personId: id]
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
    private static boolean isExcludeFromSync(Person p, List<String> ids) {
        p.organization.rating?.id?.toString() in ids
    }

    private void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'person.label'), params.id]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
