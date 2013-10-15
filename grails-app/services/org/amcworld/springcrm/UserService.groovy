/*
 * UserService.groovy
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

import java.text.DecimalFormatSymbols
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsHttpSession
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.context.request.RequestContextHolder as RCH
import org.springframework.web.servlet.support.RequestContextUtils as RCU


/**
 * The class {@code UserService} contains methods for working with users and
 * user settings.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class UserService {

    //-- Constants ------------------------------

    private static final String [] AVAILABLE_LANGUAGES = ['de', 'en']


    //-- Class variables ------------------------

    static transactional = false


    //-- Public methods -------------------------

    /**
     * Gets all available currencies.
     *
     * @return  the available currencies
     */
    Set<Currency> getAvailableCurrencies() {
        Set<Currency> res = new HashSet()
        for (Locale l in Locale.availableLocales) {
            if (l.country) {
                Currency currency = Currency.getInstance(l)
                if (currency) {
                    res << currency
                }
            }
        }
        res
    }

    /**
     * Gets a list of languages which are fully supported by this application.
     *
     * @return  the list of supported languages; the returned list is
     *          immutable
     */
    List<String> getAvailableLanguages() {
        Collections.unmodifiableList(AVAILABLE_LANGUAGES as List)
    }

    /**
     * Gets a list of locales which are fully supported by this application.
     *
     * @return  the list of supported locales; the returned list is
     *          immutable
     */
    List<Locale> getAvailableLocales() {
        def availLangs = availableLanguages
        List<Locale> res = []
        for (Locale l in Locale.availableLocales) {
            if (l.country && availLangs.contains(l.language)) {
                res << l
            }
        }
        Collections.unmodifiableList res
    }

    /**
     * Gets the currency defined in the application configuration.
     *
     * @return  the currency
     * @since   1.3
     */
    Currency getCurrency() {
        Currency currency = null
        try {
            String currencyCode = ConfigHolder.instance['currency'] as String
            if (currencyCode) {
                currency = Currency.getInstance(currencyCode)
            }
        } catch (IllegalArgumentException e) { /* ignored */ }
        currency ?: Currency.getInstance('EUR')
    }

    /**
     * Gets the currency symbol defined in the application configuration
     * localized for the current locale.
     *
     * @return  the localized currency symbol
     * @see     #getCurrentLocale()
     * @since   1.3
     */
    String getCurrencySymbol() {
        currency.getSymbol currentLocale
    }

    /**
     * Gets the currently selected locale or the locale from the request.
     *
     * @return  the current locale
     */
    Locale getCurrentLocale() {
        def request = WebUtils.retrieveGrailsWebRequest().currentRequest
        RCU.getLocale(request) ?: Locale.default
    }

    /**
     * Gets the decimal separator for the current locale.
     *
     * @return  the decimal separator
     * @see     #getCurrentLocale()
     * @see     #getGroupingSeparator()
     * @since   1.3
     */
    String getDecimalSeparator() {
        DecimalFormatSymbols.getInstance(currentLocale).decimalSeparator
    }

    /**
     * Gets the grouping separator for the current locale.
     *
     * @return  the grouping separator
     * @see     #getDecimalSeparator()
     * @see     #getCurrentLocale()
     * @since   1.3
     */
    String getGroupingSeparator() {
        DecimalFormatSymbols.getInstance(currentLocale).groupingSeparator
    }

    /**
     * Gets the number of fraction digits as defined in the application
     * configuration.
     *
     * @return  the number of fraction digits
     * @since   1.3
     */
    int getNumFractionDigits() {
        Integer numFractionDigits = ConfigHolder.instance['numFractionDigits'] as Integer
        numFractionDigits ?: currency.defaultFractionDigits
    }

    /**
     * Gets the sort order for the given class from the configuration.  If not
     * found there, the given default order is used.
     *
     * @param clazz         the given class
     * @param defaultOrder  the default order; either {@code asc} or
     *                      {@code desc}
     * @return              the sort order; either {@code asc} or {@code desc}
     */
    String getSortOrder(Class<?> clazz, String defaultOrder) {
        session.user.settings["order${clazz.simpleName}"] ?: defaultOrder
    }

    /**
     * Gets the sort property for the given class from the configuration.  If
     * not found there, the given default property is used.
     *
     * @param clazz             the given class
     * @param defaultProperty   the default property
     * @return                  the sort property
     */
    String getSortProperty(Class<?> clazz, String defaultProperty) {
        session.user.settings["sort${clazz.simpleName}"] ?: defaultProperty
    }


    //-- Non-public methods ---------------------

    protected GrailsHttpSession getSession() {
        RCH.currentRequestAttributes().session
    }
}
