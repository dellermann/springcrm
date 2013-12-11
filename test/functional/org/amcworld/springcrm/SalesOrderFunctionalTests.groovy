/*
 * SalesOrderFunctionalTests.groovy
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


/**
 * The class {@code SalesOrderFunctionalTests} represents a functional test
 * case for the sales order section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
@RunWith(JUnit4)
class SalesOrderFunctionalTests extends InvoicingTransactionTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()

    Quote quote


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        quote = prepareQuote(org, p)
        if (!name.methodName.startsWith('testCreate')) {
            prepareSalesOrder org, p, quote
        }

        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open '/sales-order/list'
    }

    @Test
    void testCreateSalesOrderSuccess() {
        clickToolbarButton 0, getUrl('/sales-order/create')
        checkTitles 'Verkaufsbestellung anlegen', 'Verkaufsbestellungen', 'Neue Verkaufsbestellung'
        setInputValue 'subject', 'Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == selectAutocompleteEx('quote', 'Werbe')
        setInputValue 'stage.id', '802'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '4.3.2013'
        setInputValue 'dueDate_date', '28.3.2013'
        setInputValue 'shippingDate_date', '5.3.2013'
        setInputValue 'carrier.id', '501'
        assert 'Dörpstraat 25' == getInputValue('billingAddr.street')
        assert '23898' == getInputValue('billingAddr.postalCode')
        assert 'Duvensee' == getInputValue('billingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('billingAddr.state')
        assert 'Deutschland' == getInputValue('billingAddr.country')
        assert 'Dörpstraat 25' == getInputValue('shippingAddr.street')
        assert '23898' == getInputValue('shippingAddr.postalCode')
        assert 'Duvensee' == getInputValue('shippingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddr.state')
        assert 'Deutschland' == getInputValue('shippingAddr.country')
        setInputValue 'headerText', 'vielen Dank für Ihren Auftrag zur Werbekampange **"Frühjahr 2013"**.'

        assert 1 == numPriceTableRows
        setPriceTableInputValue 0, 'number', 'S-10000'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption der geplanten Werbekampagne'
        setPriceTableInputValue 0, 'unitPrice', '440'
        setPriceTableInputValue 0, 'tax', '19'
        assert '440,000' == getPriceTableRowTotal(0)
        assert '440,000' == subtotalNet
        checkTaxRates([['19,0', '83,600']])
        assert '523,600' == subtotalGross
        assert '523,600' == total

        assert 2 == addNewPriceTableRow()
        openSelectorAndSelect 1, 'products', 'P-10000'
        checkRowValues 1, 'P-10000', '1', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '2,490', '7,0'
        assert '442,490' == subtotalNet
        checkTaxRates([['7,0', '0,174'], ['19,0', '83,600']])
        assert '526,264' == subtotalGross
        assert '526,264' == total
        setPriceTableInputValue 1, 'quantity', '2'
        checkRowValues 1, 'P-10000', null, 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '444,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '83,600']])
        assert '528,929' == subtotalGross
        assert '528,929' == total

        assert 3 == addNewPriceTableRow()
        openSelectorAndSelect 2, 'services', 'S-10100'
        checkRowValues 2, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        assert '894,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '169,100']])
        assert '1.064,429' == subtotalGross
        assert '1.064,429' == total
        moveRowUp 2
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'

        assert 4 == addNewPriceTableRow()
        openSelectorAndSelect 3, 'products', 'P-10001'
        checkRowValues 3, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,990', '2,990', '7,0'
        assert '897,970' == subtotalNet
        checkTaxRates([['7,0', '0,558'], ['19,0', '169,100']])
        assert '1.067,628' == subtotalGross
        assert '1.067,628' == total
        setPriceTableInputValue 3, 'unitPrice', '3'
        checkRowValues 3, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '3,000', '3,000', '7,0'
        assert '897,980' == subtotalNet
        checkTaxRates([['7,0', '0,559'], ['19,0', '169,100']])
        assert '1.067,639' == subtotalGross
        assert '1.067,639' == total
        moveRowUp 3
        moveRowUp 2
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,000', '440,000', '19'
        checkRowValues 1, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '3,000', '3,000', '7,0'
        checkRowValues 2, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 3, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '897,980' == subtotalNet
        checkTaxRates([['7,0', '0,559'], ['19,0', '169,100']])
        assert '1.067,639' == subtotalGross
        assert '1.067,639' == total
        moveRowDown 1
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,000', '440,000', '19'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '3,000', '3,000', '7,0'
        checkRowValues 3, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '897,980' == subtotalNet
        checkTaxRates([['7,0', '0,559'], ['19,0', '169,100']])
        assert '1.067,639' == subtotalGross
        assert '1.067,639' == total
        assert 3 == removeRow(2)
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,000', '440,000', '19'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '894,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '169,100']])
        assert '1.064,429' == subtotalGross
        assert '1.064,429' == total

        setInputValue 'footerText', 'Die Umsetzung des Auftrags erfolgt **nach Pflichtenheft**.'
        setInputValue 'termsAndConditions', ['700', '701']
        setInputValue 'notes', 'Erste Teilergebnisse sollten vor dem *15.03.2013* vorliegen.'
        submitForm getUrl('/sales-order/show/')

        assert 'Verkaufsbestellung Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'B-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/show/'))
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04.03.2013' == getShowFieldText(col, 1)
        assert '28.03.2013' == getShowFieldText(col, 2)
        assert '05.03.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
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
        assert 'vielen Dank für Ihren Auftrag zur Werbekampange "Frühjahr 2013".' == field.text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,000 €', '440,000 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,000 €', '450,000 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,490 €', '4,980 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Mustervorschau' == field.findElement(By.className('item-name')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgaben' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '894,980 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,349 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '169,100 €' == tr.findElement(By.className('currency')).text
        assert '1.064,429 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Umsetzung des Auftrags erfolgt nach Pflichtenheft.' == field.text
        assert 'nach Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Erste Teilergebnisse sollten vor dem 15.03.2013 vorliegen.' == field.text
        assert '15.03.2013' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testCreateSalesOrderErrors() {
        clickToolbarButton 0, getUrl('/sales-order/create')
        checkTitles 'Verkaufsbestellung anlegen', 'Verkaufsbestellungen', 'Neue Verkaufsbestellung'
        submitForm getUrl('/sales-order/save')

        assert checkErrorFields(['subject', 'organization.id'])
        List<WebElement> errorMsgs = driver.findElements(By.xpath(
            '//form[@id="sales-order-form"]/fieldset[3]/div/ul[@class="field-msgs"]/li[@class="error-msg"]'
        ))
        assert 3 == errorMsgs.size()
        assert 'Pos. 1, Artikel/Leistung: Feld darf nicht leer sein.' == errorMsgs[0].text
        assert 'Pos. 1, Nummer: Feld darf nicht leer sein.' == errorMsgs[1].text
        assert 'Pos. 1, Einheit: Feld darf nicht leer sein.' == errorMsgs[2].text
        cancelForm getUrl('/sales-order/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Verkaufsbestellung anlegen' == link.text
        assert getUrl('/sales-order/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testCreateSalesOrderFromQuote() {
        open '/quote/list'
        clickListItem 0, 1
        clickActionBarButton 2, getUrl('/sales-order/create?quote=')
        checkTitles 'Verkaufsbestellung anlegen', 'Verkaufsbestellungen', 'Neue Verkaufsbestellung'

        def col = driver.findElement(By.xpath('//form[@id="sales-order-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('B-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('quote')).getAttribute('value')
        assert '800' == getInputValue('stage.id')
        checkDate 'docDate_date'
        assert '' == getInputValue('dueDate_date')
        assert '' == getInputValue('shippingDate_date')
        assert 'null' == getInputValue('carrier.id')
        assert '' == getInputValue('deliveryDate_date')
        assert 'Dörpstraat 25' == getInputValue('billingAddr.street')
        assert '' == getInputValue('billingAddr.poBox')
        assert '23898' == getInputValue('billingAddr.postalCode')
        assert 'Duvensee' == getInputValue('billingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('billingAddr.state')
        assert 'Deutschland' == getInputValue('billingAddr.country')
        assert 'Dörpstraat 25' == getInputValue('shippingAddr.street')
        assert '' == getInputValue('shippingAddr.poBox')
        assert '23898' == getInputValue('shippingAddr.postalCode')
        assert 'Duvensee' == getInputValue('shippingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddr.state')
        assert 'Deutschland' == getInputValue('shippingAddr.country')
        assert '''für die geplante Werbekampange **"Frühjahr 2013"** möchten wir Ihnen gern folgendes Angebot unterbreiten.

Die Einzelheiten wurden im Meeting am 21.01.2013 festgelegt. Sie finden ein vollständiges Protokoll auf [unserer Webseite](http://www.example.de/protokoll/).''' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,000', '440,000', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '894,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '169,100']])
        assert '1.064,429' == subtotalGross
        assert '1.064,429' == total

        assert 'Details zu den einzelnen Punkten finden Sie _im Pflichtenheft_.' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert 'Angebot unterliegt _möglicherweise_ weiteren Änderungen.' == getInputValue('notes')

        setInputValue 'stage.id', '802'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '4.3.2013'
        setInputValue 'dueDate_date', '28.3.2013'
        setInputValue 'carrier.id', '501'
        setInputValue 'headerText', 'vielen Dank für Ihren Auftrag zur Werbekampange **"Frühjahr 2013"**.'
        setInputValue 'footerText', 'Die Umsetzung des Auftrags erfolgt **nach Pflichtenheft**.'
        setInputValue 'notes', 'Erste Teilergebnisse sollten vor dem *15.03.2013* vorliegen.'
        submitForm getUrl('/sales-order/show/')

        assert 'Verkaufsbestellung Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'B-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/show/'))
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04.03.2013' == getShowFieldText(col, 1)
        assert '28.03.2013' == getShowFieldText(col, 2)
        assert dateFormatted == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
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
        assert 'vielen Dank für Ihren Auftrag zur Werbekampange "Frühjahr 2013".' == field.text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,000 €', '440,000 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,000 €', '450,000 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,490 €', '4,980 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Mustervorschau' == field.findElement(By.className('item-name')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgaben' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '894,980 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,349 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '169,100 €' == tr.findElement(By.className('currency')).text
        assert '1.064,429 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Umsetzung des Auftrags erfolgt nach Pflichtenheft.' == field.text
        assert 'nach Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Erste Teilergebnisse sollten vor dem 15.03.2013 vorliegen.' == field.text
        assert '15.03.2013' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testShowSalesOrder() {
        int id = clickListItem 0, 1, '/sales-order/show'
        checkTitles 'Verkaufsbestellung anzeigen', 'Verkaufsbestellungen', 'Werbekampagne Frühjahr 2013'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'B-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/show/'))
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04.03.2013' == getShowFieldText(col, 1)
        assert '28.03.2013' == getShowFieldText(col, 2)
        assert '05.03.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
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
        assert 'vielen Dank für Ihren Auftrag zur Werbekampange "Frühjahr 2013".' == field.text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,000 €', '440,000 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,000 €', '450,000 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,490 €', '4,980 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Mustervorschau' == field.findElement(By.className('item-name')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgaben' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '894,980 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,349 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '169,100 €' == tr.findElement(By.className('currency')).text
        assert '1.064,429 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Umsetzung des Auftrags erfolgt nach Pflichtenheft.' == field.text
        assert 'nach Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Erste Teilergebnisse sollten vor dem 15.03.2013 vorliegen.' == field.text
        assert '15.03.2013' == field.findElement(By.tagName('em')).text

        String param = "salesOrder=${id}"
        fieldSet = getFieldset(dataSheet, 6)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/invoice/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Rechnungen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?salesOrder=${id}"))
        assert 'Rechnung anlegen' == link.text
        assert waitForEmptyRemoteList(6)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'sales-order', id

        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
        link = actions.findElement(By.xpath('li[1]/div[@class="button-group"]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/sales-order/print/${id}"))
        assert 'Drucken' == link.text
        link = actions.findElement(By.xpath('li[2]/div[@class="button-group"]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/sales-order/print/${id}?duplicate=1"))
        assert 'Kopie drucken' == link.text
        link = actions.findElement(By.xpath('li[3]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?salesOrder=${id}"))
        assert 'Rechnung erzeugen' == link.text
        driver.quit()

        assert 1 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testListSalesOrders() {
        checkTitles 'Verkaufsbestellungen', 'Verkaufsbestellungen'
        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        def link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/show/'))
        assert 'B-10000-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/show/'))
        assert 'Werbekampagne Frühjahr 2013' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('status')
        assert 'versendet' == td.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('date')
        assert '04.03.2013' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('date')
        assert '28.03.2013' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('currency')
        assert '1.064,43 €' == td.text
        td = tr.findElement(By.xpath('td[9]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/sales-order/list') == driver.currentUrl
        driver.quit()

        assert 1 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testEditSalesOrderSuccess() {
        clickListActionButton 0, 0, getUrl('/sales-order/edit/')
        checkTitles 'Verkaufsbestellung bearbeiten', 'Verkaufsbestellungen', 'Werbekampagne Frühjahr 2013'
        def col = driver.findElement(By.xpath('//form[@id="sales-order-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('B-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('quote')).getAttribute('value')
        assert '802' == getInputValue('stage.id')
        assert '04.03.2013' == getInputValue('docDate_date')
        assert '28.03.2013' == getInputValue('dueDate_date')
        assert '05.03.2013' == getInputValue('shippingDate_date')
        assert 'Dörpstraat 25' == getInputValue('billingAddr.street')
        assert '' == getInputValue('billingAddr.poBox')
        assert '23898' == getInputValue('billingAddr.postalCode')
        assert 'Duvensee' == getInputValue('billingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('billingAddr.state')
        assert 'Deutschland' == getInputValue('billingAddr.country')
        assert 'Dörpstraat 25' == getInputValue('shippingAddr.street')
        assert '' == getInputValue('shippingAddr.poBox')
        assert '23898' == getInputValue('shippingAddr.postalCode')
        assert 'Duvensee' == getInputValue('shippingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddr.state')
        assert 'Deutschland' == getInputValue('shippingAddr.country')
        assert 'vielen Dank für Ihren Auftrag zur Werbekampange **"Frühjahr 2013"**.' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,000', '440,000', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '894,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '169,100']])
        assert '1.064,429' == subtotalGross
        assert '1.064,429' == total

        assert 'Die Umsetzung des Auftrags erfolgt **nach Pflichtenheft**.' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert 'Erste Teilergebnisse sollten vor dem *15.03.2013* vorliegen.' == getInputValue('notes')

        setInputValue 'subject', 'Big Sale Spring \'13'
        setInputValue 'dueDate_date', '19.04.2013'
        setInputValue 'shippingDate_date', '6.3.2013'

        setPriceTableInputValue 0, 'unitPrice', '450'
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,000', '450,000', '19,0'
        assert '904,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '171,000']])
        assert '1.076,329' == subtotalGross
        assert '1.076,329' == total

        assert 2 == removeRow(1)
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,000', '450,000', '19,0'
        checkRowValues 1, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '454,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '85,500']])
        assert '540,829' == subtotalGross
        assert '540,829' == total

        assert 3 == addNewPriceTableRow()
        WebElement dialog = openSelector(2, 'products')
        dialog.findElement(By.xpath('.//th[2]/a')).click()
        def tbody = dialog.findElement(By.tagName('tbody'))
        def wait = new WebDriverWait(driver, 5)
        assert wait.until(ExpectedConditions.stalenessOf(tbody))
        tbody = dialog.findElement(By.tagName('tbody'))
        assert 3 == tbody.findElements(By.xpath('./tr')).size()
        assert 'Papier A4 80 g/m²' == tbody.findElement(By.xpath('./tr[1]/td[2]')).text
        assert 'Papier A4 90 g/m²' == tbody.findElement(By.xpath('./tr[2]/td[2]')).text
        assert 'Stempel' == tbody.findElement(By.xpath('./tr[3]/td[2]')).text
        dialog.findElement(By.xpath('.//th[2]/a')).click()
        assert wait.until(ExpectedConditions.stalenessOf(tbody))
        tbody = dialog.findElement(By.tagName('tbody'))
        assert 3 == tbody.findElements(By.xpath('./tr')).size()
        assert 'Stempel' == tbody.findElement(By.xpath('./tr[1]/td[2]')).text
        assert 'Papier A4 90 g/m²' == tbody.findElement(By.xpath('./tr[2]/td[2]')).text
        assert 'Papier A4 80 g/m²' == tbody.findElement(By.xpath('./tr[3]/td[2]')).text
        assert 2 == dialog.findElements(By.xpath('.//ul[@class="letter-bar"]/li[@class="available"]')).size()
        dialog.findElement(By.xpath('.//ul[@class="letter-bar"]/li[6]/a')).click()
        assert wait.until(ExpectedConditions.stalenessOf(tbody))
        tbody = dialog.findElement(By.tagName('tbody'))
        assert 3 == tbody.findElements(By.xpath('./tr')).size()
        assert 'Papier A4 80 g/m²' == tbody.findElement(By.xpath('./tr[1]/td[2]')).text
        assert 'Papier A4 90 g/m²' == tbody.findElement(By.xpath('./tr[2]/td[2]')).text
        assert 'Stempel' == tbody.findElement(By.xpath('./tr[3]/td[2]')).text
        dialog.findElement(By.name('search')).sendKeys('Papier')
        dialog.findElement(By.tagName('button')).click()
        assert wait.until(ExpectedConditions.stalenessOf(tbody))
        tbody = dialog.findElement(By.tagName('tbody'))
        assert 2 == tbody.findElements(By.xpath('./tr')).size()
        assert 'Papier A4 80 g/m²' == tbody.findElement(By.xpath('./tr[1]/td[2]')).text
        assert 'Papier A4 90 g/m²' == tbody.findElement(By.xpath('./tr[2]/td[2]')).text
        assert 'Papier' == dialog.findElement(By.name('search')).getAttribute('value')
        assert 1 == dialog.findElements(By.xpath('.//ul[@class="letter-bar"]/li[@class="available"]')).size()
        dialog.findElement(By.name('search')).clear()
        dialog.findElement(By.tagName('button')).click()
        assert wait.until(ExpectedConditions.stalenessOf(tbody))
        tbody = dialog.findElement(By.tagName('tbody'))
        assert 3 == tbody.findElements(By.xpath('./tr')).size()
        assert 'Papier A4 80 g/m²' == tbody.findElement(By.xpath('./tr[1]/td[2]')).text
        assert 'Papier A4 90 g/m²' == tbody.findElement(By.xpath('./tr[2]/td[2]')).text
        assert 'Stempel' == tbody.findElement(By.xpath('./tr[3]/td[2]')).text
        dialog.findElement(By.linkText('P-10700')).click()

        checkRowValues 2, 'P-10700', '1', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,990', '8,990', '19,0'
        setPriceTableInputValue 2, 'quantity', '4'
        checkRowValues 2, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,990', '35,960', '19,0'
        assert '490,940' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '92,332']])
        assert '583,621' == subtotalGross
        assert '583,621' == total
        openSelectorAndAbort 2, 'products'
        checkRowValues 2, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,990', '35,960', '19,0'
        moveRowUp 2
        moveRowUp 1
        checkRowValues 0, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,990', '35,960', '19,0'
        checkRowValues 1, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '490,940' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '92,332']])
        assert '583,621' == subtotalGross
        assert '583,621' == total
        moveRowDown 0
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,000', '450,000', '19,0'
        checkRowValues 1, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,990', '35,960', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '490,940' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '92,332']])
        assert '583,621' == subtotalGross
        assert '583,621' == total

        assert 4 == addNewPriceTableRow()
        openSelectorAndSelect 3, 'products', 'P-10001'
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,000', '450,000', '19,0'
        checkRowValues 1, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,990', '35,960', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        checkRowValues 3, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,990', '2,990', '7,0'
        assert '493,930' == subtotalNet
        checkTaxRates([['7,0', '0,558'], ['19,0', '92,332']])
        assert '586,820' == subtotalGross
        assert '586,820' == total

        assert !getPriceTableRow(0).findElement(By.className('up-btn')).displayed
        assert !getPriceTableRow(3).findElement(By.className('down-btn')).displayed

        setInputValue 'discountPercent', '2'
        getInput('discountAmount').click()
        assert '493,930' == subtotalNet
        checkTaxRates([['7,0', '0,558'], ['19,0', '92,332']])
        assert '586,820' == subtotalGross
        assert '11,736' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '575,084' == total
        setInputValue 'discountAmount', '5'
        getInput('adjustment').click()
        assert '493,930' == subtotalNet
        checkTaxRates([['7,0', '0,558'], ['19,0', '92,332']])
        assert '586,820' == subtotalGross
        assert '11,736' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '570,084' == total
        setInputValue 'adjustment', '-0,094'
        getInput('discountAmount').click()
        assert '493,930' == subtotalNet
        checkTaxRates([['7,0', '0,558'], ['19,0', '92,332']])
        assert '586,820' == subtotalGross
        assert '11,736' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '569,990' == total
        submitForm getUrl('/sales-order/show/')

        assert 'Verkaufsbestellung Big Sale Spring \'13 wurde geändert.' == flashMessage
        assert 'Big Sale Spring \'13' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'B-10000-10000' == getShowFieldText(col, 1)
        assert 'Big Sale Spring \'13' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/show/'))
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04.03.2013' == getShowFieldText(col, 1)
        assert '19.04.2013' == getShowFieldText(col, 2)
        assert '06.03.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
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
        assert 'vielen Dank für Ihren Auftrag zur Werbekampange "Frühjahr 2013".' == field.text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '450,000 €', '450,000 €', '19,0 %'
        checkStaticRowValues 1, 'P-10700', '4', 'Stück', 'Stempel\nMit Firmenaufdruck nach Kundenvorgabe.', '8,990 €', '35,960 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,490 €', '4,980 €', '7,0 %'
        checkStaticRowValues 3, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,990 €', '2,990 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Stempel' == field.findElement(By.className('item-name')).text
        assert 'Mit Firmenaufdruck nach Kundenvorgabe.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgabe' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        WebElement tr = tfoot.findElement(By.className('subtotal-net'))
        assert '493,930 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,558 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '92,332 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.className('subtotal-gross'))
        assert '586,820 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[5]'))
        assert '2,00 %' == tr.findElement(By.className('percentage')).text
        assert '11,736 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[6]'))
        assert '5,000 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[7]'))
        assert '-0,094 €' == tr.findElement(By.className('currency')).text
        assert '569,990 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Umsetzung des Auftrags erfolgt nach Pflichtenheft.' == field.text
        assert 'nach Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Erste Teilergebnisse sollten vor dem 15.03.2013 vorliegen.' == field.text
        assert '15.03.2013' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testEditSalesOrderErrors() {
        clickListActionButton 0, 0, getUrl('/sales-order/edit/')
        checkTitles 'Verkaufsbestellung bearbeiten', 'Verkaufsbestellungen', 'Werbekampagne Frühjahr 2013'

        clearInput 'subject'
        driver.findElement(By.id('organization')).clear()
        for (int i = 0; i < 2; i++) {
            removeRow 0
        }
        assert !getPriceTableRow(0).findElement(By.className('remove-btn')).displayed
        submitForm getUrl('/sales-order/update')

        assert checkErrorFields(['subject', 'organization.id'])
        cancelForm getUrl('/sales-order/list')

        driver.quit()

        assert 1 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testDeleteSalesOrderAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/sales-order/list'))
        assert 'Verkaufsbestellung wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == SalesOrder.count()
        checkQuote quote
    }

    @Test
    void testDeleteSalesOrderNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/sales-order/list') == driver.currentUrl
        driver.quit()

        assert 1 == SalesOrder.count()
        checkQuote quote
    }
}
