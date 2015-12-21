/*
 * OverviewController.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code OverviewController} contains actions which display the
 * overview page and handle the panels.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class OverviewController {

    //-- Instance variables ---------------------

    LruService lruService


    //-- Public methods -------------------------

    def index() {
        OverviewPanelRepository repository = OverviewPanelRepository.instance

        List<Panel> panels = Panel.findAllByUser(session.user)
        for (Panel panel : panels) {
            panel.panelDef = repository.getPanel(panel.panelId)
        }

        [panels: panels]
    }

    def listAvailablePanels() {
        OverviewPanelRepository repository = OverviewPanelRepository.instance
        Locale locale = LCH.locale

        render(contentType: 'text/json') {
            for (Map.Entry<String, OverviewPanel> entry in repository.panels) {
                setProperty(entry.key, {
                    OverviewPanel p = entry.value
                    title = p.getTitle(locale)
                    description = p.getDescription(locale)
                    url = p.url
                    style = p.style
                })
            }
        }
    }

    def lruList() {
        [lruList: lruService.retrieveLruEntries()]
    }

    def addPanel(String panelId, Integer pos) {
        new Panel(user: session.user, pos: pos, panelId: panelId)
            .save flush: true
        render status: SC_OK
    }

    def movePanel(String panelId1, Integer pos1, String panelId2, Integer pos2)
    {
        User user = session.user
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
        Panel panel = Panel.findByUserAndPanelId(session.user, panelId)
        if (!panel) {
            render status: SC_NOT_FOUND
            return
        }

        panel.delete flush: true

        render status: SC_OK
    }
}
