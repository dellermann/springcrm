/*
 * UserService.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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
import grails.plugin.springsecurity.SpringSecurityService
import java.text.DecimalFormatSymbols
import org.bson.types.ObjectId
import org.grails.web.util.WebUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.support.RequestContextUtils as RCU


/**
 * The interface {@code IUserService} contains general methods to handle users
 * in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
interface IUserService {

    /**
     * Counts the users.
     *
     * @return  the number of users
     */
    int count()

    /**
     * Counts the users which belong to the given user group.
     *
     * @param roleGroup the given user group
     * @return          the number of matching users
     */
    int countByAuthorities(RoleGroup roleGroup)

    /**
     * Counts the users which username is alphabetically before the given
     * username.
     *
     * @param username  the given username
     * @return          the number of matching users
     */
    int countByUsernameLessThan(String username)

    /**
     * Deletes the user with the given ID.
     *
     * @param id    the ID of the user
     */
    User delete(ObjectId id)

    /**
     * Gets the user with the given ID.
     *
     * @param id    the ID of the user
     * @return      the user or {@code null} if no such user exists
     */
    User get(ObjectId id)

    /**
     * Retrieves a list of all users.
     *
     * @param args  any arguments used for retrieving the users
     * @return      a list of users
     */
    List<User> list(Map args)

    /**
     * Saves the given user.
     *
     * @param user  the user which should be saved
     * @return      the saved user
     */
    User save(User user)
}


/**
 * The class {@code UserService} contains methods for working with users and
 * user settings.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.3
 */
@Service(User)
abstract class UserService implements IUserService {

    //-- Constants ------------------------------

    private static final String [] AVAILABLE_LANGUAGES = ['de', 'en']


    //-- Fields ---------------------------------

    @Autowired
    ConfigService configService

    @Autowired
    RoleGroupService roleGroupService

    @Autowired
    SpringSecurityService springSecurityService


    //-- Public methods -------------------------

    /**
     * Encodes the given password.
     *
     * @param password  the given password
     * @return          the encoded password
     * @since 3.0
     */
    String encodePassword(String password) {
        springSecurityService?.passwordEncoder \
            ? springSecurityService.encodePassword(password)
            : password
    }

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
        List<String> l = availableLanguages
        List<Locale> res = []
        for (Locale locale in Locale.availableLocales) {
            if (locale.country && l.contains(locale.language)) {
                res << locale
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
            String currencyCode = configService.getString('currency')
            if (currencyCode) {
                currency = Currency.getInstance(currencyCode)
            }
        } catch (IllegalArgumentException ignored) { /* ignored */ }

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
     * Gets the currently logged in user.
     *
     * @return  the currently logged in user
     * @since   3.0
     */
    User getCurrentUser() {
        springSecurityService.currentUser as User
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
     * Gets the number of fraction digits for internal prices as defined in the
     * application configuration.
     *
     * @return  the number of fraction digits
     * @since   1.3
     */
    int getNumFractionDigits() {
        Integer numFractionDigits =
            configService.getInteger('numFractionDigits')
        try {
            if (numFractionDigits == null) {
                numFractionDigits = currency.defaultFractionDigits
            }
        } catch (IllegalArgumentException ignored) {
            numFractionDigits = 2
        }

        numFractionDigits
    }

    /**
     * Gets the number of fraction digits for external purpose as defined in
     * the application configuration.
     *
     * @return  the number of fraction digits
     * @since   1.4
     */
    int getNumFractionDigitsExt() {
        Integer numFractionDigits =
            configService.getInteger('numFractionDigitsExt')
        try {
            if (numFractionDigits == null) {
                numFractionDigits = currency.defaultFractionDigits
            }
        } catch (IllegalArgumentException ignored) {
            numFractionDigits = 2
        }

        numFractionDigits
    }

    /**
     * Checks whether or not the given user is the only administrator of the
     * system.
     *
     * @param user  the given user
     * @return      {@code true} if the given user is the only administrator;
     *              {@code false} otherwise
     * @since 3.0
     */
    boolean isOnlyAdmin(User user) {
        Role role = Role.findByAuthority('ROLE_ADMIN')
        RoleGroup group = roleGroupService.findByAuthorities(role)

        user?.authorities?.contains(group) ? countByAuthorities(group) == 1
            : false
    }
}
