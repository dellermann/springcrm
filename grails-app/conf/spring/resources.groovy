/*
 * resources.groovy
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


import org.amcworld.springcrm.*
import org.amcworld.springcrm.converter.*
import org.amcworld.springcrm.google.*
import org.amcworld.springcrm.xml.XHTMLEntityResolver
import org.amcworld.springcrm.xml.InvoicingTransactionXMLFactory
import org.apache.fop.apps.FopFactory
import org.apache.http.impl.client.HttpClients
import org.grails.spring.DefaultBeanConfiguration
import org.xml.sax.helpers.XMLReaderFactory


beans = {

    /*
     * Implementation notes: delegate is an object of grails.spring.BeanBuilder
     */

    /* configuration handling */
    springcrmConfig(org.springframework.jndi.JndiObjectFactoryBean) {
        defaultObject = ''
        lookupOnStartup = true
        jndiName = 'java:comp/env/springcrmConfig'
    }

    /* value converters */
    defaultDateConverter(DateTimeValueConverter) {
        messageSource = ref('messageSource')
    }
    [Short.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE].each { numberType ->
        "defaultGrails${numberType.name}Converter"(PrimitiveNumberValueConverter) {
            targetType = numberType
        }
    }

    /* startup difference sets */
    startupDiffSet2(org.amcworld.springcrm.install.diffset.NoteMarkdownDiffSet) {
        markdownService = ref('markdownService')
    }
    startupDiffSet4(org.amcworld.springcrm.install.diffset.ProjectDocumentDiffSet)

    /* XML and XSLT */
    fopFactory(FopFactory) { DefaultBeanConfiguration b ->
        b.factoryMethod = 'newInstance'
        b.singleton = false     // must not be singleton! See FopService.
    }
    invoicingTransactionXMLFactory(InvoicingTransactionXMLFactory) {
        markdownService = ref('markdownService')
    }
    transformerFactory(javax.xml.transform.TransformerFactory)
    { DefaultBeanConfiguration b ->
        b.factoryMethod = 'newInstance'
        b.singleton = false     // must not be singleton! See FopService.
        errorListener = ref('xsltLogErrorListener')
    }
    xhtmlEntityResolver(XHTMLEntityResolver) {
        applicationContext = application.mainContext
    }
    xmlReader(XMLReaderFactory) { DefaultBeanConfiguration b ->
        b.factoryMethod = 'createXMLReader'
        b.singleton = false     // must not be singleton! See FopService.
        entityResolver = ref('xhtmlEntityResolver')
    }
    xsltLogErrorListener(org.amcworld.springcrm.xml.LogErrorListener)

    /* HTTP clients */
    httpClient(HttpClients) { DefaultBeanConfiguration b ->
        b.factoryMethod = 'createDefault'
    }

    /* LDAP */
    ldapFactory(org.amcworld.springcrm.ldap.LdapFactory) { DefaultBeanConfiguration b ->
        b.factoryMethod = 'getInstance'
    }

    /* Google synchronization */
    googleContactSyncTask(GoogleContactSyncTask) {
        googleContactSync = ref('googleContactSync')
    }
    googleContactSync(GoogleContactSync) {
        googleOAuthService = ref('googleOAuthService')
        messageSource = ref('messageSource')
    }
    googleCalendarSync(GoogleCalendarSync) {
        googleOAuthService = ref('googleOAuthService')
        messageSource = ref('messageSource')
    }
}
