/*
 * CreditMemoController.groovy
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
 * The class {@code CreditMemoController} contains actions which manage
 * credit memos.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class CreditMemoController extends InvoicingController<CreditMemo> {

    //-- Fields -------------------------------------

    CreditMemoService creditMemoService
    DunningService dunningService
    InvoiceService invoiceService
    OrganizationService organizationService
    PersonService personService


    //-- Constructors ---------------------------

    CreditMemoController() {
        super(CreditMemo)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        CreditMemo creditMemoInstance
        if (params.invoice) {
            Invoice invoiceInstance =
                invoiceService.get(params.long('invoice'))
            creditMemoInstance = new CreditMemo(invoiceInstance)
        } else if (params.dunning) {
            Dunning dunningInstance =
                dunningService.get(params.long('dunning'))
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

    def index() {
        List<CreditMemo> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = creditMemoService.findAllBySubjectLike(
                searchFilter, params
            )
            count = creditMemoService.countBySubjectLike(searchFilter)
        } else {
            list = creditMemoService.list(params)
            count = creditMemoService.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person, Long invoice,
                     Long dunning)
    {
        List<CreditMemo> list = null
        int count = 0
        Map linkParams = null
        if (organization) {
            Organization organizationInstance =
                organizationService.get(organization)
            if (organizationInstance) {
                list = creditMemoService.findAllByOrganization(
                    organizationInstance, params
                )
                count =
                    creditMemoService.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        } else if (person) {
            Person personInstance = personService.get(person)
            if (personInstance) {
                list = creditMemoService.findAllByPerson(
                    personInstance, params
                )
                count = creditMemoService.countByPerson(personInstance)
                linkParams = [person: personInstance.id]
            }
        } else if (invoice) {
            Invoice invoiceInstance = invoiceService.get(invoice)
            if (invoiceInstance) {
                list = creditMemoService.findAllByInvoice(
                    invoiceInstance, params
                )
                count = creditMemoService.countByInvoice(invoiceInstance)
                linkParams = [invoice: invoiceInstance.id]
            }
        } else if (dunning) {
            Dunning dunningInstance = dunningService.get(dunning)
            if (dunningInstance) {
                list = creditMemoService.findAllByDunning(
                    dunningInstance, params
                )
                count = creditMemoService.countByDunning(dunningInstance)
                linkParams = [dunning: dunningInstance.id]
            }
        }

        getListEmbeddedModel list, count, linkParams
    }

    def print(Long id, String template) {
        CreditMemo creditMemoInstance = getDomainInstanceWithStatus(id)
        if (creditMemoInstance != null) {
            printDocument(
                creditMemoInstance, template,
                [
                    invoice: creditMemoInstance.invoice,
                    invoiceFullNumber:
                        creditMemoInstance.invoice?.fullNumber,
                    dunning: creditMemoInstance.dunning,
                    dunningFullNumber:
                        creditMemoInstance.dunning?.fullNumber,
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

    @Override
    protected boolean checkAccess(CreditMemo creditMemoInstance) {
        admin || creditMemoInstance.stage.id < 2502
    }

    @Override
    protected void lowLevelDelete(CreditMemo instance) {
        creditMemoService.delete instance.id
    }

    @Override
    protected CreditMemo lowLevelSave(CreditMemo instance) {
        creditMemoService.save instance
    }

    @Override
    protected CreditMemo lowLevelGet(Long id) {
        creditMemoService.get id
    }

    @Override
    protected void updateAssociated(CreditMemo creditMemoInstance) {
        Invoice invoiceInstance = creditMemoInstance.invoice
        if (invoiceInstance != null) {
            invoiceInstance.stage = InvoiceStage.get(907)
            invoiceInstance.save failOnError: true, flush: true
        }
        Dunning dunningInstance = creditMemoInstance.dunning
        if (dunningInstance != null) {
            dunningInstance.stage = DunningStage.get(2206)
            dunningInstance.save failOnError: true, flush: true
        }
    }
}
