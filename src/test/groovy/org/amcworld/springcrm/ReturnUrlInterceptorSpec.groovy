package org.amcworld.springcrm


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ReturnUrlInterceptor)
class ReturnUrlInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test returnUrl interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"returnUrl")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
