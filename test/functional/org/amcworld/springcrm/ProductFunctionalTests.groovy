/*
 * ProductFunctionalTests.groovy
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

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.Select


/**
 * The class {@code ProductFunctionalTests} represents a functional test
 * case for the product section of SpringCRM.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class ProductFunctionalTests extends SalesItemTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        if (!name.methodName.startsWith('testCreate')) {
//            prepareProduct()
        }

        open('/', 'de')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open('/product/list')
    }

    @Test
    void testCreateProductNoPricingSuccess() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li/a')).click()
        assert getUrl('/product/create') == driver.currentUrl
        assert 'Produkt anlegen' == driver.title
        assert 'Produkte' == driver.findElement(BY_HEADER).text
        assert 'Neues Produkt' == driver.findElement(BY_SUBHEADER).text
        setInputValue 'name', 'Papier A4 80 g/m²'
        setInputValue 'category.id', '3000'
        setInputValue 'manufacturer', 'Papierwerk Brandenburg'
        setInputValue 'retailer', 'Druckereibedarf Kiel GmbH'
        setInputValue 'quantity', '1'
        setInputValue 'unit.id', '302'
        setInputValue 'unitPrice', '2,49'
        setInputValue 'weight', '200'
        setInputValue 'taxRate.id', '401'
        setInputValue 'purchasePrice', '2,19'
        setInputValue 'description', 'Packung zu 100 Blatt. Chlorfrei gebleicht.'
        assert 'Für diesen Artikel wurde noch keine Kalkulation durchgeführt.' == driver.findElement(By.cssSelector('.empty-list > p')).text
        WebElement link = driver.findElement(By.id('start-pricing'))
        assert link.displayed
        assert 'Kalkulation beginnen' == link.text
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/product/show/'))
        assert 'Produkt Papier A4 80 g/m² wurde angelegt.' == flashMessage
        assert 'Papier A4 80 g/m²' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'P-10000' == getShowFieldText(col, 1)
        assert 'Papier A4 80 g/m²' == getShowFieldText(col, 2)
        assert 'Materialien' == getShowFieldText(col, 3)
        assert 'Papierwerk Brandenburg' == getShowFieldText(col, 4)
        assert 'Druckereibedarf Kiel GmbH' == getShowFieldText(col, 5)
        assert '1' == getShowFieldText(col, 6)
        assert 'Packung' == getShowFieldText(col, 7)
        assert '2,49 €' == getShowFieldText(col, 8)
        col = fieldSet.findElement(By.className('col-r'))
        assert '200' == getShowFieldText(col, 1)
        assert '7 %' == getShowFieldText(col, 2)
        assert '2,19 €' == getShowFieldText(col, 3)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Packung zu 100 Blatt. Chlorfrei gebleicht.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Product.count()
    }

    @Test
    void testCreateProductPricingSuccess() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li/a')).click()
        assert getUrl('/product/create') == driver.currentUrl
        assert 'Produkt anlegen' == driver.title
        assert 'Produkte' == driver.findElement(BY_HEADER).text
        assert 'Neues Produkt' == driver.findElement(BY_SUBHEADER).text
        setInputValue 'name', 'Papier A4 80 g/m²'
        setInputValue 'category.id', '3000'
        setInputValue 'manufacturer', 'Papierwerk Brandenburg'
        setInputValue 'retailer', 'Druckereibedarf Kiel GmbH'
        setInputValue 'quantity', '1'
        setInputValue 'unit.id', '302'
        setInputValue 'unitPrice', '2,49'
        setInputValue 'weight', '200'
        setInputValue 'taxRate.id', '401'
        setInputValue 'description', 'Packung zu 100 Blatt. Chlorfrei gebleicht.'
        assert 'Für diesen Artikel wurde noch keine Kalkulation durchgeführt.' == driver.findElement(By.cssSelector('.empty-list > p')).text
        driver.findElement(By.id('start-pricing')).click()
        assert !getInput('quantity').displayed
        assert !getInput('unitPrice').displayed
        assert !getInput('unit.id').displayed
        setInputValue 'pricing.quantity', '10'
        setInputValue 'pricing.unit.id', '302'
        assert 1 == numStep1TableRows
        setStep1TableInputValue 0, 'quantity', '10'
        setStep1TableInputValue 0, 'unit', 'Packungen'
        setStep1TableInputValue 0, 'name', 'Materialeinkauf'
        setStep1TableInputValue 0, 'unitPrice', '2,19'
        assert '21,90' == getStep1TableRowTotal(0)
        assert '21,90' == step1Total
        assert '2,19' == step1UnitPrice
        WebElement tr = getStep1TableRow(0)
        assert !tr.findElement(By.className('up-btn')).displayed
        assert !tr.findElement(By.className('down-btn')).displayed
        assert !tr.findElement(By.className('remove-btn')).displayed
        assert 2 == addNewStep1TableRow()
        setStep1TableInputValue 1, 'quantity', '0,25'
        setStep1TableInputValue 1, 'unit', 'Stunden'
        setStep1TableInputValue 1, 'name', 'Materialbeschaffung'
        setStep1TableInputValue 1, 'unitPrice', '40'
        assert '10,00' == getStep1TableRowTotal(1)
        assert '31,90' == step1Total
        assert '3,19' == step1UnitPrice
        tr = getStep1TableRow(0)
        assert !tr.findElement(By.className('up-btn')).displayed
        assert tr.findElement(By.className('down-btn')).displayed
        assert tr.findElement(By.className('remove-btn')).displayed
        tr = getStep1TableRow(1)
        assert tr.findElement(By.className('up-btn')).displayed
        assert !tr.findElement(By.className('down-btn')).displayed
        assert tr.findElement(By.className('remove-btn')).displayed
        assert 3 == addNewStep1TableRow()
        setStep1TableInputValue 2, 'type', 'sum'
        assert !getStep1TableInput(2, 'quantity').enabled
        assert !getStep1TableInput(2, 'unit').enabled
        assert !getStep1TableInput(2, 'name').enabled
        assert !getStep1TableInput(2, 'unitPrice').enabled
        assert '31,90' == getStep1TableRowTotal(2)
        assert '31,90' == step1Total
        assert '3,19' == step1UnitPrice
        tr = getStep1TableRow(0)
        assert !tr.findElement(By.className('up-btn')).displayed
        assert tr.findElement(By.className('down-btn')).displayed
        assert tr.findElement(By.className('remove-btn')).displayed
        tr = getStep1TableRow(1)
        assert tr.findElement(By.className('up-btn')).displayed
        assert tr.findElement(By.className('down-btn')).displayed
        assert tr.findElement(By.className('remove-btn')).displayed
        tr = getStep1TableRow(2)
        assert tr.findElement(By.className('up-btn')).displayed
        assert !tr.findElement(By.className('down-btn')).displayed
        assert tr.findElement(By.className('remove-btn')).displayed
        assert 4 == addNewStep1TableRow()
        setStep1TableInputValue 3, 'type', 'relativeToCurrentSum'
        setStep1TableInputValue 3, 'quantity', '1'
        setStep1TableInputValue 3, 'unit', 'Einheit'
        setStep1TableInputValue 3, 'name', 'Gewinn'
        setStep1TableInputValue 3, 'unitPercent', '5'
        assert '1,60' == getStep1TableRowTotal(3)
        assert '33,50' == step1Total
        assert '3,35' == step1UnitPrice
        assert 5 == addNewStep1TableRow()
        setStep1TableInputValue 4, 'type', 'relativeToLastSum'
        setStep1TableInputValue 4, 'quantity', '1'
        setStep1TableInputValue 4, 'unit', 'Einheit'
        setStep1TableInputValue 4, 'name', 'Risiko'
        setStep1TableInputValue 4, 'unitPercent', '5'
        assert '1,60' == getStep1TableRowTotal(4)
        assert '35,10' == step1Total
        assert '3,51' == step1UnitPrice
        moveRowDown 0
        assert '35,10' == step1Total
        assert '3,51' == step1UnitPrice
        moveRowUp 1
        assert '35,10' == step1Total
        assert '3,51' == step1UnitPrice
        moveRowUp 2
        assert '21,90' == getStep1TableRowTotal(1)
        assert '1,60' == getStep1TableRowTotal(3)
        assert '1,10' == getStep1TableRowTotal(4)
        assert '34,60' == step1Total
        assert '3,46' == step1UnitPrice
        moveRowDown 1
        assert '35,10' == step1Total
        assert '3,51' == step1UnitPrice
        assert '10' == getValue('step2-quantity')
        assert 'Packung' == getValue('step2-unit')
        assert '3,51' == getValue('step2-unit-price')
        assert '35,10' == getValue('step2-total-price')
        assert '10' == getValue('step2-total-quantity')
        assert 'Packung' == getValue('step2-total-unit')
        assert '3,51' == getValue('step2-total-unit-price')
        assert '35,10' == getValue('step2-total')
        setInputValue 'pricing.discountPercent', '1'
        assert '0,35' == getValue('step2-discount-percent-amount')
        setInputValue 'pricing.adjustment', '0,15'
        assert '10' == getValue('step2-total-quantity')
        assert 'Packung' == getValue('step2-total-unit')
        assert '3,49' == getValue('step2-total-unit-price')
        assert '34,90' == getValue('step2-total')
        assert '---' == getValue('step3-unit-price')
        assert '34,90' == getValue('step3-total-price')
        driver.findElement(By.id('step3-quantity')).sendKeys '10'
        new Select(driver.findElement(By.id('step3-unit'))).selectByValue '302'
        assert '3,49' == getValue('step3-unit-price')
        assert '34,90' == getValue('step3-total-price')
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/product/show/'))
        assert 'Produkt Papier A4 80 g/m² wurde angelegt.' == flashMessage
        assert 'Papier A4 80 g/m²' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'P-10000' == getShowFieldText(col, 1)
        assert 'Papier A4 80 g/m²' == getShowFieldText(col, 2)
        assert 'Materialien' == getShowFieldText(col, 3)
        assert 'Papierwerk Brandenburg' == getShowFieldText(col, 4)
        assert 'Druckereibedarf Kiel GmbH' == getShowFieldText(col, 5)
        assert '10' == getShowFieldText(col, 6)
        assert 'Packung' == getShowFieldText(col, 7)
        assert '3,49 €' == getShowFieldText(col, 8)
        col = fieldSet.findElement(By.className('col-r'))
        assert '200' == getShowFieldText(col, 1)
        assert '7 %' == getShowFieldText(col, 2)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Packung zu 100 Blatt. Chlorfrei gebleicht.' == getShowFieldText(fieldSet, 1)
        fieldSet = getFieldset(dataSheet, 3)
        WebElement tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, '1.', '10', 'Packungen', 'Materialeinkauf', 'absoluter Betrag', '', '0,00', '2,19 €', '21,90 €'
        checkStaticRowValues tbody, 1, '2.', '0,25', 'Stunden', 'Materialbeschaffung', 'absoluter Betrag', '', '0,00', '40,00 €', '10,00 €'
        checkStaticRowValues tbody, 2, '3.', '', 'Zwischensumme', '', '31,90 €'
        checkStaticRowValues tbody, 3, '4.', '1', 'Einheit', 'Gewinn', 'relativ zur akt. Summe', '', '5,00', '1,60 €', '1,60 €'
        checkStaticRowValues tbody, 4, '5.', '1', 'Einheit', 'Risiko', 'relativ zur letzt. Zw.-summe', '', '5,00', '1,60 €', '1,60 €'
        WebElement tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Gesamtpreis', '', '35,09 €'
        checkStaticRowValues tfoot, 1, 'Kalkulierter Einzelpreis', '', '3,51 €'
        fieldSet = getFieldset(dataSheet, 4)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Kalkulierter Gesamtwert', '10', 'Packung', '', 'zu je', '3,51 €', '35,09 €'
        checkStaticRowValues tbody, 1, 'Rabatt %', '', '', '1,00', '', '', '0,35 €'
        checkStaticRowValues tbody, 2, 'Preisanpassung +/-', '', '', '', '', '', '0,15 €'
        tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Verkaufspreis', '10', 'Packung', '', 'zu je', '3,49 €', '34,89 €'
        fieldSet = getFieldset(dataSheet, 5)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Der Artikel wird verkauft als', '10', 'Packung', 'zu je', '3,49 €', '34,89 €'
        driver.quit()

        assert 1 == Product.count()
    }

    @Test
    void testCreateProductNoPricingErrors() {
        assert 'Produkte' == driver.title
        assert 'Produkte' == driver.findElement(BY_HEADER).text
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li/a')).click()
        assert getUrl('/product/create') == driver.currentUrl
        assert 'Produkt anlegen' == driver.title
        assert 'Produkte' == driver.findElement(BY_HEADER).text
        assert 'Neues Produkt' == driver.findElement(BY_SUBHEADER).text
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert getUrl('/product/save?returnUrl=') == driver.currentUrl
        assert checkErrorFields(['name'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert getUrl('/product/list') == driver.currentUrl
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Produkt anlegen' == link.text
        assert getUrl('/product/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Product.count()
    }

    @Test
    void testCreateProductPricingErrors() {
        assert 'Produkte' == driver.title
        assert 'Produkte' == driver.findElement(BY_HEADER).text
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li/a')).click()
        assert getUrl('/product/create') == driver.currentUrl
        assert 'Produkt anlegen' == driver.title
        assert 'Produkte' == driver.findElement(BY_HEADER).text
        assert 'Neues Produkt' == driver.findElement(BY_SUBHEADER).text
        driver.findElement(By.id('start-pricing')).click()
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert getUrl('/product/save?returnUrl=') == driver.currentUrl
        assert checkErrorFields(['name', 'quantity'])
        assert 2 == driver.findElements(By.xpath('//fieldset[3]//p//span[@class="error-msg"]')).size()
        assert 1 == driver.findElements(By.xpath('//fieldset[3]//table[@id="step1-pricing-items"]/following-sibling::span[@class="error-msg"]')).size()
        assert 1 == driver.findElements(By.xpath('//select[@id="step3-unit"]/following-sibling::span[@class="error-msg"]')).size()
        driver.findElement(By.linkText('Abbruch')).click()
        assert getUrl('/product/list') == driver.currentUrl
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Produkt anlegen' == link.text
        assert getUrl('/product/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Product.count()
    }

//    @Test
//    void testShowProduct() {
//        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[2]/a')).click()
//        def m = (driver.currentUrl =~ '/organization/show/(\\d+)')
//        assert !!m
//        int id = m[0][1] as Integer
//        assert 'Organisation anzeigen' == driver.title
//        assert 'Organisationen' == driver.findElement(BY_HEADER).text
//        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(BY_SUBHEADER).text
//        def dataSheet = driver.findElement(By.className('data-sheet'))
//        def fieldSet = getFieldset(dataSheet, 1)
//        assert 'Allgemeine Informationen' == fieldSet.findElement(By.tagName('h4')).text
//        def col = fieldSet.findElement(By.className('col-l'))
//        assert 'O-10000' == getShowFieldText(col, 1)
//        assert 'Kunde' == getShowFieldText(col, 2)
//        assert 'Landschaftsbau Duvensee GbR' == getShowFieldText(col, 3)
//        assert 'GbR' == getShowFieldText(col, 4)
//        assert 'Kunde' == getShowFieldText(col, 5)
//        assert 'Umwelt' == getShowFieldText(col, 6)
//        col = fieldSet.findElement(By.className('col-r'))
//        assert '04543 31233' == getShowFieldText(col, 1)
//        assert '04543 31235' == getShowFieldText(col, 2)
//        def link = getShowField(col, 4).findElement(By.tagName('a'))
//        assert 'mailto:info@landschaftsbau-duvensee.example' == link.getAttribute('href')
//        assert 'info@landschaftsbau-duvensee.example' == link.text
//        link = getShowField(col, 6).findElement(By.tagName('a'))
//        assert 'http://www.landschaftsbau-duvensee.example/' == link.getAttribute('href')
//        assert '_blank' == link.getAttribute('target')
//        assert 'http://www.landschaftsbau-duvensee.example' == link.text
//        fieldSet = dataSheet.findElement(By.xpath('div[@class="multicol-content"][1]'))
//        col = fieldSet.findElement(By.className('col-l'))
//        assert 'Rechnungsanschrift' == col.findElement(By.tagName('h4')).text
//        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
//        assert '23898' == getShowFieldText(col, 3)
//        assert 'Duvensee' == getShowFieldText(col, 4)
//        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
//        assert 'Deutschland' == getShowFieldText(col, 6)
//        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
//        col = fieldSet.findElement(By.className('col-r'))
//        assert 'Lieferanschrift' == col.findElement(By.tagName('h4')).text
//        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
//        assert '23898' == getShowFieldText(col, 3)
//        assert 'Duvensee' == getShowFieldText(col, 4)
//        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
//        assert 'Deutschland' == getShowFieldText(col, 6)
//        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
//        fieldSet = getFieldset(dataSheet, 2)
//        assert 'Bemerkungen' == fieldSet.findElement(By.tagName('h4')).text
//        def notes = getShowField(fieldSet, 1)
//        assert 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.' == notes.text
//        assert 1 == notes.findElements(By.tagName('br')).size()
//
//        String param = "organization=${id}"
//        fieldSet = getFieldset(dataSheet, 4)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/person/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Personen' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/person/create?organization.id=${id}"))
//        assert 'Person anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 5)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/quote/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Angebote' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/quote/create?organization.id=${id}"))
//        assert 'Angebot anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 6)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/sales-order/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Verkaufsbestellungen' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/sales-order/create?organization.id=${id}"))
//        assert 'Verkaufsbestellung anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 7)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/invoice/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Rechnungen' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?organization.id=${id}"))
//        assert 'Rechnung anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 8)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/dunning/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Mahnungen' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/dunning/create?organization.id=${id}"))
//        assert 'Mahnung anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 9)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/credit-memo/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Gutschriften' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/create?organization.id=${id}"))
//        assert 'Gutschrift anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 10)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/project/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Projekte' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/project/create?organization.id=${id}"))
//        assert 'Projekt anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 11)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/document/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Dokumente' == fieldSet.findElement(By.tagName('h4')).text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 12)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/call/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Anrufe' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/call/create?organization.id=${id}"))
//        assert 'Anruf anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        fieldSet = getFieldset(dataSheet, 13)
//        assert fieldSet.getAttribute('class').contains('remote-list')
//        assert param == fieldSet.getAttribute('data-load-params')
//        assert '/springcrm/note/list-embedded' == fieldSet.getAttribute('data-load-url')
//        assert 'Notizen' == fieldSet.findElement(By.tagName('h4')).text
//        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
//        assert link.getAttribute('href').startsWith(getUrl("/note/create?organization.id=${id}"))
//        assert 'Notiz anlegen' == link.text
//        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()
//
//        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')
//
//        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
//        link = toolbar.findElement(By.xpath('li[1]/a'))
//        assert 'white' == link.getAttribute('class')
//        assert getUrl('/organization/list?type=') == link.getAttribute('href')
//        assert 'Liste' == link.text
//        link = toolbar.findElement(By.xpath('li[2]/a'))
//        assert 'green' == link.getAttribute('class')
//        assert getUrl('/organization/create') == link.getAttribute('href')
//        assert 'Anlegen' == link.text
//        link = toolbar.findElement(By.xpath('li[3]/a'))
//        assert 'green' == link.getAttribute('class')
//        assert getUrl("/organization/edit/${id}") == link.getAttribute('href')
//        assert 'Bearbeiten' == link.text
//        link = toolbar.findElement(By.xpath('li[4]/a'))
//        assert 'blue' == link.getAttribute('class')
//        assert getUrl("/organization/copy/${id}") == link.getAttribute('href')
//        assert 'Kopieren' == link.text
//        link = toolbar.findElement(By.xpath('li[5]/a'))
//        assert link.getAttribute('class').contains('red')
//        assert link.getAttribute('class').contains('delete-btn')
//        assert getUrl("/organization/delete/${id}") == link.getAttribute('href')
//        assert 'Löschen' == link.text
//        link.click()
//        driver.switchTo().alert().dismiss()
//        assert getUrl("/organization/show/${id}?type=") == driver.currentUrl
//
//        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
//        link = actions.findElement(By.xpath('li[1]/a'))
//        assert link.getAttribute('class').contains('button')
//        assert link.getAttribute('href').startsWith(getUrl("/call/create?organization.id=${id}"))
//        assert 'Anruf anlegen' == link.text
//        link = actions.findElement(By.xpath('li[2]/a'))
//        assert link.getAttribute('class').contains('button')
//        assert link.getAttribute('href').startsWith(getUrl("/quote/create?organization.id=${id}"))
//        assert 'Angebot anlegen' == link.text
//        link = actions.findElement(By.xpath('li[3]/a'))
//        assert link.getAttribute('class').contains('button')
//        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?organization.id=${id}"))
//        assert 'Rechnung anlegen' == link.text
//        driver.quit()
//
//        assert 1 == Organization.count()
//    }
//
//    @Test
//    void testListOrganizations() {
//        assert 'Organisationen' == driver.title
//        assert 'Organisationen' == driver.findElement(BY_HEADER).text
//        def link = driver.findElement(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]/a'))
//        assert getUrl('/organization/list?letter=L') == link.getAttribute('href')
//        assert 'L' == link.text
//        assert 1 == driver.findElements(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]')).size()
//
//        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
//        assert 1 == tbody.findElements(By.tagName('tr')).size()
//        def tr = tbody.findElement(By.xpath('tr[1]'))
//        def td = tr.findElement(By.xpath('td[2]'))
//        assert td.getAttribute('class').contains('id')
//        link = td.findElement(By.tagName('a'))
//        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
//        assert 'O-10000' == link.text
//        td = tr.findElement(By.xpath('td[3]'))
//        assert td.getAttribute('class').contains('string')
//        link = td.findElement(By.tagName('a'))
//        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
//        assert 'Landschaftsbau Duvensee GbR' == link.text
//        td = tr.findElement(By.xpath('td[4]'))
//        assert td.getAttribute('class').contains('string')
//        assert 'Dörpstraat 25, 23898 Duvensee' == td.text
//        td = tr.findElement(By.xpath('td[5]'))
//        assert td.getAttribute('class').contains('string')
//        link = td.findElement(By.tagName('a'))
//        assert 'tel:04543%2031233' == link.getAttribute('href')
//        assert '04543 31233' == link.text
//        td = tr.findElement(By.xpath('td[6]'))
//        assert td.getAttribute('class').contains('string')
//        link = td.findElement(By.tagName('a'))
//        assert 'mailto:info@landschaftsbau-duvensee.example' == link.getAttribute('href')
//        assert 'info@landschaftsbau-duvensee.example' == link.text
//        td = tr.findElement(By.xpath('td[7]'))
//        assert td.getAttribute('class').contains('string')
//        link = td.findElement(By.tagName('a'))
//        assert 'http://www.landschaftsbau-duvensee.example/' == link.getAttribute('href')
//        assert '_blank' == link.getAttribute('target')
//        assert 'www.landschaftsbau-duvensee.example' == link.text
//        td = tr.findElement(By.xpath('td[8]'))
//        assert td.getAttribute('class').contains('action-buttons')
//        link = td.findElement(By.xpath('a[1]'))
//        assert link.getAttribute('href').startsWith(getUrl('/organization/edit/'))
//        assert link.getAttribute('class').contains('button')
//        assert link.getAttribute('class').contains('green')
//        assert 'Bearbeiten' == link.text
//        link = td.findElement(By.xpath('a[2]'))
//        assert link.getAttribute('href').startsWith(getUrl('/organization/delete/'))
//        assert link.getAttribute('class').contains('button')
//        assert link.getAttribute('class').contains('red')
//        assert link.getAttribute('class').contains('delete-btn')
//        assert 'Löschen' == link.text
//        link.click()
//        driver.switchTo().alert().dismiss()
//        assert getUrl('/organization/list') == driver.currentUrl
//        driver.quit()
//
//        assert 1 == Organization.count()
//    }
//
//    @Test
//    void testEditOrganizationSuccess() {
//        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
//        assert driver.currentUrl.startsWith(getUrl('/organization/edit/'))
//        assert 'Organisation bearbeiten' == driver.title
//        assert 'Organisationen' == driver.findElement(BY_HEADER).text
//        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(BY_SUBHEADER).text
//        def col = driver.findElement(By.xpath('//form[@id="organization-form"]/fieldset[1]')).findElement(By.className('col-l'))
//        assert getShowField(col, 1).text.startsWith('O-')
//        assert '10000' == getInputValue('number')
//        assert getInputValue('autoNumber')
//        assert null != driver.findElement(By.id('rec-type-1')).getAttribute('checked')
//        assert 'Landschaftsbau Duvensee GbR' == getInputValue('name')
//        assert 'GbR' == getInputValue('legalForm')
//        def select = new Select(driver.findElement(By.id('type')))
//        assert 'Kunde' == select.firstSelectedOption.text
//        select = new Select(driver.findElement(By.id('industry')))
//        assert 'Umwelt' == select.firstSelectedOption.text
//        select = new Select(driver.findElement(By.id('rating')))
//        assert '' == select.firstSelectedOption.text
//        assert '04543 31233' == getInputValue('phone')
//        assert '04543 31235' == getInputValue('fax')
//        assert 'info@landschaftsbau-duvensee.example' == getInputValue('email1')
//        assert '' == getInputValue('email2')
//        assert 'http://www.landschaftsbau-duvensee.example' == getInputValue('website')
//        assert '' == getInputValue('owner')
//        assert '' == getInputValue('numEmployees')
//        assert 'Dörpstraat 25' == getInputValue('billingAddrStreet')
//        assert '' == getInputValue('billingAddrPoBox')
//        assert '23898' == getInputValue('billingAddrPostalCode')
//        assert 'Duvensee' == getInputValue('billingAddrLocation')
//        assert 'Schleswig-Holstein' == getInputValue('billingAddrState')
//        assert 'Deutschland' == getInputValue('billingAddrCountry')
//        assert 'Dörpstraat 25' == getInputValue('shippingAddrStreet')
//        assert '' == getInputValue('shippingAddrPoBox')
//        assert '23898' == getInputValue('shippingAddrPostalCode')
//        assert 'Duvensee' == getInputValue('shippingAddrLocation')
//        assert 'Schleswig-Holstein' == getInputValue('shippingAddrState')
//        assert 'Deutschland' == getInputValue('shippingAddrCountry')
//        assert 'Kontakt über Peter Hermann hergestellt. Erstes Treffen am 13.06.2012.' == getInputValue('notes')
//
//        driver.findElement(By.id('rec-type-1')).click()
//        driver.findElement(By.id('rec-type-2')).click()
//        setInputValue 'name', 'Arne Friesing'
//        setInputValue 'legalForm', 'Einzelunternehmen'
//        setInputValue 'type', '104'
//        setInputValue 'industry', '1021'
//        setInputValue 'phone', '04541 428717'
//        setInputValue 'fax', '04541 428719'
//        setInputValue 'email1', 'arne@friesing.example'
//        setInputValue 'website', 'http://friesing.example'
//        setInputValue 'numEmployees', '1'
//        setInputValue 'billingAddrStreet', 'Kirschenallee 17a'
//        setInputValue 'billingAddrPostalCode', '23909'
//        setInputValue 'billingAddrLocation', 'Ratzeburg'
//        setInputValue 'billingAddrState', 'Schleswig-Holstein'
//        setInputValue 'billingAddrCountry', 'Deutschland'
//        setInputValue 'shippingAddrStreet', 'Kirschenallee 17a'
//        setInputValue 'shippingAddrPostalCode', '23909'
//        setInputValue 'shippingAddrLocation', 'Ratzeburg'
//        setInputValue 'shippingAddrState', 'Schleswig-Holstein'
//        setInputValue 'shippingAddrCountry', 'Deutschland'
//        setInputValue 'notes', 'Guter, zuverlässiger Designer'
//        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
//
//        assert driver.currentUrl.startsWith(getUrl('/organization/show/'))
//        assert 'Organisation Arne Friesing wurde geändert.' == flashMessage
//        assert 'Arne Friesing' == driver.findElement(BY_SUBHEADER).text
//        def dataSheet = driver.findElement(By.className('data-sheet'))
//        def fieldSet = getFieldset(dataSheet, 1)
//        col = fieldSet.findElement(By.className('col-l'))
//        assert 'O-10000' == getShowFieldText(col, 1)
//        assert 'Lieferant' == getShowFieldText(col, 2)
//        assert 'Arne Friesing' == getShowFieldText(col, 3)
//        assert 'Einzelunternehmen' == getShowFieldText(col, 4)
//        assert 'Verkäufer' == getShowFieldText(col, 5)
//        assert 'Medien' == getShowFieldText(col, 6)
//        assert '1' == getShowFieldText(col, 8)
//        col = fieldSet.findElement(By.className('col-r'))
//        assert '04541 428717' == getShowFieldText(col, 1)
//        assert '04541 428719' == getShowFieldText(col, 2)
//        def link = getShowField(col, 4).findElement(By.tagName('a'))
//        assert 'mailto:arne@friesing.example' == link.getAttribute('href')
//        assert 'arne@friesing.example' == link.text
//        link = getShowField(col, 6).findElement(By.tagName('a'))
//        assert 'http://friesing.example/' == link.getAttribute('href')
//        assert '_blank' == link.getAttribute('target')
//        assert 'http://friesing.example' == link.text
//        fieldSet = dataSheet.findElement(By.xpath('div[@class="multicol-content"][1]'))
//        col = fieldSet.findElement(By.className('col-l'))
//        assert 'Kirschenallee 17a' == getShowFieldText(col, 1)
//        assert '23909' == getShowFieldText(col, 3)
//        assert 'Ratzeburg' == getShowFieldText(col, 4)
//        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
//        assert 'Deutschland' == getShowFieldText(col, 6)
//        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
//        col = fieldSet.findElement(By.className('col-r'))
//        assert 'Kirschenallee 17a' == getShowFieldText(col, 1)
//        assert '23909' == getShowFieldText(col, 3)
//        assert 'Ratzeburg' == getShowFieldText(col, 4)
//        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
//        assert 'Deutschland' == getShowFieldText(col, 6)
//        fieldSet = getFieldset(dataSheet, 2)
//        assert 'Guter, zuverlässiger Designer' == getShowFieldText(fieldSet, 1)
//        driver.quit()
//
//        assert 1 == Organization.count()
//    }
//
//    @Test
//    void testEditOrganizationErrors() {
//        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
//        assert driver.currentUrl.startsWith(getUrl('/organization/edit/'))
//        assert 'Organisation bearbeiten' == driver.title
//        assert 'Organisationen' == driver.findElement(BY_HEADER).text
//        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(BY_SUBHEADER).text
//
//        driver.findElement(By.id('rec-type-1')).click()
//        driver.findElement(By.name('name')).clear()
//        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
//        assert getUrl('/organization/update') == driver.currentUrl
//        assert checkErrorFields(['recType', 'name'])
//        driver.findElement(By.linkText('Abbruch')).click()
//        assert driver.currentUrl.startsWith(getUrl('/organization/list'))
//        driver.quit()
//
//        assert 1 == Organization.count()
//    }
//
//    @Test
//    void testDeleteOrganizationAction() {
//        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
//        driver.switchTo().alert().accept()
//        assert driver.currentUrl.startsWith(getUrl('/organization/list'))
//        assert 'Organisation wurde gelöscht.' == flashMessage
//        def emptyList = driver.findElement(By.className('empty-list'))
//        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
//        driver.quit()
//
//        assert 0 == Organization.count()
//    }
//
//    @Test
//    void testDeleteOrganizationNoAction() {
//        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
//        driver.switchTo().alert().dismiss()
//        assert getUrl('/organization/list') == driver.currentUrl
//        driver.quit()
//
//        assert 1 == Organization.count()
//    }

}
