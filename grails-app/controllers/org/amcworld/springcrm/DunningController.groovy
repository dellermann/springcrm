/*
 * DunningController.groovy
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
 * The class {@code DunningController} contains actions which manage dunnings.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class DunningController extends InvoicingController<Dunning> {

    //-- Constructors ---------------------------

    DunningController() {
        super(Dunning)
    }


    //-- Public methods -------------------------

    def create() {
        Dunning dunningInstance
        if (params.invoice) {
            Invoice invoiceInstance = Invoice.get(params.long('invoice'))
            invoiceInstance.items?.clear()
            dunningInstance = new Dunning(invoiceInstance)
        } else {
            dunningInstance = new Dunning()
            dunningInstance.setProperties params
        }

        addWorkItem dunningInstance, 'workIdDunningCharge'
        addWorkItem dunningInstance, 'workIdDefaultInterest'

        getCreateModel dunningInstance
    }

    def copy(Long id) {
        super.copy id
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit(id)
    }

    def editPayment(Long id) {
        super.editPayment id
    }

    def find() {
        super.find()
    }

    def getClosingBalance(Long id) {
        super.getClosingBalance id
    }

    def index() {
        List<Dunning> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Dunning.findAllBySubjectLike(searchFilter, params)
            count = Dunning.countBySubjectLike(searchFilter)
        } else {
            list = Dunning.list(params)
            count = Dunning.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person, Long invoice) {
        List<Dunning> list = null
        int count = 0
        Map<String, Object> linkParams = null

        if (organization) {
            Organization organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                list = Dunning.findAllByOrganization(organizationInstance, params)
                count = Dunning.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        } else if (person) {
            Person personInstance = Person.get(person)
            if (personInstance) {
                list = Dunning.findAllByPerson(personInstance, params)
                count = Dunning.countByPerson(personInstance)
                linkParams = [person: personInstance.id]
            }
        } else if (invoice) {
            Invoice invoiceInstance = Invoice.get(invoice)
            if (invoiceInstance) {
                list = Dunning.findAllByInvoice(invoiceInstance, params)
                count = Dunning.countByInvoice(invoiceInstance)
                linkParams = [invoice: invoiceInstance.id]
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

    def updatePayment(Long id) {
        super.updatePayment id
    }

    def print(Long id, String template) {
        Dunning dunningInstance = getDomainInstanceWithStatus(id)
        if (dunningInstance != null) {
            printDocument(
                dunningInstance, template,
                [
                    invoice: dunningInstance.invoice,
                    invoiceFullNumber: dunningInstance.invoice.fullNumber,
                ]
            )
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Adds a work item which is configured under the given name to the
     * dunning instance.
     *
     * @param dunningInstance   the given dunning instance
     * @param name              the name of the configuration containing the ID
     *                          of the work
     * @since 2.2
     */
    private static void addWorkItem(Dunning dunningInstance, String name) {
        Config workId = ConfigHolder.instance.getConfig(name)
        if (workId != null) {
            Work work = Work.get(workId.toType(Long))
            if (work != null) {
                dunningInstance.addToItems workToItem(work)
            }
        }
    }

    @Override
    protected boolean checkAccess(Dunning dunningInstance) {
        admin || dunningInstance.stage.id < 2202
    }

    @Override
    protected void updateAssociated(Dunning dunningInstance) {
        Invoice invoiceInstance = dunningInstance.invoice
        invoiceInstance.stage = InvoiceStage.get(904)
        invoiceInstance.save flush: true
    }

    private static InvoicingItem workToItem(Work w) {
        new InvoicingItem(
            number: w.fullNumber, quantity: w.quantity,
            unit: w.unit.toString(), name: w.name, description: w.description,
            unitPrice: w.unitPrice, tax: w.taxRate.taxValue * 100
        )
    }
}
