/*
 * InstallController.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import org.hibernate.SessionFactory


/**
 * The class {@code InstallController} handles actions in the installation
 * area of the application.  The installation area is called when the
 * application is started for the first time or when a user calls it manually.
 * In order to get access to the installation area the user must create a file
 * named {@code ENABLE_INSTALLER} in folder {@code WEB-INF/data/install}.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class InstallController {

    //-- Fields ---------------------------------

    SessionFactory sessionFactory
    InstallService installService
    SecurityService securityService


    //-- Public methods -------------------------

    def index() {}

    def installBaseData() {
        def installStatus = Config.findByName('installStatus')

        [
            existingData: installStatus?.value,
            packages: installService.getBaseDataPackages(),
            step: 1
        ]
    }

    def installBaseDataSave() {
        installService.installBaseDataPackage(
            sessionFactory.currentSession.connection(), params.'package-select'
        )

        redirect action: 'clientData'
    }

    def clientData() {
        [client: Client.load(), step: 2]
    }

    def clientDataSave(Client client) {
        if (client.hasErrors()) {
            render view: 'clientData', model: [client: client, step: 2]
            return
        }
        client.save()

        redirect action: 'createAdmin'
    }

    def createAdmin() {
        [userInstance: new User(), step: 3]
    }

    def createAdminSave() {
        def userInstance = new User(params)
        userInstance.admin = true

        String pwdRepeat = securityService.encryptPassword(
            params.passwordRepeat.toString()
        )
        boolean passwordMismatch = params.password != pwdRepeat
        if (passwordMismatch) {
            userInstance.errors.rejectValue(
                'password', 'user.password.doesNotMatch'
            )
        }
        if (passwordMismatch || !userInstance.save(flush: true)) {
            render(
                view: 'createAdmin',
                model: [userInstance: userInstance, step: 3]
            )
            return
        }

        redirect action: 'finish'
    }

    def finish() {
        [step: 4]
    }

    def finishSave() {
        installService.disableInstaller()
        Config installStatus = new Config(name: 'installStatus', value: 1)
        installStatus.save flush: true

        redirect controller: 'overview', action: 'index'
    }
}
