/*
 * GeneralFunctionalTestCase.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

// import ch.gstream.grails.plugins.dbunitoperator.DbUnitTestCase
import grails.util.Metadata
import java.text.DateFormat
import org.junit.After
import org.junit.Before
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait


/**
 * The class {@code GeneralFunctionalTestCase} represents a general base class
 * for all functional test cases using Selenium.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
abstract class GeneralFunctionalTestCase { //extends DbUnitTestCase {

    //-- Constants ------------------------------

    protected static final By BY_HEADER = By.xpath('//header/h1')
    protected static final By BY_LOGIN_BTN = By.name('submit')
    protected static final By BY_PASSWORD = By.name('password')
    protected static final By BY_SUBHEADER = By.xpath('//div[@id="content"]/h2')
    protected static final By BY_USER_NAME = By.name('username')
    protected static final String PURCHASE_INVOICE_EXAMPLE_DOCUMENT = 'org/amcworld/springcrm/4049493-4994.pdf'
    protected static final String PURCHASE_INVOICE_EXAMPLE_DOCUMENT_ALT = 'org/amcworld/springcrm/4049493-4994-neu.pdf'


    //-- Instance variables ---------------------

    protected String baseUrl
    protected WebDriver driver


    //-- Public methods -------------------------

    @Before
    void setup() {
        String appName = Metadata.current.getProperty('app.name')
        String portNumber = System.getProperty('server.port') ?: '8080'
        baseUrl = "http://localhost:${portNumber}/${appName}"

        driver = new FirefoxDriver()
    }

    @After
    void deleteBaseFixture() {
        Person.executeUpdate 'delete Person p'
        Organization.executeUpdate 'delete Organization o'
    }


    //-- Non-public methods ---------------------

    /**
     * Cancels the form by clicking the "cancel" button and optionally checks
     * for the given expected URL (the current URL must start with the expected
     * URL).
     *
     * @param expectedUrl   the expected URL after cancelling the form; if
     *                      {@code null} no URL check is done
     */
    protected void cancelForm(String expectedUrl = null) {
        driver.findElement(By.linkText('Abbruch')).click()
        if (expectedUrl) {
            assert driver.currentUrl.startsWith(expectedUrl)
        }
    }

    /**
     * Checks whether the value of the field with the given name is represents
     * the formatted given date or current date.
     *
     * @param fieldName the name of the field to check
     * @param date      the date to check; if missing the current date is used
     */
    protected void checkDate(String fieldName, Date date = new Date()) {
        assert getDateFormatted(date) == getInputValue(fieldName)
    }

    /**
     * Checks the toolbar of the show view which is usually used.
     *
     * @param controller    the name of the controller
     * @param id            the ID of the item
     * @since               1.4
     */
    protected void checkDefaultShowToolbar(String controller, int id) {
        checkToolbar controller, [
            [
                action: 'list',
                color: 'white',
                icon: 'list',
                label: 'Liste'
            ],
            [
                action: 'create',
                color: 'green',
                icon: 'plus',
                label: 'Anlegen'
            ],
            [
                action: 'edit',
                color: 'green',
                icon: 'pencil-square-o',
                id: id,
                label: 'Bearbeiten'
            ],
            [
                action: 'copy',
                color: 'blue',
                icon: 'files-o',
                id: id,
                label: 'Kopieren'
            ],
            [
                action: 'delete',
                check: {
                    it.click()
                    driver.switchTo().alert().dismiss()
                    assert getUrl("/${controller}/show/${id}") == driver.currentUrl
                },
                color: 'red',
                cssClasses: 'delete-btn',
                icon: 'trash-o',
                id: id,
                label: 'Löschen'
            ]
        ]
    }

    /**
     * Checks whether exactly the given list of fields are marked as error.
     *
     * @param fieldNames    the names of the field which must be marked as
     *                      error
     * @return              {@code true} if exactly the fields with the given
     *                      names and not more are marked as error;
     *                      {@code false} otherwise
     */
    protected boolean checkErrorFields(List<String> fieldNames) {
        List<String> shouldBeErrors = new ArrayList<String>(fieldNames)
        List<String> shouldNotBeErrors = []
        List<WebElement> elements = driver.findElements(By.cssSelector('.field.error'))
        for (WebElement element : elements) {
            List<WebElement> inputs = element.findElements(By.tagName('input'))
            inputs.addAll element.findElements(By.tagName('textarea'))
            for (WebElement input : inputs) {
                String name = input.getAttribute('name')
                if (name in fieldNames) {
                    shouldBeErrors.remove name
                } else if (name) {
                    shouldNotBeErrors << name
                }
            }
        }
        if (!shouldBeErrors.empty) {
            println "Fields ${shouldBeErrors.toListString()} not marked as error, but should."
        }
        if (!shouldNotBeErrors.empty) {
            println "Fields ${shouldNotBeErrors.toListString()} marked as error, but shouldn't."
        }
        shouldBeErrors.empty && shouldNotBeErrors.empty
    }

    /**
     * Checks whether both the given files are equal in size and content.
     *
     * @param expected  the expected file
     * @param file      the file to test
     */
    protected void checkFile(File expected, File file) {
        assert file.exists()
        assert expected.length() == file.length()
        assert expected.text == file.text
    }

    /**
     * Checks whether the given page title, header, and subheader are set to
     * the given values.
     *
     * @param title     the expected page title in the {@code <title>} tag
     * @param header    the expected text in the header line; if {@code null}
     *                  the header is not checked
     * @param subheader the expected text in the subheader line; if
     *                  {@code null} the subheader is not checked
     */
    protected void checkTitles(String title, String header = null,
                               String subheader = null)
    {
        assert title == driver.title
        if (header != null) {
            assert header == driver.findElement(BY_HEADER).text
        }
        if (subheader != null) {
            assert subheader == driver.findElement(BY_SUBHEADER).text
        }
    }

    /**
     * Checks the toolbar of the given controller agains the given button
     * definitions.
     *
     * @param controller    the controller to check
     * @param buttons       a list of button definitions; each definition must
     *                      be a map containing the following values:
     *                      <ul>
     *                        <li>{@code action}. The action to call when
     *                        clicking the button. The value is mandatory if
     *                        the {@code url} key is not specified.</li>
     *                        <li>{@code check}. A closure which is called to
     *                        perform additional checks.  The only parameter is
     *                        the {@code WebElement} representing the button.
     *                        </li>
     *                        <li>{@code color}. The color of the button.</li>
     *                        <li>{@code cssClasses}. Any additional CSS
     *                        classes the button must have.</li>
     *                        <li>{@code icon}. The icon (without the
     *                        {@code fa-} prefix) which must be inside the
     *                        button.</li>
     *                        <li>{@code id}. The ID to use in the URL which
     *                        is called when clicking the button.</li>
     *                        <li>{@code label}. The label of the button.</li>
     *                        <li>{@code url}. The URL to call when clicking
     *                        the button.</li>
     *                      </ul>
     * @since               1.4
     */
    protected void checkToolbar(String controller,
                                List<Map<String, Object>> buttons)
    {
        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
        for (int i = 0; i < buttons.size(); i++) {
            Map button = buttons[i]
            WebElement link = toolbar.findElement(By.xpath("li[${i + 1}]/a"))
            StringBuilder buf = new StringBuilder('button')
            if (button.color) buf << ' ' << button.color
            if (button.cssClasses) buf << ' ' << button.cssClasses
            assert buf.toString() == link.getAttribute('class')
            String url = button.url
            if (!url) {
                buf = new StringBuilder('/')
                buf << controller << '/' << button.action
                if (button.id) buf << '/' << button.id
                url = buf.toString()
            }
            assert getUrl(url) == link.getAttribute('href')
            assert button.label == link.text
            if (button.icon) {
                List<String> cssClasses =
                    link.findElement(By.tagName('i')).getAttribute('class').tokenize()
                assert 'fa' in cssClasses
                assert "fa-${button.icon}".toString() in cssClasses
            }
            if (button.check) {
                button.check link
            }
        }
    }

    /**
     * Clears the content of the input control with the given name.
     *
     * @param name  the name of the input control
     */
    protected void clearInput(String name) {
        WebElement input = getInput(name)
        input.clear()

        /*
         * Some kind of input fields cannot be cleared so I use this way to
         * remove all characters from this field.
         */
        String value = input.getAttribute('value')
        if (value) {
            for (int i = 0; i < value.length(); i++) {
                input.sendKeys Keys.BACK_SPACE
            }
        }
    }

    /**
     * Clicks on a button with the given index on the action bar and optionally
     * checks for the given URL (the current URL must start with the expected
     * URL).
     *
     * @param btnIdx        the zero-based button index
     * @param expectedUrl   the expected URL after clicking the button; if
     *                      {@code null} no check is done
     */
    protected void clickActionBarButton(int btnIdx, String expectedUrl = null) {
        By by = By.xpath("//aside[@id='action-bar']/ul/li[${btnIdx + 1}]/a")
        driver.findElement(by).click()
        if (expectedUrl) {
            assert driver.currentUrl.startsWith(expectedUrl)
        }
    }

    /**
     * Clicks on an action button in the table row with the given index and
     * optionally checks for the given URL (the current URL must start with the
     * expected URL).
     *
     * @param rowIdx        the zero-based row index
     * @param btnIdx        the zero-based button index among the action
     *                      buttons
     * @param expectedUrl   the expected URL after clicking the button; if
     *                      {@code null} no check is done
     */
    protected void clickListActionButton(int rowIdx, int btnIdx,
                                         String expectedUrl = null)
    {
        By by = By.xpath("//table[@class='content-table']/tbody/tr[${rowIdx + 1}]/td[@class='action-buttons']/a[${btnIdx + 1}]")
        driver.findElement(by).click()
        if (expectedUrl) {
            assert driver.currentUrl.startsWith(expectedUrl)
        }
    }

    /**
     * Clicks on the row and column with the given indices in the table and
     * performs an optional check for the given URL.  If the URL check is done
     * an numeric value is expected after the given URL (normally an ID) which
     * is returned.  So for example, if {@code expectedUrl} is set to
     * {@code /call/show} a URL of {@code /call/show/n} is expected, where
     * {@code n} is an number which in turn is returned.
     *
     * @param rowIdx        the zero-based index of the row to click
     * @param colIdx        the zero-based index of the column to click
     * @param expectedUrl   an expected URL as described above; the given value
     *                      must not end with a slash. If {@code null} no URL
     *                      check is done.
     * @return              if {@code expectedUrl} is non-{@code null} the
     *                      numeric value after the expected URL is returned;
     *                      {@code null} otherwise
     */
    protected Integer clickListItem(int rowIdx, int colIdx,
                                    String expectedUrl = null)
    {
        By by = By.xpath("//table[@class='content-table']/tbody/tr[${rowIdx + 1}]/td[${colIdx + 1}]/a")
        driver.findElement(by).click()
        Integer id = null
        if (expectedUrl) {
            def m = (driver.currentUrl =~ "${expectedUrl}/(\\d+)")
            assert !!m
            id = m[0][1] as Integer
        }
        id
    }

    /**
     * Clicks the button with the given index in the toolbar.  After clicking
     * the button, an optional check for the given URL is done (the current URL
     * must start with the expected URL).
     *
     * @param idx           the zero-based index of the toolbar button to click
     * @param expectedUrl   the expected URL after clicking the toolbar button;
     *                      if {@code null} no check is done
     */
    protected void clickToolbarButton(int idx, String expectedUrl = null) {
        driver.findElement(By.xpath("//ul[@id='toolbar']/li[${idx + 1}]/a")).click()
        if (expectedUrl) {
            assert driver.currentUrl.startsWith(expectedUrl)
        }
    }

    /**
     * Gets the base folder containing the application data in test cases.
     *
     * @return  the application base directory
     */
    protected File getAppBaseDir() {
        File f = new File(System.getProperty('java.io.tmpdir'), 'springcrm')
        if (!f.exists()) {
            f.mkdirs()
        }
        f
    }

    /**
     * Gets the folder containing the application data files such as purchase
     * invoice documents in test cases.
     *
     * @return  the application data directory
     */
    protected File getAppDataDir() {
        File f = new File(appBaseDir, 'data')
        if (!f.exists()) {
            f.mkdirs()
        }
        f
    }

    /**
     * Gets the selected text of the autocompleteex widget with the given ID or
     * name.
     *
     * @param idOrName  the ID or the name of the autocompleteex input control
     * @return          the selected text; {@code null} if no input control
     *                  with the given ID or name was found
     */
    protected String getAutocompleteExValue(String idOrName) {
        List<WebElement> inputs = driver.findElements(By.id(idOrName))
        if (!inputs) {
            inputs = driver.findElements(By.name(idOrName))
        }
        inputs?.get(0)?.getAttribute('value')
    }

    protected Object getDatasets() { null }

    /**
     * Gets the given date or the current date formatted with the medium
     * format.
     *
     * @param date  the date to format; if missing, the current date is used
     * @return      the formatted date
     */
    protected String getDateFormatted(Date date = new Date()) {
        DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
    }

    /**
     * Gets the n-th fieldset, i. e. an HTML {@code <div>} element with class
     * {@code fieldset}.
     *
     * @param parent    the direct parent containing the fieldset to search
     * @param index     the one-based index of the fieldset to obtain
     * @return          the web element representing the fieldset
     */
    protected WebElement getFieldset(WebElement parent, int index) {
        parent.findElement(By.xpath(getFieldsetXpath(index)))
    }

    /**
     * Gets the XPath expression to select the n-th fieldset, i. e. an HTML
     * {@code <div>} element with class {@code fieldset}.
     *
     * @param index the one-based index of the fieldset to obtain
     * @return      the computed XPath expression
     */
    protected String getFieldsetXpath(int index) {
        StringBuilder buf = new StringBuilder('section[')
        buf << getXPathClassExpr('fieldset') << '][' << index << ']'
        buf.toString()
    }

    /**
     * Gets the text of the flash message.
     *
     * @return  the flash message text
     */
    protected String getFlashMessage() {
        driver.findElement(By.className('flash-message')).text
    }

    /**
     * Gets the input field with the given name.
     *
     * @param name  the given name of the input field
     * @return      the input field
     */
    protected WebElement getInput(String name) {
        driver.findElement By.name(name)
    }

    /**
     * Gets the value of the input field with the given name.  The method
     * correctly handles all input field types including text areas,
     * check boxes, radio buttons, and select fields.
     *
     * @param name  the given name of the input field
     * @return      the value of that input field; the type of value depends on
     *              the type of input:
     *              <ul>
     *                <li>if the input is a multiple select field a
     *                {@code List} is returned</li>
     *                <li>if the input is a checkbox a {@code boolean} value is
     *                returned</li>
     *                <li>otherwise a string is returned</li>
     *              </ul>
     */
    protected def getInputValue(String name) {
        def input = getInput(name)
        if (input.tagName == 'textarea') {
            return input.getAttribute('value') ?: input.text
        }
        if (input.tagName == 'select') {
            def select = new Select(input)
            return select.multiple \
                ? select.allSelectedOptions*.getAttribute('value')
                : select.firstSelectedOption.getAttribute('value')
        }

        String type = input.getAttribute('type')
        switch (type) {
        case 'checkbox':
            return input.selected
        case 'radio':
            for (WebElement radio : driver.findElements(By.name(name))) {
                if (radio.selected) {
                    return radio.getAttribute('value')
                }
            }
            return null
        default:
            return input.getAttribute('value')
        }
    }

    /**
     * Gets the local file with the given classpath-relative path.
     *
     * @param path  the path to the file relative to the class path base
     *              directory
     * @return      the local file
     */
    protected File getLocalFile(String path) {
        URI uri = getClass().getClassLoader().getResource(path)?.toURI()
        assert uri
        File file = new File(uri.path)
        assert file

        file
    }

    /**
     * Gets an example document for purchase invoices.
     *
     * @return  the purchase invoice example document
     */
    protected File getPurchaseInvoiceExampleDocument() {
        getLocalFile PURCHASE_INVOICE_EXAMPLE_DOCUMENT
    }

    /**
     * Gets the directory in test cases where the purchase invoice documents
     * are located.
     *
     * @return  the folder containing the purchase invoice documents
     */
    protected File getPurchaseInvoiceFolder() {
        File f = new File(
            appDataDir, PurchaseInvoiceController.FILE_TYPE.toString()
        )
        if (!f.exists()) {
            f.mkdirs()
        }
        f
    }

    /**
     * Gets the field in the show view with the given parent and row number.
     * The parent may be a column ({@code .col} element), fieldset
     * ({@code .fieldset} element) or similar.
     *
     * @param parent    the parent where to search for the given field
     * @param row       the one-based row number
     * @return          the web element representing the field
     */
    protected WebElement getShowField(WebElement parent, int row) {
        return parent.findElement(
            By.xpath(".//div[@class='row'][${row}]/div[@class='field']")
        )
    }

    /**
     * Gets the text of the field in the show view with the given parent and
     * row number.  The parent may be a column ({@code .col} element), fieldset
     * ({@code .fieldset} element) or similar.
     *
     * @param parent    the parent where to search for the given field
     * @param row       the one-based row number
     * @return          the text of the field
     */
    protected String getShowFieldText(WebElement col, int row) {
        getShowField(col, row).text.trim()
    }

    /**
     * Gets the absolute URL in this web application using the given relative
     * URL and an optional language.
     *
     * @param url   the given relative URL; must start with a slash
     * @return      the language which is added to the request; if {@code null}
     *              no language parameter is added
     */
    protected String getUrl(String url, String language = null) {
        StringBuilder buf = new StringBuilder(baseUrl)
        buf << url
        if (language) buf << '?lang=' << language
        buf.toString()
    }

    /**
     * Returns an XPath expression e. g. suitable for an XPath predicate which
     * tests whether nor not the {@code class} attribute of a particular HTML
     * element contains the given class name.  The method is needed because it
     * is complicated to test against multiple class names of HTML elements.
     *
     * @param className the given class name
     * @return          the XPath expression
     */
    protected String getXPathClassExpr(String className) {
        StringBuilder buf = new StringBuilder(
            'contains(concat(" ", normalize-space(@class), " "), " '
        )
        buf << className.trim() << ' ")'
        buf.toString()
    }

    /**
     * Maximizes the browser window.
     */
    protected void maximizeWindow() {
        driver.manage().window().maximize()
    }

    /**
     * Opens the given relative URL which is converted to an absolute URL in
     * the web browser.
     *
     * @param url   the given relative URL; must start with a slash
     * @return      the language which is added to the request; if {@code null}
     *              no language parameter is added
     * @see         #getUrl(String, String)
     */
    protected void open(String url, String language = null) {
        driver.get(getUrl(url, language))
    }

    /**
     * Prepares a calendar event and stores it into the database.
     *
     * @param org   the organization the calendar event belongs to
     * @return      the prepared calendar event
     */
    protected CalendarEvent prepareCalendarEvent(Organization org) {
        def calendarEvent = new CalendarEvent(
            subject: 'Besprechung Werbekonzept',
            location: 'Büro Landschaftsbau Duvensee GbR',
            description: 'Besprechung des Konzepts für die geplante Marketing-Aktion.',
            start: new GregorianCalendar(2013, Calendar.JANUARY, 23, 10, 00, 0).time,
            end: new GregorianCalendar(2013, Calendar.JANUARY, 23, 12, 00, 0).time,
            organization: org,
            owner: User.get(1)
        )
        calendarEvent.save flush: true, failOnError: true
        calendarEvent
    }

    /**
     * Prepares a phone call and stores it into the database.
     *
     * @param org   the organization the phone call belongs to
     * @param p     the person the phone call belongs to
     * @return      the prepared phone call
     */
    protected Call prepareCall(Organization org, Person p) {
        def call = new Call(
            subject: 'Bitte um Angebot',
            start: new GregorianCalendar(2013, Calendar.FEBRUARY, 13, 9, 15, 0).time,
            organization: org,
            person: p,
            phone: '04543 31233',
            type: CallType.incoming,
            status: CallStatus.completed,
            notes: 'Herr Brackmann bittet um die Zusendung eines Angebots für die **geplante Marketing-Aktion**.'
        )
        call.save flush: true, failOnError: true
        call
    }

    /**
     * Prepares a credit memo and stores it into the database.
     *
     * @param org       the organization the credit memo belongs to
     * @param p         the person the credit memo belongs to
     * @param invoice   the invoice associated to this credit memo
     * @return          the created credit memo
     */
    protected CreditMemo prepareCreditMemo(Organization org, Person p,
                                           Invoice invoice)
    {
        def creditMemo = new CreditMemo(
            subject: 'Werbekampagne Frühjahr 2013',
            docDate: new GregorianCalendar(2013, Calendar.APRIL, 8).time,
            organization: org,
            person: p,
            carrier: Carrier.get(501),
            shippingDate: new GregorianCalendar(2013, Calendar.APRIL, 9).time,
            billingAddr: new Address(org.billingAddr),
            shippingAddr: new Address(org.shippingAddr),
            headerText: 'hiermit schreiben wir Ihnen einzelne Posten aus der Rechnung zur Werbekampagne **"Frühjahr 2013"** gut.',
            footerText: 'Erläuterungen zu den einzelnen Posten finden Sie **im Pflichtenheft**.',
            notes: 'Gutschrift für _nicht_ lieferbare Artikel.',
            stage: CreditMemoStage.get(2502),
            dueDatePayment: new GregorianCalendar(2013, Calendar.APRIL, 16).time,
            invoice: invoice
        )
        creditMemo.addToItems(new InvoicingItem(
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
                description: 'Anfertigung eines Musters _nach Kundenvorgaben_.',
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
            save(flush: true, failOnError: true)
        creditMemo
    }

    /**
     * Prepares a dunning and stores it into the database.
     *
     * @param org       the organization the dunning belongs to
     * @param p         the person the dunning belongs to
     * @param invoice   the invoice associated to this dunning
     * @return          the created dunning
     */
    protected Dunning prepareDunning(Organization org, Person p,
                                     Invoice invoice)
    {
        def dunning = new Dunning(
            subject: 'Werbekampagne Frühjahr 2013',
            docDate: new GregorianCalendar(2013, Calendar.MAY, 6).time,
            organization: org,
            person: p,
            carrier: Carrier.get(501),
            shippingDate: new GregorianCalendar(2013, Calendar.MAY, 7).time,
            billingAddr: new Address(org.billingAddr),
            shippingAddr: new Address(org.shippingAddr),
            headerText: 'zur angegebenen Rechnung konnte **bis heute** kein Zahlungseingang verzeichnet werden.',
            footerText: 'Die **Mahngebühren und Verzugszinsen** ergeben sich aus unseren AGB.',
            notes: 'Zahlung auch nach _wiederholter_ telefonischer Mahnung nicht erfolgt.',
            level: DunningLevel.get(2300),
            stage: DunningStage.get(2202),
            dueDatePayment: new GregorianCalendar(2013, Calendar.MAY, 13).time,
            invoice: invoice
        )
        dunning.addToItems(new InvoicingItem(
                number: 'S-99000',
                quantity: 1.0d,
                unit: 'Einheiten',
                name: 'Mahngebühren',
                unitPrice: 3.0d,
                tax: 19.0d
            )).
            addToItems(new InvoicingItem(
                number: 'S-99001',
                quantity: 1.0d,
                unit: 'Einheiten',
                name: 'Verzugszinsen',
                description: 'Verzugszinsen 5 %',
                unitPrice: 53.22d,
                tax: 19.0d
            )).
            addToTermsAndConditions(TermsAndConditions.get(700)).
            save(flush: true, failOnError: true)
        dunning
    }

    /**
     * Prepares an invoice and stores it into the database.
     *
     * @param org           the organization the invoice belongs to
     * @param p             the person the invoice belongs to
     * @param quote         the quote associated to this invoice
     * @param salesOrder    the sales order associated to the invoice
     * @return              the created invoice
     */
    protected Invoice prepareInvoice(Organization org, Person p, Quote quote,
                                     SalesOrder salesOrder)
    {
        def invoice = new Invoice(
            subject: 'Werbekampagne Frühjahr 2013',
            docDate: new GregorianCalendar(2013, Calendar.APRIL, 1).time,
            organization: org,
            person: p,
            carrier: Carrier.get(501),
            shippingDate: new GregorianCalendar(2013, Calendar.APRIL, 2).time,
            billingAddr: new Address(org.billingAddr),
            shippingAddr: new Address(org.shippingAddr),
            headerText: '''für die durchgeführte Werbekampange **"Frühjahr 2013"** erlauben wir uns, Ihnen folgendes in Rechnung zu stellen.

Einzelheiten entnehmen Sie bitte dem beiliegenden Leistungsverzeichnis bzw. dem [Online-Verzeichnis](http://www.example.de/verzeichnis/).''',
            footerText: 'Die Ausführung und Abrechnung erfolgte _laut Pflichtenheft_.',
            notes: '**Wichtig!** Beim Versand der Rechnung Leistungsverzeichnis nicht vergessen!',
            stage: InvoiceStage.get(902),
            dueDatePayment: new GregorianCalendar(2013, Calendar.APRIL, 16).time,
            quote: quote,
            salesOrder: salesOrder
        )
        invoice.addToItems(new InvoicingItem(
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
                description: 'Anfertigung eines Musters _nach Kundenvorgaben_.',
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
            save(flush: true, failOnError: true)
        invoice
    }

    /**
     * Prepares a note and stores it into the database.
     *
     * @param org   the organization the note belongs to
     * @param p     the person the note belongs to
     * @return      the prepared note
     */
    protected Note prepareNote(Organization org, Person p) {
        def note = new Note(
            title: 'Besprechung vom 21.01.2013',
            organization: org,
            person: p,
            content: '''# Besprechung der PR-Aktion am 21.01.2013

Am 21.01.2013 trafen wir uns mit Henry Brackmann und besprachen die
Vorgehensweise bei der geplanten PR-Aktion. Herr Brackmann will den Schwerpunkt
auf Werbung in lokalen Medien (z. B. regionale Tageszeitungen) legen.

Wir vereinbarten folgende Vorgehensweise:

* Kalkulation des verfügbaren Werbebudgets durch Landschaftsbau Duvensee GbR
* Konzeption des Werbekonzepts
* Kostenermittlung der einzelnen Werbemöglichkeiten'''
        )
        note.save flush: true, failOnError: true
        note
    }

    /**
     * Prepares an organization fixture and stores it into the database.
     *
     * @return  the prepared organization
     */
    protected Organization prepareOrganization() {
        def addr = new Address(
            street: 'Dörpstraat 25',
            postalCode: '23898',
            location: 'Duvensee',
            state: 'Schleswig-Holstein',
            country: 'Deutschland',
        )
        def org = new Organization(
            recType: (byte) 1,
            name: 'Landschaftsbau Duvensee GbR',
            legalForm: 'GbR',
            type: OrgType.get(100),
            industry: Industry.get(1012),
            phone: '04543 31233',
            fax: '04543 31235',
            email1: 'info@landschaftsbau-duvensee.de',
            website: 'http://www.landschaftsbau-duvensee.de',
            billingAddr: addr,
            shippingAddr: addr,
            notes: 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.'
        )
        org.save flush: true, failOnError: true
        org
    }

    /**
     * Prepares a person fixture and stores it into the database.
     *
     * @param org   the organization the person belongs to; if {@code null} a
     *              new organization is created
     * @return      the prepared person
     */
    protected Person preparePerson(Organization org = null) {
        if (!org) {
            org = prepareOrganization()
        }
        def addr = new Address(
            street: 'Dörpstraat 25',
            postalCode: '23898',
            location: 'Duvensee',
            state: 'Schleswig-Holstein',
            country: 'Deutschland',
        )
        def person = new Person(
            organization: org,
            salutation: Salutation.get(1),
            firstName: 'Henry',
            lastName: 'Brackmann',
            mailingAddr: addr,
            otherAddr: new Address(),
            phone: '04543 31233',
            mobile: '0163 3343267',
            fax: '04543 31235',
            email1: 'h.brackmann@landschaftsbau-duvensee.de',
            jobTitle: 'Geschäftsführer',
            department: 'Geschäftsleitung',
            assistant: 'Anna Schmarge',
            birthday: new GregorianCalendar(1962, Calendar.FEBRUARY, 14).time
        )
        person.save flush: true, failOnError: true
        person
    }

    /**
     * Prepares a purchase invoice and stores it into the database.
     *
     * @param org   the vendor the purchase invoice belongs to
     * @return      the created purchase invoice
     */
    protected PurchaseInvoice preparePurchaseInvoice(Organization org = null) {
        File file = purchaseInvoiceExampleDocument
        DataFile dataFile = new DataFile(file)
        dataFile.save()
        new File(purchaseInvoiceFolder, dataFile.storageName) << file.newInputStream()

        def purchaseInvoice = new PurchaseInvoice(
            number: '4049493-4994',
            subject: 'Entwicklung eines Designs',
            vendor: org,
            vendorName: 'Katja Schmale Webdesignerin',
            docDate: new GregorianCalendar(2013, Calendar.MARCH, 15).time,
            dueDate: new GregorianCalendar(2013, Calendar.APRIL, 15).time,
            stage: PurchaseInvoiceStage.get(2101),
            notes: 'Lieferschein zur Rechnung nachfordern.',
            documentFile: dataFile,
            discountPercent: 2.0d,
            adjustment: -1.36d
        )
        purchaseInvoice.addToItems(new PurchaseInvoiceItem(
                number: '5100',
                quantity: 1.0d,
                unit: 'Einheiten',
                name: 'Konzeption und Planung',
                description: 'Konzeption des geplanten Webdesigns',
                unitPrice: 500.0d,
                tax: 19.0d
            )).
            addToItems(new PurchaseInvoiceItem(
                number: '1200',
                quantity: 1.0d,
                unit: 'Einheiten',
                name: 'Webdesign',
                unitPrice: 1300.0d,
                tax: 19.0d
            )).
            addToItems(new PurchaseInvoiceItem(
                number: '9500',
                quantity: 10.0d,
                unit: 'Packung',
                name: 'Büromaterial',
                description: 'Papier, Klebeband, Kleinteile',
                unitPrice: 4.5d,
                tax: 7.0d
            )).
            save(flush: true, failOnError: true)
        purchaseInvoice
    }

    /**
     * Prepares a quote and stores it into the database.
     *
     * @param org   the organization the quote belongs to
     * @param p     the person the quote belongs to
     * @return      the created quote
     */
    protected Quote prepareQuote(Organization org, Person p) {
        def quote = new Quote(
            subject: 'Werbekampagne Frühjahr 2013',
            docDate: new GregorianCalendar(2013, Calendar.FEBRUARY, 20).time,
            organization: org,
            person: p,
            carrier: Carrier.get(501),
            shippingDate: new GregorianCalendar(2013, Calendar.FEBRUARY, 21).time,
            billingAddr: new Address(org.billingAddr),
            shippingAddr: new Address(org.shippingAddr),
            headerText: '''für die geplante Werbekampange **"Frühjahr 2013"** möchten wir Ihnen gern folgendes Angebot unterbreiten.

Die Einzelheiten wurden im Meeting am 21.01.2013 festgelegt. Sie finden ein vollständiges Protokoll auf [unserer Webseite](http://www.example.de/protokoll/).''',
            footerText: 'Details zu den einzelnen Punkten finden Sie _im Pflichtenheft_.',
            notes: 'Angebot unterliegt _möglicherweise_ weiteren Änderungen.',
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
                description: 'Anfertigung eines Musters _nach Kundenvorgaben_.',
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
            save(flush: true, failOnError: true)
        quote
    }

    /**
     * Prepares a sales order and stores it into the database.
     *
     * @param org   the organization the sales order belongs to
     * @param p     the person the sales order belongs to
     * @param quote the quote associated to this sales order
     * @return      the created sales order
     */
    protected SalesOrder prepareSalesOrder(Organization org, Person p,
                                           Quote quote)
    {
        def salesOrder = new SalesOrder(
            subject: 'Werbekampagne Frühjahr 2013',
            docDate: new GregorianCalendar(2013, Calendar.MARCH, 4).time,
            organization: org,
            person: p,
            carrier: Carrier.get(501),
            shippingDate: new GregorianCalendar(2013, Calendar.MARCH, 5).time,
            billingAddr: new Address(org.billingAddr),
            shippingAddr: new Address(org.shippingAddr),
            headerText: 'vielen Dank für Ihren Auftrag zur Werbekampange **"Frühjahr 2013"**.',
            footerText: 'Die Umsetzung des Auftrags erfolgt **nach Pflichtenheft**.',
            notes: 'Erste Teilergebnisse sollten vor dem *15.03.2013* vorliegen.',
            stage: SalesOrderStage.get(802),
            dueDate: new GregorianCalendar(2013, Calendar.MARCH, 28).time,
            quote: quote
        )
        salesOrder.addToItems(new InvoicingItem(
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
                description: 'Anfertigung eines Musters _nach Kundenvorgaben_.',
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
            save(flush: true, failOnError: true)
        salesOrder
    }

    /**
     * Set the value of an autocompleteex input control and selects the item
     * with the given index.
     *
     * @param idOrName  the ID or the name of the autocompleteex input control
     * @param value     the value to enter into the input control
     * @param idx       the one-based position of the item which is to select
     * @return          the selected text; {@code null} if no such element
     *                  exists
     */
    protected String selectAutocompleteEx(String idOrName, String value,
                                          int idx = 1)
    {
        def inputs = driver.findElements(By.id(idOrName))
        if (!inputs) {
            inputs = driver.findElements(By.name(idOrName))
        }
        if (!inputs) {
            return null
        }

        def input = inputs[0]
        input.sendKeys value
        Thread.sleep 1000
        for (int i = 0; i < idx; i++) {
            input.sendKeys Keys.ARROW_DOWN
        }
        input.sendKeys Keys.TAB
        input.getAttribute 'value'
    }

    /**
     * Sets the input field with the given name to the stated value.  The
     * method correctly handles all input field types including text areas,
     * check boxes, radio buttons, and select fields.
     *
     * @param name  the given name of the input field
     * @param value the value to set; the type of value depends on the type of
     *              input:
     *              <ul>
     *                <li>if the input is a multiple select field you may state
     *                a {@code Collection} of values</li>
     *                <li>if the input is a checkbox the value is interpreted
     *                as {@code boolean}</li>
     *                <li>otherwise the value is converted to a {@code String}
     *                before setting it</li>
     *              </ul>
     */
    protected void setInputValue(String name, def value) {
        def input = getInput(name)
        if (input.tagName == 'input') {
            String type = input.getAttribute('type')
            switch (type) {
            case 'checkbox':
                boolean oldState = input.getAttribute('checked') != null
                boolean newState = value as boolean
                if (oldState != newState) {
                    input.click()
                }
                return
            case 'radio':
                value = value.toString()
                for (WebElement radio : driver.findElements(By.name(name))) {
                    if (radio.getAttribute('value') == value) {
                        radio.click()
                        break
                    }
                }
                return
            }
        } else if (input.tagName == 'select') {
            def select = new Select(input)
            if (select.multiple && value instanceof Collection) {
                select.deselectAll()
                for (val in value) {
                    select.selectByValue val
                }
            } else {
                select.selectByValue value.toString()
            }
            return
        }

        input.clear()
        if (value) {
            String oldValue = input.getAttribute('value')
            for (int i = 0; i < oldValue.length(); i++) {
                input.sendKeys Keys.BACK_SPACE
            }
            input.sendKeys value.toString()
        }
    }

    /**
     * Submits the form by clicking the "save" button and optionally checks for
     * the given expected URL (the current URL must start with the expected
     * URL).
     *
     * @param expectedUrl   the expected URL after submitting the form; if
     *                      {@code null} no URL check is done
     */
    protected void submitForm(String expectedUrl = null) {
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        if (expectedUrl) {
            assert driver.currentUrl.startsWith(expectedUrl)
        }
    }

    /**
     * Sets the given file in the upload field with the given name.  The file
     * must be located in the class path.
     *
     * @param name  the given name of the input field
     * @param path  the path to the file to upload relative to the class path
     *              base directory
     * @return      the uploaded file
     */
    protected File uploadFile(String name, String path) {
        File file = getLocalFile(path)
        getInput(name).sendKeys file.absolutePath
        file
    }

    /**
     * Waits until an empty remote list is loaded.
     *
     * @param fieldsetIdx   the one-based index of the fieldset containing the
     *                      remote list
     * @return              the web element representing the fieldset
     */
    protected WebElement waitForEmptyRemoteList(int fieldsetIdx) {
        def wait = new WebDriverWait(driver, 10)
        wait.until ExpectedConditions.presenceOfElementLocated(By.xpath(
            ".//${getFieldsetXpath(fieldsetIdx)}/div/div[@class='empty-list-inline']"
        ))
    }
}
