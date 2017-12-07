/*
 * UserService.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import grails.gorm.services.Service
import java.text.DecimalFormatSymbols
import javax.servlet.http.HttpServletRequest
import org.grails.web.util.WebUtils
import org.springframework.web.servlet.support.RequestContextUtils as RCU


/**
 * The interface {@code UserService} contains methods for working with users
 * and user settings.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   1.3
 */
interface UserService {

    //-- Public methods -------------------------

    /**
     * Counts all users.
     *
     * @return  the number of all users
     */
    int count()

    /**
     * Counts the users with a user name alphabetically before the given user
     * name.
     *
     * @param userName  the given user name
     * @return          the number of users
     */
    int countByUserNameLessThan(String userName)

    /**
     * Deletes the user with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the user with the given user name and encrypted password.
     *
     * @param userName  the given user name
     * @param password  the given encrypted password
     * @return          the matching user or {@code null} if no such user
     *                  exists
     */
    User findByUserNameAndPassword(String userName, String password)

    /**
     * Gets the user with the given ID.
     *
     * @param id    the given ID
     * @return      the user or {@code null} if no such user with the given ID
     *              exists
     */
    User get(Serializable id)

    /**
     * Gets all available currencies.
     *
     * @return  the available currencies
     */
    Set<Currency> getAvailableCurrencies()

    /**
     * Gets a list of languages which are fully supported by this application.
     *
     * @return  the list of supported languages; the returned list is
     *          immutable
     */
    List<String> getAvailableLanguages()

    /**
     * Gets a list of locales which are fully supported by this application.
     *
     * @return  the list of supported locales; the returned list is
     *          immutable
     */
    List<Locale> getAvailableLocales()

    /**
     * Gets the currency defined in the application configuration.
     *
     * @return  the currency
     * @since   1.3
     */
    Currency getCurrency()

    /**
     * Gets the currency symbol defined in the application configuration
     * localized for the current locale.
     *
     * @return  the localized currency symbol
     * @see     #getCurrentLocale()
     * @since   1.3
     */
    String getCurrencySymbol()

    /**
     * Gets the currently selected locale or the locale from the request.
     *
     * @return  the current locale
     */
    Locale getCurrentLocale()

    /**
     * Gets the decimal separator for the current locale.
     *
     * @return  the decimal separator
     * @see     #getCurrentLocale()
     * @see     #getGroupingSeparator()
     * @since   1.3
     */
    String getDecimalSeparator()

    /**
     * Gets the grouping separator for the current locale.
     *
     * @return  the grouping separator
     * @see     #getDecimalSeparator()
     * @see     #getCurrentLocale()
     * @since   1.3
     */
    String getGroupingSeparator()

    /**
     * Gets the number of fraction digits for internal prices as defined in the
     * application configuration.
     *
     * @return  the number of fraction digits
     * @since   1.3
     */
    int getNumFractionDigits()

    /**
     * Gets the number of fraction digits for external purpose as defined in
     * the application configuration.
     *
     * @return  the number of fraction digits
     * @since   1.4
     */
    int getNumFractionDigitsExt()

    /**
     * Gets a list of all users.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of users
     */
    List<User> list(Map args)

    /**
     * Saves the given user.
     *
     * @param instance  the given user
     * @return          the saved user
     */
    User save(User instance)
}


/**
 * The class {@code UserServiceImpl} implements methods for working with users
 * and user settings.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   1.3
 */
@Service(value = User, name = 'userService')
abstract class UserServiceImpl implements UserService {

    //-- Constants ------------------------------

    private static final String [] AVAILABLE_LANGUAGES = ['de', 'en']


    //-- Public methods -------------------------

    Set<Currency> getAvailableCurrencies() {
        Set<Currency> res = new HashSet<>()
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
        List<String> languages = availableLanguages
        List<Locale> res = []
        for (Locale l : Locale.availableLocales) {
            if (l.country && languages.contains(l.language)) {
                res << l
            }
        }
        Collections.unmodifiableList res
    }

    Currency getCurrency() {
        Currency currency = null
        try {
            String currencyCode = ConfigHolder.instance['currency'] as String
            if (currencyCode) {
                currency = Currency.getInstance(currencyCode)
            }
        } catch (IllegalArgumentException ignored) { /* ignored */ }

        currency ?: Currency.getInstance('EUR')
    }

    String getCurrencySymbol() {
        currency.getSymbol currentLocale
    }

    Locale getCurrentLocale() {
        HttpServletRequest request =
            WebUtils.retrieveGrailsWebRequest().currentRequest

        RCU.getLocale(request) ?: Locale.default
    }

    String getDecimalSeparator() {
        DecimalFormatSymbols.getInstance(currentLocale).decimalSeparator
    }

    String getGroupingSeparator() {
        DecimalFormatSymbols.getInstance(currentLocale).groupingSeparator
    }

    int getNumFractionDigits() {
        Integer numFractionDigits =
            ConfigHolder.instance['numFractionDigits']?.toType(Integer)
        try {
            if (numFractionDigits == null) {
                numFractionDigits = currency.defaultFractionDigits
            }
        } catch (IllegalArgumentException ignored) {
            numFractionDigits = 2
        }
        numFractionDigits
    }

    int getNumFractionDigitsExt() {
        Integer numFractionDigits =
            ConfigHolder.instance['numFractionDigitsExt']?.toType(Integer)
        try {
            if (numFractionDigits == null) {
                numFractionDigits = currency.defaultFractionDigits
            }
        } catch (IllegalArgumentException ignored) {
            numFractionDigits = 2
        }
        numFractionDigits
    }
}
