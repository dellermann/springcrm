/*
 * ConfigController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.GrailsClass


/**
 * The class {@code ConfigController} handles actions to configure system
 * settings such as client data, currency, selection values etc.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
class ConfigController {

    //-- Constants ------------------------------

    /**
     * A list of IDs of selector values which are considered read-only.
     */
    protected static final List<Long> READONLY_IDS = [
        *600L..604L, *800L..804L, *900L..907L, *2100L..2103L, *2200L..2206L,
        *2500L..2504L
    ]


    //-- Public methods -------------------------

    def index() {}

    def show() {
        def configData = ConfigHolder.instance.allConfig
        Map<String, Object> config = [: ]
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

    def loadClient() {
        return [client: Client.load()]
    }

    def saveClient(Client client) {
        if (client.hasErrors()) {
            render(view: 'loadClient', model: [client: client])
            return
        }

        client.save()
        redirect(action: 'index')
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


    //-- Non-public methods ---------------------

    /**
     * Gets the class for the given type of selector values.
     *
     * @param type  the name of the type; the targeted domain model class must
     *              be a subclass of {@code SelValue}
     * @return      the class of the selector value
     * @see         SelValue
     */
    private Class<?> getTypeClass(String type) {
        GrailsClass gc = grailsApplication.getArtefactByLogicalPropertyName('Domain', type)
        Class<?> cls = gc.clazz
        if (!SelValue.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Type ${type} must be of type SelValue.")
        }
        return cls
    }
}
