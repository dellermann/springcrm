/*
 * PurchaseInvoiceFunctionalTests.groovy
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
 * The class {@code PurchaseInvoiceFunctionalTests} represents a functional
 * test case for the purchase invoice section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
@RunWith(JUnit4)
class PurchaseInvoiceFunctionalTests extends InvoicingTransactionTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()

    def grailsApplication


    //-- Public methods -------------------------

    @Before
    void login() {
        appDataDir.deleteDir()

        if (!name.methodName.startsWith('testCreate')) {
            preparePurchaseInvoice()
        }

        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys 'mkampe'
        driver.findElement(BY_PASSWORD).sendKeys 'abc1234'
        driver.findElement(BY_LOGIN_BTN).click()

        open '/purchase-invoice/list'
    }

    @Test
    void testCreatePurchaseInvoiceNoDocSuccess() {
        clickToolbarButton 0, getUrl('/purchase-invoice/create')
        checkTitles 'Eingangsrechnung anlegen', 'Eingangsrechnungen', 'Neue Eingangsrechnung'
        setInputValue 'number', '4049493-4994'
        setInputValue 'subject', 'Entwicklung eines Designs'
        setInputValue 'vendorName', 'Katja Schmale Webdesignerin'
        setInputValue 'stage.id', '2101'
        checkDate 'docDate_date'
        setInputValue 'docDate_date', '15.3.2013'
        setInputValue 'dueDate_date', '15.4.2013'
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0', '0,00', 'still-unpaid-paid'

        assert 1 == numPriceTableRows
        setPriceTableInputValue 0, 'number', '5100'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption des geplanten Webdesigns'
        setPriceTableInputValue 0, 'unitPrice', '500'
        setPriceTableInputValue 0, 'tax', '19'
        assert '500,00' == getPriceTableRowTotal(0)
        assert '500,00' == subtotalNet
        checkTaxRates([['19,0', '95,00']])
        assert '595,00' == subtotalGross
        assert '595,00' == total

        assert 2 == addNewPriceTableRow()
        setPriceTableInputValue 1, 'number', '1200'
        setPriceTableInputValue 1, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[1].unit', 'Einh')
        setPriceTableInputValue 1, 'name', 'Webdesign'
        setPriceTableInputValue 1, 'unitPrice', '1300'
        setPriceTableInputValue 1, 'tax', '19'
        assert '1.300,00' == getPriceTableRowTotal(1)
        assert '1.800,00' == subtotalNet
        checkTaxRates([['19,0', '342,00']])
        assert '2.142,00' == subtotalGross
        assert '2.142,00' == total

        assert 3 == addNewPriceTableRow()
        setPriceTableInputValue 2, 'number', '1990'
        setPriceTableInputValue 2, 'quantity', '2'
        assert 'Einheiten' == selectAutocompleteEx('items[2].unit', 'Einh')
        setPriceTableInputValue 2, 'name', 'Zusätzlicher Entwurf'
        setPriceTableInputValue 2, 'unitPrice', '250'
        setPriceTableInputValue 2, 'tax', '19'
        assert '500,00' == getPriceTableRowTotal(2)
        assert '2.300,00' == subtotalNet
        checkTaxRates([['19,0', '437,00']])
        assert '2.737,00' == subtotalGross
        assert '2.737,00' == total

        assert 4 == addNewPriceTableRow()
        setPriceTableInputValue 3, 'number', '9500'
        setPriceTableInputValue 3, 'quantity', '10'
        assert 'Packung' == selectAutocompleteEx('items[3].unit', 'Pack')
        setPriceTableInputValue 3, 'name', 'Büromaterial'
        setPriceTableInputValue 3, 'description', 'Papier, Klebeband, Kleinteile'
        setPriceTableInputValue 3, 'unitPrice', '4,5'
        setPriceTableInputValue 3, 'tax', '7'
        assert '45,00' == getPriceTableRowTotal(3)
        assert '2.345,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '437,00']])
        assert '2.785,15' == subtotalGross
        assert '2.785,15' == total

        moveRowUp 2
        moveRowUp 1
        checkRowValues 0, '1990', '2', 'Einheiten', 'Zusätzlicher Entwurf', '', '250,00', '500,00', '19'
        checkRowValues 1, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '500,00', '500,00', '19'
        checkRowValues 2, '1200', '1', 'Einheiten', 'Webdesign', '', '1.300,00', '1.300,00', '19'
        checkRowValues 3, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7'
        assert '2.345,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '437,00']])
        assert '2.785,15' == subtotalGross
        assert '2.785,15' == total
        moveRowDown 0
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '500,00', '500,00', '19'
        checkRowValues 1, '1990', '2', 'Einheiten', 'Zusätzlicher Entwurf', '', '250,00', '500,00', '19'
        checkRowValues 2, '1200', '1', 'Einheiten', 'Webdesign', '', '1.300,00', '1.300,00', '19'
        checkRowValues 3, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7'
        assert '2.345,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '437,00']])
        assert '2.785,15' == subtotalGross
        assert '2.785,15' == total
        assert 3 == removeRow(1)
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '500,00', '500,00', '19'
        checkRowValues 1, '1200', '1', 'Einheiten', 'Webdesign', '', '1.300,00', '1.300,00', '19'
        checkRowValues 2, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7'
        assert '1.845,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '342,00']])
        assert '2.190,15' == subtotalGross
        assert '2.190,15' == total

        setInputValue 'discountPercent', '2'
        getInput('discountAmount').click()
        assert '43,80' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '2.146,35' == total
        setInputValue 'adjustment', '-1,36'
        getInput('discountAmount').click()
        assert '2.144,99' == total

        setInputValue 'notes', 'Lieferschein zur Rechnung nachfordern.'
        checkStillUnpaid '0', '2.144,99', 'still-unpaid-unpaid'
        submitForm getUrl('/purchase-invoice/show/')

        assert 'Eingangsrechnung Entwicklung eines Designs wurde angelegt.' == flashMessage
        assert 'Entwicklung eines Designs' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert '4049493-4994' == getShowFieldText(col, 1)
        assert 'Entwicklung eines Designs' == getShowFieldText(col, 2)
        assert 'Katja Schmale Webdesignerin' == getShowFieldText(col, 3)
        assert '' == getShowFieldText(col, 4)
        assert 'geprüft' == getShowFieldText(col, 5)
        col = fieldSet.findElement(By.className('col-r'))
        assert '15.03.2013' == getShowFieldText(col, 1)
        assert '15.04.2013' == getShowFieldText(col, 2)

        checkStaticRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption des geplanten Webdesigns', '500,00 €', '500,00 €', '19,0 %'
        checkStaticRowValues 1, '1200', '1', 'Einheiten', 'Webdesign', '1.300,00 €', '1.300,00 €', '19,0 %'
        checkStaticRowValues 2, '9500', '10', 'Packung', 'Büromaterial\nPapier, Klebeband, Kleinteile', '4,50 €', '45,00 €', '7,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '1.845,00 €' == tfoot.findElement(By.cssSelector('tr.subtotal-net td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '3,15 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '342,00 €' == tr.findElement(By.className('currency')).text
        assert '2.190,15 €' == tfoot.findElement(By.cssSelector('tr.subtotal-gross td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[5]'))
        assert '2,00 %' == tr.findElement(By.className('percentage')).text
        assert '43,80 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[6]'))
        assert '-1,36 €' == tr.findElement(By.className('currency')).text
        assert '2.144,99 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 3)
        assert 'Lieferschein zur Rechnung nachfordern.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testCreatePurchaseInvoiceDocSuccess() {
        clickToolbarButton 0, getUrl('/purchase-invoice/create')
        checkTitles 'Eingangsrechnung anlegen', 'Eingangsrechnungen', 'Neue Eingangsrechnung'
        setInputValue 'number', '4049493-4994'
        setInputValue 'subject', 'Entwicklung eines Designs'
        setInputValue 'vendorName', 'Katja Schmale Webdesignerin'
        File testFile = uploadFile 'file', PURCHASE_INVOICE_EXAMPLE_DOCUMENT
        setInputValue 'stage.id', '2101'
        checkDate 'docDate_date'
        setInputValue 'docDate_date', '15.3.2013'
        setInputValue 'dueDate_date', '15.4.2013'
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0', '0,00', 'still-unpaid-paid'

        assert 1 == numPriceTableRows
        setPriceTableInputValue 0, 'number', '5100'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption des geplanten Webdesigns'
        setPriceTableInputValue 0, 'unitPrice', '500'
        setPriceTableInputValue 0, 'tax', '19'
        assert '500,00' == getPriceTableRowTotal(0)
        assert '500,00' == subtotalNet
        checkTaxRates([['19,0', '95,00']])
        assert '595,00' == subtotalGross
        assert '595,00' == total

        assert 2 == addNewPriceTableRow()
        setPriceTableInputValue 1, 'number', '1200'
        setPriceTableInputValue 1, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[1].unit', 'Einh')
        setPriceTableInputValue 1, 'name', 'Webdesign'
        setPriceTableInputValue 1, 'unitPrice', '1300'
        setPriceTableInputValue 1, 'tax', '19'
        assert '1.300,00' == getPriceTableRowTotal(1)
        assert '1.800,00' == subtotalNet
        checkTaxRates([['19,0', '342,00']])
        assert '2.142,00' == subtotalGross
        assert '2.142,00' == total

        assert 3 == addNewPriceTableRow()
        setPriceTableInputValue 2, 'number', '1990'
        setPriceTableInputValue 2, 'quantity', '2'
        assert 'Einheiten' == selectAutocompleteEx('items[2].unit', 'Einh')
        setPriceTableInputValue 2, 'name', 'Zusätzlicher Entwurf'
        setPriceTableInputValue 2, 'unitPrice', '250'
        setPriceTableInputValue 2, 'tax', '19'
        assert '500,00' == getPriceTableRowTotal(2)
        assert '2.300,00' == subtotalNet
        checkTaxRates([['19,0', '437,00']])
        assert '2.737,00' == subtotalGross
        assert '2.737,00' == total

        assert 4 == addNewPriceTableRow()
        setPriceTableInputValue 3, 'number', '9500'
        setPriceTableInputValue 3, 'quantity', '10'
        assert 'Packung' == selectAutocompleteEx('items[3].unit', 'Pack')
        setPriceTableInputValue 3, 'name', 'Büromaterial'
        setPriceTableInputValue 3, 'description', 'Papier, Klebeband, Kleinteile'
        setPriceTableInputValue 3, 'unitPrice', '4,5'
        setPriceTableInputValue 3, 'tax', '7'
        assert '45,00' == getPriceTableRowTotal(3)
        assert '2.345,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '437,00']])
        assert '2.785,15' == subtotalGross
        assert '2.785,15' == total

        moveRowUp 2
        moveRowUp 1
        checkRowValues 0, '1990', '2', 'Einheiten', 'Zusätzlicher Entwurf', '', '250,00', '500,00', '19'
        checkRowValues 1, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '500,00', '500,00', '19'
        checkRowValues 2, '1200', '1', 'Einheiten', 'Webdesign', '', '1.300,00', '1.300,00', '19'
        checkRowValues 3, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7'
        assert '2.345,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '437,00']])
        assert '2.785,15' == subtotalGross
        assert '2.785,15' == total
        moveRowDown 0
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '500,00', '500,00', '19'
        checkRowValues 1, '1990', '2', 'Einheiten', 'Zusätzlicher Entwurf', '', '250,00', '500,00', '19'
        checkRowValues 2, '1200', '1', 'Einheiten', 'Webdesign', '', '1.300,00', '1.300,00', '19'
        checkRowValues 3, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7'
        assert '2.345,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '437,00']])
        assert '2.785,15' == subtotalGross
        assert '2.785,15' == total
        assert 3 == removeRow(1)
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '500,00', '500,00', '19'
        checkRowValues 1, '1200', '1', 'Einheiten', 'Webdesign', '', '1.300,00', '1.300,00', '19'
        checkRowValues 2, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7'
        assert '1.845,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '342,00']])
        assert '2.190,15' == subtotalGross
        assert '2.190,15' == total

        setInputValue 'notes', 'Lieferschein zur Rechnung nachfordern.'
        checkStillUnpaid '0', '2.190,15', 'still-unpaid-unpaid'
        submitForm getUrl('/purchase-invoice/show/')

        assert 'Eingangsrechnung Entwicklung eines Designs wurde angelegt.' == flashMessage
        assert 'Entwicklung eines Designs' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert '4049493-4994' == getShowFieldText(col, 1)
        assert 'Entwicklung eines Designs' == getShowFieldText(col, 2)
        assert 'Katja Schmale Webdesignerin' == getShowFieldText(col, 3)
        checkDocument getShowField(col, 4).findElement(By.tagName('a')), testFile
        assert 'geprüft' == getShowFieldText(col, 5)
        col = fieldSet.findElement(By.className('col-r'))
        assert '15.03.2013' == getShowFieldText(col, 1)
        assert '15.04.2013' == getShowFieldText(col, 2)

        checkStaticRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption des geplanten Webdesigns', '500,00 €', '500,00 €', '19,0 %'
        checkStaticRowValues 1, '1200', '1', 'Einheiten', 'Webdesign', '1.300,00 €', '1.300,00 €', '19,0 %'
        checkStaticRowValues 2, '9500', '10', 'Packung', 'Büromaterial\nPapier, Klebeband, Kleinteile', '4,50 €', '45,00 €', '7,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '1.845,00 €' == tfoot.findElement(By.cssSelector('tr.subtotal td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '3,15 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '342,00 €' == tr.findElement(By.className('currency')).text
        assert '2.190,15 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 3)
        assert 'Lieferschein zur Rechnung nachfordern.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testCreatePurchaseInvoiceErrors() {
        clickToolbarButton 0, getUrl('/purchase-invoice/create')
        checkTitles 'Eingangsrechnung anlegen', 'Eingangsrechnungen', 'Neue Eingangsrechnung'
        submitForm getUrl('/purchase-invoice/save')

        assert checkErrorFields([
            'number', 'subject', 'vendorName', 'vendor.id', 'dueDate',
            'dueDate_date'
        ])
        List<WebElement> errorMsgs = driver.findElements(By.xpath(
            '//form[@id="purchaseInvoice-form"]/fieldset[2]/div/ul[@class="field-msgs"]/li[@class="error-msg"]'
        ))
        assert 1 == errorMsgs.size()
        assert 'Pos. 1, Artikel/Leistung: Feld darf nicht leer sein.' == errorMsgs[0].text
        cancelForm getUrl('/purchase-invoice/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Eingangsrechnung anlegen' == link.text
        assert getUrl('/purchase-invoice/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == PurchaseInvoice.count()
    }

    @Test
    void testShowPurchaseInvoice() {
        int id = clickListItem 0, 1, '/purchase-invoice/show'
        checkTitles 'Eingangsrechnung anzeigen', 'Eingangsrechnungen', 'Entwicklung eines Designs'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert '4049493-4994' == getShowFieldText(col, 1)
        assert 'Entwicklung eines Designs' == getShowFieldText(col, 2)
        assert 'Katja Schmale Webdesignerin' == getShowFieldText(col, 3)
        checkDocument getShowField(col, 4).findElement(By.tagName('a'))
        assert 'geprüft' == getShowFieldText(col, 5)
        col = fieldSet.findElement(By.className('col-r'))
        assert '15.03.2013' == getShowFieldText(col, 1)
        assert '15.04.2013' == getShowFieldText(col, 2)
        assert '' == getShowFieldText(col, 3)
        assert '' == getShowFieldText(col, 4)
        assert '' == getShowFieldText(col, 5)

        checkStaticRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption des geplanten Webdesigns', '500,00 €', '500,00 €', '19,0 %'
        checkStaticRowValues 1, '1200', '1', 'Einheiten', 'Webdesign', '1.300,00 €', '1.300,00 €', '19,0 %'
        checkStaticRowValues 2, '9500', '10', 'Packung', 'Büromaterial\nPapier, Klebeband, Kleinteile', '4,50 €', '45,00 €', '7,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        assert '1.845,00 €' == tfoot.findElement(By.cssSelector('tr.subtotal-net td.currency')).text
        WebElement tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '3,15 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '342,00 €' == tr.findElement(By.className('currency')).text
        assert '2.190,15 €' == tfoot.findElement(By.cssSelector('tr.subtotal-gross td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[5]'))
        assert '2,00 %' == tr.findElement(By.className('percentage')).text
        assert '43,80 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[6]'))
        assert '-1,36 €' == tr.findElement(By.className('currency')).text
        assert '2.144,99 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 3)
        assert 'Lieferschein zur Rechnung nachfordern.' == getShowFieldText(fieldSet, 1)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'purchase-invoice', id
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testListPurchaseInvoices() {
        checkTitles 'Eingangsrechnungen', 'Eingangsrechnungen'
        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('string')
        def link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/purchase-invoice/show/'))
        assert '4049493-4994' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/purchase-invoice/show/'))
        assert 'Entwicklung eines Designs' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('ref')
        assert 'Katja Schmale Webdesignerin' == td.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('status')
        assert 'geprüft' == td.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('date')
        assert '15.03.2013' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('date')
        assert '15.04.2013' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('currency')
        assert '2.144,99 €' == td.text
        td = tr.findElement(By.xpath('td[9]'))
        assert td.getAttribute('class').contains('currency')
        assert td.getAttribute('class').contains('balance-state-red')
        assert '-2.144,99 €' == td.text
        td = tr.findElement(By.xpath('td[10]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/purchase-invoice/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/purchase-invoice/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/purchase-invoice/list') == driver.currentUrl
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testEditPurchaseInvoiceSuccess() {
        clickListActionButton 0, 0, getUrl('/purchase-invoice/edit/')
        checkTitles 'Eingangsrechnung bearbeiten', 'Eingangsrechnungen', 'Entwicklung eines Designs'
        def col = driver.findElement(By.xpath('//form[@id="purchaseInvoice-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert '4049493-4994' == getInputValue('number')
        assert 'Entwicklung eines Designs' == getInputValue('subject')
        assert 'Katja Schmale Webdesignerin' == getInputValue('vendorName')
        assert '' == getInputValue('vendor.id')
        WebElement field = getShowField(col, 4)
        WebElement link = field.findElement(By.xpath('.//div[@class="document-preview"]/a'))
        String href = link.getAttribute('href')
        assert href.startsWith(getUrl('/data-file/load-file/'))
        assert href.endsWith('?type=purchaseInvoice')
        assert purchaseInvoiceExampleDocument.name == link.text
        List<WebElement> lis = field.findElements(By.xpath('.//ul[@class="document-preview-links"]/li'))
        assert 1 == lis.size()
        assert 'document-delete' == lis[0].getAttribute('class')
        assert 'Dokument löschen' == lis[0].text
        assert '2101' == getInputValue('stage.id')
        assert '15.03.2013' == getInputValue('docDate_date')
        assert '15.04.2013' == getInputValue('dueDate_date')
        assert '0,00' == getInputValue('paymentAmount')
        checkStillUnpaid '0', '2.144,99', 'still-unpaid-unpaid'

        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '500,00', '500,00', '19,0'
        checkRowValues 1, '1200', '1', 'Einheiten', 'Webdesign', '', '1.300,00', '1.300,00', '19,0'
        checkRowValues 2, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7,0'
        assert '1.845,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '342,00']])
        assert '2.190,15' == subtotalGross
        assert '2' == getInputValue('discountPercent')
        assert '43,80' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '-1,36' == getInputValue('adjustment')
        assert '2.144,99' == total

        assert 'Lieferschein zur Rechnung nachfordern.' == getInputValue('notes')

        setInputValue 'subject', 'Planung eines Webdesigns'
        setInputValue 'stage.id', '2102'
        checkDate 'paymentDate_date'
        setInputValue 'paymentDate_date', '10.4.2013'
        stillUnpaid.click()
        assert '2.144,99' == getInputValue('paymentAmount')
        checkStillUnpaid '0', '0,00', 'still-unpaid-paid'
        setInputValue 'paymentMethod.id', '2401'

        setPriceTableInputValue 0, 'unitPrice', '450'
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '450,00', '450,00', '19,0'
        assert '1.795,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '332,50']])
        assert '2.130,65' == subtotalGross
        assert '2' == getInputValue('discountPercent')
        assert '42,61' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '-1,36' == getInputValue('adjustment')
        assert '2.086,68' == total

        assert 2 == removeRow(1)
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '450,00', '450,00', '19,0'
        checkRowValues 1, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7,0'
        assert '495,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '85,50']])
        assert '583,65' == subtotalGross
        assert '2' == getInputValue('discountPercent')
        assert '11,67' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '-1,36' == getInputValue('adjustment')
        assert '570,62' == total

        assert 3 == addNewPriceTableRow()
        setPriceTableInputValue 2, 'number', '7000'
        setPriceTableInputValue 2, 'quantity', '4'
        assert 'Stunden' == selectAutocompleteEx('items[2].unit', 'Stun')
        setPriceTableInputValue 2, 'name', 'Beratung'
        setPriceTableInputValue 2, 'description', 'Beratungsleistung vor Ort'
        setPriceTableInputValue 2, 'unitPrice', '75'
        setPriceTableInputValue 2, 'tax', '19'
        assert '300,00' == getPriceTableRowTotal(2)
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '450,00', '450,00', '19,0'
        checkRowValues 1, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7,0'
        checkRowValues 2, '7000', '4', 'Stunden', 'Beratung', 'Beratungsleistung vor Ort', '75,00', '300,00', '19'
        assert '795,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '142,50']])
        assert '940,65' == subtotalGross
        assert '2' == getInputValue('discountPercent')
        assert '18,81' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '-1,36' == getInputValue('adjustment')
        assert '920,48' == total
        moveRowUp 2
        moveRowUp 1
        checkRowValues 0, '7000', '4', 'Stunden', 'Beratung', 'Beratungsleistung vor Ort', '75,00', '300,00', '19'
        checkRowValues 1, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '450,00', '450,00', '19,0'
        checkRowValues 2, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7,0'
        assert '795,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '142,50']])
        assert '940,65' == subtotalGross
        assert '2' == getInputValue('discountPercent')
        assert '18,81' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '-1,36' == getInputValue('adjustment')
        assert '920,48' == total
        moveRowDown 0
        checkRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung', 'Konzeption des geplanten Webdesigns', '450,00', '450,00', '19,0'
        checkRowValues 1, '7000', '4', 'Stunden', 'Beratung', 'Beratungsleistung vor Ort', '75,00', '300,00', '19'
        checkRowValues 2, '9500', '10', 'Packung', 'Büromaterial', 'Papier, Klebeband, Kleinteile', '4,50', '45,00', '7,0'
        assert '795,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '142,50']])
        assert '940,65' == subtotalGross
        assert '2' == getInputValue('discountPercent')
        assert '18,81' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '-1,36' == getInputValue('adjustment')
        assert '920,48' == total

        assert !getPriceTableRow(0).findElement(By.className('up-btn')).displayed
        assert !getPriceTableRow(2).findElement(By.className('down-btn')).displayed

        setInputValue 'adjustment', '-1,85'
        getInput('discountAmount').click()
        assert '795,00' == subtotalNet
        checkTaxRates([['7,0', '3,15'], ['19,0', '142,50']])
        assert '940,65' == subtotalGross
        assert '2' == getInputValue('discountPercent')
        assert '18,81' == priceTable.findElement(By.id('discount-from-percent')).text
        assert '-1,85' == getInputValue('adjustment')
        assert '919,99' == total
        checkStillUnpaid '0', '-1.225,00', 'still-unpaid-too-much'
        submitForm getUrl('/purchase-invoice/show/')

        assert 'Eingangsrechnung Planung eines Webdesigns wurde geändert.' == flashMessage
        assert 'Planung eines Webdesigns' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert '4049493-4994' == getShowFieldText(col, 1)
        assert 'Planung eines Webdesigns' == getShowFieldText(col, 2)
        assert 'Katja Schmale Webdesignerin' == getShowFieldText(col, 3)
        checkDocument getShowField(col, 4).findElement(By.tagName('a'))
        assert 'bezahlt' == getShowFieldText(col, 5)
        col = fieldSet.findElement(By.className('col-r'))
        assert '15.03.2013' == getShowFieldText(col, 1)
        assert '15.04.2013' == getShowFieldText(col, 2)
        assert '10.04.2013' == getShowFieldText(col, 3)
        assert '2.144,99 €' == getShowFieldText(col, 4)
        assert 'Überweisung' == getShowFieldText(col, 5)

        checkStaticRowValues 0, '5100', '1', 'Einheiten', 'Konzeption und Planung\nKonzeption des geplanten Webdesigns', '450,00 €', '450,00 €', '19,0 %'
        checkStaticRowValues 1, '7000', '4', 'Stunden', 'Beratung\nBeratungsleistung vor Ort', '75,00 €', '300,00 €', '19,0 %'
        checkStaticRowValues 2, '9500', '10', 'Packung', 'Büromaterial\nPapier, Klebeband, Kleinteile', '4,50 €', '45,00 €', '7,0 %'

        WebElement tfoot = priceTable.findElement(By.tagName('tfoot'))
        WebElement tr = tfoot.findElement(By.className('subtotal-net'))
        assert '795,00 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[2]'))
        assert '7 % MwSt.' == tr.findElement(By.className('label')).text
        assert '3,15 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[3]'))
        assert '19 % MwSt.' == tr.findElement(By.className('label')).text
        assert '142,50 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.className('subtotal-gross'))
        assert '940,65 €' == tr.findElement(By.cssSelector('td.currency')).text
        tr = tfoot.findElement(By.xpath('./tr[5]'))
        assert '2,00 %' == tr.findElement(By.className('percentage')).text
        assert '18,81 €' == tr.findElement(By.className('currency')).text
        tr = tfoot.findElement(By.xpath('./tr[6]'))
        assert '-1,85 €' == tr.findElement(By.className('currency')).text
        assert '919,99 €' == tfoot.findElement(By.cssSelector('tr.total td.currency')).text

        fieldSet = getFieldset(dataSheet, 3)
        assert 'Lieferschein zur Rechnung nachfordern.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testEditPurchaseInvoiceRemoveDoc() {
        clickListActionButton 0, 0, getUrl('/purchase-invoice/edit/')
        checkTitles 'Eingangsrechnung bearbeiten', 'Eingangsrechnungen', 'Entwicklung eines Designs'
        def col = driver.findElement(By.xpath('//form[@id="purchaseInvoice-form"]/fieldset[1]')).findElement(By.className('col-l'))
        WebElement field = getShowField(col, 4)
        WebElement link = field.findElement(By.xpath('.//div[@class="document-preview"]/a'))
        String href = link.getAttribute('href')
        assert href.startsWith(getUrl('/data-file/load-file/'))
        assert href.endsWith('?type=purchaseInvoice')
        assert purchaseInvoiceExampleDocument.name == link.text
        List<WebElement> lis = field.findElements(By.xpath('.//ul[@class="document-preview-links"]/li'))
        assert 1 == lis.size()
        assert 'document-delete' == lis[0].getAttribute('class')
        assert 'Dokument löschen' == lis[0].text
        lis[0].findElement(By.tagName('span')).click()
        assert '1' == getInputValue('fileRemove')
        assert 0 == field.findElements(By.className('document-preview')).size()
        assert 0 == field.findElements(By.className('document-preview-links')).size()
        submitForm getUrl('/purchase-invoice/show/')

        assert 'Eingangsrechnung Entwicklung eines Designs wurde geändert.' == flashMessage
        assert 'Entwicklung eines Designs' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert '' == getShowFieldText(col, 4)
        assert !new File(purchaseInvoiceFolder, purchaseInvoiceExampleDocument.name).exists()
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testEditInvoiceReplaceDoc() {
        clickListActionButton 0, 0, getUrl('/purchase-invoice/edit/')
        checkTitles 'Eingangsrechnung bearbeiten', 'Eingangsrechnungen', 'Entwicklung eines Designs'
        def col = driver.findElement(By.xpath('//form[@id="purchaseInvoice-form"]/fieldset[1]')).findElement(By.className('col-l'))
        WebElement field = getShowField(col, 4)
        WebElement link = field.findElement(By.xpath('.//div[@class="document-preview"]/a'))
        String href = link.getAttribute('href')
        assert href.startsWith(getUrl('/data-file/load-file/'))
        assert href.endsWith('?type=purchaseInvoice')
        assert purchaseInvoiceExampleDocument.name == link.text
        List<WebElement> lis = field.findElements(By.xpath('.//ul[@class="document-preview-links"]/li'))
        assert 1 == lis.size()
        assert 'document-delete' == lis[0].getAttribute('class')
        assert 'Dokument löschen' == lis[0].text
        File testFile = uploadFile 'file', PURCHASE_INVOICE_EXAMPLE_DOCUMENT_ALT
        submitForm getUrl('/purchase-invoice/show/')

        assert 'Eingangsrechnung Entwicklung eines Designs wurde geändert.' == flashMessage
        assert 'Entwicklung eines Designs' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        checkDocument getShowField(col, 4).findElement(By.tagName('a')), testFile
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testEditInvoiceErrors() {
        clickListActionButton 0, 0, getUrl('/purchase-invoice/edit/')
        checkTitles 'Eingangsrechnung bearbeiten', 'Eingangsrechnungen', 'Entwicklung eines Designs'

        clearInput 'number'
        clearInput 'subject'
        clearInput 'vendorName'
        clearInput 'docDate_date'
        clearInput 'dueDate_date'
        for (int i = 0; i < 2; i++) {
            removeRow 0
        }
        assert !getPriceTableRow(0).findElement(By.className('remove-btn')).displayed
        submitForm getUrl('/purchase-invoice/update')

        assert checkErrorFields([
            'number', 'subject', 'vendorName', 'vendor.id', 'docDate',
            'docDate_date', 'dueDate', 'dueDate_date'
        ])
        cancelForm getUrl('/purchase-invoice/list')

        def purchaseInvoice = PurchaseInvoice.list().first()
        checkFile purchaseInvoiceExampleDocument, new File(purchaseInvoiceFolder, purchaseInvoice.documentFile.storageName)
        driver.quit()

        assert 1 == PurchaseInvoice.count()
    }

    @Test
    void testDeletePurchaseInvoiceAction() {
        def purchaseInvoice = PurchaseInvoice.list().first()
        String fileName = purchaseInvoice.documentFile.storageName
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/purchase-invoice/list'))
        assert 'Eingangsrechnung wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == PurchaseInvoice.count()
        assert !new File(purchaseInvoiceFolder, fileName).exists()
    }

    @Test
    void testDeletePurchaseInvoiceNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/purchase-invoice/list') == driver.currentUrl
        driver.quit()

        assert 1 == PurchaseInvoice.count()
        def purchaseInvoice = PurchaseInvoice.list().first()
        checkFile purchaseInvoiceExampleDocument, new File(purchaseInvoiceFolder, purchaseInvoice.documentFile.storageName)
    }


    //-- Non-public methods ---------------------

    /**
     * Checks the purchase invoice document behind the given link by comparing
     * it to the given file.
     *
     * @param link      the link to the purchase invoice document
     * @param document  the expected document to compare to
     * @since           1.4
     */
    protected void checkDocument(WebElement link,
                                 File document = purchaseInvoiceExampleDocument)
    {
        assert document.name == link.text
        def m = link.getAttribute('href') =~ /\/data-file\/load-file\/(\d+)\?type=purchaseInvoice$/
        assert m
        String id = Long.toHexString(m[0][1] as Long).toUpperCase().
            padLeft(16, '0')
        checkFile document, new File(purchaseInvoiceFolder, id)
    }
}
