/*
 * CreditMemoSpec.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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


import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification


@TestFor(CreditMemo)
@Mock([CreditMemo])
class CreditMemoSpec extends Specification {

    //-- Fields ---------------------------------

    CreditMemo cm = new CreditMemo(
        adjustment: 0.54,
        billingAddr: new Address(),
        discountAmount: 5,
        discountPercent: 2,
        items: [
            new InvoicingItem(
                quantity: 4, unit: 'pcs.', name: 'books', unitPrice: 44.99,
                tax: 19, salesItem: new Product(id: 10000, name: 'books')
            ),
            new InvoicingItem(
                quantity: 10.5, unit: 'm', name: 'tape', unitPrice: 0.89,
                tax: 7, salesItem: new Product(id: 10010, name: 'tape')
            ),
            new InvoicingItem(
                quantity: 4.25, unit: 'h', name: 'repairing', unitPrice: 39,
                tax: 19, salesItem: new Service(id: 10000, name: 'repairing')
            ),
            new InvoicingItem(
                quantity: 10, unit: 'units', name: 'fixing', unitPrice: 9.8,
                tax: 19
            )
        ],
        organization: new Organization(
            number: 10405, recType: 1, name: 'YourOrganization Ltd.',
            billingAddr: new Address(), shippingAddr: new Address()
        ),
        paymentAmount: 100.0d,
        total: 100.0d,
        type: 'C',
        shippingAddr: new Address(),
        shippingCosts: 4.5,
        shippingTax: 7,
        stage: new CreditMemoStage(),
        subject: 'credit memo'
    )


    //-- Feature Methods -----------------------

    def 'Compute turnover of products'() {
        when:
        double t = cm.turnoverProducts

        then:
        189.305 == t
    }

    def 'Compute turnover of services'() {
        when:
        double t = cm.turnoverServices

        then:
        165.75 == t
    }

    def 'Compute turnover of other items'() {
        when:
        double t = cm.turnoverOtherSalesItems

        then:
        98 == t
    }
}