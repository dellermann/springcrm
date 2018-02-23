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

    @SuppressWarnings("GroovyPointlessBoolean")
    def 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'a particular request is used'
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
        List<Unit> units = [
            new Unit(name: 'm', orderId: 10),
            new Unit(name: 'pkg', orderId: 20),
            new Unit(name: 'parcel', orderId: 30),
            new Unit(name: 'kg', orderId: 40)
        ]
        List<TaxRate> taxRates = [
            new TaxRate(name: '19 %', taxValue: 0.19, orderId: 10),
            new TaxRate(name: '7 %', taxValue: 0.07, orderId: 20),
            new TaxRate(name: '21 %', taxValue: 0.21, orderId: 30)
        ]

        and: 'a selector value service instance'
        SelValueService selValueService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findAllByClass(Unit) >> units
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findAllByClass(TaxRate) >> taxRates
        interceptor.selValueService = selValueService

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the units are stored in the correct order'
        ['m', 'pkg', 'parcel', 'kg'] == interceptor.model.units*.name

        and: 'the tax rates are stored in correct order'
        ['19 %', '7 %', '21 %'] == interceptor.model.taxRates*.name
    }
}
