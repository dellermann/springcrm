/*
 * UserServiceTests.groovy
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


//@TestFor(UserService)
class UserServiceTests {

    //-- Public methods -------------------------

    void testGetAvailableCurrencies() {
        Set<Currency> currencies = service.availableCurrencies
        assert currencies.contains(Currency.getInstance('EUR'))
        assert currencies.contains(Currency.getInstance('USD'))
    }

    void testGetAvailableLanguages() {
        List<String> langs = service.availableLanguages
        assert langs.contains('de')
        assert langs.contains('en')
    }

    void testGetAvailableLocales() {
        List<Locale> locales = service.availableLocales
        assert locales.contains(Locale.GERMANY)
        assert locales.contains(Locale.US)
        assert locales.contains(Locale.UK)
        for (Locale l in locales) {
            assert l.language
            assert l.country
        }
    }
}
