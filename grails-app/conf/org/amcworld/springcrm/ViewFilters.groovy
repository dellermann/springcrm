/*
 * ViewFilters.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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

import java.text.DecimalFormatSymbols
import org.codehaus.groovy.grails.commons.GrailsClass


/**
 * The class {@code ViewFilters} contains various filters concerning the view.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 */
class ViewFilters {

    def dependsOn = [LoginFilters]

    UserService userService

    def filters = {

        def exchangeSetting =
            { def params, String paramName, def settings, String settingsKey ->
                if (params[paramName] == null) {
                    params[paramName] = settings[settingsKey]
                } else {
                    settings[settingsKey] = params[paramName]
                }
            }

        commonData(controller: '*', action: '*') {
            after = { model ->
                if (model) {
                    model.locale = userService.currentLocale.toString().replace('_', '-')
                    model.currencySymbol = userService.currencySymbol
                    model.numFractionDigits = userService.numFractionDigits
                    model.numFractionDigitsExt = userService.numFractionDigitsExt
                    model.decimalSeparator = userService.decimalSeparator
                    model.groupingSeparator = userService.groupingSeparator
                }
            }
        }

        pagination(controller: '*', action: 'list') {
            def sessionKey = { String name ->
                String key = name + controllerName.capitalize()
                if (params.type) key += params.type
                key
            }

            before = {

                /* store or restore offset */
                String key = sessionKey('offset')
                exchangeSetting params, 'offset', session, key

                /* compute number of entries of the associated domain */
                GrailsClass cls =
                    grailsApplication.getArtefactByLogicalPropertyName(
                        'Domain', controllerName
                    )
                int count = cls.clazz.'count'()
                int max = Math.min(params.max ? params.int('max') : 10, 100)
                int maxOffset = Math.floor((count - 1) / max) * max
                params.offset = Math.min(maxOffset, params.int('offset') ?: 0)
                session[key] = params.offset

                /* store or restore sorting and order */
                String name = controllerName.capitalize()
                User user = session.user
                if (!user.attached) user.attach()
                exchangeSetting params, 'sort', user.settings, "sort${name}"
                exchangeSetting params, 'order', user.settings, "order${name}"
            }

            after = {
                session[sessionKey('offset')] = params.offset
            }
        }

        salesJournalView(controller: 'report', action: 'sales-journal') {
            before = {
                User user = session.user
                if (!user.attached) user.attach()
                exchangeSetting params, 'year', session, 'salesJournalYear'
                exchangeSetting params, 'month', session, 'salesJournalMonth'
            }
        }

        selectorView(controller: '*', action: 'list') {
            after = { model ->
                String view = (params.view == 'selector') ? 'selectorList' : 'list'
                render view: "/${controllerName}/${view}", model: model
            }
        }

        deleteConfirm(controller: '*', action: 'delete') {
            before = {

                /*
                 * Normally, no delete action request without confirmed
                 * parameter should be received because the JavaScript does not
                 * send the request if the user has not confirmed the deletion.
                 * However, crafted URLs or programming errors may cause this
                 * situation happen.  If so, we simply redirect to the list
                 * view.
                 */
                if (!params.confirmed) {
                    redirect controller: controllerName, action: 'list'
                }
            }
        }

        searchIndexSave(controller: '*', action: 'save',
                        controllerExclude: 'helpdesk|user')
        {
            after = { model ->
                request["${controllerName}Instance"]?.index()
            }
        }

        searchIndexUpdate(controller: '*', action: 'update|updatePayment',
                          controllerExclude: 'helpdesk|user')
        {
            after = { model ->
                request["${controllerName}Instance"]?.reindex()
            }
        }

        invoicingItems(controller: 'quote|salesOrder|invoice|dunning|creditMemo|purchaseInvoice|product|service',
                       action: 'create|edit|copy|save|update')
        {
            after = { model ->
                if (model) {
                    model.units = Unit.list(sort: 'orderId')
                    model.taxRates = TaxRate.list(sort: 'orderId')
                }
            }
        }
    }
}
