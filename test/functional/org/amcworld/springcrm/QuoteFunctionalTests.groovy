/*
 * QuoteFunctionalTests.groovy
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

import java.text.DateFormat;
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait


/**
 * The class {@code QuoteFunctionalTests} represents a functional test case for
 * the quotes section of SpringCRM.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class QuoteFunctionalTests extends InvoicingTransactionTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        if (!name.methodName.startsWith('testCreate')) {
            prepareQuote(org, p)
        }

        open('/', 'de')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open('/quote/list')
    }

    @After
    void deleteFixture() {
        Quote.executeUpdate 'delete Quote q'
    }

    @Test
    void testCreateQuoteSuccess() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/quote/create') == driver.currentUrl
        assert 'Angebot anlegen' == driver.title
        assert 'Angebote' == driver.findElement(BY_HEADER).text
        assert 'Neues Angebot' == driver.findElement(BY_SUBHEADER).text
        setInputValue('subject', 'Werbekampagne Frühjahr 2013')
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        new Select(getInput('stage.id')).selectByValue('602')
        DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) == getInputValue('shippingDate_date')
        setInputValue('docDate_date', '20.02.2013')
        setInputValue('validUntil_date', '20.03.2013')
        setInputValue('shippingDate_date', '21.02.2013')
        new Select(getInput('carrier.id')).selectByValue('501')
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
        setInputValue('headerText', '''für die geplante Werbekampange "Frühjahr 2013" möchten wir Ihnen gern folgendes Angebot unterbreiten.
Die Einzelheiten wurden im Meeting am 21.01.2013 festgelegt.''')

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
        checkRowValues 2, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters nach Kundenvorgaben.', '450,00', '450,00', '19,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total
        moveRowUp 2
        checkRowValues 1, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters nach Kundenvorgaben.', '450,00', '450,00', '19,0'
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
        checkRowValues 2, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters nach Kundenvorgaben.', '450,00', '450,00', '19,0'
        checkRowValues 3, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '897,98' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '169,10']])
        assert '1.067,64' == subtotalGross
        assert '1.067,64' == total
        moveRowDown 1
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19'
        checkRowValues 1, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters nach Kundenvorgaben.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10001', '1,0', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '3,00', '3,00', '7,0'
        checkRowValues 3, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '897,98' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '169,10']])
        assert '1.067,64' == subtotalGross
        assert '1.067,64' == total
        assert 3 == removeRow(2)
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19'
        checkRowValues 1, 'S-10100', '1,0', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters nach Kundenvorgaben.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total

        setInputValue('footerText', 'Details zu den einzelnen Punkten finden Sie im Pflichtenheft.')
        def select = new Select(getInput('termsAndConditions'))
        select.selectByValue('700')
        select.selectByValue('701')
        setInputValue('notes', 'Angebot unterliegt möglicherweise weiteren Änderungen.')

        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/quote/show/'))
        assert 'Angebot Werbekampagne Frühjahr 2013 wurde angelegt.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'A-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        assert 'versendet' == getShowFieldText(col, 5)
        col = fieldSet.findElement(By.className('col-r'))
        assert '20.02.2013' == getShowFieldText(col, 1)
        assert '20.03.2013' == getShowFieldText(col, 2)
        assert '21.02.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
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
        assert 'für die geplante Werbekampange "Frühjahr 2013" möchten wir Ihnen gern folgendes Angebot unterbreiten.\nDie Einzelheiten wurden im Meeting am 21.01.2013 festgelegt.' == field.text
        assert field.findElement(By.tagName('br'))

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,00 €', '440,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'

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
        assert 'Details zu den einzelnen Punkten finden Sie im Pflichtenheft.' == getShowFieldText(fieldSet, 1)
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        assert 'Angebot unterliegt möglicherweise weiteren Änderungen.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Quote.count()
    }

    @Test
    void testCreateQuoteErrors() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/quote/create') == driver.currentUrl
        assert 'Angebot anlegen' == driver.title
        assert 'Angebote' == driver.findElement(BY_HEADER).text
        assert 'Neues Angebot' == driver.findElement(BY_SUBHEADER).text
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert driver.currentUrl.startsWith(getUrl('/quote/save'))
        assert checkErrorFields(['subject', 'organization.id'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert getUrl('/quote/list') == driver.currentUrl
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Angebot anlegen' == link.text
        assert getUrl('/quote/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Quote.count()
    }

    @Test
    void testShowQuote() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[2]/a')).click()
        def m = (driver.currentUrl =~ '/quote/show/(\\d+)')
        assert !!m
        int id = m[0][1] as Integer
        assert 'Angebot anzeigen' == driver.title
        assert 'Angebote' == driver.findElement(BY_HEADER).text
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'A-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        assert 'versendet' == getShowFieldText(col, 5)
        col = fieldSet.findElement(By.className('col-r'))
        assert '20.02.2013' == getShowFieldText(col, 1)
        assert '20.03.2013' == getShowFieldText(col, 2)
        assert '21.02.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
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
        assert 'für die geplante Werbekampange "Frühjahr 2013" möchten wir Ihnen gern folgendes Angebot unterbreiten.\nDie Einzelheiten wurden im Meeting am 21.01.2013 festgelegt.' == field.text
        assert field.findElement(By.tagName('br'))

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '440,00 €', '440,00 €', '19,0 %'
        checkStaticRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau\nAnfertigung eines Musters nach Kundenvorgaben.', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'

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
        assert 'Details zu den einzelnen Punkten finden Sie im Pflichtenheft.' == getShowFieldText(fieldSet, 1)
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        assert 'Angebot unterliegt möglicherweise weiteren Änderungen.' == getShowFieldText(fieldSet, 1)

        String param = "quote=${id}"
        fieldSet = getFieldset(dataSheet, 6)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/sales-order/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Verkaufsbestellungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/sales-order/create?quote=${id}"))
        assert 'Verkaufsbestellung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 7)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/invoice/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Rechnungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?quote=${id}"))
        assert 'Rechnung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
        link = toolbar.findElement(By.xpath('li[1]/a'))
        assert 'white' == link.getAttribute('class')
        assert getUrl('/quote/list') == link.getAttribute('href')
        assert 'Liste' == link.text
        link = toolbar.findElement(By.xpath('li[2]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl('/quote/create') == link.getAttribute('href')
        assert 'Anlegen' == link.text
        link = toolbar.findElement(By.xpath('li[3]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl("/quote/edit/${id}") == link.getAttribute('href')
        assert 'Bearbeiten' == link.text
        link = toolbar.findElement(By.xpath('li[4]/a'))
        assert 'blue' == link.getAttribute('class')
        assert getUrl("/quote/copy/${id}") == link.getAttribute('href')
        assert 'Kopieren' == link.text
        link = toolbar.findElement(By.xpath('li[5]/a'))
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert getUrl("/quote/delete/${id}") == link.getAttribute('href')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl("/quote/show/${id}") == driver.currentUrl
        driver.quit()

        assert 1 == Quote.count()
    }

    @Test
    void testListQuotes() {
        assert 'Angebote' == driver.title
        assert 'Angebote' == driver.findElement(BY_HEADER).text
        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        def link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/show/'))
        assert 'A-10000-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/show/'))
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
        assert '20.02.2013' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('date')
        assert '21.02.2013' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('currency')
        assert '1.064,43 €' == td.text
        td = tr.findElement(By.xpath('td[9]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/quote/list') == driver.currentUrl
        driver.quit()

        assert 1 == Quote.count()
    }

    @Test
    void testEditQuoteSuccess() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert driver.currentUrl.startsWith(getUrl('/quote/edit/'))
        assert 'Angebot bearbeiten' == driver.title
        assert 'Angebote' == driver.findElement(BY_HEADER).text
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text
        def col = driver.findElement(By.xpath('//form[@id="quote-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('A-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Werbekampagne Frühjahr 2013' == getInputValue('subject')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert '602' == getInputValue('stage.id')
        assert '20.02.2013' == getInputValue('docDate_date')
        assert '20.03.2013' == getInputValue('validUntil_date')
        assert '21.02.2013' == getInputValue('shippingDate_date')
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
        assert '''für die geplante Werbekampange "Frühjahr 2013" möchten wir Ihnen gern folgendes Angebot unterbreiten.
Die Einzelheiten wurden im Meeting am 21.01.2013 festgelegt.''' == getInputValue('headerText')

        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '440,00', '440,00', '19,0'
        checkRowValues 1, 'S-10100', '1', 'Einheiten', 'Mustervorschau', 'Anfertigung eines Musters nach Kundenvorgaben.', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '894,98' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '169,10']])
        assert '1.064,43' == subtotalGross
        assert '1.064,43' == total

        assert 'Details zu den einzelnen Punkten finden Sie im Pflichtenheft.' == getInputValue('footerText')
        assert ['700', '701'] == getInputValue('termsAndConditions')
        assert 'Angebot unterliegt möglicherweise weiteren Änderungen.' == getInputValue('notes')

        setInputValue('subject', 'Werbekampagne Frühjahr 2013/03')
        setInputValue('validUntil_date', '17.04.2013')
        setInputValue('shippingDate_date', '6.3.2013')

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
        openSelectorAndSelect 2, 'products', 'P-10700'
        checkRowValues 2, 'P-10700', '1,0', 'Stück', 'Stempel', 'Mit Firmenaufdruck nach Kundenvorgabe.', '8,99', '8,99', '19,0'
        setPriceTableInputValue 2, 'quantity', '4'
        checkRowValues 2, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck nach Kundenvorgabe.', '8,99', '35,96', '19,0'
        assert '490,94' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '92,33']])
        assert '583,62' == subtotalGross
        assert '583,62' == total
        moveRowUp 2
        moveRowUp 1
        checkRowValues 0, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck nach Kundenvorgabe.', '8,99', '35,96', '19,0'
        checkRowValues 1, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '490,94' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '92,33']])
        assert '583,62' == subtotalGross
        assert '583,62' == total
        moveRowDown 0
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        checkRowValues 1, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck nach Kundenvorgabe.', '8,99', '35,96', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        assert '490,94' == subtotalNet
        checkTaxRates([['7,0', '0,35'], ['19,0', '92,33']])
        assert '583,62' == subtotalGross
        assert '583,62' == total

        assert 4 == addNewPriceTableRow()
        openSelectorAndSelect 3, 'products', 'P-10001'
        checkRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption der geplanten Werbekampagne', '450,00', '450,00', '19,0'
        checkRowValues 1, 'P-10700', '4', 'Stück', 'Stempel', 'Mit Firmenaufdruck nach Kundenvorgabe.', '8,99', '35,96', '19,0'
        checkRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,49', '4,98', '7,0'
        checkRowValues 3, 'P-10001', '1,0', 'Packung', 'Papier A4 90 g/m²', 'Packung zu 100 Blatt. Chlorfrei gebleicht.', '2,99', '2,99', '7,0'
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '586,82' == total

        setInputValue('discountPercent', '2')
        getInput('discountAmount').click()
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '11,74' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '575,08' == total
        setInputValue('discountAmount', '5')
        getInput('adjustment').click()
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '11,74' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '570,08' == total
        setInputValue('adjustment', '-0,09')
        getInput('discountAmount').click()
        assert '493,93' == subtotalNet
        checkTaxRates([['7,0', '0,56'], ['19,0', '92,33']])
        assert '586,82' == subtotalGross
        assert '11,74' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '569,99' == total

        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/quote/show/'))
        assert 'Angebot Werbekampagne Frühjahr 2013/03 wurde geändert.' == flashMessage
        assert 'Werbekampagne Frühjahr 2013/03' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'A-10000-10000' == getShowFieldText(col, 1)
        assert 'Werbekampagne Frühjahr 2013/03' == getShowFieldText(col, 2)
        def link = getShowField(col, 3).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 4).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        assert 'versendet' == getShowFieldText(col, 5)
        col = fieldSet.findElement(By.className('col-r'))
        assert '20.02.2013' == getShowFieldText(col, 1)
        assert '17.04.2013' == getShowFieldText(col, 2)
        assert '06.03.2013' == getShowFieldText(col, 3)
        assert 'elektronisch' == getShowFieldText(col, 4)
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
        assert 'für die geplante Werbekampange "Frühjahr 2013" möchten wir Ihnen gern folgendes Angebot unterbreiten.\nDie Einzelheiten wurden im Meeting am 21.01.2013 festgelegt.' == field.text
        assert field.findElement(By.tagName('br'))

        checkStaticRowValues 0, 'S-10000', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption der geplanten Werbekampagne', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 1, 'P-10700', '4', 'Stück', 'Stempel\nMit Firmenaufdruck nach Kundenvorgabe.', '8,99 €', '35,96 €', '19,0 %'
        checkStaticRowValues 2, 'P-10000', '2', 'Packung', 'Papier A4 80 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,49 €', '4,98 €', '7,0 %'
        checkStaticRowValues 3, 'P-10001', '1', 'Packung', 'Papier A4 90 g/m²\nPackung zu 100 Blatt. Chlorfrei gebleicht.', '2,99 €', '2,99 €', '7,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        WebElement tr = tfoot.findElement(By.xpath('tr[@class="subtotal"][1]'))
        assert '493,93 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '0,56 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '92,33 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('tr[@class="subtotal"][2]'))
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
        assert 'Details zu den einzelnen Punkten finden Sie im Pflichtenheft.' == getShowFieldText(fieldSet, 1)
        assert 'Dienstleistungen, Waren' == getShowFieldText(fieldSet, 2)
        fieldSet = getFieldset(dataSheet, 5)
        assert 'Angebot unterliegt möglicherweise weiteren Änderungen.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Quote.count()
    }

    @Test
    void testEditQuoteErrors() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert driver.currentUrl.startsWith(getUrl('/quote/edit/'))
        assert 'Angebot bearbeiten' == driver.title
        assert 'Angebote' == driver.findElement(BY_HEADER).text
        assert 'Werbekampagne Frühjahr 2013' == driver.findElement(BY_SUBHEADER).text

        driver.findElement(By.name('subject')).clear()
        driver.findElement(By.id('organization')).clear()
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert driver.currentUrl.startsWith(getUrl('/quote/update'))
        assert checkErrorFields(['subject', 'organization.id'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert driver.currentUrl.startsWith(getUrl('/quote/list'))
        driver.quit()

        assert 1 == Quote.count()
    }

    @Test
    void testDeleteQuoteAction() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/quote/list'))
        assert 'Angebot wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Quote.count()
    }

    @Test
    void testDeleteQuoteNoAction() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/quote/list') == driver.currentUrl
        driver.quit()

        assert 1 == Quote.count()
    }


    //-- Non-public methods ---------------------

    protected Quote prepareQuote(Organization org, Person p) {
        def quote = new Quote(
            subject: 'Werbekampagne Frühjahr 2013',
            docDate: new GregorianCalendar(2013, Calendar.FEBRUARY, 20).time,
            organization: org,
            person: p,
            carrier: Carrier.get(501),
            shippingDate: new GregorianCalendar(2013, Calendar.FEBRUARY, 21).time,
            billingAddrStreet: org.billingAddrStreet,
            billingAddrPostalCode: org.billingAddrPostalCode,
            billingAddrLocation: org.billingAddrLocation,
            billingAddrState: org.billingAddrState,
            billingAddrCountry: org.billingAddrCountry,
            shippingAddrStreet: org.shippingAddrStreet,
            shippingAddrPostalCode: org.shippingAddrPostalCode,
            shippingAddrLocation: org.shippingAddrLocation,
            shippingAddrState: org.shippingAddrState,
            shippingAddrCountry: org.shippingAddrCountry,
            headerText: '''für die geplante Werbekampange "Frühjahr 2013" möchten wir Ihnen gern folgendes Angebot unterbreiten.
Die Einzelheiten wurden im Meeting am 21.01.2013 festgelegt.''',
            footerText: 'Details zu den einzelnen Punkten finden Sie im Pflichtenheft.',
            notes: 'Angebot unterliegt möglicherweise weiteren Änderungen.',
            stage: QuoteStage.get(602),
            validUntil: new GregorianCalendar(2013, Calendar.MARCH, 20).time
        )
        quote.addToItems(new InvoicingItem(
                number: 'S-10000',
                quantity: 1.0d,
                unit: 'Einheiten',
                name: 'Konzeption und Planung',
                description: 'Konzeption der geplanten Werbekampagne',
                unitPrice: 440.0d,
                tax: 19.0d
            )).
            addToItems(new InvoicingItem(
                number: 'S-10100',
                quantity: 1.0d,
                unit: 'Einheiten',
                name: 'Mustervorschau',
                description: 'Anfertigung eines Musters nach Kundenvorgaben.',
                unitPrice: 450.0d,
                tax: 19.0d
            )).
            addToItems(new InvoicingItem(
                number: 'P-10000',
                quantity: 2.0d,
                unit: 'Packung',
                name: 'Papier A4 80 g/m²',
                description: 'Packung zu 100 Blatt. Chlorfrei gebleicht.',
                unitPrice: 2.49d,
                tax: 7.0d
            )).
            addToTermsAndConditions(TermsAndConditions.get(700)).
            addToTermsAndConditions(TermsAndConditions.get(701)).
            save(flush: true)
        return quote
    }
}
