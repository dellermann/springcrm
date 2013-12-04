/*
 * ServiceFunctionalTests.groovy
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select


/**
 * The class {@code ServiceFunctionalTests} represents a functional test
 * case for the service section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
@RunWith(JUnit4)
class ServiceFunctionalTests extends SalesItemTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        String methodName = name.methodName
        if (!methodName.startsWith('testCreate')) {
            if (methodName.endsWith('NoPricing')) {
                prepareServiceWithoutPricing()
            } else {
                prepareServiceWithPricing()
            }
        }

        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys 'mkampe'
        driver.findElement(BY_PASSWORD).sendKeys 'abc1234'
        driver.findElement(BY_LOGIN_BTN).click()

        open '/service/list'
    }

    @Test
    void testCreateServiceSuccessNoPricing() {
        clickToolbarButton 0, getUrl('/service/create')
        checkTitles 'Dienstleistung anlegen', 'Dienstleistungen', 'Neue Dienstleistung'
        setInputValue 'name', 'Mustervorschau'
        setInputValue 'category.id', '2007'
        setInputValue 'quantity', '1'
        setInputValue 'unit.id', '301'
        setInputValue 'unitPrice', '450'
        setInputValue 'taxRate.id', '400'
        setInputValue 'description', 'Anfertigung eines Musters nach Kundenvorgaben.'
        assert 'Für diesen Artikel wurde noch keine Kalkulation durchgeführt.' == driver.findElement(By.cssSelector('.empty-list > p')).text
        WebElement link = driver.findElement(By.id('start-pricing'))
        assert link.displayed
        assert 'Kalkulation beginnen' == link.text
        submitForm getUrl('/service/show/')

        assert 'Dienstleistung Mustervorschau wurde angelegt.' == flashMessage
        assert 'Mustervorschau' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10000' == getShowFieldText(col, 1)
        assert 'Mustervorschau' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '1' == getShowFieldText(col, 4)
        assert 'Einheiten' == getShowFieldText(col, 5)
        assert '450,00 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testCreateServiceSuccessPricing() {
        clickToolbarButton 0, getUrl('/service/create')
        checkTitles 'Dienstleistung anlegen', 'Dienstleistungen', 'Neue Dienstleistung'
        setInputValue 'name', 'Mustervorschau'
        setInputValue 'category.id', '2007'
        setInputValue 'taxRate.id', '400'
        setInputValue 'description', 'Anfertigung eines Musters nach Kundenvorgaben.'
        assert 'Für diesen Artikel wurde noch keine Kalkulation durchgeführt.' == driver.findElement(By.cssSelector('.empty-list > p')).text
        driver.findElement(By.id('start-pricing')).click()
        assert !getInput('quantity').displayed
        assert !getInput('unitPrice').displayed
        assert !getInput('unit.id').displayed
        setInputValue 'pricing.quantity', '1'
        setInputValue 'pricing.unit.id', '301'
        assert 1 == numStep1TableRows
        setStep1TableInputValue 0, 'quantity', '6'
        setStep1TableInputValue 0, 'unit', 'Stunden'
        setStep1TableInputValue 0, 'name', 'Arbeitsleistung (Mustererstellung)'
        setStep1TableInputValue 0, 'unitPrice', '50'
        assert '300,00' == getStep1TableRowTotal(0)
        assert '300,00' == step1Total
        assert '300,00' == step1UnitPrice
        WebElement tr = getStep1TableRow(0)
        assert !tr.findElement(By.className('up-btn')).displayed
        assert !tr.findElement(By.className('down-btn')).displayed
        assert !tr.findElement(By.className('remove-btn')).displayed
        assert 2 == addNewStep1TableRow()
        setStep1TableInputValue 1, 'quantity', '2'
        setStep1TableInputValue 1, 'unit', 'Stunden'
        setStep1TableInputValue 1, 'name', 'Arbeitsleistung (Präsentation)'
        setStep1TableInputValue 1, 'unitPrice', '50'
        assert '100,00' == getStep1TableRowTotal(1)
        assert '400,00' == step1Total
        assert '400,00' == step1UnitPrice
        assert '(1 Einheiten)' == driver.findElement(By.id('step1-total-price-quantity')).text
        assert '(1 Einheiten)' == driver.findElement(By.id('step1-unit-price-quantity')).text
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
        assert '400,00' == getStep1TableRowTotal(2)
        assert '400,00' == step1Total
        assert '400,00' == step1UnitPrice
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
        assert '20,00' == getStep1TableRowTotal(3)
        assert '420,00' == step1Total
        assert '420,00' == step1UnitPrice
        assert 5 == addNewStep1TableRow()
        setStep1TableInputValue 4, 'type', 'relativeToLastSum'
        setStep1TableInputValue 4, 'quantity', '1'
        setStep1TableInputValue 4, 'unit', 'Einheit'
        setStep1TableInputValue 4, 'name', 'Risiko'
        setStep1TableInputValue 4, 'unitPercent', '5'
        assert '20,00' == getStep1TableRowTotal(4)
        assert '440,00' == step1Total
        assert '440,00' == step1UnitPrice
        moveRowDown 0
        assert '440,00' == step1Total
        assert '440,00' == step1UnitPrice
        moveRowUp 1
        assert '440,00' == step1Total
        assert '440,00' == step1UnitPrice
        moveRowUp 2
        assert '300,00' == getStep1TableRowTotal(1)
        assert '20,00' == getStep1TableRowTotal(3)
        assert '15,00' == getStep1TableRowTotal(4)
        assert '435,00' == step1Total
        assert '435,00' == step1UnitPrice
        moveRowDown 1
        assert '440,00' == step1Total
        assert '440,00' == step1UnitPrice
        assert '1' == getValue('step2-quantity')
        assert 'Einheiten' == getValue('step2-unit')
        assert '440,00' == getValue('step2-unit-price')
        assert '440,00' == getValue('step2-total-price')
        assert '1' == getValue('step2-total-quantity')
        assert 'Einheiten' == getValue('step2-total-unit')
        assert '440,00' == getValue('step2-total-unit-price')
        assert '440,00' == getValue('step2-total')
        setInputValue 'pricing.discountPercent', '1'
        assert '4,40' == getValue('step2-discount-percent-amount')
        setInputValue 'pricing.adjustment', '-0,7'
        assert '1' == getValue('step2-total-quantity')
        assert 'Einheiten' == getValue('step2-total-unit')
        assert '434,90' == getValue('step2-total-unit-price')
        assert '434,90' == getValue('step2-total')
        assert '---' == getValue('step3-unit-price')
        assert '---' == getValue('step3-total-price')
        WebElement input = driver.findElement(By.id('step3-quantity'))
        input.sendKeys '1'
        def select = new Select(driver.findElement(By.id('step3-unit')))
        select.selectByValue '301'
        assert '434,90' == getValue('step3-unit-price')
        assert '434,90' == getValue('step3-total-price')
        input.clear()
        input.sendKeys '2'
        select.selectByValue '300'
        assert '217,45' == getValue('step3-unit-price')
        assert '434,90' == getValue('step3-total-price')
        submitForm getUrl('/service/show/')

        assert 'Dienstleistung Mustervorschau wurde angelegt.' == flashMessage
        assert 'Mustervorschau' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10000' == getShowFieldText(col, 1)
        assert 'Mustervorschau' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '2' == getShowFieldText(col, 4)
        assert 'Stück' == getShowFieldText(col, 5)
        assert '217,45 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getShowFieldText(fieldSet, 1)
        fieldSet = getFieldset(dataSheet, 3)
        assert 'In der folgenden Tabelle berechne ich den Preis für 1 Einheiten:' == fieldSet.findElement(By.xpath('./p')).text
        WebElement tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, '1.', '6', 'Stunden', 'Arbeitsleistung (Mustererstellung)', 'absoluter Betrag', '', '0,00', '50,00 €', '300,00 €'
        checkStaticRowValues tbody, 1, '2.', '2', 'Stunden', 'Arbeitsleistung (Präsentation)', 'absoluter Betrag', '', '0,00', '50,00 €', '100,00 €'
        checkStaticRowValues tbody, 2, '3.', '', 'Zwischensumme', '', '400,00 €'
        checkStaticRowValues tbody, 3, '4.', '1', 'Einheit', 'Gewinn', 'relativ zur akt. Summe', '', '5,00', '20,00 €', '20,00 €'
        checkStaticRowValues tbody, 4, '5.', '1', 'Einheit', 'Risiko', 'relativ zur letzt. Zw.-summe', '', '5,00', '20,00 €', '20,00 €'
        WebElement tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Gesamtpreis (1 Einheiten)', '', '440,00 €'
        checkStaticRowValues tfoot, 1, 'Kalkulierter Einzelpreis (1 Einheiten)', '', '440,00 €'
        fieldSet = getFieldset(dataSheet, 4)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Kalkulierter Gesamtwert', '1', 'Einheiten', '', 'zu je', '440,00 €', '440,00 €'
        checkStaticRowValues tbody, 1, 'Rabatt %', '', '', '1,00', '', '', '4,40 €'
        checkStaticRowValues tbody, 2, 'Preisanpassung +/-', '', '', '', '', '', '-0,70 €'
        tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Verkaufspreis', '1', 'Einheiten', '', 'zu je', '434,90 €', '434,90 €'
        fieldSet = getFieldset(dataSheet, 5)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Der Artikel wird verkauft als', '2', 'Stück', 'zu je', '217,45 €', '434,90 €'
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testCreateServiceErrorsNoPricing() {
        clickToolbarButton 0, getUrl('/service/create')
        checkTitles 'Dienstleistung anlegen', 'Dienstleistungen', 'Neue Dienstleistung'
        submitForm getUrl('/service/save?returnUrl=')

        assert checkErrorFields(['name'])
        cancelForm getUrl('/service/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Dienstleistung anlegen' == link.text
        assert getUrl('/service/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Service.count()
    }

    @Test
    void testCreateServiceErrorsPricing() {
        clickToolbarButton 0, getUrl('/service/create')
        checkTitles 'Dienstleistung anlegen', 'Dienstleistungen', 'Neue Dienstleistung'
        driver.findElement(By.id('start-pricing')).click()
        submitForm getUrl('/service/save?returnUrl=')

        assert checkErrorFields(['name', 'quantity'])
        WebElement fieldSet = driver.findElement(By.xpath('//fieldset[3]'))
        List<WebElement> errorMsgs = fieldSet.findElements(By.xpath('.//div[@id="step1-calculation-base"]//li[@class="error-msg"]'))
        assert 2 == errorMsgs.size()
        assert 'Muss größer als 0 sein.' == errorMsgs[0].text
        assert 'Feld darf nicht leer sein.' == errorMsgs[1].text
        errorMsgs = fieldSet.findElements(By.xpath(
            './div[1]/ul[@class="field-msgs"]/li[@class="error-msg"]'
        ))
        assert 2 == errorMsgs.size()
        assert 'Pos. 1, Bezeichnung: Feld darf nicht leer sein.' == errorMsgs[0].text
        assert 'Pos. 1, Einheit: Feld darf nicht leer sein.' == errorMsgs[1].text
        WebElement errorMsg = driver.findElement(By.xpath('//input[@id="step3-quantity"]/following-sibling::ul[@class="field-msgs"]/li[@class="error-msg"]'))
        assert 'Muss größer als 0 sein.' == errorMsg.text
        errorMsg = driver.findElement(By.xpath('//select[@id="step3-unit"]/following-sibling::ul[@class="field-msgs"]/li[@class="error-msg"]'))
        assert 'Feld darf nicht leer sein.' == errorMsg.text
        cancelForm getUrl('/service/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Dienstleistung anlegen' == link.text
        assert getUrl('/service/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Service.count()
    }

    @Test
    void testShowServiceNoPricing() {
        int id = clickListItem 0, 1, '/service/show'
        checkTitles 'Dienstleistung anzeigen', 'Dienstleistungen', 'Mustervorschau'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        assert 'Allgemeine Informationen' == fieldSet.findElement(By.tagName('h3')).text
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10000' == getShowFieldText(col, 1)
        assert 'Mustervorschau' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '1' == getShowFieldText(col, 4)
        assert 'Einheiten' == getShowFieldText(col, 5)
        assert '450,00 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Beschreibung' == fieldSet.findElement(By.tagName('h3')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getShowFieldText(fieldSet, 1)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'service', id

        assert 0 == driver.findElements(By.xpath('//aside[@id="action-bar"]/ul')).size()
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testShowServicePricing() {
        int id = clickListItem 0, 1, '/service/show'
        checkTitles 'Dienstleistung anzeigen', 'Dienstleistungen', 'Mustervorschau'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        assert 'Allgemeine Informationen' == fieldSet.findElement(By.tagName('h3')).text
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10000' == getShowFieldText(col, 1)
        assert 'Mustervorschau' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '1' == getShowFieldText(col, 4)
        assert 'Einheiten' == getShowFieldText(col, 5)
        assert '434,90 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Beschreibung' == fieldSet.findElement(By.tagName('h3')).text
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getShowFieldText(fieldSet, 1)

        fieldSet = getFieldset(dataSheet, 3)
        assert 'In der folgenden Tabelle berechne ich den Preis für 1 Einheiten:' == fieldSet.findElement(By.xpath('./p')).text
        WebElement tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, '1.', '6', 'Stunden', 'Arbeitsleistung (Mustererstellung)', 'absoluter Betrag', '', '0,00', '50,00 €', '300,00 €'
        checkStaticRowValues tbody, 1, '2.', '2', 'Stunden', 'Arbeitsleistung (Präsentation)', 'absoluter Betrag', '', '0,00', '50,00 €', '100,00 €'
        checkStaticRowValues tbody, 2, '3.', '', 'Zwischensumme', '', '400,00 €'
        checkStaticRowValues tbody, 3, '4.', '1', 'Einheit', 'Gewinn', 'relativ zur akt. Summe', '', '5,00', '20,00 €', '20,00 €'
        checkStaticRowValues tbody, 4, '5.', '1', 'Einheit', 'Risiko', 'relativ zur letzt. Zw.-summe', '', '5,00', '20,00 €', '20,00 €'
        WebElement tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Gesamtpreis (1 Einheiten)', '', '440,00 €'
        checkStaticRowValues tfoot, 1, 'Kalkulierter Einzelpreis (1 Einheiten)', '', '440,00 €'
        fieldSet = getFieldset(dataSheet, 4)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Kalkulierter Gesamtwert', '1', 'Einheiten', '', 'zu je', '440,00 €', '440,00 €'
        checkStaticRowValues tbody, 1, 'Rabatt %', '', '', '1,00', '', '', '4,40 €'
        checkStaticRowValues tbody, 2, 'Preisanpassung +/-', '', '', '', '', '', '-0,70 €'
        tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Verkaufspreis', '1', 'Einheiten', '', 'zu je', '434,90 €', '434,90 €'
        fieldSet = getFieldset(dataSheet, 5)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Der Artikel wird verkauft als', '1', 'Einheiten', 'zu je', '434,90 €', '434,90 €'

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'service', id

        assert 0 == driver.findElements(By.xpath('//aside[@id="action-bar"]/ul')).size()
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testListServicesNoPricing() {
        checkTitles 'Dienstleistungen', 'Dienstleistungen'
        def link = driver.findElement(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]/a'))
        assert getUrl('/service/list?letter=M') == link.getAttribute('href')
        assert 'M' == link.text
        assert 1 == driver.findElements(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]')).size()

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/service/show/'))
        assert 'S-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/service/show/'))
        assert 'Mustervorschau' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('string')
        assert 'Grafik und Design' == td.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('number')
        assert '1' == td.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('string')
        assert 'Einheiten' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('currency')
        assert '450,00 €' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/service/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/service/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/service/list') == driver.currentUrl
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testListServicePricing() {
        checkTitles 'Dienstleistungen', 'Dienstleistungen'
        def link = driver.findElement(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]/a'))
        assert getUrl('/service/list?letter=M') == link.getAttribute('href')
        assert 'M' == link.text
        assert 1 == driver.findElements(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]')).size()

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/service/show/'))
        assert 'S-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/service/show/'))
        assert 'Mustervorschau' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('string')
        assert 'Grafik und Design' == td.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('number')
        assert '1' == td.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('string')
        assert 'Einheiten' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('currency')
        assert '434,90 €' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/service/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/service/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/service/list') == driver.currentUrl
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testEditServiceSuccessNoPricing() {
        clickListActionButton 0, 0, getUrl('/service/edit/')
        checkTitles 'Dienstleistung bearbeiten', 'Dienstleistungen', 'Mustervorschau'
        def col = driver.findElement(By.xpath('//form[@id="service-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('S-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Mustervorschau' == getInputValue('name')
        assert '2007' == getInputValue('category.id')
        assert '1' == getInputValue('quantity')
        assert '301' == getInputValue('unit.id')
        assert '450,00' == getInputValue('unitPrice')
        assert '400' == getInputValue('taxRate.id')
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getInputValue('description')
        assert 'Für diesen Artikel wurde noch keine Kalkulation durchgeführt.' == driver.findElement(By.cssSelector('.empty-list > p')).text
        WebElement link = driver.findElement(By.id('start-pricing'))
        assert link.displayed
        assert 'Kalkulation beginnen' == link.text

        setInputValue 'autoNumber', false
        setInputValue 'number', '10200'
        setInputValue 'name', 'Druckvorstufe'
        setInputValue 'quantity', '2'
        setInputValue 'unit.id', '304'
        setInputValue 'unitPrice', '50'
        setInputValue 'taxRate.id', '400'
        setInputValue 'description', 'Vorbereitung für den Druck und Übermittlung an die Druckerei.'
        submitForm getUrl('/service/show/')

        assert 'Dienstleistung Druckvorstufe wurde geändert.' == flashMessage
        assert 'Druckvorstufe' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10200' == getShowFieldText(col, 1)
        assert 'Druckvorstufe' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '2' == getShowFieldText(col, 4)
        assert 'Stunden' == getShowFieldText(col, 5)
        assert '50,00 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Vorbereitung für den Druck und Übermittlung an die Druckerei.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testEditServiceSuccessPricing() {
        clickListActionButton 0, 0, getUrl('/service/edit/')
        checkTitles 'Dienstleistung bearbeiten', 'Dienstleistungen', 'Mustervorschau'
        def col = driver.findElement(By.xpath('//form[@id="service-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('S-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Mustervorschau' == getInputValue('name')
        assert '2007' == getInputValue('category.id')
        assert '400' == getInputValue('taxRate.id')
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getInputValue('description')
        assert '1' == getInputValue('pricing.quantity')
        assert '301' == getInputValue('pricing.unit.id')

        checkStep1RowValues 0, '6', 'Stunden', 'Arbeitsleistung (Mustererstellung)', 'absolute', null, null, '50,00', '300,00'
        checkStep1RowValues 1, '2', 'Stunden', 'Arbeitsleistung (Präsentation)', 'absolute', null, null, '50,00', '100,00'
        checkStep1RowValues 2, null, null, null, 'sum', null, null, null, '400,00'
        checkStep1RowValues 3, '1', 'Einheit', 'Gewinn', 'relativeToCurrentSum', null, '5,00', '20,00', '20,00'
        checkStep1RowValues 4, '1', 'Einheit', 'Risiko', 'relativeToLastSum', null, '5,00', '20,00', '20,00'
        assert '440,00' == step1Total
        assert '440,00' == step1UnitPrice
        assert '1' == getValue('step2-quantity')
        assert 'Einheiten' == getValue('step2-unit')
        assert '440,00' == getValue('step2-unit-price')
        assert '440,00' == getValue('step2-total-price')
        assert '1,00' == getInputValue('pricing.discountPercent')
        assert '4,40' == getValue('step2-discount-percent-amount')
        assert '-0,70' == getInputValue('pricing.adjustment')
        assert '1' == getValue('step2-total-quantity')
        assert 'Einheiten' == getValue('step2-total-unit')
        assert '434,90' == getValue('step2-total-unit-price')
        assert '434,90' == getValue('step2-total')
        assert '1' == driver.findElement(By.id('step3-quantity')).getAttribute('value')
        assert '301' == new Select(driver.findElement(By.id('step3-unit'))).firstSelectedOption.getAttribute('value')
        assert '434,90' == getValue('step3-unit-price')
        assert '434,90' == getValue('step3-total-price')

        setInputValue 'autoNumber', false
        setInputValue 'number', '10200'
        setInputValue 'name', 'Druckvorstufe'
        setInputValue 'taxRate.id', '400'
        setInputValue 'description', 'Vorbereitung für den Druck und Übermittlung an die Druckerei.'

        setInputValue 'pricing.quantity', '8'
        setInputValue 'pricing.unit.id', '304'
        setStep1TableInputValue 1, 'unitPrice', '45'
        assert '90,00' == getStep1TableRowTotal(1)
        assert '(8 Stunden)' == driver.findElement(By.id('step1-total-price-quantity')).text
        assert '(1 Stunden)' == driver.findElement(By.id('step1-unit-price-quantity')).text

        assert 6 == addNewStep1TableRow()
        moveRowUp 5
        moveRowUp 4
        moveRowUp 3
        assert 'pricing.items[2].type' == getStep1TableCell(2, 'type').findElement(By.tagName('select')).getAttribute('name')
        setStep1TableInputValue 2, 'quantity', '1'
        setStep1TableInputValue 2, 'unit', 'Einheit'
        setStep1TableInputValue 2, 'name', 'Material'
        setStep1TableInputValue 2, 'unitPrice', '10,00'
        assert '10,00' == getStep1TableRowTotal(2)
        assert '400,00' == getStep1TableRowTotal(3)

        setStep1TableInputValue 4, 'type', 'relativeToLastSum'
        assert '20,00' == getStep1TableRowTotal(4)
        WebElement td = getStep1TableCell(5, 'relative-to-pos')
        WebElement span = td.findElement(By.tagName('span'))
        WebElement icon = span.findElement(By.tagName('i'))
        WebElement strong = span.findElement(By.tagName('strong'))
        assert !span.displayed

        setStep1TableInputValue 5, 'type', 'relativeToPos'
        assert span.displayed
        assert '' == strong.text
        icon.click()
        checkStep1NonSelectableFinderRows 5
        span.sendKeys Keys.ESCAPE
        for (int i = 0; i <= 5; i++) {
            assert '' == getStep1TableRow(i).getAttribute('class')
        }
        assert '' == strong.text

        setStep1TableReference 5, 1, 5
        getStep1TableRow(1).findElement(By.className('remove-btn')).click()
        assert '4,50' == getStep1TableRowTotal(5)
        moveRowDown 1
        assert 'pricing.items[1].type' == getStep1TableCell(1, 'type').findElement(By.tagName('select')).getAttribute('name')
        assert '3' == strong.text
        assert '2' == getStep1TableInput(5, 'relToPos').getAttribute('value')
        assert '4,50' == getStep1TableRowTotal(5)
        moveRowUp 2
        assert 'pricing.items[2].type' == getStep1TableCell(2, 'type').findElement(By.tagName('select')).getAttribute('name')
        assert '2' == strong.text
        assert '1' == getStep1TableInput(5, 'relToPos').getAttribute('value')
        assert '4,50' == getStep1TableRowTotal(5)

        setStep1TableReference 5, 3, 5
        getStep1TableRow(3).findElement(By.className('remove-btn')).click()
        assert '20,00' == getStep1TableRowTotal(5)
        assert '440,00' == getStep1Total()
        assert '55,00' == getStep1UnitPrice()
        moveRowUp 5
        moveRowUp 4
        Alert alert = driver.switchTo().alert()
        assert 'Zeile kann nicht verschoben werden, da "relativ zu Pos."-Einträge immer hinter der referenzierten Zeile stehen müssen.' == alert.text
        alert.accept()
        moveRowDown 4
        for (int i = 0; i <= 5; i++) {
            if (i == 3) {
                checkDisabledTypes i, 'relativeToPos'
            } else {
                checkDisabledTypes i
            }
        }

        assert 7 == addNewStep1TableRow()
        setStep1TableInputValue 6, 'quantity', '1'
        setStep1TableInputValue 6, 'unit', 'Einheit'
        setStep1TableInputValue 6, 'name', 'Test'
        setStep1TableInputValue 6, 'unitPrice', '40,00'
        checkDisabledTypes 6
        setStep1TableReference 5, 6, 5
        checkDisabledTypes 6, 'relativeToPos', 'relativeToLastSum', 'relativeToCurrentSum', 'sum'
        moveRowUp 6
        checkDisabledTypes 5, 'relativeToPos'
        moveRowDown 5
        setStep1TableReference 5, 3, 5
        setStep1TableInputValue 6, 'type', 'relativeToPos'
        setStep1TableReference 5, 3, 5, 6
        assert 6 == removeRow(6)

        setInputValue 'pricing.discountPercent', '2'
        assert '8,80' == getValue('step2-discount-percent-amount')
        setInputValue 'pricing.adjustment', '-1,2'
        assert '8' == getValue('step2-total-quantity')
        assert 'Stunden' == getValue('step2-total-unit')
        assert '53,75' == getValue('step2-total-unit-price')
        assert '430,00' == getValue('step2-total')
        WebElement input = driver.findElement(By.id('step3-quantity'))
        input.clear()
        input.sendKeys '8'
        new Select(driver.findElement(By.id('step3-unit'))).selectByValue '304'
        assert '53,75' == getValue('step3-unit-price')
        assert '430,00' == getValue('step3-total-price')
        submitForm getUrl('/service/show/')

        assert 'Dienstleistung Druckvorstufe wurde geändert.' == flashMessage
        assert 'Druckvorstufe' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10200' == getShowFieldText(col, 1)
        assert 'Druckvorstufe' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '8' == getShowFieldText(col, 4)
        assert 'Stunden' == getShowFieldText(col, 5)
        assert '53,75 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Vorbereitung für den Druck und Übermittlung an die Druckerei.' == getShowFieldText(fieldSet, 1)

        fieldSet = getFieldset(dataSheet, 3)
        assert 'In der folgenden Tabelle berechne ich den Preis für 8 Stunden:' == fieldSet.findElement(By.xpath('./p')).text
        WebElement tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, '1.', '6', 'Stunden', 'Arbeitsleistung (Mustererstellung)', 'absoluter Betrag', '', '0,00', '50,00 €', '300,00 €'
        checkStaticRowValues tbody, 1, '2.', '2', 'Stunden', 'Arbeitsleistung (Präsentation)', 'absoluter Betrag', '', '0,00', '45,00 €', '90,00 €'
        checkStaticRowValues tbody, 2, '3.', '1', 'Einheit', 'Material', 'absoluter Betrag', '', '0,00', '10,00 €', '10,00 €'
        checkStaticRowValues tbody, 3, '4.', '', 'Zwischensumme', '', '400,00 €'
        checkStaticRowValues tbody, 4, '5.', '1', 'Einheit', 'Gewinn', 'relativ zur letzt. Zw.-summe', '', '5,00', '20,00 €', '20,00 €'
        checkStaticRowValues tbody, 5, '6.', '1', 'Einheit', 'Risiko', 'relativ zu Pos.', '4', '5,00', '20,00 €', '20,00 €'
        WebElement tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Gesamtpreis (8 Stunden)', '', '440,00 €'
        checkStaticRowValues tfoot, 1, 'Kalkulierter Einzelpreis (1 Stunden)', '', '55,00 €'
        fieldSet = getFieldset(dataSheet, 4)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Kalkulierter Gesamtwert', '8', 'Stunden', '', 'zu je', '55,00 €', '440,00 €'
        checkStaticRowValues tbody, 1, 'Rabatt %', '', '', '2,00', '', '', '8,80 €'
        checkStaticRowValues tbody, 2, 'Preisanpassung +/-', '', '', '', '', '', '-1,20 €'
        tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Verkaufspreis', '8', 'Stunden', '', 'zu je', '53,75 €', '430,00 €'
        fieldSet = getFieldset(dataSheet, 5)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Der Artikel wird verkauft als', '8', 'Stunden', 'zu je', '53,75 €', '430,00 €'

        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testEditServiceErrorsNoPricing() {
        clickListActionButton 0, 0, getUrl('/service/edit/')
        checkTitles 'Dienstleistung bearbeiten', 'Dienstleistungen', 'Mustervorschau'

        clearInput 'name'
        submitForm getUrl('/service/update')

        assert checkErrorFields(['name'])
        cancelForm getUrl('/service/list')

        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testEditServiceErrorsPricing() {
        clickListActionButton 0, 0, getUrl('/service/edit/')
        checkTitles 'Dienstleistung bearbeiten', 'Dienstleistungen', 'Mustervorschau'

        clearInput 'name'
        clearInput 'pricing.quantity'
        setInputValue 'pricing.unit.id', 'null'
        driver.findElement(By.id('step3-quantity')).clear()
        new Select(driver.findElement(By.id('step3-unit'))).selectByIndex(0)
        for (int i = 0; i < 4; i++) {
            removeRow 0
        }
        assert !getStep1TableRow(0).findElement(By.className('remove-btn')).displayed
        setStep1TableInputValue 0, 'unit', ''
        setStep1TableInputValue 0, 'name', ''
        submitForm getUrl('/service/update')

        assert checkErrorFields(['name', 'quantity'])
        WebElement fieldSet = driver.findElement(By.xpath('//fieldset[3]'))
        List<WebElement> errorMsgs = fieldSet.findElements(By.xpath('.//div[@id="step1-calculation-base"]//li[@class="error-msg"]'))
        assert 2 == errorMsgs.size()
        assert 'Muss größer als 0 sein.' == errorMsgs[0].text
        assert 'Feld darf nicht leer sein.' == errorMsgs[1].text
        errorMsgs = fieldSet.findElements(By.xpath(
            './div[1]/ul[@class="field-msgs"]/li[@class="error-msg"]'
        ))
        assert 2 == errorMsgs.size()
        assert 'Pos. 1, Bezeichnung: Feld darf nicht leer sein.' == errorMsgs[0].text
        assert 'Pos. 1, Einheit: Feld darf nicht leer sein.' == errorMsgs[1].text
        WebElement errorMsg = driver.findElement(By.xpath('//input[@id="step3-quantity"]/following-sibling::ul[@class="field-msgs"]/li[@class="error-msg"]'))
        assert 'Muss größer als 0 sein.' == errorMsg.text
        errorMsg = driver.findElement(By.xpath('//select[@id="step3-unit"]/following-sibling::ul[@class="field-msgs"]/li[@class="error-msg"]'))
        assert 'Feld darf nicht leer sein.' == errorMsg.text
        cancelForm getUrl('/service/list')

        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testEditServiceTransitionFromNoPricing() {
        clickListActionButton 0, 0, getUrl('/service/edit/')
        checkTitles 'Dienstleistung bearbeiten', 'Dienstleistungen', 'Mustervorschau'
        driver.findElement(By.id('start-pricing')).click()
        assert !getInput('quantity').displayed
        assert !getInput('unitPrice').displayed
        assert !getInput('unit.id').displayed
        setInputValue 'pricing.quantity', '1'
        setInputValue 'pricing.unit.id', '301'
        assert 1 == numStep1TableRows
        setStep1TableInputValue 0, 'quantity', '6'
        setStep1TableInputValue 0, 'unit', 'Stunden'
        setStep1TableInputValue 0, 'name', 'Arbeitsleistung (Mustererstellung)'
        setStep1TableInputValue 0, 'unitPrice', '50'
        assert '300,00' == getStep1TableRowTotal(0)
        assert '300,00' == step1Total
        assert '300,00' == step1UnitPrice
        assert '(1 Einheiten)' == driver.findElement(By.id('step1-total-price-quantity')).text
        assert '(1 Einheiten)' == driver.findElement(By.id('step1-unit-price-quantity')).text
        WebElement tr = getStep1TableRow(0)
        assert !tr.findElement(By.className('up-btn')).displayed
        assert !tr.findElement(By.className('down-btn')).displayed
        assert !tr.findElement(By.className('remove-btn')).displayed
        assert 2 == addNewStep1TableRow()
        setStep1TableInputValue 1, 'quantity', '2'
        setStep1TableInputValue 1, 'unit', 'Stunden'
        setStep1TableInputValue 1, 'name', 'Arbeitsleistung (Präsentation)'
        setStep1TableInputValue 1, 'unitPrice', '50'
        assert '100,00' == getStep1TableRowTotal(1)
        assert '400,00' == step1Total
        assert '400,00' == step1UnitPrice
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
        assert '400,00' == getStep1TableRowTotal(2)
        assert '400,00' == step1Total
        assert '400,00' == step1UnitPrice
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
        assert '20,00' == getStep1TableRowTotal(3)
        assert '420,00' == step1Total
        assert '420,00' == step1UnitPrice
        assert 5 == addNewStep1TableRow()
        setStep1TableInputValue 4, 'type', 'relativeToLastSum'
        setStep1TableInputValue 4, 'quantity', '1'
        setStep1TableInputValue 4, 'unit', 'Einheit'
        setStep1TableInputValue 4, 'name', 'Risiko'
        setStep1TableInputValue 4, 'unitPercent', '5'
        assert '20,00' == getStep1TableRowTotal(4)
        assert '440,00' == step1Total
        assert '440,00' == step1UnitPrice
        assert '1' == getValue('step2-quantity')
        assert 'Einheiten' == getValue('step2-unit')
        assert '440,00' == getValue('step2-unit-price')
        assert '440,00' == getValue('step2-total-price')
        assert '1' == getValue('step2-total-quantity')
        assert 'Einheiten' == getValue('step2-total-unit')
        assert '440,00' == getValue('step2-total-unit-price')
        assert '440,00' == getValue('step2-total')
        setInputValue 'pricing.discountPercent', '1'
        assert '4,40' == getValue('step2-discount-percent-amount')
        setInputValue 'pricing.adjustment', '-0,7'
        assert '1' == getValue('step2-total-quantity')
        assert 'Einheiten' == getValue('step2-total-unit')
        assert '434,90' == getValue('step2-total-unit-price')
        assert '434,90' == getValue('step2-total')
        assert '434,90' == getValue('step3-unit-price')
        assert '434,90' == getValue('step3-total-price')
        WebElement input = driver.findElement(By.id('step3-quantity'))
        input.clear()
        input.sendKeys '2'
        assert '434,90' == getValue('step3-unit-price')
        assert '869,80' == getValue('step3-total-price')
        def select = new Select(driver.findElement(By.id('step3-unit')))
        select.selectByValue '300'
        assert '217,45' == getValue('step3-unit-price')
        assert '434,90' == getValue('step3-total-price')
        input.clear()
        input.sendKeys '1'
        select.selectByValue '301'
        assert '434,90' == getValue('step3-unit-price')
        assert '434,90' == getValue('step3-total-price')
        submitForm getUrl('/service/show/')

        assert 'Dienstleistung Mustervorschau wurde geändert.' == flashMessage
        assert 'Mustervorschau' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10000' == getShowFieldText(col, 1)
        assert 'Mustervorschau' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '1' == getShowFieldText(col, 4)
        assert 'Einheiten' == getShowFieldText(col, 5)
        assert '434,90 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getShowFieldText(fieldSet, 1)
        fieldSet = getFieldset(dataSheet, 3)
        assert 'In der folgenden Tabelle berechne ich den Preis für 1 Einheiten:' == fieldSet.findElement(By.xpath('./p')).text
        WebElement tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, '1.', '6', 'Stunden', 'Arbeitsleistung (Mustererstellung)', 'absoluter Betrag', '', '0,00', '50,00 €', '300,00 €'
        checkStaticRowValues tbody, 1, '2.', '2', 'Stunden', 'Arbeitsleistung (Präsentation)', 'absoluter Betrag', '', '0,00', '50,00 €', '100,00 €'
        checkStaticRowValues tbody, 2, '3.', '', 'Zwischensumme', '', '400,00 €'
        checkStaticRowValues tbody, 3, '4.', '1', 'Einheit', 'Gewinn', 'relativ zur akt. Summe', '', '5,00', '20,00 €', '20,00 €'
        checkStaticRowValues tbody, 4, '5.', '1', 'Einheit', 'Risiko', 'relativ zur letzt. Zw.-summe', '', '5,00', '20,00 €', '20,00 €'
        WebElement tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Gesamtpreis (1 Einheiten)', '', '440,00 €'
        checkStaticRowValues tfoot, 1, 'Kalkulierter Einzelpreis (1 Einheiten)', '', '440,00 €'
        fieldSet = getFieldset(dataSheet, 4)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Kalkulierter Gesamtwert', '1', 'Einheiten', '', 'zu je', '440,00 €', '440,00 €'
        checkStaticRowValues tbody, 1, 'Rabatt %', '', '', '1,00', '', '', '4,40 €'
        checkStaticRowValues tbody, 2, 'Preisanpassung +/-', '', '', '', '', '', '-0,70 €'
        tfoot = fieldSet.findElement(By.tagName('tfoot'))
        checkStaticRowValues tfoot, 0, 'Verkaufspreis', '1', 'Einheiten', '', 'zu je', '434,90 €', '434,90 €'
        fieldSet = getFieldset(dataSheet, 5)
        tbody = fieldSet.findElement(By.tagName('tbody'))
        checkStaticRowValues tbody, 0, 'Der Artikel wird verkauft als', '1', 'Einheiten', 'zu je', '434,90 €', '434,90 €'
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testEditServiceTransitionFromPricing() {
        clickListActionButton 0, 0, getUrl('/service/edit/')
        checkTitles 'Dienstleistung bearbeiten', 'Dienstleistungen', 'Mustervorschau'
        driver.findElement(By.id('remove-pricing')).click()
        driver.switchTo().alert().dismiss()
        assert !driver.findElement(By.id('start-pricing')).displayed
        assert !getInput('quantity').displayed
        assert !getInput('unitPrice').displayed
        assert !getInput('unit.id').displayed
        assert step1Table.displayed

        driver.findElement(By.id('remove-pricing')).click()
        driver.switchTo().alert().accept()
        assert driver.findElement(By.id('start-pricing')).displayed
        WebElement input = getInput('quantity')
        assert input.displayed
        assert '1' == input.getAttribute('value')
        input = getInput('unit.id')
        assert input.displayed
        assert '301' == new Select(input).firstSelectedOption.getAttribute('value')
        input = getInput('unitPrice')
        assert input.displayed
        assert '434,90' == input.getAttribute('value')
        submitForm getUrl('/service/show/')

        assert 'Dienstleistung Mustervorschau wurde geändert.' == flashMessage
        assert 'Mustervorschau' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'S-10000' == getShowFieldText(col, 1)
        assert 'Mustervorschau' == getShowFieldText(col, 2)
        assert 'Grafik und Design' == getShowFieldText(col, 3)
        assert '1' == getShowFieldText(col, 4)
        assert 'Einheiten' == getShowFieldText(col, 5)
        assert '434,90 €' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '19 %' == getShowFieldText(col, 1)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Anfertigung eines Musters nach Kundenvorgaben.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Service.count()
    }

    @Test
    void testEditServiceNoActionTransitionFromPricing() {
        clickListActionButton 0, 0, getUrl('/service/edit/')
        checkTitles 'Dienstleistung bearbeiten', 'Dienstleistungen', 'Mustervorschau'
        driver.findElement(By.id('remove-pricing')).click()
        driver.switchTo().alert().accept()
        assert driver.findElement(By.id('start-pricing')).displayed
        WebElement input = getInput('quantity')
        assert input.displayed
        assert '1' == input.getAttribute('value')
        input = getInput('unit.id')
        assert input.displayed
        assert '301' == new Select(input).firstSelectedOption.getAttribute('value')
        input = getInput('unitPrice')
        assert input.displayed
        assert '434,90' == input.getAttribute('value')
        cancelForm getUrl('/service/list')

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        assert '434,90 €' == tr.findElement(By.xpath('td[7]')).text
        tr.findElement(By.xpath('td[8]/a[1]')).click()
        assert 5 == numStep1TableRows
        assert driver.findElement(By.id('remove-pricing')).displayed
        driver.quit()

        assert 1 == Service.count()
        assert 1 == SalesItemPricing.count()
        assert 5 == SalesItemPricingItem.count()
    }

    @Test
    void testDeleteServiceActionNoPricing() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/service/list'))
        assert 'Dienstleistung wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Service.count()
        assert 0 == SalesItemPricing.count()
        assert 0 == SalesItemPricingItem.count()
    }

    @Test
    void testDeleteServiceActionPricing() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/service/list'))
        assert 'Dienstleistung wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Service.count()
        assert 0 == SalesItemPricing.count()
        assert 0 == SalesItemPricingItem.count()
    }

    @Test
    void testDeleteServiceNoActionNoPricing() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/service/list') == driver.currentUrl
        driver.quit()

        assert 1 == Service.count()
        assert 0 == SalesItemPricing.count()
        assert 0 == SalesItemPricingItem.count()
    }

    @Test
    void testDeleteServiceNoActionPricing() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/service/list') == driver.currentUrl
        driver.quit()

        assert 1 == Service.count()
        assert 1 == SalesItemPricing.count()
        assert 5 == SalesItemPricingItem.count()
    }


    //-- Non-public methods ---------------------

    protected Service prepareServiceWithoutPricing() {
        def service = new Service(
            name: 'Mustervorschau',
            quantity: 1.0d,
            unit: Unit.get(301),
            unitPrice: 450.0d,
            taxRate: TaxRate.get(400),
            description: 'Anfertigung eines Musters nach Kundenvorgaben.',
            category: ServiceCategory.get(2007)
        )
        service.save flush: true
        service
    }

    protected Service prepareServiceWithPricing() {
        def pricing = new SalesItemPricing(
            quantity: 1.0d,
            unit: Unit.get(301),
            discountPercent: 1.0d,
            adjustment: -0.7d
        )
        pricing.addToItems(new SalesItemPricingItem(
                quantity: 6.0d,
                unit: 'Stunden',
                name: 'Arbeitsleistung (Mustererstellung)',
                type: PricingItemType.absolute,
                unitPrice: 50.0d
            )).addToItems(new SalesItemPricingItem(
                quantity: 2.0d,
                unit: 'Stunden',
                name: 'Arbeitsleistung (Präsentation)',
                type: PricingItemType.absolute,
                unitPrice: 50.0d
            )).addToItems(new SalesItemPricingItem(
                type: PricingItemType.sum,
            )).addToItems(new SalesItemPricingItem(
                quantity: 1.0d,
                unit: 'Einheit',
                name: 'Gewinn',
                type: PricingItemType.relativeToCurrentSum,
                unitPercent: 5.0d
            )).addToItems(new SalesItemPricingItem(
                quantity: 1.0d,
                unit: 'Einheit',
                name: 'Risiko',
                type: PricingItemType.relativeToLastSum,
                unitPercent: 5.0d
            ))
        pricing.save()
        def service = new Service(
            name: 'Mustervorschau',
            quantity: 1.0d,
            unit: Unit.get(301),
            taxRate: TaxRate.get(400),
            description: 'Anfertigung eines Musters nach Kundenvorgaben.',
            category: ServiceCategory.get(2007),
            pricing: pricing
        )
        service.save flush: true
        service
    }
}
