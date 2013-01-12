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

import grails.util.Metadata
import org.junit.Before
import org.openqa.selenium.By
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
class GeneralTestCase {

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
    void setUp() {
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
}
