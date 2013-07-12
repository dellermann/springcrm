/*
 * DunningFunctionalTests.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


/**
 * The class {@code DunningFunctionalTests} represents a functional test case
 * for the dunning section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
class DunningFunctionalTests extends InvoicingTransactionTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()

    Invoice invoice


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        invoice = prepareInvoice(org, p, null, null)
        if (!name.methodName.startsWith('testCreate')) {
            prepareDunning org, p, invoice
        }

        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open '/dunning/list'
    }

    @Test
    void testCreateDunningSuccess() {
        clickToolbarButton 0, getUrl('/dunning/create')
        checkTitles 'Mahnung anlegen', 'Mahnungen', 'Neue Mahnung'
        setInputValue 'subject', 'Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == selectAutocompleteEx('invoice', 'Werbe')
        setInputValue 'stage.id', '2202'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '6.5.2013'
        setInputValue 'dueDatePayment_date', '13.5.2013'
        setInputValue 'shippingDate_date', '7.5.2013'
        setInputValue 'carrier.id', '501'

        assert 'Dörpstraat 25' == getInputValue('billingAddrStreet')
        assert '23898' == getInputValue('billingAddrPostalCode')
        assert 'Duvensee' == getInputValue('billingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('billingAddrState')
        assert 'Deutschland' == getInputValue('billingAddrCountry')
        assert 'Dörpstraat 25' == getInputValue('shippingAddrStreet')
        assert '23898' == getInputValue('shippingAddrPostalCode')
        assert 'Duvensee' == getInputValue('shippingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddrState')
        assert 'Deutschland' == getInputValue('shippingAddrCountry')
        setInputValue 'headerText', 'zur angegebenen Rechnung konnte **bis heute** kein Zahlungseingang verzeichnet werden.'

        assert 2 == numPriceTableRows
        checkRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', null, '5,00', '5,00', '19,0'
        setPriceTableInputValue 0, 'unitPrice', '3'
        assert '3,00' == getPriceTableRowTotal(0)
        assert '13,00' == subtotalNet
        checkTaxRates([['19,0', '2,47']])
        assert '15,47' == subtotalGross
        assert '15,47' == total

        checkRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen', null, '10,00', '10,00', '19,0'
        setPriceTableInputValue 1, 'unitPrice', '53,22'
        getPriceTableInput(1, 'name').click()
        assert '56,22' == subtotalNet
        checkTaxRates([['19,0', '10,68']])
        assert '66,90' == subtotalGross
        assert '66,90' == total

        setInputValue 'footerText', 'Die **Mahngebühren und Verzugszinsen** ergeben sich aus unseren AGB.'
        setInputValue 'termsAndConditions', ['700']
        setInputValue 'notes', 'Zahlung auch nach _wiederholter_ telefonischer Mahnung nicht erfolgt.'

        checkStillUnpaid '0.0', '66,90', 'still-unpaid-unpaid'
        submitForm getUrl('/dunning/show/')

        assert 'Mahnung Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'M-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/show/'))
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 6)
        assert '1. Mahnung' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '06.05.2013' == getShowFieldText(col, 1)
        assert '13.05.2013' == getShowFieldText(col, 2)
        assert '07.05.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        WebElement balanceField = getShowField(col, 8)
        assert '-66,90 €' == balanceField.text.trim()
        assert 'still-unpaid-unpaid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
        fieldSet = dataSheet.findElement(By.xpath('div[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        def field = getShowField(fieldSet, 1)
        assert 'zur angegebenen Rechnung konnte bis heute kein Zahlungseingang verzeichnet werden.' == field.text
        assert 'bis heute' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', '3,00 €', '3,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen\nVerzugszinsen 5 %', '53,22 €', '53,22 €', '19,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '56,22 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '10,68 €' == tr.findElement(By.className('currency')).text
        assert '66,90 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Mahngebühren und Verzugszinsen ergeben sich aus unseren AGB.' == field.text
        assert 'Mahngebühren und Verzugszinsen' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Zahlung auch nach wiederholter telefonischer Mahnung nicht erfolgt.' == field.text
        assert 'wiederholter' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testCreateDunningErrors() {
        clickToolbarButton 0, getUrl('/dunning/create')
        checkTitles 'Mahnung anlegen', 'Mahnungen', 'Neue Mahnung'
        submitForm getUrl('/dunning/save')

        assert checkErrorFields([
            'subject', 'organization.id', 'invoice.id', 'dueDatePayment',
            'dueDatePayment_date'
        ])
        cancelForm getUrl('/dunning/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Mahnung anlegen' == link.text
        assert getUrl('/dunning/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testCreateDunningFromInvoice() {
        open '/invoice/list'
        clickListItem 0, 1
        clickActionBarButton 2, getUrl('/dunning/create?invoice=')
        checkTitles 'Mahnung anlegen', 'Mahnungen', 'Neue Mahnung'

        def col = driver.findElement(By.xpath('//form[@id="dunning-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('M-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('invoice')).getAttribute('value')
        assert '2200' == getInputValue('stage.id')
        checkDate 'docDate_date'
        assert '' == getInputValue('dueDatePayment_date')
        assert '' == getInputValue('shippingDate_date')
        assert 'null' == getInputValue('carrier.id')
        assert '' == getInputValue('paymentDate_date')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '17,85', 'still-unpaid-unpaid'
        assert 'null' == getInputValue('paymentMethod.id')
        assert 'Dörpstraat 25' == getInputValue('billingAddrStreet')
        assert '' == getInputValue('billingAddrPoBox')
        assert '23898' == getInputValue('billingAddrPostalCode')
        assert 'Duvensee' == getInputValue('billingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('billingAddrState')
        assert 'Deutschland' == getInputValue('billingAddrCountry')
        assert 'Dörpstraat 25' == getInputValue('shippingAddrStreet')
        assert '' == getInputValue('shippingAddrPoBox')
        assert '23898' == getInputValue('shippingAddrPostalCode')
        assert 'Duvensee' == getInputValue('shippingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddrState')
        assert 'Deutschland' == getInputValue('shippingAddrCountry')
        assert '' == getInputValue('headerText')

        checkRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', null, '5,00', '5,00', '19,0'
        checkRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen', null, '10,00', '10,00', '19,0'
        assert '15,00' == subtotalNet
        checkTaxRates([['19,0', '2,85']])
        assert '17,85' == subtotalGross
        assert '17,85' == total

        assert '' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert '**Wichtig!** Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == getInputValue('notes')

        setInputValue 'stage.id', '2202'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '6.5.2013'
        setInputValue 'dueDatePayment_date', '13.5.2013'
        setInputValue 'shippingDate_date', '7.5.2013'
        setInputValue 'carrier.id', '501'
        setInputValue 'headerText', 'zur angegebenen Rechnung konnte **bis heute** kein Zahlungseingang verzeichnet werden.'
        setInputValue 'footerText', 'Die **Mahngebühren und Verzugszinsen** ergeben sich aus unseren AGB.'
        setInputValue 'termsAndConditions', ['700']
        setInputValue 'notes', 'Zahlung auch nach _wiederholter_ telefonischer Mahnung nicht erfolgt.'
        checkStillUnpaid '0.0', '17,85', 'still-unpaid-unpaid'
        submitForm getUrl('/dunning/show/')

        assert 'Mahnung Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'M-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/show/'))
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 6)
        assert '1. Mahnung' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '06.05.2013' == getShowFieldText(col, 1)
        assert '13.05.2013' == getShowFieldText(col, 2)
        assert '07.05.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        WebElement balanceField = getShowField(col, 8)
        assert '-17,85 €' == balanceField.text.trim()
        assert 'still-unpaid-unpaid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
        fieldSet = dataSheet.findElement(By.xpath('div[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        def field = getShowField(fieldSet, 1)
        assert 'zur angegebenen Rechnung konnte bis heute kein Zahlungseingang verzeichnet werden.' == field.text
        assert 'bis heute' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', '5,00 €', '5,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen\nVerzugszinsen 5 %', '10,00 €', '10,00 €', '19,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '15,00 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '2,85 €' == tr.findElement(By.className('currency')).text
        assert '17,85 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Mahngebühren und Verzugszinsen ergeben sich aus unseren AGB.' == field.text
        assert 'Mahngebühren und Verzugszinsen' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Zahlung auch nach wiederholter telefonischer Mahnung nicht erfolgt.' == field.text
        assert 'wiederholter' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testShowDunning() {
        int id = clickListItem 0, 1, '/dunning/show'
        checkTitles 'Mahnung anzeigen', 'Mahnungen', 'Werbekampagne Frühjahr 2013'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'M-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/show/'))
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 6)
        assert '1. Mahnung' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '06.05.2013' == getShowFieldText(col, 1)
        assert '13.05.2013' == getShowFieldText(col, 2)
        assert '07.05.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        WebElement balanceField = getShowField(col, 8)
        assert '-66,90 €' == balanceField.text.trim()
        assert 'still-unpaid-unpaid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
        fieldSet = dataSheet.findElement(By.xpath('div[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        def field = getShowField(fieldSet, 1)
        assert 'zur angegebenen Rechnung konnte bis heute kein Zahlungseingang verzeichnet werden.' == field.text
        assert 'bis heute' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', '3,00 €', '3,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen\nVerzugszinsen 5 %', '53,22 €', '53,22 €', '19,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '56,22 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '10,68 €' == tr.findElement(By.className('currency')).text
        assert '66,90 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Mahngebühren und Verzugszinsen ergeben sich aus unseren AGB.' == field.text
        assert 'Mahngebühren und Verzugszinsen' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Zahlung auch nach wiederholter telefonischer Mahnung nicht erfolgt.' == field.text
        assert 'wiederholter' == field.findElement(By.tagName('em')).text

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
        link = toolbar.findElement(By.xpath('li[1]/a'))
        assert 'white' == link.getAttribute('class')
        assert getUrl('/dunning/list') == link.getAttribute('href')
        assert 'Liste' == link.text
        link = toolbar.findElement(By.xpath('li[2]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl('/dunning/create') == link.getAttribute('href')
        assert 'Anlegen' == link.text
        link = toolbar.findElement(By.xpath('li[3]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl("/dunning/edit/${id}") == link.getAttribute('href')
        assert 'Bearbeiten' == link.text
        link = toolbar.findElement(By.xpath('li[4]/a'))
        assert 'blue' == link.getAttribute('class')
        assert getUrl("/dunning/copy/${id}") == link.getAttribute('href')
        assert 'Kopieren' == link.text
        link = toolbar.findElement(By.xpath('li[5]/a'))
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert getUrl("/dunning/delete/${id}") == link.getAttribute('href')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl("/dunning/show/${id}") == driver.currentUrl

        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
        link = actions.findElement(By.xpath('li[1]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('menu-button')
        assert link.getAttribute('href').startsWith(getUrl("/dunning/print/${id}"))
        assert 'Drucken' == link.text
        link = actions.findElement(By.xpath('li[2]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('menu-button')
        assert link.getAttribute('href').startsWith(getUrl("/dunning/print/${id}?duplicate=1"))
        assert 'Kopie drucken' == link.text
        link = actions.findElement(By.xpath('li[3]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/create?dunning=${id}"))
        assert 'Gutschrift erzeugen' == link.text
        driver.quit()

        assert 1 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testListDunnings() {
        checkTitles 'Mahnungen', 'Mahnungen'
        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        def link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/dunning/show/'))
        assert 'M-10000-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/dunning/show/'))
        assert 'Werbekampagne Frühjahr 2013' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('status')
        assert td.getAttribute('class').contains('payment-state-red')
        assert 'versendet' == td.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('date')
        assert '06.05.2013' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('date')
        assert '13.05.2013' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('currency')
        assert '66,90 €' == td.text
        td = tr.findElement(By.xpath('td[9]'))
        assert td.getAttribute('class').contains('currency')
        assert td.getAttribute('class').contains('balance-state-red')
        assert '-66,90 €' == td.text
        td = tr.findElement(By.xpath('td[10]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/dunning/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/dunning/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/dunning/list') == driver.currentUrl
        driver.quit()

        assert 1 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testEditDunningSuccess() {
        maximizeWindow()
        clickListActionButton 0, 0, getUrl('/dunning/edit/')
        checkTitles 'Mahnung bearbeiten', 'Mahnungen', 'Werbekampagne Frühjahr 2013'
        def col = driver.findElement(By.xpath('//form[@id="dunning-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('M-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('invoice')).getAttribute('value')
        assert '2202' == getInputValue('stage.id')
        assert '2300' == getInputValue('level.id')
        assert '06.05.2013' == getInputValue('docDate_date')
        assert '13.05.2013' == getInputValue('dueDatePayment_date')
        assert '07.05.2013' == getInputValue('shippingDate_date')
        assert '501' == getInputValue('carrier.id')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '66,90', 'still-unpaid-unpaid'
        assert 'Dörpstraat 25' == getInputValue('billingAddrStreet')
        assert '' == getInputValue('billingAddrPoBox')
        assert '23898' == getInputValue('billingAddrPostalCode')
        assert 'Duvensee' == getInputValue('billingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('billingAddrState')
        assert 'Deutschland' == getInputValue('billingAddrCountry')
        assert 'Dörpstraat 25' == getInputValue('shippingAddrStreet')
        assert '' == getInputValue('shippingAddrPoBox')
        assert '23898' == getInputValue('shippingAddrPostalCode')
        assert 'Duvensee' == getInputValue('shippingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddrState')
        assert 'Deutschland' == getInputValue('shippingAddrCountry')
        assert 'zur angegebenen Rechnung konnte **bis heute** kein Zahlungseingang verzeichnet werden.' == getInputValue('headerText')

        checkRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', null, '3,00', '3,00', '19,0'
        checkRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen', 'Verzugszinsen 5 %', '53,22', '53,22', '19,0'
        assert '56,22' == subtotalNet
        checkTaxRates([['19,0', '10,68']])
        assert '66,90' == subtotalGross
        assert '66,90' == total

        assert 'Die **Mahngebühren und Verzugszinsen** ergeben sich aus unseren AGB.' == getInputValue('footerText')
        assert ['700'] == getInputValue('termsAndConditions')
        assert 'Zahlung auch nach _wiederholter_ telefonischer Mahnung nicht erfolgt.' == getInputValue('notes')

        setInputValue 'subject', 'Werbekampagne Spring \'13'
        setInputValue 'stage.id', '2203'
        checkDate 'paymentDate_date'
        setInputValue 'level.id', '2301'
        setInputValue 'paymentDate_date', '10.5.2013'
        stillUnpaid.click()
        assert '66,90' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '0,00', 'still-unpaid-paid'
        setInputValue 'paymentMethod.id', '2401'

        setPriceTableInputValue 0, 'unitPrice', '5'
        checkRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', null, '5,00', '5,00', '19,0'
        assert '58,22' == subtotalNet
        checkTaxRates([['19,0', '11,06']])
        assert '69,28' == subtotalGross
        assert '69,28' == total

        setPriceTableInputValue 1, 'description', 'Verzugszinsen 7 %'
        setPriceTableInputValue 1, 'unitPrice', '74,51'
        checkRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen', 'Verzugszinsen 7 %', '74,51', '74,51', '19,0'
        assert '79,51' == subtotalNet
        checkTaxRates([['19,0', '15,11']])
        assert '94,62' == subtotalGross
        assert '94,62' == total

        moveRowUp 1
        checkRowValues 0, 'S-99001', '1', 'Einheiten', 'Verzugszinsen', 'Verzugszinsen 7 %', '74,51', '74,51', '19,0'
        checkRowValues 1, 'S-99000', '1', 'Einheiten', 'Mahngebühren', null, '5,00', '5,00', '19,0'
        assert '79,51' == subtotalNet
        checkTaxRates([['19,0', '15,11']])
        assert '94,62' == subtotalGross
        assert '94,62' == total
        assert !getPriceTableRow(0).findElement(By.className('up-btn')).displayed
        assert !getPriceTableRow(1).findElement(By.className('down-btn')).displayed

        moveRowDown 0
        checkRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', null, '5,00', '5,00', '19,0'
        checkRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen', 'Verzugszinsen 7 %', '74,51', '74,51', '19,0'
        assert '79,51' == subtotalNet
        checkTaxRates([['19,0', '15,11']])
        assert '94,62' == subtotalGross
        assert '94,62' == total
        assert !getPriceTableRow(0).findElement(By.className('up-btn')).displayed
        assert !getPriceTableRow(1).findElement(By.className('down-btn')).displayed

        checkStillUnpaid '0.0', '27,72', 'still-unpaid-unpaid'
        stillUnpaid.click()
        assert '94,62' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '0,00', 'still-unpaid-paid'
        submitForm getUrl('/dunning/show/')

        assert 'Mahnung Werbekampagne Spring \'13 wurde geändert.' == flashMessage
        assert 'Werbekampagne Spring \'13' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'M-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Spring \'13' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/show/'))
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'bezahlt' == getShowFieldText(col, 6)
        assert '2. Mahnung' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '06.05.2013' == getShowFieldText(col, 1)
        assert '13.05.2013' == getShowFieldText(col, 2)
        assert '07.05.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '10.05.2013' == getShowFieldText(col, 5)
        assert '94,62 €' == getShowFieldText(col, 6)
        assert 'Überweisung' == getShowFieldText(col, 7)
        WebElement balanceField = getShowField(col, 8)
        assert '0,00 €' == balanceField.text.trim()
        assert 'still-unpaid-paid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
        fieldSet = dataSheet.findElement(By.xpath('div[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        def field = getShowField(fieldSet, 1)
        assert 'zur angegebenen Rechnung konnte bis heute kein Zahlungseingang verzeichnet werden.' == field.text
        assert 'bis heute' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-99000', '1', 'Einheiten', 'Mahngebühren', '5,00 €', '5,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-99001', '1', 'Einheiten', 'Verzugszinsen\nVerzugszinsen 7 %', '74,51 €', '74,51 €', '19,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        WebElement tr = tfoot.findElement(By.className('subtotal-net'))
        assert '79,51 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '15,11 €' == tr.findElement(By.className('currency')).text
        assert '94,62 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Mahngebühren und Verzugszinsen ergeben sich aus unseren AGB.' == field.text
        assert 'Mahngebühren und Verzugszinsen' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Zahlung auch nach wiederholter telefonischer Mahnung nicht erfolgt.' == field.text
        assert 'wiederholter' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testEditDunningErrors() {
        maximizeWindow()
        clickListActionButton 0, 0, getUrl('/dunning/edit/')
        checkTitles 'Mahnung bearbeiten', 'Mahnungen', 'Werbekampagne Frühjahr 2013'

        clearInput 'subject'
        clearInput 'dueDatePayment_date'
        driver.findElement(By.id('organization')).clear()
        driver.findElement(By.id('invoice')).clear()
        removeRow 0
        assert !getPriceTableRow(0).findElement(By.className('remove-btn')).displayed
        submitForm getUrl('/dunning/update')

        assert checkErrorFields([
            'subject', 'organization.id', 'invoice.id', 'dueDatePayment',
            'dueDatePayment_date'
        ])
        cancelForm getUrl('/dunning/list')
        driver.quit()

        assert 1 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testDeleteDunningAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/dunning/list'))
        assert 'Mahnung wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Dunning.count()
        checkInvoice invoice
    }

    @Test
    void testDeleteDunningNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/dunning/list') == driver.currentUrl
        driver.quit()

        assert 1 == Dunning.count()
        checkInvoice invoice
    }


    //-- Non-public methods ---------------------

    @Override
    protected Object getDatasets() {
        [
            'test-data/install-data.xml', 'test-data/sales-items.xml',
            'test-data/dunning.xml'
        ]
    }
}
