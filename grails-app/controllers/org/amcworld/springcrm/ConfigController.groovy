package org.amcworld.springcrm

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.GrailsClass;
import org.codehaus.groovy.grails.web.json.JSONObject


class ConfigController {

    protected static final List<Long> READONLY_IDS = [
        *600L..604L, *800L..804L, *900L..907L, *2100L..2103L, *2200L..2206L,
        *2500L..2504L
    ]

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
        def list = getTypeClass(params.type).list(sort: 'orderId')
        render(contentType: 'text/json') {
            array {
                for (i in list) {
                    item id: i.ident(), name: i.name, disabled: i.ident() in READONLY_IDS
                }
            }
        }
    }

    def loadTaxRates() {
        def list = TaxRate.list(sort: 'orderId')
        render(contentType: 'text/json') {
            array {
                for (i in list) {
                    item id: i.ident(), name: (i.taxValue * 100d).round(2)
                }
            }
        }
    }

    def loadSeqNumbers() {
        def list = SeqNumber.list()
        render(view: 'seqNumbers', model: [seqNumberList: list])
    }

    def saveSelValues() {
        for (Map.Entry entry in params.selValues?.entrySet()) {
            int orderId = 10
            Class<?> cls = getTypeClass(entry.key)
            def list = JSON.parse(entry.value)
            for (def item in list) {
                Long id = item.id as Long
                def selValue = (id < 0L) ? cls.newInstance() : cls.get(id)
                if (item.remove) {
                    selValue.delete(flush: true)
                } else {
                    if (!(id in READONLY_IDS)) {
                        selValue.name = item.name
                    }
                    selValue.orderId = orderId
                    selValue.save(flush: true)
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

    def saveTaxRates() {
        String taxRates = params.selValues?.taxRates
        if (taxRates) {
            int orderId = 10
            def list = JSON.parse(taxRates)
            for (def item in list) {
                def entry = (item.id < 0) ? new TaxRate() : TaxRate.get(item.id)
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

    def saveSeqNumbers() {
        def l = []
        boolean hasErrors = false
        for (entry in params.seqNumbers) {
            try {
                Long id = Long.valueOf(entry.key)
                SeqNumber seqNumber = SeqNumber.get(id)
                seqNumber.properties['prefix', 'suffix', 'startValue', 'endValue'] = entry.value
                l.add(seqNumber)
                hasErrors |= (seqNumber.save(flush: true) == null)
            } catch (NumberFormatException e) { /* ignored */ }
        }
        if (hasErrors) {
            l.sort { it.ident() }
            render(view: 'seqNumbers', model: [seqNumberList: l])
            return
        }

        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'index')
        }
    }

    private Class<?> getTypeClass(String type) {
        GrailsClass gc = grailsApplication.getArtefactByLogicalPropertyName('Domain', type)
        Class<?> cls = gc.clazz
        if (!SelValue.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Type ${type} must be of type SelValue.")
        }
        return cls
    }
}
