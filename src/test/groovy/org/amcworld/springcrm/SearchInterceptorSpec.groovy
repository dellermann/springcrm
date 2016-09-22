package org.amcworld.springcrm


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SearchInterceptor)
class SearchInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test search interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"search")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
