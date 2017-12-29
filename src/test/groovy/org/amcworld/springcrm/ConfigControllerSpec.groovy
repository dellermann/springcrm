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

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification


class ConfigControllerSpec extends Specification
    implements ControllerUnitTest<ConfigController>
{

    //-- Feature methods ------------------------

    def 'Show action with empty data'() {
        given: 'a page ID'
        params.page = 'page1'

        when: 'I call the show action'
        controller.show()

        then: 'I get the required page'
        '/config/page1' == view

        and: 'there are no configuration data'
        null != model.configData
        model.configData.isEmpty()  // don't use .empty here!
    }

    def 'Show action with non-empty data'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'a page ID'
        params.page = 'page2'

        when: 'I call the show action'
        controller.show()

        then: 'I get the required page'
        '/config/page2' == view

        and: 'there are valid configuration data'
        Map d = model.configData
        null != d
        9 == d.size()
        'foo-value' == d.foo
        '15' == d.'int-val'
        'EUR' == d.currency
        'secret' == d.ldapBindPasswd
    }

    def 'Currency action'() {
        given: 'a UserService instance'
        UserService us = Mock()
        us.getCurrentLocale() >> Locale.GERMANY
        us.getAvailableCurrencies() >> [
            Currency.getInstance('USD'), Currency.getInstance('EUR')
        ]
        us.getNumFractionDigits() >> 3
        us.getNumFractionDigitsExt() >> 2
        controller.userService = us

        and: 'some configuration data'
        makeConfigFixture()

        when: 'I call the currency action'
        def model = controller.currency()

        then: 'I get the currencies ordered by currency ID'
        LinkedHashMap<String, String> currencies = model.currencies
        Iterator iter = currencies.iterator()
        iter.hasNext()
        Map.Entry<String, String> e1 = iter.next()
        'EUR' == e1.key
        'EUR (€)' == e1.value
        iter.hasNext()
        Map.Entry<String, String> e2 = iter.next()
        'USD' == e2.key
        'USD' == e2.value
        !iter.hasNext()

        and: 'the current currency'
        'EUR' == model.currentCurrency

        and: 'the number of fraction digits'
        3 == model.numFractionDigits
        2 == model.numFractionDigitsExt
    }

    def 'Save action with common data'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'some request parameters'
        params.config = [foo: 'new-foo-value1', 'new-val': 'new', _bar: '25']

        when: 'I call the save action'
        controller.save()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'I get a message about the success'
        'default.updated.message' == flash.message

        and: 'the submitted values are stored'
        def ch = ConfigHolder.instance
        'new-foo-value1' == ch['foo'].value
        'new' == ch['new-val'].value
        '15' == ch['int-val'].value
        'false' == ch['bar'].value

        and: 'all other data are unmodified'
        'EUR' == ch['currency'].value
    }

    def 'Save action with LDAP data'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'some request parameters'
        params.config = [
            ldapHost: 'earth.mynet.example', ldapPort: '', ldapBindPasswd: ''
        ]

        when: 'I call the save action'
        controller.save()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'I get a message about the success'
        'default.updated.message' == flash.message

        and: 'the submitted values are stored'
        def ch = ConfigHolder.instance
        'earth.mynet.example' == ch['ldapHost'].value
        '389' == ch['ldapPort'].value                // issue #35
        'secret' == ch['ldapBindPasswd'].value       // issue #35

        and: 'all other data are unmodified'
        'foo-value' == ch['foo'].value
        'EUR' == ch['currency'].value
    }

    def 'Save mail data with no configuration'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'some request parameters'
        params.config = [mailUseConfig: 'null', mailHost: '192.168.100.1']

        when: 'I call the save action'
        controller.save()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'I get a message about the success'
        'default.updated.message' == flash.message

        and: 'the submitted values are stored'
        def ch = ConfigHolder.instance
        null == ch['mailUseConfig']
        '192.168.100.1' == ch['mailHost'].value

        and: 'all other data are unmodified'
        'EUR' == ch['currency'].value
    }

    def 'Save mail data with system configuration'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'some request parameters'
        params.config = [mailUseConfig: 'false', mailHost: '192.168.100.1']

        when: 'I call the save action'
        controller.save()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'I get a message about the success'
        'default.updated.message' == flash.message

        and: 'the submitted values are stored'
        def ch = ConfigHolder.instance
        'false' == ch['mailUseConfig'].value
        '192.168.100.1' == ch['mailHost'].value

        and: 'all other data are unmodified'
        'EUR' == ch['currency'].value
    }

    def 'Save mail data with user configuration'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'some request parameters'
        params.config = [
            mailUseConfig: 'false', mailHost: '192.168.100.1', mailPort: '25',
            mailUserName: 'jdoe', mailPassword: 'secret', mailAuth: 'true',
            mailEncryption: 'starttls'
        ]

        when: 'I call the save action'
        controller.save()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'I get a message about the success'
        'default.updated.message' == flash.message

        and: 'the submitted values are stored'
        def ch = ConfigHolder.instance
        'false' == ch['mailUseConfig'].value
        '192.168.100.1' == ch['mailHost'].value
        '25' == ch['mailPort'].value
        'jdoe' == ch['mailUserName'].value
        'secret' == ch['mailPassword'].value
        'true' == ch['mailAuth'].value
        'starttls' == ch['mailEncryption'].value

        and: 'all other data are unmodified'
        'EUR' == ch['currency'].value
    }

    def 'Save mail data with user configuration without password'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'some request parameters'
        params.config = [
            mailUseConfig: 'false', mailHost: '192.168.100.1', mailPort: '25',
            mailUserName: 'jdoe', mailAuth: 'true', mailEncryption: 'starttls'
        ]

        when: 'I call the save action'
        controller.save()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'I get a message about the success'
        'default.updated.message' == flash.message

        and: 'the submitted values are stored'
        def ch = ConfigHolder.instance
        'false' == ch['mailUseConfig'].value
        '192.168.100.1' == ch['mailHost'].value
        '25' == ch['mailPort'].value
        'jdoe' == ch['mailUserName'].value
        'very-secret' == ch['mailPassword'].value
        'true' == ch['mailAuth'].value
        'starttls' == ch['mailEncryption'].value

        and: 'all other data are unmodified'
        'EUR' == ch['currency'].value
    }

    def 'LoadClient action'() {
        given: 'some client data'
        makeClientFixture()

        when: 'I call the loadClient action'
        def model = controller.loadClient()

        then: 'I get the client data'
        null != model
        matchClient model.client
    }

    def 'SaveClient action success'() {
        given: 'a client object'
        def client = mockCommandObject(Client)
        client.name = 'AMC World Technologies GmbH'
        client.street = 'Fischerinsel 1'
        client.postalCode = '10179'
        client.location = 'Berlin'
        client.phone = '+49 30 8321475-0'
        client.email = 'info@amc-world.de'
        assert client.validate()

        when: 'I call the save action'
        controller.saveClient client

        then: 'I am redirected to the configuration overview page'
        assert '/config/index' == response.redirectedUrl
    }

    def 'SaveClient action error'() {
        given: 'a client object'
        def client = mockCommandObject(Client)
        client.name = 'AMC World Technologies GmbH'
        client.street = ''
        assert !client.validate()

        when: 'I call the save action'
        controller.saveClient client

        then: 'the form is displayed anew'
        '/config/loadClient' == view
        'AMC World Technologies GmbH' == model.client.name
        '' == model.client.street
    }

    def 'LoadSelValues action with enabled values'() {
        given: 'some selection values'
        makeSelValuesFixture()
        makeTaxRatesFixture()

        when: 'I call the loadSelValues action'
        params.type = 'salutation'
        def res = controller.loadSelValues()

        then: 'I get valid selection values'
        null != res.selValueList
        3 == res.selValueList.size()
        'Mr.' == res.selValueList[0].name
        'Mrs.' == res.selValueList[1].name
    }

    def 'LoadSelValues action with disabled values'() {
        given: 'some selection values'
        makeSelValuesFixture()

        when: 'I call the loadSelValues action'
        params.type = 'quoteStage'
        def res = controller.loadSelValues()

        then: 'I get valid selection values'
        null != res.selValueList
        5 == res.selValueList.size()
        600L == res.selValueList[0].id
        'created' == res.selValueList[0].name
        601L == res.selValueList[1].id
        'revised' == res.selValueList[1].name
    }

    def 'SaveSelValues action with enabled values'() {
        given: 'some selection values'
        makeSelValuesFixture()

        when: 'I call the saveSelValues action'
        params.selValues = [
            salutation: '[{id: 2, name: "Frau"}, {id: 1, name: "Herr"}, {id: 3, remove: true}]'
        ]
        controller.saveSelValues()

        then: 'I am redirected to the configuration overview page'
        assert '/config/index' == response.redirectedUrl

        and: 'the selection values are stored correctly'
        2 == Salutation.count()
        'Herr' == Salutation.get(1).name
        20 == Salutation.get(1).orderId
        'Frau' == Salutation.get(2).name
        10 == Salutation.get(2).orderId

        and: 'the other selection values are unmodified'
        5 == QuoteStage.count()
    }

    def 'SaveSelValues action with disabled values'() {
        given: 'some selection values'
        makeSelValuesFixture()

        when: 'I call the saveSelValues action'
        params.selValues = [
            quoteStage: '[{id: 601, name: "durchgesehen"}, {id: 600, name: "erstellt"}, {id: 602, remove: true}]'
        ]
        controller.saveSelValues()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'the selection values are ordered correctly but not renamed'
        5 == QuoteStage.count()
        'created' == QuoteStage.get(600).name
        20 == QuoteStage.get(600).orderId
        'revised' == QuoteStage.get(601).name
        10 == QuoteStage.get(601).orderId

        and: 'the disabled value has not been deleted'
        'sent' == QuoteStage.get(602).name
        30 == QuoteStage.get(602).orderId

        and: 'the other selection values are unmodified'
        3 == Salutation.count()
    }

    def 'LoadTaxRates action'() {
        given: 'some selection values'
        makeSelValuesFixture()
        makeTaxRatesFixture()

        when: 'I call the loadTaxRates action'
        def res = controller.loadTaxRates()

        then: 'I get valid tax rates'
        null != res.taxRateList
        2 == res.taxRateList.size()
        '7 %' == res.taxRateList[0].name
        '19 %' == res.taxRateList[1].name
    }

    def 'SaveTaxRates action'() {
        given: 'some selection values'
        makeTaxRatesFixture()

        when: 'I call the saveTaxRates action'
        params.taxRates = '[{id: 1, name: "8"}, {id: 2, name: null}, {id: -1, name: "20"}]'
        controller.saveTaxRates()

        then: 'I am redirected to the configuration overview page'
        assert '/config/index' == response.redirectedUrl

        and: 'the tax rates are stored correctly'
        2 == TaxRate.count()
        def tr1 = TaxRate.get(1)
        null != tr1
        '8 %' == tr1.name
        10 == tr1.orderId
        0.08 == tr1.taxValue
        null == TaxRate.get(2)
        def tr2 = TaxRate.get(3)
        null != tr2
        '20 %' == tr2.name
        20 == tr2.orderId
        0.2 == tr2.taxValue
    }

    def 'LoadSeqNumbers action without special services'() {
        given: 'some sequence numbers'
        makeSeqNumberFixture()

        and: 'a mock of the Work class'
        mockDomain Work

        when: 'I call the loadSeqNumbers action'
        controller.loadSeqNumbers()

        then: 'the sequence number configuration page is displayed'
        '/config/seqNumbers' == view

        and: 'the sequence numbers are loaded correctly'
        List<SeqNumber> l = model.seqNumberList
        null != l
        2 == l.size()
        SeqNumber sn = l.find { it.controllerName == 'invoice' }
        'I' == sn.prefix
        20000 == sn.startValue
        99999 == sn.endValue

        and: 'the special services are not defined'
        null == model.workDunningCharge
        null == model.workDefaultInterest
    }

    def 'LoadSeqNumbers action with special services'() {
        given: 'some sequence numbers'
        makeSeqNumberFixture()

        and: 'a mock of the Work class'
        def w1 = new Work(number: 59000, name: 'Mahngebühr')
        def w2 = new Work(number: 59010, name: 'Verzugszinsen')
        mockDomain Work, [w1, w2]

        and: 'the associated configuration values'
        mockDomain Config, [
            [name: 'workIdDunningCharge', value: w1.id.toString()],
            [name: 'workIdDefaultInterest', value: w2.id.toString()]
        ]

        when: 'I call the loadSeqNumbers action'
        controller.loadSeqNumbers()

        then: 'the sequence number configuration page is displayed'
        '/config/seqNumbers' == view

        and: 'the sequence numbers are loaded correctly'
        List<SeqNumber> l = model.seqNumberList
        null != l
        2 == l.size()

        and: 'the special services are not defined'
        1 == model.workDunningCharge.id
        'Mahngebühr' == model.workDunningCharge.name
        2 == model.workDefaultInterest.id
        'Verzugszinsen' == model.workDefaultInterest.name
    }

    def 'SaveSeqNumbers action success without special services'() {
        given: 'some sequence numbers'
        makeSeqNumberFixture()

        when: 'I call the saveSeqNumbers action'
        params.seqNumbers = [
            '1': [prefix: 'A', suffix: '', startValue: 30000, endValue: 99999],
            '2': [prefix: 'R', suffix: '', startValue: 40000, endValue: 99999]
        ]
        controller.saveSeqNumbers()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'the sequence numbers are stored correctly'
        2 == SeqNumber.count()
        def s1 = SeqNumber.get(1)
        'A' == s1.prefix
        30000 == s1.startValue
        99999 == s1.endValue
        def s2 = SeqNumber.get(2)
        'R' == s2.prefix
        40000 == s2.startValue
        99999 == s2.endValue

        and: 'there are no settings concerning special services'
        0 == Config.countByName('workIdDunningCharge')
        0 == Config.countByName('workIdDefaultInterest')
    }

    def 'SaveSeqNumbers action success with special services'() {
        given: 'some sequence numbers'
        makeSeqNumberFixture()

        when: 'I call the saveSeqNumbers action'
        params.seqNumbers = [
            '1': [prefix: 'A', suffix: '', startValue: 30000, endValue: 99999],
            '2': [prefix: 'M', suffix: '', startValue: 40000, endValue: 79999]
        ]
        params.workIdDunningCharge = '15'
        params.workIdDefaultInterest = '392'
        controller.saveSeqNumbers()

        then: 'I am redirected to the configuration overview page'
        '/config/index' == response.redirectedUrl

        and: 'the sequence numbers are stored correctly'
        2 == SeqNumber.count()
        def s1 = SeqNumber.get(1)
        'A' == s1.prefix
        30000 == s1.startValue
        99999 == s1.endValue
        def s2 = SeqNumber.get(2)
        'M' == s2.prefix
        40000 == s2.startValue
        79999 == s2.endValue

        and: 'there are two settings concerning special services'
        ConfigHolder ch = ConfigHolder.instance
        '15' == ch['workIdDunningCharge'].value
        '392' == ch['workIdDefaultInterest'].value
    }

    def 'SaveSeqNumbers action error'() {
        given: 'some sequence numbers'
        makeSeqNumberFixture()

        when: 'I call the saveSeqNumbers action'
        params.seqNumbers = [
            '1': [prefix: 'A', suffix: 'XXXXXX', startValue: 30000, endValue: 99999],
            '2': [prefix: 'R', suffix: '', startValue: 40000, endValue: 99999]
        ]
        controller.saveSeqNumbers()

        then: 'the form is displayed anew'
        '/config/seqNumbers' == view

        and: 'the enterred sequence numbers are available'
        List<SeqNumber> l = model.seqNumberList
        null != l
        2 == l.size()
        SeqNumber sq1 = l[0]
        'quote' == sq1.controllerName
        'A' == sq1.prefix
        'XXXXXX' == sq1.suffix
        30000 == sq1.startValue
        99999 == sq1.endValue
        SeqNumber sq2 = l[1]
        'invoice' == sq2.controllerName
        'R' == sq2.prefix
        '' == sq2.suffix
        40000 == sq2.startValue
        99999 == sq2.endValue
    }


    //-- Non-public methods ---------------------

    protected void makeClientFixture() {
        mockDomain Config, [
            [name: 'clientName', value: 'MyOrganization Ltd.'],
            [name: 'clientStreet', value: '45, Park Ave.'],
            [name: 'clientPostalCode', value: 'NY-39344'],
            [name: 'clientLocation', value: 'Santa Barbara'],
            [name: 'clientPhone', value: '+1 21 20404044'],
            [name: 'clientEmail', value: 'info@myorganization.example'],
            [name: 'clientWebsite', value: 'www.myorganization.example'],
            [name: 'clientBankName', value: 'YourBank'],
            [name: 'clientBankCode', value: '123456789'],
            [name: 'clientAccountNumber', value: '987654321'],
        ]
    }

    protected void makeConfigFixture() {
        mockDomain Config, [
            [name: 'foo', value: 'foo-value'],
            [name: 'bar', value: 'bar-value'],
            [name: 'int-val', value: '15'],
            [name: 'float-val', value: '8.45'],
            [name: 'boolean-val', value: 'true'],
            [name: 'currency', value: 'EUR'],
            [name: 'termOfPayment', value: '14'],
            [name: 'ldapBindPasswd', value: 'secret'],
            [name: 'mailPassword', value: 'very-secret']
        ]
    }

    protected void makeSelValuesFixture() {
        mockDomain Salutation, [
            [name: 'Mr.', orderId: 10],
            [name: 'Mrs.', orderId: 20],
            [name: 'Ms.', orderId: 30]
        ]

        /*
         * Implementation notes: currently, we cannot call something like this:
         *   new InvoiceStage(id: 900, name: 'created')
         * In this case the ID is null. We must set it afterwards otherwise it
         * is not stored.
         */
        def l = []
        def stage = new QuoteStage(name: 'created', orderId: 10)
        stage.id = 600
        l << stage
        stage = new QuoteStage(name: 'revised', orderId: 20)
        stage.id = 601
        l << stage
        stage = new QuoteStage(name: 'sent', orderId: 30)
        stage.id = 602
        l << stage
        stage = new QuoteStage(name: 'accepted', orderId: 40)
        stage.id = 603
        l << stage
        stage = new QuoteStage(name: 'rejected', orderId: 50)
        stage.id = 604
        l << stage
        mockDomain QuoteStage, l
    }

    protected void makeTaxRatesFixture() {
        mockDomain TaxRate, [
            [name: '7 %', orderId: 10, taxValue: 0.07d],
            [name: '19 %', orderId: 20, taxValue: 0.19d]
        ]
    }

    protected void makeSeqNumberFixture() {
        mockDomain SeqNumber, [
            [controllerName: 'quote', prefix: 'Q', startValue: 10000i, endValue: 99999i],
            [controllerName: 'invoice', prefix: 'I', startValue: 20000i, endValue: 99999i]
        ]
    }

    protected void matchClient(Client client) {
        assert 'MyOrganization Ltd.' == client.name
        assert '45, Park Ave.' == client.street
        assert 'NY-39344' == client.postalCode
        assert 'Santa Barbara' == client.location
        assert '+1 21 20404044' == client.phone
        assert 'info@myorganization.example' == client.email
        assert 'www.myorganization.example' == client.website
        assert 'YourBank' == client.bankName
        assert '123456789' == client.bankCode
        assert '987654321' == client.accountNumber
    }
}
