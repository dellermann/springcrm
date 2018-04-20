/*
 * ConfigControllerSpec.groovy
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

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.bson.types.ObjectId
import spock.lang.Specification


class ConfigControllerSpec extends Specification
    implements ControllerUnitTest<ConfigController>, DataTest
{

    //-- Feature methods ------------------------

    void 'The currency action returns a correct model'() {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        userService.getAvailableCurrencies() >> [
            Currency.getInstance('USD'), Currency.getInstance('EUR')
        ]
        userService.getNumFractionDigits() >> 3
        userService.getNumFractionDigitsExt() >> 2
        controller.userService = userService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        configService.getString('currency') >> 'EUR'
        configService.getInteger('termOfPayment') >> 21i
        controller.configService = configService

        when: 'the action is executed'
        controller.currency()

        then: 'the currencies are ordered by currency ID'
        //noinspection GroovyAssignabilityCheck
        [EUR: 'EUR (€)', USD: 'USD'] as LinkedHashMap == model.currencies

        and: 'the remaining model is correct'
        'EUR' == model.currentCurrency
        3 == model.numFractionDigits
        2 == model.numFractionDigitsExt
        21i == model.termOfPayment
    }

    void 'The fixSeqNumbers action returns a correct model'() {
        given: 'a list of sequence numbers'
        def seq1 = new SeqNumber(prefix: 'I')
        seq1.id = 'invoice'
        def seq2 = new SeqNumber(prefix: 'Q')
        seq2.id = 'quote'
        def seqNumbers = [seq1, seq2]

        and: 'a sequence number service instance'
        SeqNumberService seqNumberService = Mock()
        //noinspection GroovyAssignabilityCheck
        2 * seqNumberService.getFixedSeqNumbers() >> seqNumbers
        controller.seqNumberService = seqNumberService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        and: 'a work service instance'
        WorkService workService = Mock()
        controller.workService = workService

        and: 'some values'
        ObjectId id = new ObjectId()
        Work work = new Work(name: 'Groovy programming')
        work.id = id

        when: 'the action is executed with list and work instances'
        controller.fixSeqNumbers()

        then: 'the work items are loaded'
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDunningCharge') >> id
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDefaultInterest') >> id
        //noinspection GroovyAssignabilityCheck
        2 * workService.get(id) >> work

        and: 'the view is correctly set'
        'seqNumbers' == view
        null != flash.message

        and: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        seqNumbers == model.seqNumberList
        work == model.workDunningCharge
        work == model.workDefaultInterest

        when: 'the action is executed without list and work instances'
        response.reset()
        controller.fixSeqNumbers()

        then: 'the work items are loaded'
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDunningCharge') >> null
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDefaultInterest') >> null
        0 * workService.get(id)

        and: 'the view is correctly set'
        'seqNumbers' == view
        null != flash.message

        and: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        seqNumbers == model.seqNumberList
        null == model.workDunningCharge
        null == model.workDefaultInterest
    }

    void 'The loadSelValues action returns a correct model'() {
        given: 'a selection value service instance'
        SelValueService selValueService = Mock()
        controller.selValueService = selValueService

        and: 'some selection values'
        mockDomains Industry
        def list = [
            new Industry(name: 'IT'),
            new Industry(name: 'Banking'),
            new Industry(name: 'Education')
        ]

        when: 'the action is executed with an empty list'
        params.type = 'industry'
        controller.loadSelValues()

        then: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findAllByClass(Industry) >> []
        model.selValueList.empty

        when: 'the action is executed with a list'
        response.reset()
        params.type = 'industry'
        controller.loadSelValues()

        then: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findAllByClass(Industry) >> list
        //noinspection GroovyAssignabilityCheck
        list == model.selValueList
    }

    void 'The loadSeqNumbers action returns a correct model'() {
        given: 'a list of sequence numbers'
        def seq1 = new SeqNumber(prefix: 'I')
        seq1.id = 'invoice'
        def seq2 = new SeqNumber(prefix: 'Q')
        seq2.id = 'quote'
        def seqNumbers = [seq1, seq2]

        and: 'a sequence number service instance'
        SeqNumberService seqNumberService = Mock()
        controller.seqNumberService = seqNumberService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        and: 'a work service instance'
        WorkService workService = Mock()
        controller.workService = workService

        and: 'some values'
        ObjectId id = new ObjectId()
        Work work = new Work(name: 'Groovy programming')
        work.id = id

        when: 'the action is executed with list and work instances'
        controller.loadSeqNumbers()

        then: 'the work items are loaded'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.list(null) >> seqNumbers
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDunningCharge') >> id
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDefaultInterest') >> id
        //noinspection GroovyAssignabilityCheck
        2 * workService.get(id) >> work

        and: 'the view is correctly set'
        'seqNumbers' == view

        and: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        seqNumbers == model.seqNumberList
        work == model.workDunningCharge
        work == model.workDefaultInterest
    }

    void 'The loadSeqNumbers action with empty list returns a correct model'() {
        given: 'a sequence number service instance'
        SeqNumberService seqNumberService = Mock()
        controller.seqNumberService = seqNumberService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        and: 'a work service instance'
        WorkService workService = Mock()
        controller.workService = workService

        when: 'the action is executed without a list and work instances'
        controller.loadSeqNumbers()

        then: 'the work items are loaded'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.list(null) >> []
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDunningCharge') >> null
        //noinspection GroovyAssignabilityCheck
        1 * configService.getObjectId('workIdDefaultInterest') >> null
        2 * workService.get(null)

        and: 'the view is correctly set'
        'seqNumbers' == view

        and: 'the model is correctly set'
        null == model.seqNumberList
        null == model.workDunningCharge
        null == model.workDefaultInterest
    }

    void 'The loadTaxRates action returns a correct model'() {
        given: 'a selection value service instance'
        SelValueService selValueService = Mock()
        controller.selValueService = selValueService

        and: 'some selection values'
        mockDomains TaxRate
        def list = [
            new TaxRate(name: '9 %'),
            new TaxRate(name: '19 %'),
            new TaxRate(name: '21 %')
        ]

        when: 'the action is executed with an empty list'
        controller.loadTaxRates()

        then: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findAllByClass(TaxRate) >> []
        null == model.taxRateList

        when: 'the action is executed with a list'
        response.reset()
        controller.loadTaxRates()

        then: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findAllByClass(TaxRate) >> list
        //noinspection GroovyAssignabilityCheck
        list == model.taxRateList
    }

    void 'The loadTenant action returns a correct model'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        and: 'a tenant'
        def tenant = new Tenant()

        when: 'the action is executed'
        controller.loadTenant()

        then: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        1 * configService.loadTenant() >> tenant
        tenant == model.tenant
    }

    void 'The save action persists configuration values'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        when: 'the action is executed without values'
        controller.save()

        then: 'a redirect is issued'
        //noinspection GroovyAssignabilityCheck
        0 * configService.store(_, _)
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with values'
        response.reset()
        params.config = [
            _foo: 'on',
            bar: 'amc-world.de',
            whee: 4859,
            ldapPort: 400,
            ldapBindPasswd: 'secret',
            mailPassword: 'verysecret'
        ]
        controller.save()

        then: 'the values are stored'
        1 * configService.store('foo', false)
        1 * configService.store('bar', 'amc-world.de')
        1 * configService.store('whee', 4859)
        1 * configService.store('ldapPort', 400)
        1 * configService.store('ldapBindPasswd', 'secret')
        1 * configService.store('mailPassword', 'verysecret')

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with a null LDAP port'
        response.reset()
        params.config = [ldapPort: null]
        controller.save()

        then: 'the value is stored'
        1 * configService.store('ldapPort', 389)

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with an empty LDAP port'
        response.reset()
        params.config = [ldapPort: '']
        controller.save()

        then: 'the value is stored'
        1 * configService.store('ldapPort', 389)

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with a null LDAP password'
        response.reset()
        params.config = [ldapBindPasswd: null]
        controller.save()

        then: 'the value is not stored'
        0 * configService.store('ldapBindPasswd', _)

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with an empty LDAP password'
        response.reset()
        params.config = [ldapBindPasswd: '']
        controller.save()

        then: 'the value is not stored'
        0 * configService.store('ldapBindPasswd', _)

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with a null mail password'
        response.reset()
        params.config = [mailPassword: null]
        controller.save()

        then: 'the value is not stored'
        0 * configService.store('mailPassword', _)

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with an empty mail password'
        response.reset()
        params.config = [mailPassword: '']
        controller.save()

        then: 'the value is not stored'
        0 * configService.store('mailPassword', _)

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with an unset mail config value'
        response.reset()
        params.config = [mailUseConfig: 'null']
        controller.save()

        then: 'the value is deleted'
        1 * configService.delete('mailUseConfig')

        and: 'a redirect is issued'
        null != flash.message
        '/config/index' == response.redirectedUrl
    }

    void 'The saveSelValues action persists the configuration values'() {
        given: 'a selection value service instance'
        SelValueService selValueService = Mock()
        controller.selValueService = selValueService

        and: 'some mocks'
        mockDomains Industry, QuoteStage, Rating, Salutation
        def salutation1 = new Salutation(name: 'Herr', orderId: 10i)
        def salutation2 = new Salutation(name: 'Frau', orderId: 20i)
        def stage1 = new QuoteStage(name: 'revised', orderid: 20i)

        when: 'the action is executed with some values'
        params.selValues = [
            salutation: '''[
                {"id": 2, "name": "Mrs."},
                {"id": -1, "name": "Ms."},
                {"id": 3, "remove": true},
                {"id": 1, "name": "Mr."}
            ]''',
            industry: null,
            rating: '',
            quoteStage: '''[
                {"id": 603, "remove": true},
                {"id": 601, "name": "foobar"}
            ]'''
        ]
        controller.saveSelValues()

        then: 'the values are correctly stored'
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findByClassAndId(Salutation, 2L) >> salutation2
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.save(
            { it == salutation2 && it.name == 'Mrs.' && it.orderId == 10i }
        )
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.save({ it.name == 'Ms.' && it.orderId == 20i })
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findByClassAndId(Salutation, 1L) >> salutation1
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.save(
            { it == salutation1 && it.name == 'Mr.' && it.orderId == 30i }
        )
        1 * selValueService.delete(3L)

        and: 'some names are not changed'
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findByClassAndId(QuoteStage, 601L) >> stage1
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.save({ it == stage1 && it.orderId == 10i })
        0 * selValueService.delete(603L)

        and: 'some types are not considered'
        //noinspection GroovyAssignabilityCheck
        0 * selValueService.findByClassAndId(Industry, _)
        0 * selValueService.findByClassAndId(Rating, _)
    }

    void 'The saveSeqNumbers action persists the sequence numbers'() {
        given: 'a sequence number service instance'
        SeqNumberService seqNumberService = Mock()
        controller.seqNumberService = seqNumberService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        and: 'some sequence numbers'
        def seq1 = new SeqNumber(
            prefix: 'R', startValue: 10000, endValue: 99999, orderId: 40i
        )
        seq1.id = 'invoice'
        def seq2 = new SeqNumber(
            prefix: 'A', startValue: 10000, endValue: 99999, orderId: 20i
        )
        seq2.id = 'quote'

        when: 'the action is executed'
        params.seqNumbers = [
            invoice: [prefix: 'I', startValue: '40000', endValue: '89999'],
            quote: [prefix: 'Q', startValue: '30000', endValue: '49999']
        ]
        controller.saveSeqNumbers()

        then: 'the sequence numbers are correctly stored'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('invoice') >> seq1
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.save({
            it.id == 'invoice' && it.prefix == 'I' &&
                it.startValue == 40000 && it.endValue == 89999
        }) >> seq1
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('quote') >> seq2
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.save({
            it.id == 'quote' && it.prefix == 'Q' &&
                it.startValue == 30000 && it.endValue == 49999
        }) >> seq2

        and: 'the service IDs are deleted'
        1 * configService.delete('workIdDunningCharge')
        1 * configService.delete('workIdDefaultInterest')

        and: 'a redirect is issued'
        '/config/index' == response.redirectedUrl

        and: 'some IDs'
        def id1 = new ObjectId()
        def id2 = new ObjectId()

        when: 'the action is executed with input errors'
        response.reset()
        params.seqNumbers = [
            invoice: [prefix: 'I', startValue: '40000', endValue: -1],
            quote: [prefix: 'Q', startValue: '30000', endValue: -10]
        ]
        params.workIdDunningCharge = id1.toString()
        params.workIdDefaultInterest = id2.toString()
        controller.saveSeqNumbers()

        then: 'the sequence numbers are correctly stored'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('invoice') >> seq1
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.save({
            it.id == 'invoice' && it.prefix == 'I' &&
                it.startValue == 40000
        }) >> null
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('quote') >> seq2
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.save({
            it.id == 'quote' && it.prefix == 'Q' &&
                it.startValue == 30000
        }) >> null

        and: 'the service IDs are deleted'
        1 * configService.store('workIdDunningCharge', id1.toString())
        1 * configService.store('workIdDefaultInterest', id2.toString())

        and: 'the sequence number view is used again'
        //noinspection GroovyAssignabilityCheck
        [seq2, seq1] == model.seqNumberList
        'seqNumbers' == view
    }

    void 'The saveTaxRates action persists the tax rates'() {
        given: 'a selection value service instance'
        SelValueService selValueService = Mock()
        controller.selValueService = selValueService

        and: 'some tax rates'
        def tr1 = new TaxRate(name: '7 %', taxValue: 7.0, orderId: 10i)
        tr1.id = 700L
        def tr2 = new TaxRate(name: '18 %', taxValue: 18.0, orderId: 20i)
        tr2.id = 701L
        def tr3 = new TaxRate(name: '21 %', taxValue: 21.0, orderId: 30i)
        tr3.id = 702L

        when: 'the action is executed without values'
        controller.saveTaxRates()

        then: 'only the redirect is issued'
        //noinspection GroovyAssignabilityCheck
        0 * selValueService.delete(_)
        //noinspection GroovyAssignabilityCheck
        0 * selValueService.save(_)
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with values'
        response.reset()
        params.taxRates = '''[
            {"id": 701, "name": "19"},
            {"id": 700, "name": "7"},
            {"id": 702, "name": null},
            {"id": -1, "name": "22"}
        ]'''
        controller.saveTaxRates()

        then: 'the tax rates are correctly stored'
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findByClassAndId(TaxRate, tr2.id) >> tr2
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.save({
            it.name == '19 %' && it.taxValue == 0.19 && it.orderId == 10i
        })
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.findByClassAndId(TaxRate, tr1.id) >> tr1
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.save({
            it.name == '7 %' && it.taxValue == 0.07 && it.orderId == 20i
        })
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.delete(702L)
        //noinspection GroovyAssignabilityCheck
        1 * selValueService.save({
            it.name == '22 %' && it.taxValue == 0.22 && it.orderId == 30i
        })

        and: 'a redirect is issued'
        '/config/index' == response.redirectedUrl
    }

    void 'The saveTenant action persists the tenant data'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        and: 'some tenant data'
        def tenant = new Tenant(
            name: 'MyOrganization Ltd.',
            street: '45, Park Ave.',
            postalCode: 'NY-39344',
            location: 'Santa Barbara',
            phone: '+1 21 20404044',
            fax: '+1 21 20404049',
            email: 'info@myorganization.org',
            website: 'www.myorganization.example',
            bankName: 'YourBank',
            bankCode: '123456789',
            accountNumber: '987654321'
        )

        when: 'the action is executed'
        controller.saveTenant tenant

        then: 'the tenant data is stored'
        1 * configService.storeTenant(tenant)
        '/config/index' == response.redirectedUrl

        when: 'the action is executed with errors'
        response.reset()
        tenant.name = null
        controller.saveTenant tenant

        then: 'the tenant data is not stored'
        //noinspection GroovyAssignabilityCheck
        0 * configService.storeTenant(_)
        tenant == model.tenant
        'loadTenant' == view
    }

    void 'The show action returns a correct model'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        and: 'a list of configuration values'
        def config1 = new Config(value: 'whee')
        config1.id = 'foo'
        def config2 = new Config(value: '45')
        config2.id = 'bar'
        def list = [config1, config2]

        when: 'the action is executed without configurations'
        params.page = 'page1'
        controller.show()

        then: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        1 * configService.list() >> []
        '/config/page1' == view
        model.configData.isEmpty()

        when: 'the action is executed without configurations'
        response.reset()
        params.page = 'taxRates'
        controller.show()

        then: 'the model is correctly set'
        //noinspection GroovyAssignabilityCheck
        1 * configService.list() >> list
        '/config/taxRates' == view
        list.size() == model.configData.size()
        'whee' == model.configData.foo
        '45' == model.configData.bar
    }
}
