/*
 * CommonViewDataInterceptorSpec.groovy
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

import grails.testing.gorm.DataTest
import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification


class CommonViewDataInterceptorSpec extends Specification
    implements InterceptorUnitTest<CommonViewDataInterceptor>, DataTest
{

    //-- Fixture methods ------------------------

    void setup() {
        mockDomains Config, TaxRate
    }


    //-- Feature methods ------------------------

    void 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    void 'No model is not populated'() {
        when: 'I call the interceptor'
        interceptor.before()

        then: 'the model is unset'
        null == interceptor.model
    }

    void 'A model is populated by important data'() {
        given: 'a user service instance'
        UserService service = Mock()
        1 * service.currentLocale >> Locale.GERMANY
        1 * service.currencySymbol >> '€'
        1 * service.numFractionDigits >> 3
        1 * service.numFractionDigitsExt >> 2
        1 * service.decimalSeparator >> ','
        1 * service.groupingSeparator >> '.'
        interceptor.userService = service

        and: 'some tax rates'
        def tr1 = new TaxRate(name: '7 %', taxValue: 0.07, orderId: 2i)
        def tr2 = new TaxRate(name: '19 %', taxValue: 0.19, orderId: 1i)

        and: 'a selection value service instance'
        SelValueService selValueService = Mock()
        1 * selValueService.findAllByClass(TaxRate) >> [tr2, tr1]
        interceptor.selValueService = selValueService

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'the interceptor is executed'
        interceptor.after()

        then: 'the model is correctly populated'
        'de-DE' == interceptor.model.locale
        'de' == interceptor.model.lang
        '€' == interceptor.model.currencySymbol
        3 == interceptor.model.numFractionDigits
        2 == interceptor.model.numFractionDigitsExt
        ',' == interceptor.model.decimalSeparator
        '.' == interceptor.model.groupingSeparator
        '19.00,7.00' == interceptor.model.taxRatesString
    }
}
