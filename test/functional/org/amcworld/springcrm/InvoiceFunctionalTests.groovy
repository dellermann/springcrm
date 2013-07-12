/*
 * InvoiceFunctionalTests.groovy
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
 * The class {@code InvoiceFunctionalTests} represents a functional test case
 * for the invoice section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
class InvoiceFunctionalTests extends InvoicingTransactionTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()

    Quote quote
    SalesOrder salesOrder


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        quote = prepareQuote(org, p)
        salesOrder = prepareSalesOrder(org, p, quote)
        if (!name.methodName.startsWith('testCreate')) {
            prepareInvoice org, p, quote, salesOrder
        }

        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open '/invoice/list'
    }

    @Test
    void testCreateInvoiceSuccess() {
        clickToolbarButton 0, getUrl('/invoice/create')
        checkTitles 'Rechnung anlegen', 'Rechnungen', 'Neue Rechnung'
        setInputValue 'subject', 'Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == selectAutocompleteEx('quote', 'Werbe')
        assert 'B-10000-10000 Werbekampagne Frühjahr 2013' == selectAutocompleteEx('salesOrder', 'Werbe')
        setInputValue 'stage.id', '902'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '1.4.2013'
        setInputValue 'dueDatePayment_date', '16.4.2013'
        setInputValue 'shippingDate_date', '2.4.2013'
        setInputValue 'carrier.id', '501'
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '0,00', 'still-unpaid-paid'
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
        setInputValue 'headerText', '''für die durchgeführte Werbekampange **"Frühjahr 2013"** erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.

Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem [Online-Verzeichnis](http://www.example.de/verzeichnis/).'''

        assert 1 == numPriceTableRows
        setPriceTableInputValue 0, 'number', 'S-10000'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption der geplanten Werbekampagne'
        setPriceTableInputValue 0, 'unitPrice', '440'
        setPriceTableInputValue 0, 'tax', '19'
        assert '440,00' == getPriceTableRowTotal(0)
        assert '440,00' == subtotalNet
        checkTaxRates([['19,0', '83,60']])
        assert '523,60' == subtotalGross
        assert '523,60' == total

        assert 2 == addNewPriceTableRow()
        openSelectorAndSelect 1, 'products', 'P-10000'
        checkRowValues 1, 'P-10000', '1,0', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '2,49', '7,0'
        assert '442,49' == subtotalNet
        checkTaxRates([['7,0', '0,17'], ['19,0', '83,60']])
        assert '526,26' == subtotalGross
        assert '526,26' == total
        setPriceTableInputValue 1, 'quantity', '2'
        checkRowValues 1, 'P-10000', null, 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '444,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '83,60']])
        assert '528,93' == subtotalGross
        assert '528,93' == total

        assert 3 == addNewPriceTableRow()
        openSelectorAndSelect 2, 'services', 'S-10100'
        checkRowValues 2, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total
        moveRowUp 2
        checkRowValues 1, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'

        assert 4 == addNewPriceTableRow()
        openSelectorAndSelect 3, 'products', 'P-10001'
        checkRowValues 3, 'P-10001', '1,0', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,99', '2,99', '7,0'
        assert '897,97' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '169,10']])
        assert '1.067,63' == subtotalGross
        assert '1.067,63' == total
        setPriceTableInputValue 3, 'unitPrice', '3'
        checkRowValues 3, 'P-10001', '1,0', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '3,00', '3,00', '7,0'
        assert '897,98' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '169,10']])
        assert '1.067,64' == subtotalGross
        assert '1.067,64' == total
        moveRowUp 3
        moveRowUp 2
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19'
        checkRowValues 1, 'P-10001', '1,0', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '3,00', '3,00', '7,0'
        checkRowValues 2, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        checkRowValues 3, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '897,98' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '169,10']])
        assert '1.067,64' == subtotalGross
        assert '1.067,64' == total
        moveRowDown 1
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19'
        checkRowValues 1, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10001', '1,0', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '3,00', '3,00', '7,0'
        checkRowValues 3, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '897,98' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '169,10']])
        assert '1.067,64' == subtotalGross
        assert '1.067,64' == total
        assert 3 == removeRow(2)
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19'
        checkRowValues 1, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total

        setInputValue 'footerText', 'Die Ausführung und Abrechnung erfolgte _laut Pflichtenheft_.'
        setInputValue 'termsAndConditions', ['700', '701']
        setInputValue 'notes', '**Wichtig!** Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!'
        checkStillUnpaid '0.0', '1.064,43', 'still-unpaid-unpaid'
        submitForm getUrl('/invoice/show/')

        assert 'Rechnung Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'R-10000-10000' == getShowFieldText(col, 1)
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
        link = getShowField(col, 6).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/show/'))
        assert 'B-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '01.04.2013' == getShowFieldText(col, 1)
        assert '16.04.2013' == getShowFieldText(col, 2)
        assert '02.04.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        WebElement balanceField = getShowField(col, 8)
        assert '-1.064,43 €' == balanceField.text.trim()
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
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.\nEinzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.text
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.' == field.findElement(By.xpath('.//p[1]')).text
        assert 'Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.findElement(By.xpath('.//p[2]')).text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text
        link = field.findElement(By.tagName('a'))
        assert 'Online-Verzeichnis' == link.text
        assert 'http://www.example.de/verzeichnis/' == link.getAttribute('href')

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,00 €', '440,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Mustervorschau' == field.findElement(By.className('item-name')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgaben' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '894,98 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,35 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '169,10 €' == tr.findElement(By.className('currency')).text
        assert '1.064,43 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Ausführung und Abrechnung erfolgte laut Pflichtenheft.' == field.text
        assert 'laut Pflichtenheft' == field.findElement(By.tagName('em')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Wichtig! Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == field.text
        assert 'Wichtig!' == field.findElement(By.tagName('strong')).text
        driver.quit()

        assert 1 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testCreateInvoiceErrors() {
        clickToolbarButton 0, getUrl('/invoice/create')
        checkTitles 'Rechnung anlegen', 'Rechnungen', 'Neue Rechnung'
        submitForm getUrl('/invoice/save')

        assert checkErrorFields([
            'subject', 'organization.id', 'dueDatePayment',
            'dueDatePayment_date'
        ])
        List<WebElement> errorMsgs = driver.findElements(By.xpath(
            '//form[@id="invoice-form"]/fieldset[3]/div[@class="fieldset-content"]/span[@class="error-msg"]'
        ))
        assert 2 == errorMsgs.size()
        assert 'Pos. 1, Artikel/Leistung: Feld darf nicht leer sein.' == errorMsgs[0].text
        assert 'Pos. 1, Nummer: Feld darf nicht leer sein.' == errorMsgs[1].text
        cancelForm getUrl('/invoice/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Rechnung anlegen' == link.text
        assert getUrl('/invoice/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testCreateInvoiceFromQuote() {
        open '/quote/list'
        clickListItem 0, 1
        clickActionBarButton 3, getUrl('/invoice/create?quote=')
        checkTitles 'Rechnung anlegen', 'Rechnungen', 'Neue Rechnung'

        def col = driver.findElement(By.xpath('//form[@id="invoice-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('R-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('quote')).getAttribute('value')
        assert '' == driver.findElement(By.id('salesOrder')).getAttribute('value')
        assert '900' == getInputValue('stage.id')
        checkDate 'docDate_date'
        assert '' == getInputValue('dueDatePayment_date')
        assert '' == getInputValue('shippingDate_date')
        assert 'null' == getInputValue('carrier.id')
        assert '' == getInputValue('paymentDate_date')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '1.064,43', 'still-unpaid-unpaid'
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
        assert '''für die geplante Werbekampange **"Frühjahr 2013"** möchten wir Ihnen gern folgendes Angebot unterbreiten.

Die Einzelheiten wurden im Meeting am 21.01.2013 festgelegt. Sie finden ein vollständiges Protokoll auf [unserer Webseite](http://www.example.de/protokoll/).''' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total

        assert 'Details zu den einzelnen Punkten finden Sie _im Pflichtenheft_.' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert 'Angebot unterliegt _möglicherweise_ weiteren Änderungen.' == getInputValue('notes')

        setInputValue 'stage.id', '902'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '1.4.2013'
        setInputValue 'dueDatePayment_date', '16.4.2013'
        setInputValue 'carrier.id', '501'
        setInputValue 'headerText', '''für die durchgeführte Werbekampange **"Frühjahr 2013"** erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.

Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem [Online-Verzeichnis](http://www.example.de/verzeichnis/).'''
        setInputValue 'footerText', 'Die Ausführung und Abrechnung erfolgte _laut Pflichtenheft_.'
        setInputValue 'notes', '**Wichtig!** Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!'
        checkStillUnpaid '0.0', '1.064,43', 'still-unpaid-unpaid'
        submitForm getUrl('/invoice/show/')

        assert 'Rechnung Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'R-10000-10000' == getShowFieldText(col, 1)
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
        assert '' == getShowFieldText(col, 6)
        assert 'versendet' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '01.04.2013' == getShowFieldText(col, 1)
        assert '16.04.2013' == getShowFieldText(col, 2)
        assert dateFormatted == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)
        assert '' == getShowFieldText(col, 6)
        assert '' == getShowFieldText(col, 7)
        WebElement balanceField = getShowField(col, 8)
        assert '-1.064,43 €' == balanceField.text.trim()
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
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.\nEinzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.text
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.' == field.findElement(By.xpath('.//p[1]')).text
        assert 'Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.findElement(By.xpath('.//p[2]')).text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text
        link = field.findElement(By.tagName('a'))
        assert 'Online-Verzeichnis' == link.text
        assert 'http://www.example.de/verzeichnis/' == link.getAttribute('href')

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,00 €', '440,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Mustervorschau' == field.findElement(By.className('item-name')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgaben' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '894,98 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,35 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '169,10 €' == tr.findElement(By.className('currency')).text
        assert '1.064,43 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Ausführung und Abrechnung erfolgte laut Pflichtenheft.' == field.text
        assert 'laut Pflichtenheft' == field.findElement(By.tagName('em')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Wichtig! Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == field.text
        assert 'Wichtig!' == field.findElement(By.tagName('strong')).text
        driver.quit()

        assert 1 == Invoice.count()
        checkQuote quote
    }

    @Test
    void testCreateInvoiceFromSalesOrder() {
        open '/sales-order/list'
        clickListItem 0, 1
        clickActionBarButton 2, getUrl('/invoice/create?salesOrder=')
        checkTitles 'Rechnung anlegen', 'Rechnungen', 'Neue Rechnung'

        def col = driver.findElement(By.xpath('//form[@id="invoice-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('R-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('quote')).getAttribute('value')
        assert 'B-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('salesOrder')).getAttribute('value')
        assert '900' == getInputValue('stage.id')
        checkDate 'docDate_date'
        assert '' == getInputValue('dueDatePayment_date')
        assert '' == getInputValue('shippingDate_date')
        assert 'null' == getInputValue('carrier.id')
        assert '' == getInputValue('paymentDate_date')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '1.064,43', 'still-unpaid-unpaid'
        assert '1.064,43' == stillUnpaid.findElement(By.tagName('output')).text
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
        assert 'vielen Dank für Ihren Auftrag zur Werbekampange **"Frühjahr 2013"**.' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total

        assert 'Die Umsetzung des Auftrags erfolgt **nach Pflichtenheft**.' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert 'Erste Teilergebnisse sollten vor dem *15.03.2013* vorliegen.' == getInputValue('notes')

        setInputValue 'stage.id', '902'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '1.4.2013'
        setInputValue 'dueDatePayment_date', '16.4.2013'
        setInputValue 'carrier.id', '501'
        setInputValue 'headerText', '''für die durchgeführte Werbekampange **"Frühjahr 2013"** erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.

Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem [Online-Verzeichnis](http://www.example.de/verzeichnis/).'''
        setInputValue 'footerText', 'Die Ausführung und Abrechnung erfolgte _laut Pflichtenheft_.'
        setInputValue 'notes', '**Wichtig!** Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!'
        checkStillUnpaid '0.0', '1.064,43', 'still-unpaid-unpaid'
        submitForm getUrl('/invoice/show/')

        assert 'Rechnung Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'R-10000-10000' == getShowFieldText(col, 1)
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
        link = getShowField(col, 6).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/show/'))
        assert 'B-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '01.04.2013' == getShowFieldText(col, 1)
        assert '16.04.2013' == getShowFieldText(col, 2)
        assert dateFormatted == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)
        assert '' == getShowFieldText(col, 6)
        assert '' == getShowFieldText(col, 7)
        WebElement balanceField = getShowField(col, 8)
        assert '-1.064,43 €' == balanceField.text.trim()
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
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.\nEinzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.text
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.' == field.findElement(By.xpath('.//p[1]')).text
        assert 'Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.findElement(By.xpath('.//p[2]')).text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text
        link = field.findElement(By.tagName('a'))
        assert 'Online-Verzeichnis' == link.text
        assert 'http://www.example.de/verzeichnis/' == link.getAttribute('href')

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,00 €', '440,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Mustervorschau' == field.findElement(By.className('item-name')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgaben' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '894,98 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,35 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '169,10 €' == tr.findElement(By.className('currency')).text
        assert '1.064,43 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Ausführung und Abrechnung erfolgte laut Pflichtenheft.' == field.text
        assert 'laut Pflichtenheft' == field.findElement(By.tagName('em')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Wichtig! Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == field.text
        assert 'Wichtig!' == field.findElement(By.tagName('strong')).text
        driver.quit()

        assert 1 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testShowInvoice() {
        int id = clickListItem 0, 1, '/invoice/show'
        checkTitles 'Rechnung anzeigen', 'Rechnungen', 'Werbekampagne Frühjahr 2013'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'R-10000-10000' == getShowFieldText(col, 1)
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
        link = getShowField(col, 6).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/show/'))
        assert 'B-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'versendet' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '01.04.2013' == getShowFieldText(col, 1)
        assert '16.04.2013' == getShowFieldText(col, 2)
        assert '02.04.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)
        assert '' == getShowFieldText(col, 6)
        assert '' == getShowFieldText(col, 7)
        WebElement balanceField = getShowField(col, 8)
        assert '-1.064,43 €' == balanceField.text.trim()
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
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.\nEinzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.text
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.' == field.findElement(By.xpath('.//p[1]')).text
        assert 'Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.findElement(By.xpath('.//p[2]')).text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text
        link = field.findElement(By.tagName('a'))
        assert 'Online-Verzeichnis' == link.text
        assert 'http://www.example.de/verzeichnis/' == link.getAttribute('href')

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,00 €', '440,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Mustervorschau' == field.findElement(By.className('item-name')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgaben' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '894,98 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,35 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '169,10 €' == tr.findElement(By.className('currency')).text
        assert '1.064,43 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Ausführung und Abrechnung erfolgte laut Pflichtenheft.' == field.text
        assert 'laut Pflichtenheft' == field.findElement(By.tagName('em')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Wichtig! Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == field.text
        assert 'Wichtig!' == field.findElement(By.tagName('strong')).text

        String param = "invoice=${id}"
        fieldSet = getFieldset(dataSheet, 6)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/dunning/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Mahnungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/dunning/create?${param}"))
        assert 'Mahnung anlegen' == link.text
        assert waitForEmptyRemoteList(6)

        fieldSet = getFieldset(dataSheet, 7)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/credit-memo/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Gutschriften' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/create?${param}"))
        assert 'Gutschrift anlegen' == link.text
        assert waitForEmptyRemoteList(7)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
        link = toolbar.findElement(By.xpath('li[1]/a'))
        assert 'white' == link.getAttribute('class')
        assert getUrl('/invoice/list') == link.getAttribute('href')
        assert 'Liste' == link.text
        link = toolbar.findElement(By.xpath('li[2]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl('/invoice/create') == link.getAttribute('href')
        assert 'Anlegen' == link.text
        link = toolbar.findElement(By.xpath('li[3]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl("/invoice/edit/${id}") == link.getAttribute('href')
        assert 'Bearbeiten' == link.text
        link = toolbar.findElement(By.xpath('li[4]/a'))
        assert 'blue' == link.getAttribute('class')
        assert getUrl("/invoice/copy/${id}") == link.getAttribute('href')
        assert 'Kopieren' == link.text
        link = toolbar.findElement(By.xpath('li[5]/a'))
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert getUrl("/invoice/delete/${id}") == link.getAttribute('href')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl("/invoice/show/${id}") == driver.currentUrl

        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
        link = actions.findElement(By.xpath('li[1]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('menu-button')
        assert link.getAttribute('href').startsWith(getUrl("/invoice/print/${id}"))
        assert 'Drucken' == link.text
        link = actions.findElement(By.xpath('li[2]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('menu-button')
        assert link.getAttribute('href').startsWith(getUrl("/invoice/print/${id}?duplicate=1"))
        assert 'Kopie drucken' == link.text
        link = actions.findElement(By.xpath('li[3]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/dunning/create?invoice=${id}"))
        assert 'Mahnung erzeugen' == link.text
        link = actions.findElement(By.xpath('li[4]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/create?invoice=${id}"))
        assert 'Gutschrift erzeugen' == link.text
        driver.quit()

        assert 1 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testListInvoices() {
        checkTitles 'Rechnungen', 'Rechnungen'
        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        def link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/show/'))
        assert 'R-10000-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/show/'))
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
        assert '01.04.2013' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('date')
        assert '16.04.2013' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('currency')
        assert '1.064,43 €' == td.text
        td = tr.findElement(By.xpath('td[9]'))
        assert td.getAttribute('class').contains('currency')
        assert td.getAttribute('class').contains('balance-state-red')
        assert '-1.064,43 €' == td.text
        td = tr.findElement(By.xpath('td[10]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/invoice/list') == driver.currentUrl
        driver.quit()

        assert 1 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testEditInvoiceSuccess() {
        clickListActionButton 0, 0, getUrl('/invoice/edit/')
        checkTitles 'Rechnung bearbeiten', 'Rechnungen', 'Werbekampagne Frühjahr 2013'
        def col = driver.findElement(By.xpath('//form[@id="invoice-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('R-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('quote')).getAttribute('value')
        assert 'B-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('salesOrder')).getAttribute('value')
        assert '902' == getInputValue('stage.id')
        assert '01.04.2013' == getInputValue('docDate_date')
        assert '16.04.2013' == getInputValue('dueDatePayment_date')
        assert '02.04.2013' == getInputValue('shippingDate_date')
        assert '501' == getInputValue('carrier.id')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '1.064,43', 'still-unpaid-unpaid'
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
        assert '''für die durchgeführte Werbekampange **"Frühjahr 2013"** erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.

Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem [Online-Verzeichnis](http://www.example.de/verzeichnis/).''' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total

        assert 'Die Ausführung und Abrechnung erfolgte _laut Pflichtenheft_.' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert '**Wichtig!** Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == getInputValue('notes')

        setInputValue 'subject', 'Werbekampagne Spring \'13'
        setInputValue 'stage.id', '903'
        checkDate 'paymentDate_date'
        setInputValue 'paymentDate_date', '15.4.2013'
        stillUnpaid.click()
        assert '1.064,43' == getInputValue('paymentAmount')
        checkStillUnpaid '0.0', '0,00', 'still-unpaid-paid'
        setInputValue 'paymentMethod.id', '2401'

        setPriceTableInputValue 0, 'unitPrice', '450'
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        assert '904,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '171,00']])
        assert '1.076,33' == subtotalGross
        assert '1.076,33' == total

        assert 2 == removeRow(1)
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        checkRowValues 1, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '454,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '85,50']])
        assert '540,83' == subtotalGross
        assert '540,83' == total

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

        checkRowValues 2, 'P-10700', '1,0', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,99', '8,99', '19,0'
        setPriceTableInputValue 2, 'quantity', '4'
        checkRowValues 2, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,99', '35,96', '19,0'
        assert '490,94' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '92,33']])
        assert '583,62' == subtotalGross
        assert '583,62' == total
        openSelectorAndAbort 2, 'products'
        checkRowValues 2, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,99', '35,96', '19,0'
        moveRowUp 2
        moveRowUp 1
        checkRowValues 0, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,99', '35,96', '19,0'
        checkRowValues 1, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '490,94' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '92,33']])
        assert '583,62' == subtotalGross
        assert '583,62' == total
        moveRowDown 0
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        checkRowValues 1, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,99', '35,96', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '490,94' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '92,33']])
        assert '583,62' == subtotalGross
        assert '583,62' == total

        assert 4 == addNewPriceTableRow()
        openSelectorAndSelect 3, 'products', 'P-10001'
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        checkRowValues 1, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck _nach Kundenvorgabe_.', '8,99', '35,96', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        checkRowValues 3, 'P-10001', '1,0', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,99', '2,99', '7,0'
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '586,82' == total

        assert !getPriceTableRow(0).findElement(By.className('up-btn')).displayed
        assert !getPriceTableRow(3).findElement(By.className('down-btn')).displayed

        setInputValue 'discountPercent', '2'
        getInput('discountAmount').click()
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '11,74' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '575,08' == total
        setInputValue 'discountAmount', '5'
        getInput('adjustment').click()
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '11,74' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '570,08' == total
        setInputValue 'adjustment', '-0,09'
        getInput('discountAmount').click()
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '11,74' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '569,99' == total
        checkStillUnpaid '0.0', '-494,44', 'still-unpaid-too-much'
        submitForm getUrl('/invoice/show/')

        assert 'Rechnung Werbekampagne Spring \'13 wurde geändert.' == flashMessage
        assert 'Werbekampagne Spring \'13' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'R-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Spring \'13' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        link = getShowField(col, 5).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/show/'))
        assert 'A-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        link = getShowField(col, 6).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/show/'))
        assert 'B-10000-10000 Werbekampagne Frühjahr 2013' == link.text
        assert 'bezahlt' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '01.04.2013' == getShowFieldText(col, 1)
        assert '16.04.2013' == getShowFieldText(col, 2)
        assert '02.04.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
        assert '15.04.2013' == getShowFieldText(col, 5)
        assert '1.064,43 €' == getShowFieldText(col, 6)
        assert 'Überweisung' == getShowFieldText(col, 7)
        WebElement balanceField = getShowField(col, 8)
        assert '494,44 €' == balanceField.text.trim()
        assert 'still-unpaid-too-much' == balanceField.findElement(By.tagName('span')).getAttribute('class')
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
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.\nEinzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.text
        assert 'für die durchgeführte Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.' == field.findElement(By.xpath('.//p[1]')).text
        assert 'Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem Online-Verzeichnis.' == field.findElement(By.xpath('.//p[2]')).text
        assert '"Frühjahr 2013"' == field.findElement(By.tagName('strong')).text
        link = field.findElement(By.tagName('a'))
        assert 'Online-Verzeichnis' == link.text
        assert 'http://www.example.de/verzeichnis/' == link.getAttribute('href')

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 1, 'P-10700', '4', 'Stück', 'Stempel\nMit Firmenaufdruck nach Kundenvorgabe.', '8,99 €', '35,96 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'
        checkStaticRowValues 3, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,99 €', '2,99 €', '7,0 %'
        field = getPriceTableRow(1).findElement(By.xpath('./td[5]'))
        assert 'Stempel' == field.findElement(By.className('item-name')).text
        assert 'Mit Firmenaufdruck nach Kundenvorgabe.' == field.findElement(By.className('item-description')).text
        assert 'nach Kundenvorgabe' == field.findElement(By.xpath('./div[@class="item-description"]//em')).text

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        WebElement tr = tfoot.findElement(By.className('subtotal-net'))
        assert '493,93 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,56 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '92,33 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.className('subtotal-gross'))
        assert '586,82 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[5]'))
        assert '2,00 %' == tr.findElement(By.className('percentage')).text
        assert '11,74 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[6]'))
        assert '5,00 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[7]'))
        assert '-0,09 €' == tr.findElement(By.className('currency')).text
        assert '569,99 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 4)
        field = getShowField(fieldSet, 1)
        assert 'Die Ausführung und Abrechnung erfolgte laut Pflichtenheft.' == field.text
        assert 'laut Pflichtenheft' == field.findElement(By.tagName('em')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Wichtig! Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == field.text
        assert 'Wichtig!' == field.findElement(By.tagName('strong')).text
        driver.quit()

        assert 1 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testEditInvoiceErrors() {
        clickListActionButton 0, 0, getUrl('/invoice/edit/')
        checkTitles 'Rechnung bearbeiten', 'Rechnungen', 'Werbekampagne Frühjahr 2013'

        clearInput 'subject'
        driver.findElement(By.id('organization')).clear()
        clearInput 'dueDatePayment_date'
        for (int i = 0; i < 2; i++) {
            removeRow 0
        }
        assert !getPriceTableRow(0).findElement(By.className('remove-btn')).displayed
        submitForm getUrl('/invoice/update')

        assert checkErrorFields([
            'subject', 'organization.id', 'dueDatePayment',
            'dueDatePayment_date'
        ])
        cancelForm getUrl('/invoice/list')

        driver.quit()

        assert 1 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testDeleteInvoiceAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/invoice/list'))
        assert 'Rechnung wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Invoice.count()
        checkSalesOrder salesOrder
    }

    @Test
    void testDeleteInvoiceNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/invoice/list') == driver.currentUrl
        driver.quit()

        assert 1 == Invoice.count()
        checkSalesOrder salesOrder
    }
}
