/*
 * InvoicingController.groovy
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

import grails.util.GrailsNameUtils
import org.grails.datastore.mapping.query.api.BuildableCriteria


/**
 * The class {@code InvoicingController} handles actions common to all
 * invoicing transactions.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
abstract class InvoicingController<T extends InvoicingTransaction>
    extends GenericDomainController<T>
{

    //-- Fields ---------------------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService


    //-- Constructors ---------------------------

    /**
     * Creates a new controller the handle invoicing transaction instances of
     * the given domain model type.
     *
     * @param domainType the given domain model type
     */
    InvoicingController(Class<? extends T> domainType) {
        super(domainType)
    }


    //-- Public methods -------------------------

    /**
     * Prepares the form to edit payment fields.
     *
     * @param id    the ID of the invoicing transaction instance
     * @return      the model for the view
     */
    def editPayment(Long id) {
        getEditModel getDomainInstance(id)
    }

    /**
     * Finds invoicing transaction by number, subject and optionally by
     * organization.
     *
     * @return  the model for the view
     */
    def find() {
        Integer number = null
        try {
            number = params.name as Integer
        } catch (NumberFormatException ignored) { /* ignored */ }
        Organization organization = params.organization \
            ? Organization.get(params.long('organization')) \
            : null

        BuildableCriteria c = domainType.createCriteria()
        List<T> list = (List<T>) c.list {
            or {
                eq('number', number)
                ilike('subject', "%${params.name}%")
            }
            if (organization) {
                and {
                    eq('organization', organization)
                }
            }
            order('number', 'desc')
        }

        [(getDomainInstanceName('List')): list]
    }

    /**
     * Gets the invoicing transaction instance with the given ID to obtain the
     * closing balance.
     *
     * @param id    the ID of the invoicing transaction instance
     * @return      the model for the view
     */
    def getClosingBalance(Long id) {
        [(domainInstanceName): lowLevelGet(id)]
    }

    @Override
    def save() {
        T instance = doSave()
        if (instance) {
            updateAssociated instance
        }
    }

    @Override
    def update(Long id) {
        T instance = doUpdate(id)
        if (instance) {
            updateAssociated instance
        }
    }

    /**
     * Updates payment fields of the invoicing transaction instance with the
     * given ID.
     *
     * @param id    the ID of the invoicing transaction instance
     */
    def updatePayment(Long id) {
        T instance = getDomainInstance(id)
        if (instance != null) {
            updatePayment instance
        }
    }


    //-- Non-public methods ---------------------

    @Override
    protected Map getCreateModel(T instance) {
        instance.copyAddressesFromOrganization()

        super.getCreateModel instance
    }

    /**
     * Generates a PDF document for the given invoicing transaction instance
     * using the stated template.
     *
     * @param instance          the given invoicing transaction instance
     * @param template          the name of the template used for rendering
     * @param additionalData    any additional data which are added to the
     *                          generated data structure; all entries in this
     *                          table overwrite possible existing entries in
     *                          the generated data structure
     */
    def printDocument(T instance, String template,
                      Map<String, Object> additionalData = null)
    {
        String type = GrailsNameUtils.getScriptName(domainType)

        String xml = invoicingTransactionService.generateXML(
            instance, instance.createUser ?: credential.loadUser(),
            params.boolean('duplicate') ?: false, additionalData
        )

        StringBuilder buf = new StringBuilder(label)
        buf << ' ' << instance.fullNumber
        if (params.duplicate) {
            buf << '' << message(code: 'invoicingTransaction.duplicate')
        }
        buf << '.pdf'

        fopService.outputPdf xml, type, template, response, buf.toString()
    }

    @Override
    protected <D extends T> D saveInstance(T instance) {
        saveOrUpdateInstance instance
    }

    /**
     * Saves or updates the given invoicing transaction instance.  The method
     * handles the items of the invoicing transaction.
     *
     * @param instance  the given invoicing transaction instance
     * @return          the saved or updated invoicing transaction instance
     */
    protected <D extends T> D saveOrUpdateInstance(T instance) {
        boolean create = !instance.id
//        instance.items?.retainAll { it != null }

        /*
         * XXX  This code is necessary because the default implementation
         *      in Grails does not work.  The above lines worked in Grails
         *      2.0.0.  Now, either data binding or saving does not work
         *      correctly if items were deleted and gaps in the indices
         *      occurred (e. g. 0, 1, null, null, 4) or the items were
         *      re-ordered.  Then I observed cluttering in saved data
         *      columns.
         *      The following lines do not make me happy but they work.
         *      In future, this problem hopefully will be fixed in Grails
         *      so we can remove these lines.
         */
        if (instance.items == null) {
            instance.items = []
        } else {
            instance.items.clear()
        }
        boolean itemErrors = false
        for (int i = 0; params."items[${i}]"; i++) {
            if (create || params."items[${i}]".id != 'null') {
                InvoicingItem item = params."items[${i}]"
                itemErrors |= item.hasErrors()
                instance.addToItems item
            }
        }

        if (itemErrors || !instance.validate()) {
            instance.discard()
            return null
        }

        lowLevelSave instance
    }

    /**
     * Updates associated instances after the invoicing transaction instance
     * has been updated.
     *
     * @param instance  the given invoicing transaction instance
     */
    protected void updateAssociated(T instance) {}

    @Override
    protected <D extends T> D updateInstance(T instance) {
        bindData instance, params, [exclude: ['items']]

        saveOrUpdateInstance instance
    }

    /**
     * Updates the payment fields of the given invoicing transaction instance.
     *
     * @param instance  the given invoicing transaction instance
     */
    protected void updatePayment(T instance) {
        if (!checkVersion(instance)) {
            return
        }

        bindData(
            instance, params,
            [
                include: [
                    'stage', 'paymentDate', 'paymentAmount', 'paymentMethod'
                ]
            ]
        )

        if (lowLevelSave(instance) == null) {
            render view: 'edit', model: [(domainInstanceName): instance]
            return
        }

        request[domainInstanceName] = instance

        flash.message = message(
            code: 'default.updated.message', args: [label, instance.toString()]
        ) as Object
    }
}
