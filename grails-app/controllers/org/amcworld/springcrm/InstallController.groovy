package org.amcworld.springcrm

import groovy.sql.Sql

class InstallController {

    def sessionFactory
    def installService

    def index() {}

    def createAdmin() {
        return [userInstance: new User(), step: 1]
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
        return [packages: installService.getBaseDataPackages(), step: 2]
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
        return [step: 3]
    }

    def finishSave() {
        installService.disableInstaller()
        Config installStatus = new Config(name: 'installStatus', value: 1)
        installStatus.save(flush: true)
        redirect(uri: '/')
    }
}
