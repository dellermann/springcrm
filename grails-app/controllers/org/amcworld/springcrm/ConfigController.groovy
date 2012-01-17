package org.amcworld.springcrm

class ConfigController {

    def index() {}

    def show() {
        def configData = Config.list()
        Map<String, Object> config = [:]
        configData.each { config[it.name] = it.value }

        render(view: params.page, model: [configData: config])
    }

    def save() {
        def configHolder = ConfigHolder.instance
        params.config.each {
            configHolder.setConfig(it.key, it.value)
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'config.label', default: 'System setting'), ''])
        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'index')
        }
    }
}
