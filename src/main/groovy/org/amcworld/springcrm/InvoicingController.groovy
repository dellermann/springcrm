/*
 * InvoicingController.groovy
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

import grails.util.GrailsNameUtils
import org.grails.datastore.mapping.query.api.BuildableCriteria


/**
 * The class {@code InvoicingController} handles actions common to all
 * invoicing transactions.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.2
 */
class InvoicingController<T extends InvoicingTransaction>
    extends GeneralController<T>
{

    //-- Fields ---------------------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService
    SeqNumberService seqNumberService


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
        respond getDomainInstance(id)
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

        respond list
    }

    /**
     * Gets the invoicing transaction instance with the given ID to obtain the
     * closing balance.
     *
     * @param id    the ID of the invoicing transaction instance
     * @return      the model for the view
     */
    def getClosingBalance(Long id) {
        [(domainInstanceName): domainType.get(id)]
    }

    @Override
    def save() {
        T instance = saveInstance()
        if (instance) {
            updateAssociated instance
        }
    }

    @Override
    def update(Long id) {
        T instance = updateInstance(id)
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
    protected Map<String, Object> getCreateModel(T instance) {
        instance.copyAddressesFromOrganization()

        super.getCreateModel instance
    }

    @Override
    protected <D extends T> D lowLevelSave() {
        D instance = newInstance(params)

        invoicingTransactionService.save(instance, params) ? instance : null
    }

    @Override
    protected <D extends T> D lowLevelUpdate(T instance) {
        invoicingTransactionService.save instance, params
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
        buf << ' ' << seqNumberService.getFullNumber(instance)
        if (params.duplicate) {
            buf << '' << message(code: 'invoicingTransaction.duplicate')
        }
        buf << '.pdf'

        fopService.outputPdf xml, type, template, response, buf.toString()
    }

    /**
     * Updates associated instances after the invoicing transaction instance
     * has been updated.
     *
     * @param instance  the given invoicing transaction instance
     */
    protected void updateAssociated(T instance) {}

    /**
     * Updates the payment fields of the given invoicing transaction instance.
     *
     * @param instance  the given invoicing transaction instance
     */
    protected void updatePayment(T instance) {
        if (params.version) {
            long version = params.version.toLong()
            if (instance.version > version) {
                instance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [label] as Object[], ''
                )
                render view: 'edit', model: [(domainInstanceName): instance]
                return
            }
        }

        bindData(
            instance, params,
            [
                include: [
                    'stage', 'paymentDate', 'paymentAmount', 'paymentMethod'
                ]
            ]
        )

        if (!instance.save(failOnError: true, flush: true)) {
            render view: 'edit', model: [(domainInstanceName): instance]
            return
        }

        request[domainInstanceName] = instance

        flash.message = message(
            code: 'default.updated.message', args: [label, instance.toString()]
        ) as Object
    }
}
