package org.amcworld.springcrm

import grails.converters.JSON

import org.codehaus.groovy.grails.commons.GrailsClass;

class ConfigController {

    protected static final List<Long> READONLY_IDS = [ 600L, 601L, 602L, 603L, 604L ]

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

    def loadSelValues() {
        def list = typeClass.list(sort: 'orderId')
        render(contentType: 'text/json') {
            array {
                for (i in list) {
                    item id: (i.ident() in READONLY_IDS) ? null : i.ident(), name: i.name
                }
            }
        }
    }

    def saveSelValues() {
        Class<?> cls = typeClass
        def list = request.JSON
        for (item in list) {
            println item.dump()
        }
    }

    private Class<?> getTypeClass() {
        GrailsClass gc = grailsApplication.getArtefactByLogicalPropertyName('Domain', params.type)
        Class<?> cls = gc.clazz
        if (!SelValue.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Type ${params.type} must be of type SelValue.")
        }
        return cls
    }
}
