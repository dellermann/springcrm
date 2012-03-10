/*
 * ConfigControllerTests.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * The class {@code ConfigControllerTests} contains the unit test cases for
 * {@code ConfigController}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(ConfigController)
@Mock([Config, Salutation, QuoteStage, TaxRate, SeqNumber])
class ConfigControllerTests {

    //-- Public methods -------------------------

    void testIndex() {}

    void testShowEmpty() {
        params.page = 'page1'
        controller.show()
        assert '/config/page1' == view
        assert null != model.configData
        assert model.configData.isEmpty()
    }

    void testShowNonEmpty() {
        makeConfigFixture()
        params.page = 'page2'
        controller.show()
        assert '/config/page2' == view
        assert null != model.configData
        assert 5 == model.configData.size()
        assert 'foo-value' == model.configData['foo']
        assert '15' == model.configData['int-val']
    }

    void testSave() {
        makeConfigFixture()
        params.config = [
            [key: 'foo', value: 'new-foo-value1'],
            [key: 'new-val', value: 'new']
        ]
        controller.save()
        assert 'default.updated.message' == flash.message
        assert '/config/index' == response.redirectedUrl
        def configHolder = ConfigHolder.instance
        assert 'new-foo-value1' == configHolder['foo'].value
        assert 'new' == configHolder['new-val'].value
        assert '15' == configHolder['int-val'].value
    }

    void testSaveWithReturnUrl() {
        makeConfigFixture()
        params.config = [
            [key: 'foo', value: 'new-foo-value2'],
            [key: 'xyz-val', value: 'xyz']
        ]
        params.returnUrl = '/organization/show/5'
        controller.save()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        def configHolder = ConfigHolder.instance
        assert 'new-foo-value2' == configHolder['foo'].value
        assert 'xyz' == configHolder['xyz-val'].value
        assert '15' == configHolder['int-val'].value
    }

    void testLoadClient() {
        def model = controller.loadClient()
        assert null != model
    }

    void testSaveClientSuccess() {
        def client = mockCommandObject(Client)
        client.name = 'AMC World Technologies GmbH'
        client.street = 'Fischerinsel 1'
        client.postalCode = '10179'
        client.location = 'Berlin'
        client.phone = '+49 30 8321475-0'
        client.email = 'info@amc-world.de'
        client.validate()
        controller.saveClient(client)
        assert '/config/index' == response.redirectedUrl
    }

    void testSaveClientError() {
        def client = mockCommandObject(Client)
        client.name = 'AMC World Technologies GmbH'
        client.street = ''
        client.validate()
        controller.saveClient(client)
        assert '/config/loadClient' == view
        assert 'AMC World Technologies GmbH' == model.client.name
        assert '' == model.client.street
    }

    void testLoadSelValuesEnabled() {
        makeSelValuesFixture()
        makeTaxRatesFixture()
        params.type = 'salutation'
        controller.loadSelValues()
        assert 3 == response.json.size()
        assert 'Mr.' == response.json[0].name
        assert !response.json[0].disabled
        assert 'Mrs.' == response.json[1].name
        assert !response.json[1].disabled
    }

    void testLoadSelValuesDisabled() {
        makeSelValuesFixture()
        params.type = 'quoteStage'
        controller.loadSelValues()
        assert 5 == response.json.size()
        assert 600 == response.json[0].id
        assert 'created' == response.json[0].name
        assert response.json[0].disabled
        assert 601 == response.json[1].id
        assert 'revised' == response.json[1].name
        assert response.json[1].disabled
    }

    void testSaveSelValuesEnabled() {
        makeSelValuesFixture()
        params.selValues = [
            salutation: '[{id: 2, name: "Frau"}, {id: 1, name: "Herr"}, {id: 3, remove: true}]'
        ]
        controller.saveSelValues()
        assert '/config/index' == response.redirectedUrl
        assert 2 == Salutation.count()
        assert 'Herr' == Salutation.get(1).name
        assert 20 == Salutation.get(1).orderId
        assert 'Frau' == Salutation.get(2).name
        assert 10 == Salutation.get(2).orderId
        assert 5 == QuoteStage.count()
    }

    void testSaveSelValuesDisabled() {
        makeSelValuesFixture()
        params.selValues = [
            quoteStage: '[{id: 601, name: "durchgesehen"}, {id: 600, name: "erstellt"}, {id: 602, remove: true}]'
        ]
        controller.saveSelValues()
        assert '/config/index' == response.redirectedUrl
        assert 3 == Salutation.count()
        assert 5 == QuoteStage.count()
        assert 'created' == QuoteStage.get(600).name
        assert 20 == QuoteStage.get(600).orderId
        assert 'revised' == QuoteStage.get(601).name
        assert 10 == QuoteStage.get(601).orderId
        assert 'sent' == QuoteStage.get(602).name
        assert 30 == QuoteStage.get(602).orderId
    }

    void testLoadTaxRates() {
        makeSelValuesFixture()
        makeTaxRatesFixture()
        controller.loadTaxRates()
        assert 2 == response.json.size()
        assert 7 == response.json[0].name
        assert 19 == response.json[1].name
    }

    void testSaveTaxRates() {
        makeTaxRatesFixture()
        params.selValues = [
            taxRates: '[{id: 1, name: "8"}, {id: 2, name: null}, {id: -1, name: "20"}]'
        ]
        controller.saveTaxRates()
        assert '/config/index' == response.redirectedUrl
        assert 2 == TaxRate.count()
        def taxRate = TaxRate.get(1)
        assert null != taxRate
        assert '8 %' == taxRate.name
        assert 10 == taxRate.orderId
        assert 0.08 == taxRate.taxValue
        assert null == TaxRate.get(2)
        taxRate = TaxRate.get(3)
        assert null != taxRate
        assert '20 %' == taxRate.name
        assert 20 == taxRate.orderId
        assert 0.2 == taxRate.taxValue
    }

    void testLoadSeqNumbers() {
        makeSeqNumberFixture()
        controller.loadSeqNumbers()
        assert '/config/seqNumbers' == view
        assert null != model.seqNumberList
        assert 2 == model.seqNumberList.size()
    }

    void testSaveSeqNumbersSuccess() {
        makeSeqNumberFixture()
        params.seqNumbers = [
            '1': [prefix: 'A', suffix: '', startValue: 30000, endValue: 99999],
            '2': [prefix: 'R', suffix: '', startValue: 40000, endValue: 99999]
        ]
        controller.saveSeqNumbers()
        assert '/config/index' == response.redirectedUrl
        assert 2 == SeqNumber.count()
        def seq = SeqNumber.get(1)
        assert 'A' == seq.prefix
        assert 30000 == seq.startValue
        seq = SeqNumber.get(2)
        assert 'R' == seq.prefix
        assert 40000 == seq.startValue
    }

    void testSaveSeqNumbersError() {
        makeSeqNumberFixture()
        params.seqNumbers = [
            '1': [prefix: 'A', suffix: 'XXXXXX', startValue: 30000, endValue: 99999],
            '2': [prefix: 'R', suffix: '', startValue: 40000, endValue: 99999]
        ]
        controller.saveSeqNumbers()
        assert '/config/seqNumbers' == view
        assert null != model.seqNumberList
        assert 2 == model.seqNumberList.size()
    }


    //-- Non-public methods ---------------------

    protected void makeConfigFixture() {
        mockDomain(
            Config, [
                [name: 'foo', value: 'foo-value'],
                [name: 'bar', value: 'bar-value'],
                [name: 'int-val', value: '15'],
                [name: 'float-val', value: '8.45'],
                [name: 'boolean-val', value: 'true']
            ]
        )
    }

    protected void makeSelValuesFixture() {
        mockDomain(
            Salutation, [
                [name: 'Mr.', orderId: 10],
                [name: 'Mrs.', orderId: 20],
                [name: 'Ms.', orderId: 30]
            ]
        )

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
        mockDomain(QuoteStage, l)
    }

    protected void makeTaxRatesFixture() {
        mockDomain(
            TaxRate, [
                [name: '7 %', orderId: 10, taxValue: 0.07d],
                [name: '19 %', orderId: 20, taxValue: 0.19d]
            ]
        )
    }

    protected void makeSeqNumberFixture() {
        mockDomain(
            SeqNumber, [
                [controllerName: 'quote', prefix: 'Q', startValue: 10000i, endValue: 99999i],
                [controllerName: 'invoice', prefix: 'I', startValue: 20000i, endValue: 99999i]
            ]
        )
    }
}
