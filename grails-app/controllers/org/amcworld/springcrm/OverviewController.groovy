/*
 * OverviewController.groovy
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
import static javax.servlet.http.HttpServletResponse.SC_OK

import grails.plugin.springsecurity.annotation.Secured
import java.text.NumberFormat
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code OverviewController} contains actions which display the
 * overview page and handle the panels.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured('isAuthenticated()')
class OverviewController {

    //-- Fields ---------------------------------

    LruService lruService
    OverviewService overviewService
    SeqNumberService seqNumberService


    //-- Public methods -------------------------

    /**
     * Adds a new panel with the given ID and position.
     *
     * @param panelId   the given panel ID
     * @param pos       the zero-based position of the panel on the overview
     *                  page
     */
    def addPanel(String panelId, Integer pos) {
        new Panel(user: (User) authenticatedUser, pos: pos, panelId: panelId)
            .save flush: true

        render status: SC_OK
    }

    /**
     * Renders the part of the changelog file in
     * {@code WEB-INF/data/changelog.md} until the marker
     * {@code [comment]: STOP}.
     *
     * @return  the part of the Markdown changelog
     * @since   2.0
     */
    def changelog() {
        render(
            text: overviewService.getChangelog(LCH.locale),
            contentType: 'text/markdown',
            encoding: 'UTF-8'
        )
    }

    /**
     * Stores the current version in the user settings to prevent display of
     * changelog for this version.
     *
     * @return  always HTTP status code 200 (OK)
     * @since   2.0
     */
    def changelogDontShowAgain() {
        overviewService.dontShowAgain((User) authenticatedUser)

        render status: SC_OK
    }

    def index() {
        OverviewPanelRepository repository = OverviewPanelRepository.instance
        User user = (User) authenticatedUser

        List<Panel> panels = Panel.findAllByUser(user)
        for (Panel panel : panels) {
            panel.panelDef = repository.getPanel(panel.panelId)
        }

        boolean showSeqNumberChangeHint =
            !session['dontShowSeqNumberChangeHint'] &&
            !seqNumberService.checkNumberScheme()
        if (showSeqNumberChangeHint) {
            session['dontShowSeqNumberChangeHint'] = true
        }
        boolean showChangelog = !session['dontShowChangelog'] &&
            overviewService.showChangelog(user)
        if (showChangelog) {
            session['dontShowChangelog'] = true
        }

        respond([
            allPanelDefs: repository.panels.values(),
            panels: panels,
            showSeqNumberChangeHint: showSeqNumberChangeHint,
            showChangelog: showChangelog,
            user: user
        ])
    }

    def listAvailablePanels() {
        respond([repository: OverviewPanelRepository.instance, l: LCH.locale])
    }

    def lruList() {
        respond lruService.retrieveLruEntries()
    }

    def movePanel(String panelId1, Integer pos1, String panelId2, Integer pos2)
    {
        User user = (User) authenticatedUser
        Panel panel1 = Panel.findByUserAndPanelId(user, panelId1)
        Panel panel2 = Panel.findByUserAndPanelId(user, panelId2)
        if (!panel1 || !panel2) {
            render status: SC_NOT_FOUND
            return
        }

        panel1.pos = pos1
        panel1.save flush: true
        panel2.pos = pos2
        panel2.save flush: true

        render status: SC_OK
    }

    def removePanel(String panelId) {
        Panel panel = Panel.findByUserAndPanelId(
            (User) authenticatedUser, panelId
        )
        if (!panel) {
            render status: SC_NOT_FOUND
            return
        }

        panel.delete flush: true

        render status: SC_OK
    }

    /**
     * Stores the selection whether or not to show the hint about changing the
     * sequence number scheme.
     *
     * @param value the selection what to not show again: possible values are
     *              0 (show again), 1 (don't show again for this year) and 2
     *              (never show again)
     * @return      always HTTP status code 200 (OK)
     * @since 2.1
     */
    def seqNumberHintDontShowAgain(int value) {
        User user = (User) authenticatedUser
        switch (value) {
        case 1:
            seqNumberService.setDontShowAgain user
            break
        case 2:
            seqNumberService.setNeverShowAgain user
            break
        }

        render status: SC_OK
    }

    /**
     * Stores the settings for the list of unpaid bills as user settings.
     *
     * @param minimum   the minimum of unpaid amount that should be displayed;
     *                  the value is parsed as localized formatted number
     * @param sort      the property used for sorting
     * @param order     the order of sorting, either {@code asc} or {@code desc}
     * @param max       the maximum number of items that should be displayed
     */
    def settingsUnpaidBillsSave(String minimum, String sort, String order,
                                String max)
    {
        NumberFormat formatter = NumberFormat.getInstance(request.locale)
        BigDecimal min = formatter.parse(minimum) as BigDecimal

        User user = (User) authenticatedUser
        user.settings.putAll(
            unpaidBillsMinimum: min <= BigDecimal.ZERO ? '' : min.toString(),
            unpaidBillsSort: sort,
            unpaidBillsMax: max,
            unpaidBillsOrder: order
        )
        user.save flush: true

        redirect action: 'index'
    }
}
