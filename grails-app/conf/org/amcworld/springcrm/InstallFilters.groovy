package org.amcworld.springcrm

class InstallFilters {

    def installService

    def filters = {
        security(controller:'install', action:'*') {
            before = {
                if (installService.isInstallerDisabled()) {
                    redirect(uri: '/')
                    return false
                }

                return true
            }
        }
    }
}
