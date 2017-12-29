/*
 * RedirectInterceptorSpec.groovy
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


class RedirectInterceptorSpec extends Specification
    implements InterceptorUnitTest<RedirectInterceptor>
{

    //-- Feature methods ------------------------

    def 'Interceptor matches the correct controller/action pairs'(
        String a, boolean b
    ) {
        when: 'I use a particular request'
        withRequest controller: 'Call', action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        a                   || b
        null                || false
        'create'            || false
        'show'              || false
        'index'             || false
        'listEmbedded'      || false
        'save'              || true
        'update'            || true
        'delete'            || true
        'updatePayment'     || true
        'saveSelValues'     || true
        'saveSeqNumbers'    || true
        'saveTaxRates'      || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.afterView()
        interceptor.before()
    }

    def 'Configuration actions redirect if view is unset'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'a redirect to the index action has been set'
        '/call/index' == response.redirectedUrl

        where:
        a << ['saveSelValues', 'saveSeqNumbers', 'saveTaxRates']
    }

    def 'Configuration actions return to URL if view is unset'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'a redirect to the index action has been set'
        '/organization/show/3' == response.redirectedUrl

        where:
        a << ['saveSelValues', 'saveSeqNumbers', 'saveTaxRates']
    }

    def 'Configuration actions do not redirect if view is set'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        and: 'a set view'
        interceptor.view = 'edit'

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'no redirect has been set'
        null == response.redirectedUrl

        where:
        a << ['saveSelValues', 'saveSeqNumbers', 'saveTaxRates']
    }

    def 'Other actions do not redirect if domain is unset'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'no redirect has been set'
        null == response.redirectedUrl

        where:
        a << ['delete', 'save', 'update', 'updatePayment']
    }

    def 'Other actions do not redirect if a wrong domain is set'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain with an invalid name'
        request.noteInstance = new Note()

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'no redirect has been set'
        null == response.redirectedUrl

        where:
        a << ['delete', 'save', 'update', 'updatePayment']
    }

    def 'Delete action redirects'() {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'delete'

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/call/index' == response.redirectedUrl
    }

    def 'Delete action redirects with parameters'() {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'delete'

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'redirect parameters'
        request.redirectParams = [foo: 'bar', whee: 1]

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/call/index?foo=bar&whee=1' == response.redirectedUrl
    }

    def 'Delete action returns to URL'() {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'delete'

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is returned'
        '/organization/show/3' == response.redirectedUrl
    }

    def 'Delete action returns to URL ignoring parameters'() {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'delete'

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        and: 'redirect parameters'
        request.redirectParams = [foo: 'bar', whee: 1]

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is returned'
        '/organization/show/3' == response.redirectedUrl
    }

    def 'Save actions redirect'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/call/edit/56' == response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }

    def 'Save actions redirect with parameters'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'redirect parameters'
        request.redirectParams = [foo: 'bar', whee: 1]

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/call/edit/56?foo=bar&whee=1' == response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }

    def 'Save actions redirect with return URL'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is returned'
        '/call/edit/56?returnUrl=%2Forganization%2Fshow%2F3' == response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }

    def 'Save actions redirect with return URL and parameters'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        and: 'redirect parameters'
        request.redirectParams = [foo: 'bar', whee: 1]

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is returned'
        '/call/edit/56?foo=bar&whee=1&returnUrl=%2Forganization%2Fshow%2F3' ==
            response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }

    def 'Save actions redirect at close'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a close parameter'
        webRequest.params.close = 1

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/call/show/56' == response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }

    def 'Save actions redirect at close with parameters'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a close parameter'
        webRequest.params.close = 1

        and: 'redirect parameters'
        request.redirectParams = [foo: 'bar', whee: 1]

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/call/show/56?foo=bar&whee=1' == response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }

    def 'Save actions return to URL at close'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a close parameter'
        webRequest.params.close = 1

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/organization/show/3' == response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }

    def 'Save actions return to URL at close ignoring parameters'(String a) {
        given: 'a particular controller and action'
        webRequest.controllerName = 'call'
        webRequest.actionName = a

        and: 'a domain'
        request.callInstance = new Call()
        request.callInstance.id = 56

        and: 'a close parameter'
        webRequest.params.close = 1

        and: 'a return URL'
        webRequest.params.returnUrl = '/organization/show/3'

        and: 'redirect parameters'
        request.redirectParams = [foo: 'bar', whee: 1]

        when: 'I call the interceptor method'
        interceptor.after()

        then: 'the user is redirected'
        '/organization/show/3' == response.redirectedUrl

        where:
        a << ['save', 'update', 'updatePayment']
    }
}
