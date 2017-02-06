/*
 * DepartmentControllerSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(DepartmentController)
@Mock([Department])
class DepartmentControllerSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Index action works with no items'() {
        when: 'I call the method'
        def model = controller.index()

        then: 'I get no items and the number of items'
        0 == model.departmentInstanceTotal
        model.departmentInstanceList.empty
    }

    def 'Index action returns list of items'() {
        given: 'some departments'
        mockDomain Department, [
            [name: 'Field service', costCenter: '47953'],
            [name: 'IT', costCenter: '01843']
        ]

        when: 'I call the method'
        def model = controller.index()

        then: 'I get items and the number of items'
        2 == model.departmentInstanceTotal
        'Field service' == model.departmentInstanceList[0].name
        '47953' == model.departmentInstanceList[0].costCenter
        'IT' == model.departmentInstanceList[1].name
        '01843' == model.departmentInstanceList[1].costCenter
    }

//    def 'Index action max parameter is preset'() {
//        given: 'a lot of departments'
//        mockDomain Department, [
//            [name: 'Field service', costCenter: '47953'],
//            [name: 'IT', costCenter: '01843'],
//            [name: 'Foo', costCenter: '39743'],
//            [name: 'Bar', costCenter: '27480'],
//            [name: 'Whee', costCenter: '45934'],
//            [name: 'Bizz', costCenter: '32047'],
//            [name: 'Buzz', costCenter: '43975'],
//            [name: 'FooBar', costCenter: '93749'],
//            [name: 'FooWhee', costCenter: '47951'],
//            [name: 'BarBizz', costCenter: '84392'],
//            [name: 'Bagg', costCenter: '28407'],
//            [name: 'Bonk', costCenter: '49076'],
//        ]
//
//        when: 'I call the method'
//        def model = controller.index()
//
//        then: 'I get the correct number of items'
//        10 == model.departmentInstanceTotal
//        'Field service' == model.departmentInstanceList[0].name
//        'BarBizz' == model.departmentInstanceList[9].name
//    }
}
