/*
 * InvoicingItemsInterceptorSpec.groovy
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

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification


class InvoicingItemsInterceptorSpec extends Specification
    implements InterceptorUnitTest<InvoicingItemsInterceptor>
{

    //-- Feature methods ------------------------

    def 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'I use a particular request'
        withRequest controller: c, action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        c                   | a                     || b
        'phoneCall'         | null                  || false
        'quote'             | null                  || false
        'salesOrder'        | null                  || false
        'invoice'           | null                  || false
        'creditMemo'        | null                  || false
        'dunning'           | null                  || false
        'purchaseInvoice'   | null                  || false
        'product'           | null                  || false
        'work'              | null                  || false
        'phoneCall'         | 'index'               || false
        'quote'             | 'index'               || false
        'salesOrder'        | 'index'               || false
        'invoice'           | 'index'               || false
        'creditMemo'        | 'index'               || false
        'dunning'           | 'index'               || false
        'purchaseInvoice'   | 'index'               || false
        'product'           | 'index'               || false
        'work'              | 'index'               || false
        'phoneCall'         | 'create'              || false
        'quote'             | 'create'              || true
        'salesOrder'        | 'create'              || true
        'invoice'           | 'create'              || true
        'creditMemo'        | 'create'              || true
        'dunning'           | 'create'              || true
        'purchaseInvoice'   | 'create'              || true
        'product'           | 'create'              || true
        'work'              | 'create'              || true
        'phoneCall'         | 'edit'                || false
        'quote'             | 'edit'                || true
        'salesOrder'        | 'edit'                || true
        'invoice'           | 'edit'                || true
        'creditMemo'        | 'edit'                || true
        'dunning'           | 'edit'                || true
        'purchaseInvoice'   | 'edit'                || true
        'product'           | 'edit'                || true
        'work'              | 'edit'                || true
        'phoneCall'         | 'copy'                || false
        'quote'             | 'copy'                || true
        'salesOrder'        | 'copy'                || true
        'invoice'           | 'copy'                || true
        'creditMemo'        | 'copy'                || true
        'dunning'           | 'copy'                || true
        'purchaseInvoice'   | 'copy'                || true
        'product'           | 'copy'                || true
        'work'              | 'copy'                || true
        'phoneCall'         | 'save'                || false
        'quote'             | 'save'                || true
        'salesOrder'        | 'save'                || true
        'invoice'           | 'save'                || true
        'creditMemo'        | 'save'                || true
        'dunning'           | 'save'                || true
        'purchaseInvoice'   | 'save'                || true
        'product'           | 'save'                || true
        'work'              | 'save'                || true
        'phoneCall'         | 'update'              || false
        'quote'             | 'update'              || true
        'salesOrder'        | 'update'              || true
        'invoice'           | 'update'              || true
        'creditMemo'        | 'update'              || true
        'dunning'           | 'update'              || true
        'purchaseInvoice'   | 'update'              || true
        'product'           | 'update'              || true
        'work'              | 'update'              || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'Units and tax rates are stored in model'() {
        given: 'some units and tax rates'
        makeUnits()
        makeTaxRates()

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the units are stored in the correct order'
        ['m', 'pkg', 'parcel', 'kg'] == interceptor.model.units*.name

        and: 'the tax rates are stored in correct order'
        ['19 %', '7 %', '21 %'] == interceptor.model.taxRates*.name
    }


    //-- Non-public methods ---------------------

    private void makeUnits() {
        new Unit(name: 'kg', orderId: 40).save failOnError: true
        new Unit(name: 'm', orderId: 10).save failOnError: true
        new Unit(name: 'parcel', orderId: 30).save failOnError: true
        new Unit(name: 'pkg', orderId: 20).save failOnError: true
    }

    private void makeTaxRates() {
        new TaxRate(name: '7 %', taxValue: 0.07, orderId: 20).save failOnError: true
        new TaxRate(name: '19 %', taxValue: 0.19, orderId: 10).save failOnError: true
        new TaxRate(name: '21 %', taxValue: 0.21, orderId: 30).save failOnError: true
    }
}
