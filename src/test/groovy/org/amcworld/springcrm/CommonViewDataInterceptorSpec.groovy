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

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No model is not populated'() {
        when: 'I call the interceptor'
        interceptor.before()

        then: 'the model is unset'
        null == interceptor.model
    }

    def 'A model is populated by important data'() {

        /*
         * IMPLEMENTATION NOTES:
         * Testing tax rates does not work because they are loaded in a
         * different Hibernate session.  It seems that is not supported yet.
         */

        given: 'a user service'
        UserService service = Mock()
        service.currentLocale >> Locale.GERMANY
        service.currencySymbol >> '€'
        service.numFractionDigits >> 3
        service.numFractionDigitsExt >> 2
        service.decimalSeparator >> ','
        service.groupingSeparator >> '.'
        interceptor.userService = service

//        and: 'some tax rates'
//        new TaxRate(name: '7 %', taxValue: 0.07, orderId: 2)
//            .save failOnError: true
//        new TaxRate(name: '19 %', taxValue: 0.19, orderId: 1)
//            .save failOnError: true

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the model is populated correctly'
        'de-DE' == interceptor.model.locale
        'de' == interceptor.model.lang
        '€' == interceptor.model.currencySymbol
        3 == interceptor.model.numFractionDigits
        2 == interceptor.model.numFractionDigitsExt
        ',' == interceptor.model.decimalSeparator
        '.' == interceptor.model.groupingSeparator
//        '19.00,7.00' == interceptor.model.taxRatesString
    }
}
