/*
 * LoginFunctionalSpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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

import org.amcworld.springcrm.page.LoginPage
import org.amcworld.springcrm.page.OverviewPage


class LoginFunctionalSpec extends DbUnitSpecBase {

    //-- Feature methods ------------------------

    def 'Login with empty fields'() {
        given:
        to LoginPage

        when: 'I login without filling out credential fields'
        loginBtn.click()

        then: 'I get an error message and must re-enter data'
        waitFor { at LoginPage }
        'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == message
    }

    def 'Login with invalid credentials'() {
        given:
        to LoginPage

        when: 'I enter an invalid user name and login'
        userName = 'rkampe'
        password = 'abc1234'
        loginBtn.click()

        then: 'I get an error message and must re-enter data'
        waitFor { at LoginPage }
        'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == message

        when: 'I enter an invalid password and login'
        userName = 'mkampe'
        password = 'abc1235'
        loginBtn.click()

        then: 'again, I get an error message and must re-enter data'
        waitFor { at LoginPage }
        'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == message
    }

    def 'Login with missing credentials'() {
        given:
        to LoginPage

        when: 'I enter a missing user name and login'
        password = 'abc1234'
        loginBtn.click()

        then: 'I get an error message and must re-enter data'
        waitFor { at LoginPage }
        'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == message

        when: 'I enter a missing password and login'
        userName = 'mkampe'
        loginBtn.click()

        then: 'again, I get an error message and must re-enter data'
        waitFor { at LoginPage }
        'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == message
    }

    void 'Login with valid credentials'() {
        given:
        to LoginPage

        when: 'I enter a valid user name and password'
        userName = 'mkampe'
        password = 'abc1234'
        loginBtn.click()

        then: 'I am logged in'
        waitFor { at OverviewPage }
        'Übersicht' == header

        when: 'I click the logout button'
        logoutLink.click()

        then: 'I return to the login page'
        waitFor { at LoginPage }
    }


    //-- Non-public methods ---------------------

    @Override
    protected List<String> getDatasets() {
        ['test-data/install-data.xml']
    }
}
