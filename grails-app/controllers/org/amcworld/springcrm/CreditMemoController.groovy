/*
 * CreditMemoController.groovy
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


/**
 * The class {@code CreditMemoController} contains actions which manage credit
 * memos.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class CreditMemoController extends InvoicingController<CreditMemo> {

    //-- Class fields -------------------------------

    SeqNumberService seqNumberService


    //-- Constructors ---------------------------

    CreditMemoController() {
        super(CreditMemo)
    }


    //-- Public methods -------------------------

    def index() {
        List<CreditMemo> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = CreditMemo.findAllBySubjectLike(searchFilter, params)
            count = CreditMemo.countBySubjectLike(searchFilter)
        } else {
            list = CreditMemo.list(params)
            count = CreditMemo.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person, Long invoice,
                     Long dunning)
    {
        List<CreditMemo> list = null
        int count = 0
        Map<String, Object> linkParams = null
        if (organization) {
            Organization organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                list = CreditMemo.findAllByOrganization(
                    organizationInstance, params
                )
                count = CreditMemo.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        } else if (person) {
            Person personInstance = Person.get(person)
            if (personInstance) {
                list = CreditMemo.findAllByPerson(personInstance, params)
                count = CreditMemo.countByPerson(personInstance)
                linkParams = [person: personInstance.id]
            }
        } else if (invoice) {
            Invoice invoiceInstance = Invoice.get(invoice)
            if (invoiceInstance) {
                list = CreditMemo.findAllByInvoice(invoiceInstance, params)
                count = CreditMemo.countByInvoice(invoiceInstance)
                linkParams = [invoice: invoiceInstance.id]
            }
        } else if (dunning) {
            Dunning dunningInstance = Dunning.get(dunning)
            if (dunningInstance) {
                list = CreditMemo.findAllByDunning(dunningInstance, params)
                count = CreditMemo.countByDunning(dunningInstance)
                linkParams = [dunning: dunningInstance.id]
            }
        }

        getListEmbeddedModel list, count, linkParams
    }

    def copy(Long id) {
        super.copy id
    }

    def create() {
        CreditMemo creditMemoInstance
        if (params.invoice) {
            Invoice invoiceInstance = Invoice.get(params.long('invoice'))
            creditMemoInstance = new CreditMemo(invoiceInstance)
        } else if (params.dunning) {
            Dunning dunningInstance = Dunning.get(params.long('dunning'))
            creditMemoInstance = new CreditMemo(dunningInstance)
        } else {
            creditMemoInstance = new CreditMemo(params)
        }

        getCreateModel creditMemoInstance
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

    def print(Long id, String template) {
        CreditMemo creditMemoInstance = getDomainInstanceWithStatus(id)
        if (creditMemoInstance != null) {
            printDocument(
                creditMemoInstance, template,
                [
                    invoice: creditMemoInstance.invoice,
                    invoiceFullNumber: seqNumberService.getFullNumber(
                        creditMemoInstance.invoice
                    ),
                    dunning: creditMemoInstance.dunning,
                    dunningFullNumber: seqNumberService.getFullNumber(
                        creditMemoInstance.dunning
                    ),
                    paymentMethod: creditMemoInstance.paymentMethod?.name
                ]
            )
        }
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


    //-- Non-public methods ---------------------

    protected boolean checkAccess(CreditMemo creditMemoInstance) {
        admin || creditMemoInstance.stage.id < 2502
    }

    protected void updateAssociated(CreditMemo creditMemoInstance) {
        Invoice invoiceInstance = creditMemoInstance.invoice
        if (invoiceInstance) {
            invoiceInstance.stage = InvoiceStage.get(907)
            invoiceInstance.save failOnError: true, flush: true
        }
        Dunning dunningInstance = creditMemoInstance.dunning
        if (dunningInstance) {
            dunningInstance.stage = DunningStage.get(2206)
            dunningInstance.save failOnError: true, flush: true
        }
    }
}
