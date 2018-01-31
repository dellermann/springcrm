/*
 * OverviewController.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

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
    PanelService panelService
    SeqNumberService seqNumberService
    UserService userService
    UserSettingService userSettingService


    //-- Public methods -------------------------

    /**
     * Adds a new panel with the given ID and position.
     *
     * @param panelId   the given panel ID
     * @param pos       the zero-based position of the panel on the overview
     *                  page
     */
    def addPanel(String panelId, Integer pos) {
        panelService.savePanel new Panel(user: user, pos: pos, panelId: panelId)

        render status: OK
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
        overviewService.dontShowAgain user

        render status: OK
    }

    def index() {
        OverviewPanelRepository repository = OverviewPanelRepository.instance
        User user = getUser()

        List<Panel> panels = panelService.findAllByUser(user)
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

        respond(
            allPanelDefs: repository.panels.values(),
            panels: panels,
            showSeqNumberChangeHint: showSeqNumberChangeHint,
            showChangelog: showChangelog,
            user: user
        )
    }

    def listAvailablePanels() {
        respond repository: OverviewPanelRepository.instance, l: LCH.locale
    }

    def lruList() {
        respond lruService.retrieveLruEntries()
    }

    def movePanel(String panelId1, Integer pos1, String panelId2, Integer pos2)
    {
        User user = getUser()
        Panel panel1 = panelService.findByUserAndPanelId(user, panelId1)
        Panel panel2 = panelService.findByUserAndPanelId(user, panelId2)
        if (panel1 == null || panel2 == null) {
            render status: NOT_FOUND
            return
        }

        panel1.pos = pos1
        panelService.savePanel panel1
        panel2.pos = pos2
        panelService.savePanel panel2

        render status: OK
    }

    def removePanel(String panelId) {
        Panel panel = panelService.findByUserAndPanelId(user, panelId)
        if (panel == null) {
            render status: NOT_FOUND
            return
        }

        panelService.deletePanel panel.id

        render status: OK
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
        switch (value) {
        case 1:
            seqNumberService.setDontShowAgain user
            break
        case 2:
            seqNumberService.setNeverShowAgain user
            break
        }

        render status: OK
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

        User u = user
        userSettingService.store(
            u, 'unpaidBillsMinimum',
            min <= BigDecimal.ZERO ? '' : min.toString()
        )
        userSettingService.store u, 'unpaidBillsSort', sort
        userSettingService.store u, 'unpaidBillsMax', max
        userSettingService.store u, 'unpaidBillsOrder', order

        redirect action: 'index'
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the currently logged in user.
     *
     * @return  the currently logged in user
     * @since   3.0
     */
    private User getUser() {
        userService.currentUser
    }
}
