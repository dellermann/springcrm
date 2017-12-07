/*
 * TicketController.groovy
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

import javax.servlet.http.HttpServletResponse
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code TicketController} handles actions concerning tickets in a
 * helpdesk.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   1.4
 */
class TicketController extends GenericDomainController<Ticket> {

    //-- Constants ------------------------------

    private static final EnumSet<TicketStage> REQ_STAGES_ASSIGN =
        EnumSet.of(TicketStage.assigned, TicketStage.inProcess)
    private static final EnumSet<TicketStage> REQ_STAGES_SEND_MESSAGE =
        REQ_STAGES_ASSIGN
    private static final EnumSet<TicketStage> REQ_STAGES_TAKE_ON =
        EnumSet.of(TicketStage.created, TicketStage.resubmitted)


    //-- Fields ---------------------------------

    HelpdeskService helpdeskService
    HelpdeskUserService helpdeskUserService
    MailSystemService mailSystemService
    OrganizationService organizationService
    TicketService ticketService


    //-- Constructors ---------------------------

    TicketController() {
        super(Ticket)
    }


    //-- Public methods -------------------------

    def assignToUser(Long id, Long user) {
        Ticket ticketInstance = getDomainInstance(id)
        if (ticketInstance == null) {
            return
        }

        User creator = getUser()
        User recipient = User.get(user)
        if (recipient &&
            (recipient in ticketInstance.helpdesk.users) &&
            creator != recipient &&
            (creator.admin || creator == ticketInstance.assignedUser) &&
            ticketInstance.stage in REQ_STAGES_ASSIGN)
        {
            ticketService.assignUser ticketInstance, creator, recipient
        }

        redirect action: 'show', id: id
    }

    def changeStage(Long id, String stage) {
        Ticket ticketInstance = getDomainInstance(id)
        if (ticketInstance == null) {
            return
        }

        if (user.admin || user == ticketInstance.assignedUser) {
            EnumSet<TicketStage> allowedStages
            switch (ticketInstance.stage) {
            case TicketStage.assigned:
                allowedStages = EnumSet.of(
                    TicketStage.inProcess, TicketStage.closed
                )
                break
            case TicketStage.inProcess:
                allowedStages = EnumSet.of(TicketStage.closed)
                break
            case TicketStage.closed:
                allowedStages = EnumSet.of(TicketStage.resubmitted)
                break
            default:
                allowedStages = EnumSet.noneOf(TicketStage)
                break
            }

            TicketStage requestedStage = TicketStage.valueOf(stage)
            if (requestedStage in allowedStages) {
                ticketService.changeStage ticketInstance, requestedStage, user
            }
        }

        redirect action: 'show', id: id
    }

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def createNote(Long id) {
        String message = params.messageText
        if (message) {
            Ticket ticketInstance = getDomainInstance(id)
            if (ticketInstance == null) {
                return
            }

            ticketService.createNote(
                ticketInstance, message, (MultipartFile) params.attachment,
                user
            )
        }

        redirect action: 'show', id: id
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def frontendCloseTicket(Long id) {
        frontendChangeStage id, TicketStage.closed
    }

    def frontendCreate() {
        Helpdesk helpdeskInstance = helpdesk
        if (helpdeskInstance == null) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        Organization organization = helpdeskInstance.organization
        Ticket ticketInstance = new Ticket(
            address: organization?.shippingAddr, phone: organization?.phone,
            fax: organization?.fax, email1: organization?.email1,
            email2: organization?.email2
        )

        getFrontendModel ticketInstance, helpdeskInstance
    }

    def frontendResubmitTicket(Long id) {
        frontendChangeStage id, TicketStage.resubmitted
    }

    def frontendSave() {
        Helpdesk helpdeskInstance = helpdesk
        if (helpdeskInstance == null) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }
        params.helpdesk = helpdeskInstance

        Ticket ticketInstance = new Ticket(params)
        ticketInstance.stage = TicketStage.created
        ticketInstance.address = new Address()
        String messageText = ticketInstance.messageText = params.messageText

        if (!ticketInstance.validate() || !messageText) {
            if (log.debugEnabled) {
                log.debug "Ticket validation failed: ${ticketInstance.errors}"
            }
            if (!messageText) {
                ticketInstance.errors.rejectValue(
                    'messageText', 'default.blank.message'
                )
            }

            render(
                view: '/ticket/frontendCreate',
                model: getFrontendModel(ticketInstance, helpdeskInstance)
            )
            return
        }

        ticketInstance = ticketService.createTicket(
            ticketInstance, messageText, params.attachment as MultipartFile
        )
        if (ticketInstance == null) {
            log.debug 'No ticket created.'
            render(
                view: '/helpdesk/frontendIndex',
                model: getFrontendModel(ticketInstance, helpdeskInstance)
            )
            return
        }

        flash.message = message(
            code: 'default.created.message',
            args: [label, ticketInstance.toString()]
        ) as Object
        if (helpdeskInstance.forEndUsers) {
            def params = [
                accessCode: helpdeskInstance.accessCode,
                helpdesk: helpdeskInstance.id,
                id: ticketInstance.id
            ]
            redirect mapping: 'ticketFrontendShow', params: params
        } else {
            redirectToHelpdeskFrontend helpdeskInstance
        }
    }

