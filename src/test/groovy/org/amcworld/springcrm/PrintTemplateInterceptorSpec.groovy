/*
 * PrintTemplateInterceptorSpec.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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
import spock.lang.Specification


@TestFor(PrintTemplateInterceptor)
class PrintTemplateInterceptorSpec extends Specification {

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
        'call'              | null                  || false
        'quote'             | null                  || false
        'salesOrder'        | null                  || false
        'invoice'           | null                  || false
        'creditMemo'        | null                  || false
        'dunning'           | null                  || false
        'call'              | 'index'               || false
        'quote'             | 'index'               || false
        'salesOrder'        | 'index'               || false
        'invoice'           | 'index'               || false
        'creditMemo'        | 'index'               || false
        'dunning'           | 'index'               || false
        'call'              | 'show'                || false
        'quote'             | 'show'                || true
        'salesOrder'        | 'show'                || true
        'invoice'           | 'show'                || true
        'creditMemo'        | 'show'                || true
        'dunning'           | 'show'                || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'Print templates are stored in model'() {
        given: 'a mocked FOP service'
        interceptor.fopService = Mock(FopService)
        1 * interceptor.fopService.templateNames >> [
            default: 'Default', amc: 'AMC World', draft: 'Draft'
        ]

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the print templates have been stored in the model'
        3 == interceptor.model.printTemplates.size()
        'Default' == interceptor.model.printTemplates.default
        'AMC World' == interceptor.model.printTemplates.amc
        'Draft' == interceptor.model.printTemplates.draft
    }
}
