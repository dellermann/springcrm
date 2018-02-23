/*
 * HelpdeskController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND


/**
 * The class {@code HelpdeskController} handles helpdesks.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   1.4
 */
class HelpdeskController extends OldGeneralController<Helpdesk> {

    //-- Fields ---------------------------------

    HelpdeskService helpdeskService
    MailSystemService mailSystemService


    //-- Constructors ---------------------------

    HelpdeskController() {
        super(Helpdesk)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def frontendIndex(String urlName) {
        Helpdesk helpdeskInstance = Helpdesk.findByUrlName(urlName)
        if (!helpdeskInstance) {
            render status: SC_NOT_FOUND
            return
        }

        if (helpdeskInstance.forEndUsers) {
            redirect(
                controller: 'ticket', action: 'frontendCreate',
                params: [
                    helpdesk: helpdeskInstance.id,
                    accessCode: helpdeskInstance.accessCode
                ]
            )
            return
        }

        List<Ticket> ticketInstanceList =
            Ticket.findAllByHelpdesk(helpdeskInstance, params)

        [
            (domainInstanceName): helpdeskInstance,
            ticketInstanceList: ticketInstanceList
        ]
    }

    def index() {
        Map<String, Object> model = super.index()
        model['mailSystemConfigured'] = mailSystemService.configured

        model
    }

    def listEmbedded(Long organization) {
        Organization organizationInstance = Organization.get(organization)
        List<Helpdesk> list =
            Helpdesk.findAllByOrganization(organizationInstance, params)
        int count = Helpdesk.countByOrganization(organizationInstance)

        getListEmbeddedModel(
            list, count, [organization: organizationInstance.id]
        )
    }

    def save() {
        super.save()
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods ---------------------

    @Override
    protected Helpdesk lowLevelSave() {
        helpdeskService.saveHelpdesk new Helpdesk(), params
    }

    @Override
    protected Helpdesk lowLevelUpdate(Helpdesk helpdeskInstance) {
        helpdeskService.saveHelpdesk helpdeskInstance, params
    }
}
