package org.amcworld.springcrm

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.GrailsClass;
import org.codehaus.groovy.grails.web.json.JSONObject


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

    def loadTaxRates() {
        def list = TaxClass.list(sort: 'orderId')
        render(contentType: 'text/json') {
            array {
                for (i in list) {
                    item id: i.ident(), name: (i.taxValue * 100d).round(2)
                }
            }
        }
    }

    def saveSelValues() {
        for (Map.Entry item in params.selValues?.entrySet()) {
            def data = JSON.parse(item.value)
            println item.key + ': ' + data.dump()
        }
//        Class<?> cls = typeClass
        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'index')
        }
    }

    def saveTaxRates() {
        String taxRates = params.selValues?.taxRates
        if (taxRates) {
            int orderId = 10
            def list = JSON.parse(taxRates)
            for (def item in list) {
                def entry = (item.id < 0) ? new TaxClass() : TaxClass.get(item.id)
                if (item.isNull('name')) {
                    entry.delete(flush: true)
                } else {
                    entry.name = "${item.name} %"
                    entry.orderId = orderId
                    entry.taxValue = (item.name as Double) / 100d
                    entry.save(flush: true)
                    orderId += 10
                }
            }
        }

        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'index')
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
