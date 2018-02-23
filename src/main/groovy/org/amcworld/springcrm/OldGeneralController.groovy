/*
 * GeneralController.groovy
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

import grails.artefact.Controller
import javax.servlet.http.HttpServletResponse
import org.grails.datastore.gorm.GormEntity
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code OldGeneralController} represents a generic base class for all
 * controllers of this application.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
class OldGeneralController<T extends GormEntity<? super T>> implements Controller {

    //-- Class fields ---------------------------

    static allowedMethods = [
        copy: 'GET', delete: 'GET', edit: 'GET', index: 'GET', save: 'POST',
        show: 'GET', update: 'POST'
    ]


    //-- Fields ---------------------------------

    /**
     * The type of domain model which is handled by this controller.
     */
    protected Class<? extends T> domainType

    /**
     * A cache for the computed instance names of the domain model type.  The
     * key represents the suffix (may be {@code null} and the value the
     * computed name.
     */
    private Map<String, String> cachedDomainInstanceNames


    //-- Constructors ---------------------------

    /**
     * Creates a new generic controller the handle instances of the given
     * domain model type.
     *
     * @param domainType    the given domain model type
     */
    OldGeneralController(Class<? extends T> domainType) {
        this.domainType = domainType

        cachedDomainInstanceNames = new HashMap<>()
    }


    //-- Public methods -------------------------

    /**
     * Creates a copy of a domain model instance with the given ID and shows
     * that instance in the create view.
     *
     * @param id    the ID of the domain model instance
     * @return      any rendering information
     */
    def copy(Long id) {
        T instance = getDomainInstance(id)
        if (instance) {
            instance = domainType.newInstance(instance)
            render view: 'create', model: [(domainInstanceName): instance]
        }
    }

    /**
     * Creates a new instance of the domain model using any optional parameters
     * given in the request.
     *
     * @return  the model for the view
     */
    def create() {
        getCreateModel newInstance(params)
    }

    /**
     * Deletes the domain model instance with the given ID.  The method checks
     * for concurrent modification.
     *
     * @param id    the ID of the domain model instance
     * @return      any redirection information
     */
    def delete(Long id) {
        T instance = getDomainInstance(id)
        if (instance != null && !checkAccess(instance)) {
            redirect action: 'index', params: indexActionParams
            return
        }

        deleteInstance instance
    }

    /**
     * Edits the domain model instance with the given ID.  The method loads the
     * instance and shows it in the edit view.
     *
     * @param id    the ID of the domain model instance
     * @return      the model for the view
     */
    def edit(Long id) {
        T instance = getDomainInstance(id)

        if (!checkAccess(instance)) {
            redirect action: 'index', params: indexActionParams
            return
        }

        getEditModel instance
    }

    /**
     * Gets the domain model instance with the given ID for usage in
     * client-side scripts.
     *
     * @param id    the ID of the domain model instance
     * @return      the model for the view
     */
    def get(Long id) {
        [(domainInstanceName): getDomainInstanceWithStatus(id)]
    }

    /**
     * Loads all domain model instances which are defined by the request
     * parameters and shows them in the index view.
     *
     * @return  the model for the view
     */
    def index() {
        getIndexModel domainType.list(params), domainType.count()
    }

    /**
     * Saves a new instance of the domain model using the form data in the
     * request parameters.
     *
     * @return  any rendering information
     */
    def save() {
        saveInstance()
    }

    /**
     * Shows the domain model instance with the given ID.  The method loads the
     * instance and shows it in the show view.
     *
     * @param id    the ID of the domain model instance
     * @return      the model for the view
     */
    def show(Long id) {
        [(domainInstanceName): getDomainInstance(id)]
    }

    /**
     * Updates an existing instance of the domain model with the given ID using
     * the form data in the request parameters.
     *
     * @param id    the ID of the domain model instance
     * @return      any rendering information
     */
    def update(Long id) {
        updateInstance id
    }


    //-- Non-public methods ---------------------

    /**
     * Checks whether or not the currently logged in user is allowed to perform
     * the required action.  The implementation in this class always returns
     * {@code true}.  Derived classes may overwrite this method to do real
     * access checking.
     *
     * @param instance  the instance which should be processed
     * @return          {@code true} if access is permitted; {@code false}
     *                  otherwise
     */
    protected boolean checkAccess(T instance) {
        true
    }

    /**
     * Deletes the given domain model instance.
     *
     * @param instance  the domain model instance which should be deleted
     */
    protected void deleteInstance(T instance) {
        try {
            lowLevelDelete instance
            flash.message = message(
                code: 'default.deleted.message', args: [label]
            ) as Object
            request[domainInstanceName] = instance
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message', args: [label]
            ) as Object
            redirect action: 'show', id: instance.id, params: showActionParams
        }
    }

    /**
     * Gets the model for the create view containing the given domain model
     * instance.
     *
     * @param instance  the given domain model instance
     * @return          the model for the create view
     */
    protected Map<String, Object> getCreateModel(T instance) {
        [(domainInstanceName): instance]
    }

    /**
     * Gets the domain model instance with the given ID.  If no such instance
     * exists the method stores an error message in the flash scope and
     * redirects to the index action.
     *
     * @param id    the ID of the domain model instance
     * @return      the instance of the domain model or {@code null} if no such
     *              domain model exists
     */
    protected <D extends T> D getDomainInstance(Long id) {
        D instance = domainType.get(id)
        if (!instance) {
            flash.message = message(
                code: 'default.not.found.message', args: [label, id]
            ) as Object
            redirect action: 'index', params: indexActionParams
        }

        instance
    }

    /**
     * Gets the name of the domain model instance in the view models optionally
     * using the given suffix.  For example, for a domain model type
     * {@code Foo} the computed instance name is {@code fooInstance}.  If a
     * suffix is specified it is appended to the instance name.
     *
     * @param suffix    an optional suffix that should be appended to the
     *                  instance name
     * @return          the computed instance name
     */
    protected String getDomainInstanceName(String suffix = null) {
        String name = cachedDomainInstanceNames[suffix]
        if (name == null) {
            String baseName = cachedDomainInstanceNames[null]
            if (baseName == null) {
                baseName = domainType.simpleName.uncapitalize() + 'Instance'
                cachedDomainInstanceNames[null] = baseName
            }

            name = baseName
            if (suffix != null) {
                name += suffix
                cachedDomainInstanceNames[suffix] = name
            }
        }

        name
    }

    /**
     * Gets the domain model instance with the given ID.  If no such instance
     * exists the method renders HTTP status 404.
     *
     * @param id    the ID of the domain model instance
     * @return      the instance of the domain model or {@code null} if no such
     *              domain model exists
     */
    protected T getDomainInstanceWithStatus(Long id) {
        T instance = domainType.get(id)
        if (instance == null) {
            render status: HttpServletResponse.SC_NOT_FOUND
        }

        instance
    }

    /**
     * Gets the model for the edit view containing the given domain model
     * instance.
     *
     * @param instance  the given domain model instance
     * @return          the model for the edit view
     */
    protected Map<String, Object> getEditModel(T instance) {
        [(domainInstanceName): instance]
    }

    /**
     * Gets additional parameters which are used to generate the URL of the
     * index action.  The method of this class simply return {@code null} which
     * means no parameters.  Derived classes may override this method and
     * provide additional parameters.
     *
     * @return  any additional parameters
     */
    protected Map<String, Object> getIndexActionParams() {
        null
    }

    /**
     * Gets the model for the index view containing the given list of items and
     * count.
     *
     * @param list  the list of items
     * @param count the count of items
     * @return      the model for the view
     */
    protected Map<String, Object> getIndexModel(List<T> list, int count) {
        [
            (getDomainInstanceName('List')): list,
            (getDomainInstanceName('Total')): count
        ]
    }

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
     * Gets the model for the list embedded view containing the given list of
     * items, the count and any parameters which are added to the links within
     * the embedded list.
     *
     * @param list          the list of items
     * @param count         the count of items
     * @param linkParams    any link parameters
     * @return              the model for the view
     */
    protected Map<String, Object> getListEmbeddedModel(
        List<T> list, int count, Map<String, Object> linkParams
    ) {
        [
            (getDomainInstanceName('List')): list,
            (getDomainInstanceName('Total')): count,
            linkParams: linkParams
        ]
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
     * Gets the localized plural label of this domain model type.
     *
     * @return  the plural label of this domain model type
     */
    protected String getPlural() {
        message(code: pluralCode)
    }

    /**
     * Gets the message code of the plural label of this domain model type.
     *
     * @return  the message code of the plural label
     */
    protected String getPluralCode() {
        getMessageCode 'plural'
    }

    /**
     * Gets additional parameters which are used to generate the URL of the
     * show action.  The method of this class simply return {@code null} which
     * means no parameters.  Derived classes may override this method and
     * provide additional parameters.
     *
     * @return  any additional parameters
     */
    protected Map<String, Object> getShowActionParams() {
        null
    }

    /**
     * Gets the data of the currently logged in user.
     *
     * @return  the user data
     */
    protected User getUser() {
        null
    }

    /**
     * Checks whether or not the currently logged in user is administrator.
     *
     * @return  {@code true} if the currently logged in user is administator;
     *          {@code false} otherwise
     */
    protected boolean isAdmin() {
        false
    }

    /**
     * Deletes the given domain model instance from the underlying layer.
     *
     * @param instance  the given domain model instance
     */
    protected void lowLevelDelete(T instance) {
        instance.delete flush: true
    }

    /**
     * Creates a new instance from the submitted form data and saves the domain
     * model instance in the underlying layer.
     *
     * @return  the generated and saved domain model instance
     */
    protected <D extends T> D lowLevelSave() {
        newInstance(params).save failOnError: true, flush: true
    }

    /**
     * Updates the given domain model instance with the submitted form data and
     * updates it in the underlying layer.
     *
     * @param instance  the given domain model instance or {@code null} if an
     *                  error occurred
     */
    protected <D extends T> D lowLevelUpdate(T instance) {
        instance.properties = params

        instance.save failOnError: true, flush: true
    }

    /**
     * Creates a new instance of the domain model and initializes its
     * properties with the given parameters.
     *
     * @param params    any parameters used for object initialization
     * @return          the new instance
     */
    protected <D extends T> D newInstance(Map<String, Object> params) {
        domainType.newInstance params
    }

    /**
     * Saves a new instance of the domain model using the form data in the
     * request parameters.
     *
     * @return  the saved instance or {@code null} if an error occurred
     */
    protected <D extends T> D saveInstance() {
        D instance = lowLevelSave()
        if (instance == null) {
            render view: 'create', model: getCreateModel(instance)
            return null
        }

        request[domainInstanceName] = instance
        flash.message = message(
            code: 'default.created.message', args: [label, instance.toString()]
        ) as Object

        instance
    }

    /**
     * Updates an existing instance of the domain model with the given ID using
     * the form data in the request parameters.
     *
     * @param id    the ID of the domain model instance
     * @return      the saved instance or {@code null} if an error occurred
     */
    protected <D extends T> D updateInstance(Long id) {
        D instance = getDomainInstance(id)
        if (instance == null || !checkAccess(instance)) {
            redirect action: 'index', params: indexActionParams
            return null
        }

        if (params.version) {
            long version = params.long('version')
            if (instance.version > version) {
                instance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [label] as Object[], ''
                )
                render view: 'edit', model: getEditModel(instance)
                return null
            }
        }

        if (instance instanceof NumberedDomain && params.autoNumber) {
            params.number = instance.number
        }

        if (lowLevelUpdate(instance) == null) {
            render view: 'edit', model: getEditModel(instance)
            return null
        }

        request[domainInstanceName] = instance
        flash.message = message(
            code: 'default.updated.message', args: [label, instance.toString()]
        ) as Object

        instance
    }
}