    def frontendSendMessage(Long id) {
        Ticket ticketInstance = lowLevelGet(id)
        if (ticketInstance == null) {
            redirectToHelpdeskFrontend helpdesk
            return
        }

        String msg = params.message
        if (!msg) {
            redirectToFrontendPage ticketInstance.helpdesk
            return
        }

        ticketService.sendMessage(
            ticketInstance, msg, params.attachment as MultipartFile
        )

        flash.message = message(code: 'ticket.sendMessage.flash') as Object
        redirectToFrontendPage ticketInstance.helpdesk
    }

    def frontendShow(Long id) {
        Ticket ticketInstance = ticketService.get(id)

        getFrontendModel ticketInstance, ticketInstance.helpdesk
    }

    def index() {
        List<Ticket> list
        int count

        if (params.helpdesk) {
            Helpdesk helpdesk =
                helpdeskService.get(params.long('helpdesk'))
            list = ticketService.findAllByHelpdesk(helpdesk)
            count = ticketService.countByHelpdesk(helpdesk)
        } else if (admin) {
            list = ticketService.list(params)
            count = ticketService.count()
        } else {
            List<HelpdeskUser> helpdeskUsers =
                helpdeskUserService.findAllByUser(user)
            if (!helpdeskUsers) {
                render view: 'noHelpdesks'
                return
            }

            List<Helpdesk> helpdesks = helpdeskUsers*.helpdesk
            list = ticketService.findAllByHelpdeskInList(helpdesks, params)
            count = ticketService.countByHelpdeskInList(helpdesks)
        }

        Map model = getIndexModel(list, count)
        model.mailSystemConfigured = mailSystemService.configured

        model
    }

    def listEmbedded(Long organization) {
        Organization organizationInstance =
            organizationService.get(organization)

        List<Ticket> list = ticketService.findAllByUserAndOrganization(
            user, organizationInstance, params
        )
        int count = ticketService.countByUserAndOrganization(
            user, organizationInstance
        )

        getListEmbeddedModel(
            list, count, [organization: organizationInstance.id]
        )
    }

    def save() {
        super.save()
    }

    def sendMessage(Long id) {
        String msg = params.message
        if (msg) {
            Ticket ticketInstance = getDomainInstance(id)
            if (ticketInstance == null) {
                return
            }

            User creator = user
            User recipient =
                params.recipient ? User.get(params.long('recipient')) : null
            if (recipient || creator.admin ||
                (creator == ticketInstance.assignedUser &&
                ticketInstance.stage in REQ_STAGES_SEND_MESSAGE))
            {
                ticketService.sendMessage(
                    ticketInstance, msg, (MultipartFile) params.attachment,
                    creator, recipient
                )
            }
        }

        redirect action: 'show', id: id
    }

