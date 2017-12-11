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
class InstallController {

    //-- Fields ---------------------------------

    InstallService installService
    SecurityService securityService


    //-- Public methods -------------------------

    def clientData() {
        respond([client: Client.load(), step: 2])
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
        respond([userInstance: new User(), step: 3])
    }

    def createAdminSave() {
        User user = new User(params)
        user.admin = true

        String pwdRepeat = securityService.encryptPassword(
            params.passwordRepeat.toString()
        )
        boolean passwordMismatch = params.password != pwdRepeat
        if (passwordMismatch) {
            user.errors.rejectValue 'password', 'user.password.doesNotMatch'
        }
        if (passwordMismatch || !user.save(flush: true)) {
            render(
                view: 'createAdmin',
                model: [userInstance: user, step: 3]
            )
            return
        }

        redirect action: 'finish'
    }

    def finish() {
        respond([step: 4])
    }

    def finishSave() {
        installService.disableInstaller()
        Config config = new Config(name: 'installStatus', value: 1)
        config.save flush: true

        redirect controller: 'overview', action: 'index'
    }

    def index() {}

    def installBaseData() {
        def installStatus = Config.findByName('installStatus')

        respond([
            existingData: installStatus?.value,
            packages: installService.getBaseDataPackages(),
            step: 1
        ])
    }

    def installBaseDataSave() {
        installService.installBaseDataPackage(
            params['package-select']?.toString()
        )

        redirect action: 'clientData'
    }
}
