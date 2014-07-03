/*
 * ErrorControllerSpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
import grails.test.mixin.domain.DomainClassUnitTestMixin
import groovyx.net.http.HTTPBuilder
import spock.lang.Specification


@TestFor(ErrorController)
class ErrorControllerSpec extends Specification {

    //-- Feature methods ------------------------

    def 'General error'() {
        given: 'an exception'
        def e = new IllegalStateException('This is a test.')

        when: 'I call the action'
        request.exception = e
        def model = controller.error()

        then: 'I get the exception in the model'
        e == model.exception
        e.message == model.exception.message
    }

    def 'Google auth exception'() {
        given: 'an exception'
        def e = new IllegalStateException('This is a test.')

        when: 'I call the action'
        request.exception = e
        def model = controller.googleAuthException()

        then: 'I get the exception in the model'
        e == model.exception
        e.message == model.exception.message
    }

    // TODO test ldapPerson action

    def 'Report application error'() {
        given: 'an XML error report'
        def xml = '''<?xml version="1.0"?>

<error-report xmlns="http://www.amc-world.de/data/xml/springcrm"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://www.amc-world.de/data/xml/springcrm http://www.amc-world.de/data/xml/springcrm/error-report-1.1.xsd">
  <report-version>1.1</report-version>
  <application>
    <version>1.2.5</version>
    <build-number>1489</build-number>
    <build-date>2012-11-29T16:00:52+0100</build-date>
    <build-profile>live</build-profile>
  </application>
  <customer>
    <name>John Smith</name>
    <email>j.smith@example.com</email>
  </customer>
  <description></description>
  <details>
    <status-code>500</status-code>
    <message>null id in org.amcworld.springcrm.InvoicingItem entry (don't flush the Session after an exception occurs)</message>
    <servlet>grails</servlet>
    <uri>/springcrm/grails/quote/update.dispatch</uri>
  </details>
  <exception>
    <message>null id in org.amcworld.springcrm.InvoicingItem entry (don't flush the Session after an exception occurs)</message>
    <caused-by>null id in org.amcworld.springcrm.InvoicingItem entry (don't flush the Session after an exception occurs)</caused-by>
    <class-name>LruFilters</class-name>
    <line-number>68</line-number>
    <code-snippet>

    </code-snippet>
    <stack-trace>
    org.hibernate.AssertionFailure: null id in org.amcworld.springcrm.InvoicingItem entry (don't flush the Session after an exception occurs)
    at grails.orm.HibernateCriteriaBuilder.invokeMethod(HibernateCriteriaBuilder.java:1591)
    at org.amcworld.springcrm.LruService.recordItem(LruService.groovy:57)
    at org.amcworld.springcrm.LruFilters$_closure1_closure3_closure6.doCall(LruFilters.groovy:68)
    at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:895)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:918)
    at java.lang.Thread.run(Thread.java:662)
    </stack-trace>
  </exception>
</error-report>'''

        and: 'a mock for the HTTPBuilder class'
        def postData
        HTTPBuilder.metaClass.post = { Map data, Closure c = null -> postData = data }

        when: 'I call the report error action'
        params.xml = xml
        controller.reportError()

        then: 'the data is submitted to AMC World'
        '/scripts/springcrm/error-report.php' == postData.path
        xml == postData.body.xml
        '40783a444b360af71f3f34574fa85dda05ddd7ea' == postData.body.checksum

        cleanup:
        HTTPBuilder.metaClass = null
    }
}