    def show(Long id) {
        super.show id
    }

    def takeOn(Long id) {
        Ticket ticketInstance = getDomainInstance(id)
        if (ticketInstance != null) {
            if ((admin || user in ticketInstance.helpdesk.users) &&
                ticketInstance.stage in REQ_STAGES_TAKE_ON)
            {
                ticketInstance.stage = TicketStage.assigned
                ticketService.assignUser ticketInstance, user, user
            }

            redirect action: 'show', id: id
        }
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods ---------------------

    /**
     * Changes the stage of the ticket with the given ID as requested from
     * frontend.
     *
     * @param id    the ID of the ticket
     * @param stage the stage that should be changed to
     */
    private void frontendChangeStage(Long id, TicketStage stage) {
        Ticket ticketInstance = lowLevelGet(id)
        if (ticketInstance == null) {
            redirectToHelpdeskFrontend helpdesk
            return
        }

        ticketService.changeStage ticketInstance, stage
        flash.message = message(code: "ticket.${stage}.flash") as Object
        redirectToFrontendPage ticketInstance.helpdesk
    }

    @Override
    protected Map getCreateModel(Ticket instance) {
        Map res = super.getCreateModel(instance)
        res.helpdeskInstanceList = helpdesks

        res
    }

    @Override
    protected Map getEditModel(Ticket instance) {
        Map res = super.getEditModel(instance)
        res.helpdeskInstanceList = helpdesks

        res
    }

    @Override
    protected void lowLevelDelete(Ticket instance) {
        ticketService.delete instance.id
    }

    @Override
    protected Ticket lowLevelGet(Long id) {
        ticketService.get id
    }

    @Override
    protected Ticket lowLevelSave(Ticket instance) {
        ticketService.save instance
    }

    /**
     * Gets the model which is used in frontend views containing the given
     * ticket and helpdesk instance.
     *
     * @param ticketInstance    the given ticket instance
     * @param helpdeskInstance  the associated helpdesk
     * @return                  the model for the frontend views
     * @since 2.2
     */
    private Map<String, Object> getFrontendModel(Ticket ticketInstance,
                                                 Helpdesk helpdeskInstance)
    {
        [
            (domainInstanceName): ticketInstance,
            helpdeskInstance: helpdeskInstance
        ]
    }

    /**
     * Gets the helpdesk with the ID submitted in parameter {@code helpdesk}.
     *
     * @return  the helpdesk or {@code null} if no such a helpdesk exists
     * @since   2.2
     */
    private Helpdesk getHelpdesk() {
        helpdeskService.get params.long('helpdesk')
    }

    /**
     * Gets the helpdesks the current user may access.
     *
     * @return  the list of helpdesks
     */
    private List<Helpdesk> getHelpdesks() {
        user.admin ? helpdeskService.list()
            : helpdeskUserService.findAllByUser(user)*.helpdesk.unique()
    }

    @Override
    protected Ticket saveInstance(Ticket instance) {
        String messageText = instance.messageText = params.messageText

        if (!instance.validate() || !messageText) {
            if (!messageText) {
                instance.errors.rejectValue(
                    'messageText', 'default.blank.message'
                )
            }
            return null
        }

        ticketService.createTicket(
            instance, messageText, (MultipartFile) params.attachment
        )
    }

    /**
     * Redirects the to origin helpdesk frontend page, either to the overview
     * page or the show view of a ticket.
     *
     * @param helpdesk  the given helpdesk
     */
    private void redirectToFrontendPage(Helpdesk helpdesk) {
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirectToHelpdeskFrontend helpdesk
        }
    }

    /**
     * Redirects to the frontend index page of the given helpdesk.
     *
     * @param helpdesk  the given helpdesk
     */
    private void redirectToHelpdeskFrontend(Helpdesk helpdesk) {
        redirect(
            mapping: 'helpdeskFrontend',
            params: [accessCode: helpdesk.accessCode, urlName: helpdesk.urlName]
        )
    }
}
