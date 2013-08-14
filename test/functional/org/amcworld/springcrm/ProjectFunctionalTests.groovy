/*
 * ProjectFunctionalTests.groovy
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

import grails.util.GrailsNameUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


/**
 * The class {@code ProjectFunctionalTests} represents a functional test case
 * for the project section of SpringCRM.
 *
 * @author	Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
class ProjectFunctionalTests extends InvoicingTransactionTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        if (!name.methodName.startsWith('testCreate')) {
            def data = [: ]
            data.phoneCall = prepareCall org, p
            data.note = prepareNote org, p
            data.quote = prepareQuote(org, p)
            data.salesOrder = prepareSalesOrder(org, p, data.quote)
            data.invoice = prepareInvoice(org, p, data.quote, data.salesOrder)
            if (name.methodName == 'testSelectProjectItems') {
                data.clear()
            }
            prepareProject org, p, data
        }

        driver.manage().window().maximize()
        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open '/project/list'
    }

    @Test
    void testCreateProjectSuccess() {
        clickToolbarButton 0, getUrl('/project/create')
        checkTitles 'Projekt anlegen', 'Projekte', 'Neues Projekt'
        setInputValue 'title', 'Marketing-Aktion Frühjahr'
        setInputValue 'phase', 'quote'
        setInputValue 'status.id', '2600'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        setInputValue 'description', 'Marketing-Aktion zum Frühjahr für die Landschaftsbau Duvensee GbR.'
        submitForm getUrl('/project/show/')

        assert 'Projekt Marketing-Aktion Frühjahr wurde angelegt.' == flashMessage
        assert 'Marketing-Aktion Frühjahr' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'J-10000' == getShowFieldText(col, 1)
        assert 'Marketing-Aktion Frühjahr' == getShowFieldText(col, 2)
        assert 'Angebot' == getShowFieldText(col, 3)
        assert '2600' == getInputValue('project-status')
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Marketing-Aktion zum Frühjahr für die Landschaftsbau Duvensee GbR.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Project.count()
        assert 0 == ProjectItem.count()
    }

    @Test
    void testCreateProjectErrors() {
        clickToolbarButton 0, getUrl('/project/create')
        checkTitles 'Projekt anlegen', 'Projekte', 'Neues Projekt'
        submitForm getUrl('/project/save')

        assert checkErrorFields(['title', 'organization.id'])
        cancelForm getUrl('/project/list')
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Projekt anlegen' == link.text
        assert getUrl('/project/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Project.count()
        assert 0 == ProjectItem.count()
    }

    @Test
    void testShowProject() {
        int id = clickListItem 0, 1, '/project/show'
        checkTitles 'Projekt anzeigen', 'Projekte', 'Marketing-Aktion Frühjahr'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'J-10000' == getShowFieldText(col, 1)
        assert 'Marketing-Aktion Frühjahr' == getShowFieldText(col, 2)
        assert 'Angebot' == getShowFieldText(col, 3)
        assert '2600' == getInputValue('project-status')
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Marketing-Aktion zum Frühjahr für die Landschaftsbau Duvensee GbR.' == getShowFieldText(fieldSet, 1)

        List<WebElement> phasesList = phases.findElements(By.tagName('section'))
        def phasesSet = EnumSet.allOf(ProjectPhase)
        assert phasesSet.size() + 1 == phasesList.size()
        int i = 0
        for (ProjectPhase p : phasesSet) {
            WebElement phaseEl = phasesList[i]
            assert p.toString() == phaseEl.getAttribute('data-phase')
            if (p == ProjectPhase.quote) {
                assert 'current' == phaseEl.getAttribute('class')
            }
            assert "project-phase-${p}" == phaseEl.findElement(By.tagName('h4')).getAttribute('id')
            assert 2 == phaseEl.findElements(By.xpath('.//ul[@class="project-phase-actions"]/li')).size()
            i++
        }

        checkProjectItems 0, [
            [
                url: '/call/show/', icon: 'phone',
                label: 'Bitte um Angebot', editUrl: '/call/edit'
            ],
            [
                url: '/note/show/', icon: 'pencil',
                label: 'Besprechung vom 21.01.2013', editUrl: '/note/edit'
            ]
        ]
        checkProjectItems 1, []
        checkProjectItems 2, [
            [
                url: '/quote/show/', icon: 'dollar',
                label: 'Werbekampagne Frühjahr 2013', editUrl: '/quote/edit'
            ]
        ]
        checkProjectItems 3, []
        checkProjectItems 4, [
            [
                url: '/sales-order/show/', icon: 'list',
                label: 'Werbekampagne Frühjahr 2013',
                editUrl: '/sales-order/edit'
            ]
        ]
        checkProjectItems 5, []
        checkProjectItems 6, []
        checkProjectItems 7, [
            [
                url: '/invoice/show/', icon: 'euro',
                label: 'Werbekampagne Frühjahr 2013', editUrl: '/invoice/edit'
            ]
        ]
        checkProjectItems 8, []
        checkProjectItems 9, []

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'project', id
        driver.quit()

        assert 1 == Project.count()
        assert 5 == ProjectItem.count()
    }

    @Test
    void testAddProjectItems() {
        clickListItem 0, 1, '/project/show'
        checkTitles 'Projekt anzeigen', 'Projekte', 'Marketing-Aktion Frühjahr'
        assert 1 == getProjectItems(2).size()
        clickPhaseActionButton 2, 0
        WebDriverWait wait = new WebDriverWait(driver, 10)
        By dlgBy = By.id('create-project-item-dialog')
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(dlgBy))
        dialog.findElement(By.xpath('./preceding-sibling::div/a')).click()
        wait.until(ExpectedConditions.invisibilityOfElementLocated(dlgBy))

        createProjectItem 2, 7, '/call/create'
        checkTitles 'Anruf anlegen', 'Anrufe', 'Neuer Anruf'
        assert 'Landschaftsbau Duvensee GbR' == getAutocompleteExValue('organization')
        assert 'Henry Brackmann' == getAutocompleteExValue('person')
        setInputValue 'subject', 'Mitteilung von Änderungswünschen'
        setInputValue 'start_date', '16.02.2013'
        setInputValue 'start_time', '14:30'
        setInputValue 'status', 'completed'
        submitForm getUrl('/project/show/')

        checkCurrentProjectPhase 2
        checkProjectItems 2, [
            [
                url: '/call/show/', icon: 'phone',
                label: 'Mitteilung von Änderungswünschen'
            ],
            [
                url: '/quote/show/', icon: 'dollar',
                label: 'Werbekampagne Frühjahr 2013'
            ]
        ]
        assert 2 == Call.count()
        assert 0 == CreditMemo.count()
        assert 0 == Dunning.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()

        createProjectItem 2, 0, '/quote/create'
        checkTitles 'Angebot anlegen', 'Angebote', 'Neues Angebot'
        setInputValue 'subject', 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == getAutocompleteExValue('organization')
        assert 'Henry Brackmann' == getAutocompleteExValue('person')
        setInputValue 'stage.id', '602'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '25.02.2013'
        setInputValue 'validUntil_date', '25.03.2013'
        setInputValue 'shippingDate_date', '26.02.2013'
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
        setInputValue 'headerText', 'für Änderungswünsche zur geplanten Werbekampange "Frühjahr 2013" möchten wir Ihnen gern folgendes Angebot unterbreiten.'
        setPriceTableInputValue 0, 'number', 'S-10000'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption der geplanten Werbekampagne'
        setPriceTableInputValue 0, 'unitPrice', '440'
        setPriceTableInputValue 0, 'tax', '19'
        submitForm getUrl('/project/show/')

        checkCurrentProjectPhase 2
        checkProjectItems 2, [
            [
                url: '/call/show/', icon: 'phone',
                label: 'Mitteilung von Änderungswünschen'
            ],
            [
                url: '/quote/show/', icon: 'dollar',
                label: 'Werbekampagne Frühjahr 2013'
            ],
            [
                url: '/quote/show/', icon: 'dollar',
                label: 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
            ]
        ]
        assert 2 == Call.count()
        assert 0 == CreditMemo.count()
        assert 0 == Dunning.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 2 == Quote.count()
        assert 1 == SalesOrder.count()

        createProjectItem 3, 8, '/note/create'
        checkTitles 'Notiz anlegen', 'Notizen', 'Neue Notiz'
        setInputValue 'title', 'Auftragserteilung Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == getAutocompleteExValue('organization')
        assert 'Henry Brackmann' == getAutocompleteExValue('person')
        def iframeDriver = driver.switchTo().frame('note-content_ifr')
        def rte = iframeDriver.findElement(By.xpath('//body'))
        rte.sendKeys 'Herr Brackmann erteilte uns am 20.03.2013 den Auftrag für die Werbekampange Frühjahr 2013 einschl. der Änderungswünsche.'
        driver.switchTo().defaultContent()
        submitForm getUrl('/project/show/')

        checkCurrentProjectPhase 3
        checkProjectItems 3, [
            [
                url: '/note/show/', icon: 'pencil',
                label: 'Auftragserteilung Werbekampagne Frühjahr 2013'
            ]
        ]
        assert 2 == Call.count()
        assert 0 == CreditMemo.count()
        assert 0 == Dunning.count()
        assert 1 == Invoice.count()
        assert 2 == Note.count()
        assert 2 == Quote.count()
        assert 1 == SalesOrder.count()

        createProjectItem 4, 1, '/sales-order/create'
        checkTitles 'Verkaufsbestellung anlegen', 'Verkaufsbestellungen', 'Neue Verkaufsbestellung'
        setInputValue 'subject', 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == getAutocompleteExValue('organization')
        assert 'Henry Brackmann' == getAutocompleteExValue('person')
        assert 'A-10001-10000 Änderungswünsche zur Werbekampagne Frühjahr 2013' == selectAutocompleteEx('quote', 'Änderungs')
        setInputValue 'stage.id', '802'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '5.3.2013'
        setInputValue 'dueDate_date', '29.3.2013'
        setInputValue 'shippingDate_date', '6.3.2013'
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
        setInputValue 'headerText', 'vielen Dank für Ihren Auftrag zu den Änderungswünschen zur Werbekampange "Frühjahr 2013".'
        setPriceTableInputValue 0, 'number', 'S-10000'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption der geplanten Werbekampagne'
        setPriceTableInputValue 0, 'unitPrice', '440'
        setPriceTableInputValue 0, 'tax', '19'
        submitForm getUrl('/project/show/')

        checkCurrentProjectPhase 4
        checkProjectItems 4, [
            [
                url: '/sales-order/show/', icon: 'list',
                label: 'Werbekampagne Frühjahr 2013'
            ],
            [
                url: '/sales-order/show/', icon: 'list',
                label: 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
            ]
        ]
        assert 2 == Call.count()
        assert 0 == CreditMemo.count()
        assert 0 == Dunning.count()
        assert 1 == Invoice.count()
        assert 2 == Note.count()
        assert 2 == Quote.count()
        assert 2 == SalesOrder.count()

        // TODO test creation of calendar event in phase 5 (zero-based)

        createProjectItem 7, 2, '/invoice/create'
        checkTitles 'Rechnung anlegen', 'Rechnungen', 'Neue Rechnung'
        setInputValue 'subject', 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == getAutocompleteExValue('organization')
        assert 'Henry Brackmann' == getAutocompleteExValue('person')
        assert 'A-10001-10000 Änderungswünsche zur Werbekampagne Frühjahr 2013' == selectAutocompleteEx('quote', 'Werbe')
        assert 'B-10001-10000 Änderungswünsche zur Werbekampagne Frühjahr 2013' == selectAutocompleteEx('salesOrder', 'Werbe')
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
        setInputValue('headerText', '''für durchgeführte Änderungen an der  Werbekampange "Frühjahr 2013" erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.
Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis.''')
        setPriceTableInputValue 0, 'number', 'S-10000'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption der geplanten Werbekampagne'
        setPriceTableInputValue 0, 'unitPrice', '440'
        setPriceTableInputValue 0, 'tax', '19'
        submitForm getUrl('/project/show/')

        checkCurrentProjectPhase 7
        checkProjectItems 7, [
            [
                url: '/invoice/show/', icon: 'euro',
                label: 'Werbekampagne Frühjahr 2013'
            ],
            [
                url: '/invoice/show/', icon: 'euro',
                label: 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
            ]
        ]
        assert 2 == Call.count()
        assert 0 == CreditMemo.count()
        assert 0 == Dunning.count()
        assert 2 == Invoice.count()
        assert 2 == Note.count()
        assert 2 == Quote.count()
        assert 2 == SalesOrder.count()

        createProjectItem 7, 3, '/credit-memo/create'
        checkTitles 'Gutschrift anlegen', 'Gutschriften', 'Neue Gutschrift'
        setInputValue 'subject', 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == getAutocompleteExValue('organization')
        assert 'Henry Brackmann' == getAutocompleteExValue('person')
        assert 'R-10001-10000 Änderungswünsche zur Werbekampagne Frühjahr 2013' == selectAutocompleteEx('invoice', 'Werbe')
        setInputValue 'stage.id', '2502'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '8.4.2013'
        setInputValue 'shippingDate_date', '9.4.2013'
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
        setInputValue 'headerText', 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zu den Änderungswünschen zur Werbekampagne "Frühjahr 2013" gut.'
        setPriceTableInputValue 0, 'number', 'S-10000'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Konzeption und Planung'
        setPriceTableInputValue 0, 'description', 'Konzeption der geplanten Werbekampagne'
        setPriceTableInputValue 0, 'unitPrice', '440'
        setPriceTableInputValue 0, 'tax', '19'
        submitForm getUrl('/project/show/')

        checkCurrentProjectPhase 7
        checkProjectItems 7, [
            [
                url: '/credit-memo/show/', icon: 'money',
                label: 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
            ],
            [
                url: '/invoice/show/', icon: 'euro',
                label: 'Werbekampagne Frühjahr 2013'
            ],
            [
                url: '/invoice/show/', icon: 'euro',
                label: 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
            ]
        ]
        assert 2 == Call.count()
        assert 1 == CreditMemo.count()
        assert 0 == Dunning.count()
        assert 2 == Invoice.count()
        assert 2 == Note.count()
        assert 2 == Quote.count()
        assert 2 == SalesOrder.count()

        // TODO test creation of purchase invoice in phase 7 (zero-based)

        createProjectItem 8, 4, '/dunning/create'
        checkTitles 'Mahnung anlegen', 'Mahnungen', 'Neue Mahnung'
        setInputValue 'subject', 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
        assert 'Landschaftsbau Duvensee GbR' == getAutocompleteExValue('organization')
        assert 'Henry Brackmann' == getAutocompleteExValue('person')
        assert 'R-10001-10000 Änderungswünsche zur Werbekampagne Frühjahr 2013' == selectAutocompleteEx('invoice', 'Werbe')
        setInputValue 'stage.id', '2202'
        checkDate 'shippingDate_date'
        setInputValue 'docDate_date', '6.5.2013'
        setInputValue 'dueDatePayment_date', '13.5.2013'
        setInputValue 'shippingDate_date', '7.5.2013'
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
        setInputValue 'headerText', 'zur angegebenen Rechnung konnte bis heute kein Zahlungseingang verzeichnet werden.'
        setPriceTableInputValue 0, 'number', 'S-99000'
        setPriceTableInputValue 0, 'quantity', '1'
        assert 'Einheiten' == selectAutocompleteEx('items[0].unit', 'Einh')
        setPriceTableInputValue 0, 'name', 'Mahngebühren'
        setPriceTableInputValue 0, 'unitPrice', '5'
        setPriceTableInputValue 0, 'tax', '19'
        submitForm getUrl('/project/show/')

        checkCurrentProjectPhase 8
        checkProjectItems 8, [
            [
                url: '/dunning/show/', icon: 'suitcase',
                label: 'Änderungswünsche zur Werbekampagne Frühjahr 2013'
            ]
        ]
        assert 2 == Call.count()
        assert 1 == CreditMemo.count()
        assert 1 == Dunning.count()
        assert 2 == Invoice.count()
        assert 2 == Note.count()
        assert 2 == Quote.count()
        assert 2 == SalesOrder.count()

        driver.quit()

        assert 1 == Project.count()
        assert 12 == ProjectItem.count()
    }

    @Test
    void testSelectProjectItems() {
        clickListItem 0, 1, '/project/show'
        checkTitles 'Projekt anzeigen', 'Projekte', 'Marketing-Aktion Frühjahr'

        selectProjectItem 0, 'call'
        checkCurrentProjectPhase 0
        checkProjectItems 0, [
            [
                url: '/call/show/', icon: 'phone',
                label: 'Bitte um Angebot'
            ]
        ]

        selectProjectItem 0, 'note'
        checkCurrentProjectPhase 0
        checkProjectItems 0, [
            [
                url: '/call/show/', icon: 'phone',
                label: 'Bitte um Angebot'
            ],
            [
                url: '/note/show/', icon: 'pencil',
                label: 'Besprechung vom 21.01.2013'
            ]
        ]

        selectProjectItem 2, 'quote'
        checkCurrentProjectPhase 2
        checkProjectItems 2, [
            [
                url: '/quote/show/', icon: 'dollar',
                label: 'Werbekampagne Frühjahr 2013'
            ]
        ]

        selectProjectItem 4, 'sales-order'
        checkCurrentProjectPhase 4
        checkProjectItems 4, [
            [
                url: '/sales-order/show/', icon: 'list',
                label: 'Werbekampagne Frühjahr 2013'
            ]
        ]

        selectProjectItem 7, 'invoice'
        checkCurrentProjectPhase 7
        checkProjectItems 7, [
            [
                url: '/invoice/show/', icon: 'euro',
                label: 'Werbekampagne Frühjahr 2013'
            ]
        ]

        driver.quit()

        assert 1 == Project.count()
        assert 5 == ProjectItem.count()
    }

    @Test
    void testChangeProjectStatus() {
        clickListItem 0, 1, '/project/show'
        checkTitles 'Projekt anzeigen', 'Projekte', 'Marketing-Aktion Frühjahr'
        WebDriverWait wait = new WebDriverWait(driver, 10)
        wait.until(ExpectedConditions.elementToBeClickable(By.id('project-statusSelectBoxIt'))).click()
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath('//ul[@id="project-statusSelectBoxItOptions"]/li[3]/a'))).click()
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/project/list') == driver.currentUrl
        WebElement td = driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[7]'))
        assert td.getAttribute('class').contains('project-status-2602')
        assert 'wartet auf Kunden' == td.text

        clickListItem 0, 1, '/project/show'
        checkTitles 'Projekt anzeigen', 'Projekte', 'Marketing-Aktion Frühjahr'
        wait = new WebDriverWait(driver, 10)
        wait.until(ExpectedConditions.elementToBeClickable(By.id('project-statusSelectBoxIt'))).click()
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath('//ul[@id="project-statusSelectBoxItOptions"]/li[2]/a'))).click()
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/project/list') == driver.currentUrl
        td = driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[7]'))
        assert td.getAttribute('class').contains('project-status-2601')
        assert 'pausiert' == td.text

        clickListItem 0, 1, '/project/show'
        checkTitles 'Projekt anzeigen', 'Projekte', 'Marketing-Aktion Frühjahr'
        wait = new WebDriverWait(driver, 10)
        wait.until(ExpectedConditions.elementToBeClickable(By.id('project-statusSelectBoxIt'))).click()
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath('//ul[@id="project-statusSelectBoxItOptions"]/li[5]/a'))).click()
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/project/list') == driver.currentUrl
        td = driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[7]'))
        assert td.getAttribute('class').contains('project-status-2604')
        assert 'abgeschlossen' == td.text
        driver.quit()

        assert 1 == Project.count()
        assert 5 == ProjectItem.count()
    }

    @Test
    void testListProjects() {
        checkTitles 'Projekte', 'Projekte'
        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('number')
        WebElement link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/project/show/'))
        assert 'J-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/project/show/'))
        assert 'Marketing-Aktion Frühjahr' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('string')
        assert 'Angebot' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('status')
        assert td.getAttribute('class').contains('project-status-2600')
        assert 'in Bearbeitung' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/project/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/project/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/project/list') == driver.currentUrl
        driver.quit()

        assert 1 == Project.count()
        assert 5 == ProjectItem.count()
    }

    @Test
    void testEditProjectSuccess() {
        clickListActionButton 0, 0, getUrl('/project/edit/')
        checkTitles 'Projekt bearbeiten', 'Projekte', 'Marketing-Aktion Frühjahr'
        def col = driver.findElement(By.xpath('//form[@id="project-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert 'Marketing-Aktion Frühjahr' == getInputValue('title')
        assert 'quote' == getInputValue('phase')
        assert '2600' == getInputValue('status.id')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert 'Marketing-Aktion zum Frühjahr für die Landschaftsbau Duvensee GbR.' == getInputValue('description')

        setInputValue 'title', 'Marketing-Aktion spätes Frühjahr'
        setInputValue 'phase', 'ordering'
        setInputValue 'status.id', '2601'
        setInputValue 'description', 'Marketing-Aktion zum späten Frühjahr für die Landschaftsbau Duvensee GbR.'
        submitForm getUrl('/project/show/')

        assert 'Projekt Marketing-Aktion spätes Frühjahr wurde geändert.' == flashMessage
        assert 'Marketing-Aktion spätes Frühjahr' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Marketing-Aktion spätes Frühjahr' == getShowFieldText(col, 2)
        assert 'Auftragserteilung' == getShowFieldText(col, 3)
        assert '2601' == getInputValue('project-status')
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Marketing-Aktion zum späten Frühjahr für die Landschaftsbau Duvensee GbR.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Project.count()
        assert 5 == ProjectItem.count()
    }

    @Test
    void testEditProjectErrors() {
        clickListActionButton 0, 0, getUrl('/project/edit/')
        checkTitles 'Projekt bearbeiten', 'Projekte', 'Marketing-Aktion Frühjahr'
        clearInput 'title'
        driver.findElement(By.id('organization')).clear()
        submitForm getUrl('/project/update')

        assert checkErrorFields(['title', 'organization.id'])
        cancelForm getUrl('/project/list')
        driver.quit()

        assert 1 == Project.count()
        assert 5 == ProjectItem.count()
    }

    @Test
    void testDeleteProjectItem() {
        clickListItem 0, 1, '/project/show'
        checkTitles 'Projekt anzeigen', 'Projekte', 'Marketing-Aktion Frühjahr'
        WebDriverWait wait = new WebDriverWait(driver, 10)

        List<WebElement> items = getProjectItems(0)
        assert 2 == items.size()
        items[0].findElement(By.xpath('.//span[@class="item-actions"]/a[2]')).click()
        driver.switchTo().alert().dismiss()
        items = getProjectItems(0)
        assert 2 == items.size()

        items[0].findElement(By.xpath('.//span[@class="item-actions"]/a[2]')).click()
        driver.switchTo().alert().accept()
        wait.until ExpectedConditions.stalenessOf(items[0])
        items = getProjectItems(0)
        assert 1 == items.size()
        assert 4 == ProjectItem.count()
        assert 1 == Call.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()

        items[0].findElement(By.xpath('.//span[@class="item-actions"]/a[2]')).click()
        driver.switchTo().alert().accept()
        wait.until ExpectedConditions.stalenessOf(items[0])
        items = getProjectItems(0)
        assert 0 == items.size()
        assert 3 == ProjectItem.count()
        assert 1 == Call.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()
        checkCurrentProjectPhase 2

        items = getProjectItems(2)
        assert 1 == items.size()
        items[0].findElement(By.xpath('.//span[@class="item-actions"]/a[2]')).click()
        driver.switchTo().alert().accept()
        wait.until ExpectedConditions.stalenessOf(items[0])
        items = getProjectItems(2)
        assert 0 == items.size()
        assert 2 == ProjectItem.count()
        assert 1 == Call.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()
        checkCurrentProjectPhase 2

        items = getProjectItems(4)
        assert 1 == items.size()
        items[0].findElement(By.xpath('.//span[@class="item-actions"]/a[2]')).click()
        driver.switchTo().alert().accept()
        wait.until ExpectedConditions.stalenessOf(items[0])
        items = getProjectItems(4)
        assert 0 == items.size()
        assert 1 == ProjectItem.count()
        assert 1 == Call.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()
        checkCurrentProjectPhase 2

        items = getProjectItems(7)
        assert 1 == items.size()
        items[0].findElement(By.xpath('.//span[@class="item-actions"]/a[2]')).click()
        driver.switchTo().alert().accept()
        wait.until ExpectedConditions.stalenessOf(items[0])
        items = getProjectItems(7)
        assert 0 == items.size()
        assert 0 == ProjectItem.count()
        assert 1 == Call.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()
        checkCurrentProjectPhase 2

        driver.quit()
    }

    @Test
    void testDeleteProjectAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/project/list'))
        assert 'Projekt wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Project.count()
        assert 0 == ProjectItem.count()
        assert 1 == Call.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()
    }

    @Test
    void testDeleteProjectNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/project/list') == driver.currentUrl
        driver.quit()

        assert 1 == Project.count()
        assert 5 == ProjectItem.count()
        assert 1 == Call.count()
        assert 1 == Invoice.count()
        assert 1 == Note.count()
        assert 1 == Quote.count()
        assert 1 == SalesOrder.count()
    }


    //-- Non-public methods ---------------------

    /**
     * Checks whether the given project phase is marked as the current phase.
     *
     * @param sectionIdx    the zero-based index of the project phase section
     *                      which should be checked
     */
    protected void checkCurrentProjectPhase(int sectionIdx) {
        assert 'current' == getPhase(sectionIdx).getAttribute('class')
    }

    /**
     * Checks for the given project items in the section with the stated index.
     *
     * @param sectionIdx    the zero-based index of the project phase section
     *                      which should be checked
     * @param expectedItems a list of expected project items; each entry in the
     *                      list must be a map containing the following keys:
     *                      <ul>
     *                        <li><b>url</b>. The expected URL of the link when
     *                        clicking the project item.</li>
     *                        <li><b>icon</b>. The expected icon of the
     *                        link.</li>
     *                        <li><b>label</b>. The label (text) of the link.
     *                        </li>
     *                        <li><b>editUrl</b>. The expected URL of the link
     *                        to edit the project item (optional).</li>
     *                      </ul>
     */
    protected void checkProjectItems(int sectionIdx,
                                     List<Map<String, String>> expectedItems)
    {
        List<WebElement> items = getProjectItems(sectionIdx)
        int n = expectedItems.size()
        assert n == items.size()

        for (int i = 0; i < n; i++) {
            WebElement link = items[i].findElement(By.tagName('a'))
            Map<String, String> expectedItem = expectedItems[i]
            assert link.getAttribute('href').startsWith(getUrl(expectedItem.url))
            String icon = expectedItem.icon
            if (icon) {
                assert link.findElement(By.tagName('i')).getAttribute('class').contains("icon-${icon}")
            }
            assert expectedItem.label == link.text
            String editUrl = expectedItem.editUrl
            if (editUrl) {
                assert items[i].findElement(By.xpath('.//span[@class="item-actions"]/a[1]')).getAttribute('href').startsWith(getUrl(editUrl))
            }
        }
    }

    /**
     * Clicks the action button with the given index in the project phase with
     * the stated index.
     *
     * @param sectionIdx    the zero-based index of the project section where
     *                      the button should be clicked
     * @param btnIdx        the zero-based index of the action button which
     *                      should be clicked
     * @since               1.4
     */
    protected void clickPhaseActionButton(int sectionIdx, int btnIdx) {
        getPhase(sectionIdx).
            findElement(By.xpath(
                ".//ul[@class='project-phase-actions']/li[${btnIdx + 1}]/span"
            )).
            click()
    }

    /**
     * Performs a click in the selector dialog to create a new project item.
     *
     * @param sectionIdx    the zero-based index of the project phase section
     *                      where the new project items should be created
     * @param btnIdx        the zero-based index of the button to click in
     *                      order to select the type of project item
     * @param expectedUrl   the expected URL after loading the form to create
     *                      the project item
     */
    protected void createProjectItem(int sectionIdx, int btnIdx,
                                     String expectedUrl)
    {
        clickPhaseActionButton sectionIdx, 0
        WebDriverWait wait = new WebDriverWait(driver, 10)
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id('create-project-item-dialog')))
        assert 9 == dialog.findElements(By.xpath('./ul/li')).size()
        dialog.findElement(By.xpath("./ul/li[${btnIdx + 1}]/a")).click()
        wait.until ExpectedConditions.visibilityOfElementLocated(BY_HEADER)
        assert driver.currentUrl.startsWith(getUrl(expectedUrl))
    }

    @Override
    protected Object getDatasets() {
        ['test-data/install-data.xml']
    }

    /**
     * Gets the web element representing the phase with the given index.
     *
     * @param sectionIdx    the zero-based index of the phase
     * @return              the project phase
     * @since               1.4
     */
    protected WebElement getPhase(int sectionIdx) {
        phases.findElement By.xpath("./section[${sectionIdx + 1}]")
    }

    /**
     * Gets the web element representing the project phases.
     *
     * @return  the project phases
     */
    protected WebElement getPhases() {
        driver.findElement(By.id('project-phases'))
    }

    /**
     * Gets the project items of the section with the given index.
     *
     * @param sectionIdx    the zero-based index of the given project phase
     *                      section
     * @return              a list of web elements representing the project
     *                      items
     */
    protected List<WebElement> getProjectItems(int sectionIdx) {
        getPhase(sectionIdx).findElements(
            By.cssSelector("ul.project-phase-items > li")
        )
    }

    protected Project prepareProject(Organization org, Person p,
                                     Map<String, Object> data)
    {
        def project = new Project(
            title: 'Marketing-Aktion Frühjahr',
            phase: ProjectPhase.quote,
            status: ProjectStatus.get(2600),
            organization: org,
            person: p,
            description: 'Marketing-Aktion zum Frühjahr für die Landschaftsbau Duvensee GbR.'
        )
        if (data.phoneCall) {
            project.addToItems(new ProjectItem(
                phase: ProjectPhase.planning,
                controller: 'call',
                itemId: data.phoneCall.id,
                title: data.phoneCall.toString()
            ))
        }
        if (data.note) {
            project.addToItems(new ProjectItem(
                phase: ProjectPhase.planning,
                controller: 'note',
                itemId: data.note.id,
                title: data.note.toString()
            ))
        }
        if (data.quote) {
            project.addToItems(new ProjectItem(
                phase: ProjectPhase.quote,
                controller: 'quote',
                itemId: data.quote.id,
                title: data.quote.toString()
            ))
        }
        if (data.salesOrder) {
            project.addToItems(new ProjectItem(
                phase: ProjectPhase.fulfillmentConfirmation,
                controller: 'salesOrder',
                itemId: data.salesOrder.id,
                title: data.salesOrder.toString()
            ))
        }
        if (data.invoice) {
            project.addToItems(new ProjectItem(
                phase: ProjectPhase.accounting,
                controller: 'invoice',
                itemId: data.invoice.id,
                title: data.invoice.toString()
            ))
        }
        project.save flush: true
        project
    }

    /**
     * Performs a click in the selector dialog to select a new project item of
     * the given type.
     *
     * @param sectionIdx    the zero-based index of the project phase section
     *                      where the new project items should be created
     * @param type          the type of project item to be selected; if the
     *                      type consists of more than one word use the hyphen
     *                      separated name such as {@code sales-order}
     * @param rowIdx        the zero-based index of the row to be selected in
     *                      the selector table
     * @return              the text of the link which has been clicked; this
     *                      should be the label of the project item
     */
    protected String selectProjectItem(int sectionIdx, String type,
                                       int rowIdx = 0)
    {
        clickPhaseActionButton sectionIdx, 1
        def wait = new WebDriverWait(driver, 10)
        By byDlg = By.id('select-project-item-dialog')
        WebElement dlg = wait.until(ExpectedConditions.visibilityOfElementLocated(byDlg))
        wait.until ExpectedConditions.visibilityOfElementLocated(By.xpath('//table[@class="content-table"]'))

        String controller = GrailsNameUtils.getPropertyNameForLowerCaseHyphenSeparatedName(type)
        dlg.findElement(By.xpath(".//select[@id='select-project-item-type-selector']/option[@data-controller='${controller}']")).click()
        wait.until ExpectedConditions.visibilityOfElementLocated(By.id("${type}-row-selector"))
        WebElement table = dlg.findElement(By.xpath('.//table[@class="content-table"]'))
        WebElement link = table.findElement(By.xpath("./tbody/tr[${rowIdx + 1}]/td[2]/a"))
        String itemLabel = link.text
        link.click()
        wait.until ExpectedConditions.invisibilityOfElementLocated(byDlg)
        wait.until ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#project-phases > section:nth-child(${sectionIdx + 1}) ul.project-phase-items > li"))
        itemLabel
    }
}
