/*
 * ViewFilters.groovy
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

import org.codehaus.groovy.grails.commons.GrailsClass


/**
 * The class {@code ViewFilters} contains various filters concerning the view.
 *
 * @author  Daniel Ellermann
 * @version 1.2
 */
class ViewFilters {

    def dependsOn = [LoginFilters]

    def filters = {
        pagination(controller: '*', action: 'list') {
            def sessionKey = { String name ->
                String key = name + controllerName.capitalize()
                if (params.type) key += params.type
                return key
            }

            before = {
                def f = { pk, s, sk ->
                    if (params[pk] == null) params[pk] = s[sk]
                    else s[sk] = params[pk]
                }

                /* store or restore offset */
                String key = sessionKey('offset')
                f('offset', session, key)

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
                f('sort', user.settings, "sort${name}")
                f('order', user.settings, "order${name}")
                user.save(flush: true)
            }

            after = {
                session[sessionKey('offset')] = params.offset
            }
        }

        selectorView(controller: '*', action: 'list') {
            after = { model ->
                String view = (params.view == 'selector') ? 'selectorList' : 'list'
                render(view: "/${controllerName}/${view}", model: model)
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
                    redirect(controller: controllerName, action: 'list')
                }
            }
        }

        invoicingItems(controller: 'quote|salesOrder|invoice|dunning|creditMemo|purchaseInvoice',
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
