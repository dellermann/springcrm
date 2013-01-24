/*
 * InstallController.groovy
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

import groovy.sql.Sql


/**
 * The class {@code InstallController} handles actions in the installation
 * area of the application.  The installation area is called when the
 * application is started for the first time or when a user calls it manually.
 * In order to get access to the installation area the user must create a file
 * named {@code ENABLE_INSTALLER} in folder {@code WEB-INF/data/install}.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class InstallController {

    //-- Instance variables ---------------------

    def sessionFactory
    def installService
    def securityService


    //-- Public methods -------------------------

    def index() {}

    def installBaseData() {
        def installStatus = Config.findByName('installStatus')
        return [packages: installService.getBaseDataPackages(), existingData: installStatus?.value, step: 1]
    }

    def installBaseDataSave() {
        InputStream is = installService.loadPackage(params.package)
        Reader r = is.newReader('utf-8')
        Sql sql = new Sql(sessionFactory.currentSession.connection())
        r.eachLine {
            if (!(it =~ /^\s*$/) && !(it =~ /^\s*--/)) {
                sql.execute it
            }
        }
        redirect(action: 'clientData')
    }

    def clientData() {
        return [client: Client.load(), step: 2]
    }

    def clientDataSave(Client client) {
        if (client.hasErrors()) {
            render(view: 'clientData', model: [client: client, step: 2])
            return
        }
        client.save()
        redirect(action: 'createAdmin')
    }

    def createAdmin() {
        return [userInstance: new User(), step: 3]
    }

    def createAdminSave() {
        def userInstance = new User(params)
        userInstance.admin = true

        boolean passwordMismatch =
            params.password != securityService.encryptPassword(params.passwordRepeat)
        if (passwordMismatch) {
            userInstance.errors.rejectValue('password', 'user.password.doesNotMatch')
        }
        if (passwordMismatch || !userInstance.save(flush: true)) {
            render(view: 'createAdmin', model: [userInstance: userInstance, step: 3])
            return
        }

        redirect(action: 'finish')
    }

    def finish() {
        return [step: 4]
    }

    def finishSave() {
        installService.disableInstaller()
        Config installStatus = new Config(name: 'installStatus', value: 1)
        installStatus.save(flush: true)
        redirect(uri: '/')
    }
}
