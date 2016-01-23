/*
 * ErrorController.groovy
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

import java.security.MessageDigest
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair


/**
 * The class {@code ErrorController} handles errors in the application.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.4
 */
class ErrorController {

    //-- Public methods -------------------------

    def error() {
        [exception: request.exception]
    }

    def forbidden() {}

    def googleAuthException() {
        [exception: request.exception]
    }

    def ldapPerson(String type, String origAction, Long personId) {
        ConfigHolder ch = ConfigHolder.instance
        String msg = null
        switch (type) {
        case 'communication':
            msg = message(
                code: 'error.ldap.person.description.communicate', args: [
                    ch['ldapHost'].value, ch['ldapPort'].value
                ]
            )
            break
        case 'nameNotFound':
            msg = message(
                code: 'error.ldap.person.description.nameNotFound', args: [
                    ch['ldapContactDn'].value
                ]
            )
            break
        case 'authentication':
            msg = message(
                code: 'error.ldap.person.description.authentication', args: [
                    ch['ldapHost'].value, ch['ldapBindDn'].value,
                    ch['ldapContactDn'].value
                ]
            )
            break
        }

        String retryUrl = null
        String backUrl = null
        switch (origAction) {
        case 'save':
        case 'update':
            retryUrl = createLink(
                controller: 'person', action: 'ldapexport', id: personId
            )
            backUrl = createLink(
                controller: 'person', action: 'edit', id: personId
            )
            break
        case 'delete':
            def id = params.id
            retryUrl = createLink(
                controller: 'person', action: 'ldapdelete', id: personId
            )
            // no backUrl -> use list button instead
            break
        case 'ldapexport':
            retryUrl = createLink(
                controller: 'person', action: origAction, id: personId
            )
            // no backUrl -> use list button instead
            break
        }

        render(
            view: 'ldap',
            model: [
                msg: msg,
                retryUrl: retryUrl,
                backUrl: backUrl,
                listUrl: createLink(controller: 'person', action: 'index'),
                admin: session.credential.admin
            ]
        )
    }

    def notFound() {}

    /**
     * Sends an error report to AMC World Technologies.
     */
    def reportError() {
        String xml = params.xml
        def messageDigest = MessageDigest.getInstance('SHA1')
        messageDigest.update(xml.getBytes('UTF-8'))
        def sw = new StringWriter()
        messageDigest.digest().encodeHex().writeTo sw

        def httpClient = HttpClients.createDefault()
        def httpPost = new HttpPost(
            'http://dev.amc-world.de/scripts/springcrm/error-report.php'
        )
        List<NameValuePair> nvps = [
            new BasicNameValuePair('xml', xml),
            new BasicNameValuePair('checksum', sw.toString())
        ]
        httpPost.entity = new UrlEncodedFormEntity(nvps)
        httpClient.execute httpPost

//        def body = [ xml: xml, checksum: sw.toString() ]
//
//        def http = new HTTPBuilder('http://dev.amc-world.de')
//        http.post(
//            path: '/scripts/springcrm/error-report.php', body: body,
//            requestContentType: groovyx.net.http.ContentType.URLENC
//        )
    }
}
