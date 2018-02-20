/*
 * InstallController.groovy
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

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException


/**
 * The class {@code InstallController} handles actions in the installation
 * area of the application.  The installation area is called when the
 * application is started for the first time or when a user calls it manually.
 * In order to get access to the installation area the user must create a file
 * named {@code ENABLE_INSTALLER} in folder {@code WEB-INF/data/install}.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured('permitAll')
class InstallController {

    //-- Fields ---------------------------------

    ConfigService configService
    InstallService installService
    UserService userService


    //-- Public methods -------------------------

    def clientData() {
        respond client: configService.loadTenant(), step: 2
    }

    def clientDataSave(Tenant client) {
        if (client.hasErrors()) {
            respond([client: client, step: 2], [view: 'clientData'])
            return
        }
        configService.storeTenant client

        redirect action: 'createAdmin'
    }

    def createAdmin() {
        respond new User(), model: [step: 3]
    }

    def createAdminSave() {
        User user = new User(params)
        RoleGroup group = installService.installAdminGroup(
            message(code: 'roleGroup.admin') as String
        )
        user.authorities = [group] as Set

        if (!user.password) {
            user.errors.rejectValue(
                'password', 'default.blank.message',
                [message(code: 'user.password.label')] as Object[], ''
            )
        } else if (params.password != params.passwordRepeat) {
            user.errors.rejectValue 'password', 'user.password.doesNotMatch'
        } else {
            user.password = userService.encodePassword(user.password)
        }

        try {
            userService.save user
        } catch (ValidationException ignored) {
            respond user.errors, model: [step: 3], view: 'createAdmin'
            return
        }

        redirect action: 'finish'
    }

    def finish() {
        respond step: 4
    }

    def finishSave() {
        installService.disableInstaller()
        Config config = new Config(name: 'installStatus', value: 1)
        config.save flush: true

        redirect controller: 'overview', action: 'index'
    }

    def index() {}

    def installBaseData() {
        respond(
            existingData: Config.findByName('installStatus')?.value,
            packages: installService.getBaseDataPackages(),
            step: 1
        )
    }

    def installBaseDataSave() {
        installService.installBaseDataPackage(
            params['package-select']?.toString()
        )

        redirect action: 'clientData'
    }
}
