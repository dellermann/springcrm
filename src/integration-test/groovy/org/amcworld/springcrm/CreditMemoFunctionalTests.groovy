/*
 * CreditMemoFunctionalTests.groovy
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
 * The class {@code CreditMemoFunctionalTests} represents a functional test
 * case for the quotes section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
@RunWith(JUnit4)
class CreditMemoFunctionalTests extends InvoicingTransactionTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()

    Invoice invoice


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        def quote = prepareQuote(org, p)
        def salesOrder = prepareSalesOrder(org, p, quote)
        invoice = prepareInvoice(org, p, quote, salesOrder)
        if (!name.methodName.startsWith('testCreate')) {
            prepareCreditMemo org, p, invoice
        }

        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open '/credit-memo/list'
    }

    @Test
    void testCreateCreditMemoSuccess() {
        clickToolbarButton 0, getUrl('/credit-memo/create')
        checkTitles 'Gutschrift anlegen', 'Gutschriften', 'Neue Gutschrift'
        setInputValue 'subject', 'Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == selectAutocompleteEx('invoice', 'Werbe')
        setInputValue 'stage.id', '2502'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '8.4.2013'
        setInputValue 'shippingDate_date', '9.4.2013'
        setInputValue 'carrier.id', '501'

        /*
         * The modified closing balance is changed by JavaScript to 1064,43 €
         * after setting the associated invoice.  But JavaScript set the
         * new modified closing balance via data() function which does not
         * change the value of attribute "data-modified-closing-balance".  So
         * we must expect "0.0" instead of "1064.43" here.
         */
        checkStillUnpaid '0.0', '-1.064,43', 'still-unpaid-too-much'
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
        setInputValue 'headerText', 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne **"Frühjahr 2013"** gut.'

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

        setInputValue 'footerText', 'Erläuterungen zu den einzelnen Posten finden Sie **im Pflichtenheft**.'
        setInputValue 'termsAndConditions', ['700', '701']
        setInputValue 'notes', 'Gutschrift für _nicht_ lieferbare Artikel.'

        /*
         * The modified closing balance is changed by JavaScript to 1064,43 €
         * after setting the associated invoice.  But JavaScript set the
         * new modified closing balance via data() function which does not
         * change the value of attribute "data-modified-closing-balance".  So
         * we must expect "0.0" instead of "1064.43" here.
         */
        checkStillUnpaid '0.0', '0,00', 'still-unpaid-paid'
        submitForm getUrl('/credit-memo/show/')

        assert 'Gutschrift Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'G-10000-10000' == getShowFieldText(col, 1)
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
        assert '' == getShowFieldText(col, 6)
        assert 'versendet' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '08.04.2013' == getShowFieldText(col, 1)
        assert '09.04.2013' == getShowFieldText(col, 2)
        assert 'elektronisch' == getShowFieldText(col, 3)
        WebElement balanceField = getShowField(col, 7)
        assert '0,00 €' == balanceField.text.trim()
        assert 'still-unpaid-paid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
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
        assert 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne "Frühjahr 2013" gut.' == field.text
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
        assert 'Erläuterungen zu den einzelnen Posten finden Sie im Pflichtenheft.' == field.text
        assert 'im Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Gutschrift für nicht lieferbare Artikel.' == field.text
        assert 'nicht' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testCreateCreditMemoErrors() {
        clickToolbarButton 0, getUrl('/credit-memo/create')
        checkTitles 'Gutschrift anlegen', 'Gutschriften', 'Neue Gutschrift'
        submitForm getUrl('/credit-memo/save')

        assert checkErrorFields(['subject', 'organization.id'])
        List<WebElement> errorMsgs = driver.findElements(By.xpath(
            '//form[@id="credit-memo-form"]/fieldset[3]/div/ul[@class="field-msgs"]/li[@class="error-msg"]'
        ))
        assert 3 == errorMsgs.size()
        assert 'Pos. 1, Artikel/Leistung: Feld darf nicht leer sein.' == errorMsgs[0].text
        assert 'Pos. 1, Nummer: Feld darf nicht leer sein.' == errorMsgs[1].text
        assert 'Pos. 1, Einheit: Feld darf nicht leer sein.' == errorMsgs[2].text
        cancelForm getUrl('/credit-memo/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Gutschrift anlegen' == link.text
        assert getUrl('/credit-memo/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testCreateCreditMemoFromInvoice() {
        open '/invoice/list'
        clickListItem 0, 1
        clickActionBarButton 3, getUrl('/credit-memo/create?invoice=')
        checkTitles 'Gutschrift anlegen', 'Gutschriften', 'Neue Gutschrift'

        def col = driver.findElement(By.xpath('//form[@id="credit-memo-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('G-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('invoice')).getAttribute('value')
        assert '' == driver.findElement(By.id('dunning')).getAttribute('value')
        assert '2500' == getInputValue('stage.id')
        checkDate 'docDate_date'
        assert '' == getInputValue('shippingDate_date')
        assert 'null' == getInputValue('carrier.id')
        assert '' == getInputValue('paymentDate_date')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '1064.43', '0,00', 'still-unpaid-paid'
        assert 'null' == getInputValue('paymentMethod.id')
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
        assert '' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,000', '440,000', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '894,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '169,100']])
        assert '1.064,429' == subtotalGross
        assert '1.064,429' == total

        assert '' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert '**Wichtig!** Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!' == getInputValue('notes')

        setInputValue 'stage.id', '2502'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '8.4.2013'
        setInputValue 'carrier.id', '501'
        setInputValue 'headerText', 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne **"Frühjahr 2013"** gut.'
        setInputValue 'footerText', 'Erläuterungen zu den einzelnen Posten finden Sie **im Pflichtenheft**.'
        setInputValue 'notes', 'Gutschrift für _nicht_ lieferbare Artikel.'
        checkStillUnpaid '1064.43', '0,00', 'still-unpaid-paid'
        submitForm getUrl('/credit-memo/show/')

        assert 'Gutschrift Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'G-10000-10000' == getShowFieldText(col, 1)
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
        assert '' == getShowFieldText(col, 6)
        assert 'versendet' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '08.04.2013' == getShowFieldText(col, 1)
        assert dateFormatted == getShowFieldText(col, 2)
        assert 'elektronisch' == getShowFieldText(col, 3)
        WebElement balanceField = getShowField(col, 7)
        assert '0,00 €' == balanceField.text.trim()
        assert 'still-unpaid-paid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
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
        assert 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne "Frühjahr 2013" gut.' == field.text
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
        assert 'Erläuterungen zu den einzelnen Posten finden Sie im Pflichtenheft.' == field.text
        assert 'im Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Gutschrift für nicht lieferbare Artikel.' == field.text
        assert 'nicht' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testShowCreditMemo() {
        int id = clickListItem 0, 1, '/credit-memo/show'
        checkTitles 'Gutschrift G-10000-10000 anzeigen', 'Gutschriften', 'Werbekampagne Frühjahr 2013'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'G-10000-10000' == getShowFieldText(col, 1)
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
        assert '' == getShowFieldText(col, 6)
        assert 'versendet' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '08.04.2013' == getShowFieldText(col, 1)
        assert '09.04.2013' == getShowFieldText(col, 2)
        assert 'elektronisch' == getShowFieldText(col, 3)
        assert '' == getShowFieldText(col, 4)
        assert '0,00 €' == getShowFieldText(col, 5)
        assert '' == getShowFieldText(col, 6)
        WebElement balanceField = getShowField(col, 7)
        assert '0,00 €' == balanceField.text.trim()
        assert 'still-unpaid-paid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
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
        assert 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne "Frühjahr 2013" gut.' == field.text
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
        assert 'Erläuterungen zu den einzelnen Posten finden Sie im Pflichtenheft.' == field.text
        assert 'im Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Gutschrift für nicht lieferbare Artikel.' == field.text
        assert 'nicht' == field.findElement(By.tagName('em')).text

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'credit-memo', id

        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
        link = actions.findElement(By.xpath('li[1]/div[@class="button-group"]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/print/${id}"))
        assert 'Drucken' == link.text
        link = actions.findElement(By.xpath('li[2]/div[@class="button-group"]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/print/${id}?duplicate=1"))
        assert 'Kopie drucken' == link.text
        driver.quit()

        assert 1 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testListCreditMemos() {
        checkTitles 'Gutschriften', 'Gutschriften'
        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        def link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/credit-memo/show/'))
        assert 'G-10000-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/credit-memo/show/'))
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
        assert '08.04.2013' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('date')
        assert '' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('currency')
        assert '1.064,43 €' == td.text
        td = tr.findElement(By.xpath('td[9]'))
        assert td.getAttribute('class').contains('currency')
        assert td.getAttribute('class').contains('balance-state-default')
        assert '0,00 €' == td.text
        td = tr.findElement(By.xpath('td[10]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/credit-memo/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/credit-memo/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/credit-memo/list') == driver.currentUrl
        driver.quit()

        assert 1 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testEditCreditMemoSuccess() {
        clickListActionButton 0, 0, getUrl('/credit-memo/edit/')
        checkTitles 'Gutschrift G-10000-10000 bearbeiten', 'Gutschriften', 'Werbekampagne Frühjahr 2013'
        def col = driver.findElement(By.xpath('//form[@id="credit-memo-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('G-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'R-10000-10000 Werbekampagne Frühjahr 2013' == driver.findElement(By.id('invoice')).getAttribute('value')
        assert '' == driver.findElement(By.id('dunning')).getAttribute('value')
        assert '2502' == getInputValue('stage.id')
        assert '08.04.2013' == getInputValue('docDate_date')
        assert '09.04.2013' == getInputValue('shippingDate_date')
        assert '501' == getInputValue('carrier.id')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '1064.43', '0,00', 'still-unpaid-paid'
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
        assert 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne **"Frühjahr 2013"** gut.' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,000', '440,000', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters _nach Kundenvorgaben_.', '450,000', '450,000', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,490', '4,980', '7,0'
        assert '894,980' == subtotalNet
        checkTaxRates([['7,0', '0,349'], ['19,0', '169,100']])
        assert '1.064,429' == subtotalGross
        assert '1.064,429' == total

        assert 'Erläuterungen zu den einzelnen Posten finden Sie **im Pflichtenheft**.' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert 'Gutschrift für _nicht_ lieferbare Artikel.' == getInputValue('notes')

        setInputValue 'subject', 'Werbekampagne Spring \'13'
        setInputValue 'stage.id', '2503'
        checkDate 'paymentDate_date'
        setInputValue 'paymentDate_date', '10.4.2013'
        stillUnpaid.click()
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '1064.43', '0,00', 'still-unpaid-paid'
        setInputValue 'paymentMethod.id', '2401'

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
        checkStillUnpaid '1064.43', '-494,44', 'still-unpaid-too-much'
        submitForm getUrl('/credit-memo/show/')

        assert 'Gutschrift Werbekampagne Spring \'13 wurde geändert.' == flashMessage
        assert 'Werbekampagne Spring \'13' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'G-10000-10000' == getShowFieldText(col, 1)
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
        assert '' == getShowFieldText(col, 6)
        assert 'bezahlt' == getShowFieldText(col, 7)
        col = fieldSet.findElement(By.className('col-r'))
        assert '08.04.2013' == getShowFieldText(col, 1)
        assert '09.04.2013' == getShowFieldText(col, 2)
        assert 'elektronisch' == getShowFieldText(col, 3)
        assert '10.04.2013' == getShowFieldText(col, 4)
        assert '0,00 €' == getShowFieldText(col, 5)
        assert 'Überweisung' == getShowFieldText(col, 6)
        WebElement balanceField = getShowField(col, 7)
        assert '-494,44 €' == balanceField.text.trim()
        assert 'still-unpaid-unpaid' == balanceField.findElement(By.tagName('span')).getAttribute('class')
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
        assert 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne "Frühjahr 2013" gut.' == field.text
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
        assert 'Erläuterungen zu den einzelnen Posten finden Sie im Pflichtenheft.' == field.text
        assert 'im Pflichtenheft' == field.findElement(By.tagName('strong')).text
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        field = getShowField(fieldSet, 1)
        assert 'Gutschrift für nicht lieferbare Artikel.' == field.text
        assert 'nicht' == field.findElement(By.tagName('em')).text
        driver.quit()

        assert 1 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testEditCreditMemoErrors() {
        clickListActionButton 0, 0, getUrl('/credit-memo/edit/')
        checkTitles 'Gutschrift G-10000-10000 bearbeiten', 'Gutschriften', 'Werbekampagne Frühjahr 2013'

        clearInput 'subject'
        driver.findElement(By.id('organization')).clear()
        for (int i = 0; i < 2; i++) {
            removeRow 0
        }
        assert !getPriceTableRow(0).findElement(By.className('remove-btn')).displayed
        submitForm getUrl('/credit-memo/update')

        assert checkErrorFields(['subject', 'organization.id'])
        cancelForm getUrl('/credit-memo/list')
        driver.quit()

        assert 1 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testDeleteCreditMemoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/credit-memo/list'))
        assert 'Gutschrift wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == CreditMemo.count()
        checkInvoice invoice
    }

    @Test
    void testDeleteCreditMemoNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/credit-memo/list') == driver.currentUrl
        driver.quit()

        assert 1 == CreditMemo.count()
        checkInvoice invoice
    }
}
