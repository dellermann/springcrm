/*
 * resources.groovy
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


import org.amcworld.springcrm.*
import org.amcworld.springcrm.converter.*
import org.amcworld.springcrm.google.*


beans = {

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
//    [Short.TYPE, Integer.TYPE, Float.TYPE, Long.TYPE, Double.TYPE].each { numberType ->
//        "defaultGrails${numberType.name}Converter"(PrimitiveNumberValueConverter) {
//            targetType = numberType
//        }
//    }
    invoicingTransactionXMLFactory(InvoicingTransactionXMLFactory) {
        markdownService = ref('markdownService')
    }

    /* startup difference sets */
    startupDiffSet2(org.amcworld.springcrm.install.diffset.NoteMarkdownDiffSet) {
        markdownService = ref('markdownService')
    }

    /* Google synchronization types */
    googleSyncFactory(GoogleSyncFactory) { bean ->
        bean.factoryMethod = 'getDefaultInstance'
        grailsApplication = ref('grailsApplication')
    }
    "${GoogleSyncType.CONTACT.beanName}"(GoogleContactSync) { bean ->
        bean.singleton = false
        googleOAuthService = ref('googleOAuthService')
        messageSource = ref('messageSource')
    }
    "${GoogleSyncType.CALENDAR.beanName}"(GoogleCalendarSync) { bean ->
        bean.singleton = false
        googleOAuthService = ref('googleOAuthService')
        messageSource = ref('messageSource')
    }
}
