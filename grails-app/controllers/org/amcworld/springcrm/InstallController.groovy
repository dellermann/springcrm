/*
 * InstallController.groovy
 *
 * Copyright (c) 2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
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
 * @version 0.9
 */
class InstallController {

    def sessionFactory
    def installService

    def index() {}

    def clientData() {
        return [client: Client.load(), step: 1]
    }

    def clientDataSave(Client client) {
        if (client.hasErrors()) {
            render(view: 'clientData', model: [client: client, step: 1])
            return
        }
        client.save()
        redirect(action: 'createAdmin')
    }

    def createAdmin() {
        return [userInstance: new User(), step: 2]
    }

    def createAdminSave() {
        def userInstance = new User(params)
        userInstance.admin = true
        if (!userInstance.save(flush: true)) {
            render(view: 'createAdmin', model: [userInstance: userInstance, step: 1])
            return
        }

        redirect(action: 'installBaseData')
    }

    def installBaseData() {
        return [packages: installService.getBaseDataPackages(), step: 3]
    }

    def installBaseDataSave() {
        File f = installService.loadPackage(params.package)
        Sql sql = new Sql(sessionFactory.currentSession.connection())
        f.eachLine {
            sql.execute(it)
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
