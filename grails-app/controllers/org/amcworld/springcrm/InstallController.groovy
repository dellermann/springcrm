/*
 * InstallController.groovy
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

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured


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
@Transactional(readOnly = true)
@Secured('permitAll')
class InstallController {

    //-- Fields ---------------------------------

    InstallService installService
    UserService userService


    //-- Public methods -------------------------

    def clientData() {
        respond client: Client.load(), step: 2
    }

    @Transactional
    def clientDataSave(Client client) {
        if (client.hasErrors()) {
            respond([client: client, step: 2], [view: 'clientData'])
            return
        }
        client.save()

        redirect action: 'createAdmin'
    }

    def createAdmin() {
        respond([userInstance: new User(), step: 3])
    }

    @Transactional
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

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond(
                user.errors, view: 'createAdmin',
                model: [userInstance: user, step: 3]
            )
            return
        }

        userService.saveUser user

        redirect action: 'finish'
    }

    def finish() {
        respond step: 4
    }

    @Transactional
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

    @Transactional
    def installBaseDataSave() {
        installService.installBaseDataPackage(
            params['package-select']?.toString()
        )

        redirect action: 'clientData'
    }
}
