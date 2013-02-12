/*
 * GeneralTestCase.groovy
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

import ch.gstream.grails.plugins.dbunitoperator.DbUnitTestCase
import grails.util.Metadata
import org.junit.Before
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver


/**
 * The class {@code GeneralTestCase} represents a general base class for all
 * functional test cases using Selenium.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
abstract class GeneralTestCase extends DbUnitTestCase {

    //-- Constants ------------------------------

    protected static final By BY_HEADER = By.xpath('//div[@id="main-container-header"]/h2')
    protected static final By BY_LOGIN_BTN = By.name('submit')
    protected static final By BY_PASSWORD = By.name('password')
    protected static final By BY_SUBHEADER = By.xpath('//section[@id="content"]/h3')
    protected static final By BY_USER_NAME = By.name('userName')


    //-- Instance variables ---------------------

    protected String baseUrl
    protected WebDriver driver


    //-- Public methods -------------------------

    @Before
    @Override
    void setUp() {
        super.setUp()
        String appName = Metadata.current.getProperty('app.name')
        String portNumber = System.getProperty('server.port') ?: '8080'
        baseUrl = "http://localhost:${portNumber}/${appName}"
        driver = new FirefoxDriver()
    }


    //-- Non-public methods ---------------------

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
            for (WebElement input : inputs) {
                String name = input.getAttribute('name')
                if (name in fieldNames) {
                    shouldBeErrors.remove(name)
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
        return shouldBeErrors.empty && shouldNotBeErrors.empty
    }

    /**
     * Gets the n-th fieldset, i. e. an HTML {@code<div>} element with class
     * {@code fieldset}.
     *
     * @param parent    the direct parent containing the fieldset to search
     * @param index     the one-based index of the fieldset to obtain
     * @return          the web element representing the fieldset
     */
    protected WebElement getFieldset(WebElement parent, int index) {
        StringBuilder buf = new StringBuilder('div[')
        buf << getXPathClassExpr('fieldset')
        buf << ']['
        buf << index
        buf << ']'
        return parent.findElement(By.xpath(buf.toString()))
    }

    /**
     * Gets the text of the flash message.
     *
     * @return  the flash message text
     */
    protected String getFlashMessage() {
        return driver.findElement(By.className('flash-message')).text
    }

    /**
     * Gets the input field with the given name.
     *
     * @param name  the given name of the input field
     * @return      the input field
     */
    protected WebElement getInput(String name) {
        return driver.findElement(By.name(name))
    }

    /**
     * Gets the value of the input field with the given name.  The method
     * correctly handles all input field types including text areas,
     * check boxes, and radio buttons.
     *
     * @param name  the given name of the input field
     * @return      the value of that input field
     */
    protected String getInputValue(String name) {
        def input = getInput(name)
        if (input.tagName == 'textarea') {
            return input.text
        }

        String type = input.getAttribute('type')
        switch (type) {
        case 'checkbox':
            return input.getAttribute('checked') != null
        case 'radio':
            for (WebElement radio : driver.findElements(By.name(name))) {
                if (radio.getAttribute('checked') != null) {
                    return radio.getAttribute('value')
                }
                return null
            }
        default:
            return input.getAttribute('value')
        }
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
        return getShowField(col, row).text
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
        if (language) {
            buf << '?lang='
            buf << language
        }
        return buf.toString()
    }

    /**
     * Returns an XPath expression e. g. suitable for an XPath predicate which
     * tests, whether nor not the {@code class} attribute of a particular HTML
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
        buf << className.trim()
        buf << ' ")'
        return buf.toString()
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
     * Prepares an organization fixture and stores it into the database.
     *
     * @return      the prepared organization
     */
    protected Organization prepareOrganization() {
        def org = new Organization(
            recType: (byte) 1,
            name: 'Landschaftsbau Duvensee GbR',
            legalForm: 'GbR',
            type: OrgType.get(100),
            industry: Industry.get(1012),
            phone: '04543 31233',
            fax: '04543 31235',
            email1: 'info@landschaftsbau-duvensee.example',
            website: 'http://www.landschaftsbau-duvensee.example',
            billingAddrStreet: 'Dörpstraat 25',
            billingAddrPostalCode: '23898',
            billingAddrLocation: 'Duvensee',
            billingAddrState: 'Schleswig-Holstein',
            billingAddrCountry: 'Deutschland',
            shippingAddrStreet: 'Dörpstraat 25',
            shippingAddrPostalCode: '23898',
            shippingAddrLocation: 'Duvensee',
            shippingAddrState: 'Schleswig-Holstein',
            shippingAddrCountry: 'Deutschland',
            notes: 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.'
        )
        org.save(flush: true)
        return org
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
        def person = new Person(
            organization: org,
            salutation: Salutation.get(1),
            firstName: 'Henry',
            lastName: 'Brackmann',
            mailingAddrStreet: 'Dörpstraat 25',
            mailingAddrPostalCode: '23898',
            mailingAddrLocation: 'Duvensee',
            mailingAddrState: 'Schleswig-Holstein',
            mailingAddrCountry: 'Deutschland',
            phone: '04543 31233',
            mobile: '0163 3343267',
            fax: '04543 31235',
            email1: 'h.brackmann@landschaftsbau-duvensee.example',
            jobTitle: 'Geschäftsführer',
            department: 'Geschäftsleitung',
            assistant: 'Anna Schmarge',
            birthday: new GregorianCalendar(1962, Calendar.FEBRUARY, 14).time
        )
        person.save(flush: true)
        return person
    }

    /**
     * Set the value of an autocompleteex input control and selects the item
     * with the given index.
     *
     * @param id    the ID of the autocompleteex input control
     * @param value the value to enter into the input control
     * @param idx   the one-based position of the item which is to select
     * @return      the selected text
     */
    protected String selectAutocompleteEx(String id, String value, int idx = 1)
    {
        def input = driver.findElement(By.id(id))
        input.sendKeys(value)
        Thread.sleep(1000)
        for (int i = 0; i < idx; i++) {
            input.sendKeys(Keys.ARROW_DOWN)
        }
        input.sendKeys(Keys.TAB)
        return input.getAttribute('value')
    }

    /**
     * Sets the input field with the given name to the stated value.  The
     * method correctly handles all input field types including text areas,
     * check boxes, and radio buttons.
     *
     * @param name  the given name of the input field
     * @param value the value to set
     */
    protected void setInputValue(String name, String value) {
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
                for (WebElement radio : driver.findElements(By.name(name))) {
                    if (radio.getAttribute('value') == value) {
                        radio.click()
                        break
                    }
                }
                return
            }
        }

        input.clear()
        if (value) {
            input.sendKeys(value)
        }
    }
}
