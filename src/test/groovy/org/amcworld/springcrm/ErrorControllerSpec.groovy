/*
 * ErrorControllerSpec.groovy
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
import org.apache.http.HttpStatus
import org.apache.http.HttpVersion
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicStatusLine
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

        and: 'a mock for the HttpClient class'
        def postData = [: ]
        CloseableHttpClient client = Mock()
        1 * client.execute(_) >> { HttpPost post ->
            postData.uri = post.URI
            postData.contentType = post.entity.contentType.value
            postData.content = post.entity.content.text

            CloseableHttpResponse response = Mock()
            response.statusLine >> new BasicStatusLine(
                HttpVersion.HTTP_1_1, HttpStatus.SC_OK, 'OK'
            )

            response
        }
        controller.httpClient = client

        when: 'I call the report error action'
        params.xml = xml
        controller.reportError()

        then: 'the data is submitted to AMC World'
        'http://dev.amc-world.de/scripts/springcrm/error-report.php' == postData.uri.toString()
        'application/x-www-form-urlencoded' == postData.contentType
        'xml=%3C%3Fxml+version%3D%221.0%22%3F%3E%0A%0A%3Cerror-report+xmlns%3D%22http%3A%2F%2Fwww.amc-world.de%2Fdata%2Fxml%2Fspringcrm%22%0A++++++++++++++xmlns%3Axsi%3D%22http%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema-instance%22%0A++++++++++++++xsi%3AschemaLocation%3D%22http%3A%2F%2Fwww.amc-world.de%2Fdata%2Fxml%2Fspringcrm+http%3A%2F%2Fwww.amc-world.de%2Fdata%2Fxml%2Fspringcrm%2Ferror-report-1.1.xsd%22%3E%0A++%3Creport-version%3E1.1%3C%2Freport-version%3E%0A++%3Capplication%3E%0A++++%3Cversion%3E1.2.5%3C%2Fversion%3E%0A++++%3Cbuild-number%3E1489%3C%2Fbuild-number%3E%0A++++%3Cbuild-date%3E2012-11-29T16%3A00%3A52%2B0100%3C%2Fbuild-date%3E%0A++++%3Cbuild-profile%3Elive%3C%2Fbuild-profile%3E%0A++%3C%2Fapplication%3E%0A++%3Ccustomer%3E%0A++++%3Cname%3EJohn+Smith%3C%2Fname%3E%0A++++%3Cemail%3Ej.smith%40example.com%3C%2Femail%3E%0A++%3C%2Fcustomer%3E%0A++%3Cdescription%3E%3C%2Fdescription%3E%0A++%3Cdetails%3E%0A++++%3Cstatus-code%3E500%3C%2Fstatus-code%3E%0A++++%3Cmessage%3Enull+id+in+org.amcworld.springcrm.InvoicingItem+entry+%28don%27t+flush+the+Session+after+an+exception+occurs%29%3C%2Fmessage%3E%0A++++%3Cservlet%3Egrails%3C%2Fservlet%3E%0A++++%3Curi%3E%2Fspringcrm%2Fgrails%2Fquote%2Fupdate.dispatch%3C%2Furi%3E%0A++%3C%2Fdetails%3E%0A++%3Cexception%3E%0A++++%3Cmessage%3Enull+id+in+org.amcworld.springcrm.InvoicingItem+entry+%28don%27t+flush+the+Session+after+an+exception+occurs%29%3C%2Fmessage%3E%0A++++%3Ccaused-by%3Enull+id+in+org.amcworld.springcrm.InvoicingItem+entry+%28don%27t+flush+the+Session+after+an+exception+occurs%29%3C%2Fcaused-by%3E%0A++++%3Cclass-name%3ELruFilters%3C%2Fclass-name%3E%0A++++%3Cline-number%3E68%3C%2Fline-number%3E%0A++++%3Ccode-snippet%3E%0A%0A++++%3C%2Fcode-snippet%3E%0A++++%3Cstack-trace%3E%0A++++org.hibernate.AssertionFailure%3A+null+id+in+org.amcworld.springcrm.InvoicingItem+entry+%28don%27t+flush+the+Session+after+an+exception+occurs%29%0A++++at+grails.orm.HibernateCriteriaBuilder.invokeMethod%28HibernateCriteriaBuilder.java%3A1591%29%0A++++at+org.amcworld.springcrm.LruService.recordItem%28LruService.groovy%3A57%29%0A++++at+org.amcworld.springcrm.LruFilters%24_closure1_closure3_closure6.doCall%28LruFilters.groovy%3A68%29%0A++++at+java.util.concurrent.ThreadPoolExecutor%24Worker.runTask%28ThreadPoolExecutor.java%3A895%29%0A++++at+java.util.concurrent.ThreadPoolExecutor%24Worker.run%28ThreadPoolExecutor.java%3A918%29%0A++++at+java.lang.Thread.run%28Thread.java%3A662%29%0A++++%3C%2Fstack-trace%3E%0A++%3C%2Fexception%3E%0A%3C%2Ferror-report%3E&checksum=40783a444b360af71f3f34574fa85dda05ddd7ea' == postData.content
    }
}
