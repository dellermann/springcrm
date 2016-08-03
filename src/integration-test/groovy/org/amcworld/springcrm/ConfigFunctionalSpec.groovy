/*
 * ConfigFunctionalSpec.groovy
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

import org.amcworld.springcrm.page.ConfigMailPage
import org.amcworld.springcrm.page.ConfigOverviewPage


class ConfigFunctionalSpec extends GeneralFunctionalTest {

    //-- Fixture methods ------------------------

    def setup() {
        login()
    }


    //-- Feature methods ------------------------

    def 'Display system settings overview page'() {
        when: 'I go to the system settings overview page'
        to ConfigOverviewPage

        then: 'I get to this page'
        waitFor { at ConfigOverviewPage }
        'Systemeinstellungen' == header

        and: 'there are configuration items on the page'
        9 == items.size()
    }

    def 'Show the mail configuration'() {
        given: 'I go to the system settings overview page'
        to ConfigOverviewPage

        when: 'I click the mail configuration item'
        item('mail').link.click()

        then: 'I get to the mail configuration page'
        waitFor { at ConfigMailPage }

        and: 'the input controls are set correctly'
        def fs0 = fieldset(0)
        'Verwendung des E-Mail-Versands' == fs0.find('h3').text()
        'true' == form.'config.mailUseConfig'
        'localhost' == form.'config.mailHost'
        '25' == form.'config.mailPort'
        'true' == form.'config.mailAuth'
        'jdoe' == form.'config.mailUserName'
        '' == form.'config.mailPassword'
        'starttls' == form.'config.mailEncryption'

        when: 'I deactivate the mail configuation'
        form.'config.mailUseConfig' = 'null'

        then: 'all other controls are disabled'
        checkNonUserConfiguration()

        when: 'I change the mail configuation to system settings'
        form.'config.mailUseConfig' = 'false'

        then: 'all other controls are disabled'
        checkNonUserConfiguration()

        when: 'I change the mail configuation to user settings'
        form.'config.mailUseConfig' = 'true'

        then: 'all controls are enabled'
        checkUserConfiguration()

        when: 'I deactivate the authentication'
        form.'config.mailAuth' = 'false'

        then: 'the user name and password controls are disabled'
        checkAuthFields true

        when: 'I activate the authentication'
        form.'config.mailAuth' = 'true'

        then: 'the user name and password controls are enabled'
        checkAuthFields false

        when: 'I deactivate the authentication and then the mail configuration'
        form.'config.mailAuth' = 'false'
        form.'config.mailUseConfig' = 'null'

        then: 'all other controls are disabled'
        checkNonUserConfiguration()

        when: 'I reactivate the mail configuration'
        form.'config.mailUseConfig' = 'true'

        then: 'all controls except username and password are enabled'
        checkAuthFields false
        checkFieldsWithoutAuthFields true
    }

    def 'Save unconfigured mail settings'() {
        given: 'I go to the system settings overview page'
        to ConfigOverviewPage

        and: 'I click the mail configuration item'
        item('mail').link.click ConfigMailPage

        when: 'I disable mail configuration and save'
        form.'config.mailUseConfig' = 'null'
        submitBtn.click()

        then: 'I get to the configuration overview page'
        waitFor { at ConfigOverviewPage }

        and: 'the mail configuration has been disabled'
        null == ConfigHolder.instance['mailUseConfig']

        when: 'I again click the mail configuration item'
        item('mail').link.click()

        then: 'I get to the mail configuration page and all controls are disabled'
        waitFor { at ConfigMailPage }
        'null' == form.'config.mailUseConfig'
        checkNonUserConfiguration()
    }

    def 'Save system mail settings'() {
        given: 'I go to the system settings overview page'
        to ConfigOverviewPage

        and: 'I click the mail configuration item'
        item('mail').link.click ConfigMailPage

        when: 'I set mail configuration to system and save'
        form.'config.mailUseConfig' = 'false'
        submitBtn.click()

        then: 'I get to the configuration overview page'
        waitFor { at ConfigOverviewPage }

        and: 'the mail configuration has been disabled'
        !(ConfigHolder.instance['mailUseConfig'] as Boolean)

        when: 'I again click the mail configuration item'
        item('mail').link.click()

        then: 'I get to the mail configuration page and all controls are disabled'
        waitFor { at ConfigMailPage }
        'false' == form.'config.mailUseConfig'
        checkNonUserConfiguration()
    }

    def 'Save mail settings with authentication'() {
        given: 'I go to the system settings overview page'
        to ConfigOverviewPage

        and: 'I click the mail configuration item'
        item('mail').link.click ConfigMailPage

        when: 'I set mail configuration to system and save'
        form.'config.mailHost' = '192.168.100.1'
        form.'config.mailPort' = '465'
        form.'config.mailUserName' = 'jsmith'
        form.'config.mailPassword' = 'very-secret'
        form.'config.mailEncryption' = 'ssl'
        submitBtn.click()

        then: 'I get to the configuration overview page'
        waitFor { at ConfigOverviewPage }

        and: 'the mail configuration has been disabled'
        def config = ConfigHolder.instance
        (config['mailUseConfig'] as Boolean)
        '192.168.100.1' == config['mailHost'] as String
        465 == config['mailPort'] as Integer
        (config['mailAuth'] as Boolean)
        'jsmith' == config['mailUserName'] as String
        'very-secret' == config['mailPassword'] as String
        'ssl' == config['mailEncryption'] as String

        when: 'I again click the mail configuration item'
        item('mail').link.click()

        then: 'I get to the mail configuration page and all controls are disabled'
        waitFor { at ConfigMailPage }
        'true' == form.'config.mailUseConfig'
        checkUserConfiguration()
    }

    def 'Save mail settings with authentication but without password'() {
        given: 'I go to the system settings overview page'
        to ConfigOverviewPage

        and: 'I click the mail configuration item'
        item('mail').link.click ConfigMailPage

        when: 'I set mail configuration to system and save'
        form.'config.mailHost' = '192.168.100.1'
        form.'config.mailPort' = '465'
        form.'config.mailUserName' = 'jsmith'
        form.'config.mailEncryption' = 'ssl'
        submitBtn.click()

        then: 'I get to the configuration overview page'
        waitFor { at ConfigOverviewPage }

        and: 'the mail configuration has been disabled'
        def config = ConfigHolder.instance
        (config['mailUseConfig'] as Boolean)
        '192.168.100.1' == config['mailHost'] as String
        465 == config['mailPort'] as Integer
        (config['mailAuth'] as Boolean)
        'jsmith' == config['mailUserName'] as String
        'secret' == config['mailPassword'] as String
        'ssl' == config['mailEncryption'] as String

        when: 'I again click the mail configuration item'
        item('mail').link.click()

        then: 'I get to the mail configuration page and all controls are disabled'
        waitFor { at ConfigMailPage }
        'true' == form.'config.mailUseConfig'
        checkUserConfiguration()
    }

    def 'Save mail settings without authentication'() {
        given: 'I go to the system settings overview page'
        to ConfigOverviewPage

        and: 'I click the mail configuration item'
        item('mail').link.click ConfigMailPage

        when: 'I set mail configuration to system and save'
        form.'config.mailHost' = '192.168.100.1'
        form.'config.mailPort' = '465'
        form.'config.mailAuth' = 'false'
        form.'config.mailEncryption' = 'ssl'
        submitBtn.click()

        then: 'I get to the configuration overview page'
        waitFor { at ConfigOverviewPage }

        and: 'the mail configuration has been disabled'
        def config = ConfigHolder.instance
        (config['mailUseConfig'] as Boolean)
        '192.168.100.1' == config['mailHost'] as String
        465 == config['mailPort'] as Integer
        !(config['mailAuth'] as Boolean)
        'jdoe' == config['mailUserName'] as String
        'secret' == config['mailPassword'] as String
        'ssl' == config['mailEncryption'] as String

        when: 'I again click the mail configuration item'
        item('mail').link.click()

        then: 'I get to the mail configuration page and all controls are disabled'
        waitFor { at ConfigMailPage }
        'true' == form.'config.mailUseConfig'
        checkFieldsWithoutAuthFields true
        checkAuthFields false
    }


    //-- Non-public methods ---------------------

    void checkAuthFields(boolean disabled) {
        form.find('input', name: ~/^config\.mail(UserName|Password)$/).every {
            it.disabled == disabled
        }
    }

    void checkFieldsWithoutAuthFields(boolean disabled) {
        form.find('input', name: startsWith('config.mail')).every {
            it.@name == 'config.mailUserName' ||
                it.@name == 'config.mailPassword' ||
                it.disabled == disabled
        }
    }

    void checkNonUserConfiguration() {
        assert !form.'config.mailUseConfig'().disabled
        assert form.find('input', name: startsWith('config.mail')).every {
            it.@name == 'config.mailUseConfig' || it.disabled
        }
    }

    void checkUserConfiguration() {
        assert form.find('input', name: startsWith('config.mail')).every {
            !it.disabled
        }
    }
}
