/*
 * GeneralController.groovy
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

import static grails.web.mapping.LinkGenerator.*

import org.grails.datastore.gorm.GormEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus


/**
 * The class {@code GeneralController} represents a generic base class for all
 * controllers of this application.
 *
 * @param <T>   the type of domain model which is handled by the controller
 * @author      Daniel Ellermann
 * @version     3.0
 * @since       3.0
 */
class GeneralController<T extends GormEntity<? super T>> {

    //-- Fields ---------------------------------

    UserService userService

    /**
     * The type of domain model which is handled by the controller.
     */
    protected Class<? extends T> domainType


    //-- Constructors ---------------------------

    /**
     * Creates a new generic controller which handles instances of the given
     * domain model type.
     *
     * @param domainType    the given domain model type
     */
    GeneralController(Class<? extends T> domainType) {
        this.domainType = domainType
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the localized label of this domain model type.
     *
     * @return  the label of this domain model type
     */
    protected String getLabel() {
        message(code: labelCode)
    }

    /**
     * Gets the message code of the label of this domain model type.
     *
     * @return  the message code of the label
     */
    protected String getLabelCode() {
        getMessageCode 'label'
    }

    /**
     * Gets a message code with the given suffix of this domain model type.
     * For example, if this domain model type is {@code Foo} and the given
     * suffix is {@code plural} the computed message code is {@code foo.plural}.
     *
     * @param suffix    the given suffix
     * @return          the computed message code
     */
    protected String getMessageCode(String suffix) {
        StringBuilder buf =
            new StringBuilder(domainType.simpleName.uncapitalize())
        buf << '.' << suffix

        buf.toString()
    }

    /**
     * Gets the message code depending on the current action.
     *
     * @return  the computed message code
     */
    private String getActionMessageCode() {
        StringBuilder buf = new StringBuilder('default.')
        switch (actionName) {
        case 'delete':
            buf << 'deleted'
            break
        case 'save':
            buf << 'created'
            break
        case 'update':
            buf << 'updated'
            break
        default:
            throw new IllegalStateException(
                "No message code for action '${actionName}' defined."
            )
        }
        buf << '.message'

        buf.toString()
    }

    /**
     * Gets the HTTP status code depending on the current action.
     *
     * @return  the computed status code
     */
    private HttpStatus getActionStatus() {
        switch (actionName) {
        case 'delete':
            HttpStatus.NO_CONTENT
            break
        case 'save':
            HttpStatus.CREATED
            break
        default:
            HttpStatus.OK
            break
        }
    }

    /**
     * Gets the currently logged in user.
     *
     * @return  the currently logged in user
     */
    protected User getCurrentUser() {
        userService.getCurrentUser()
    }

    /**
     * Handles the situation when a requested domain model instance does not
     * exist.  It sets an error message in flash scope and redirects to the
     * index action.
     */
    protected void notFound() {
        request.withFormat {
            //noinspection GroovyAssignabilityCheck
            form multipartForm {
                flash.message = message(
                    code: 'default.not.found.message', args: [label, params.id]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: HttpStatus.NOT_FOUND }
        }
    }

    /**
     * Handles the redirect after deletion of the given domain model instance.
     *
     * @param instance  the given domain model instance
     */
    protected void redirectAfterDelete(T instance) {
        request.withFormat {
            //noinspection GroovyAssignabilityCheck
            form multipartForm {
                flash.message = message(
                    code: getActionMessageCode(), args: [label, instance]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: getActionStatus() }
        }

    }

    /**
     * Handles the redirect after saving or updating the given domain model
     * instance.
     *
     * @param instance          the given domain model instance
     * @param redirectParams    any additional parameters which are included in
     *                          the redirect URL; may be {@code null}
     */
    protected void redirectAfterStorage(T instance, Map redirectParams = null) {
        request.withFormat {
            //noinspection GroovyAssignabilityCheck
            form multipartForm {
                flash.message = message(
                    code: getActionMessageCode(), args: [label, instance]
                ) as Object
                redirectInstance instance, redirectParams
            }
            '*' { respond instance, [status: getActionStatus()] }
        }
    }

    /**
     * Redirects to the proper action after storing the given instance.  The
     * action depends on the submitted {@code close} parameter and an optional
     * submitted return URL.
     *
     * @param instance          the stored instance
     * @param redirectParams    any additional parameters which are included in
     *                          the redirect URL; may be {@code null}
     */
    private void redirectInstance(T instance, Map redirectParams) {
        Map args = [: ]
        redirectParams = redirectParams ?: [: ]
        args[ATTRIBUTE_PARAMS] = redirectParams

        String returnUrl = params.returnUrl
        if (params.close) {
            if (returnUrl) {
                args.put ATTRIBUTE_URL, returnUrl
            } else {
                args.put ATTRIBUTE_RESOURCE, instance
                args.put ATTRIBUTE_METHOD, HttpMethod.GET.toString()
            }
        } else {
            args.put ATTRIBUTE_ACTION, 'edit'
            args.put ATTRIBUTE_ID, instance.ident()
            if (returnUrl) {
                redirectParams.put 'returnUrl', returnUrl
            }
        }

        if (!args.isEmpty()) {
            redirect args
        }
    }
}
