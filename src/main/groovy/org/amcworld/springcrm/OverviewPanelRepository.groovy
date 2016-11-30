/*
 * OverviewPanelRepository.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.util.slurpersupport.GPathResult


/**
 * The class {@code OverviewPanelRepository} represents a repository of panels
 * which are used on the overview page. The repository is read from an XML file
 * by calling method {@code initialize()}.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
@CompileStatic
class OverviewPanelRepository {

    //-- Fields ---------------------------------

    private Map<String, OverviewPanel> repository


    //-- Constructors ---------------------------

    @PackageScope
    OverviewPanelRepository() {}


    //-- Public methods -------------------------

    /**
     * Gets the only instance of this class.
     *
     * @return    the instance
     */
    static OverviewPanelRepository getInstance() {
        InstanceHolder.INSTANCE
    }

    /**
     * Gets the panel with the given ID.
     *
     * @param id    the given ID
     * @return      the panel definition; {@code null} if no panel definition
     *              with the given ID exists
     */
    OverviewPanel getPanel(String id) {
        repository[id]
    }

    /**
     * Gets all panels of the repository.
     *
     * @return  a map containing all the panels; the key contains the ID of the
     *          panel, the value the panel definition
     */
    Map<String, OverviewPanel> getPanels() {
        repository
    }

    /**
     * Initializes the panel repository by reading the XML repository
     * definitions from the given input stream.
     *
     * @param is    the input stream containing the XML repository definitions
     */
    @CompileDynamic
    void initialize(InputStream is) {
        XmlSlurper slurper = new XmlSlurper()
        GPathResult rep = slurper.parse(is)

        HashMap<String, OverviewPanel> m =
            new HashMap<String, OverviewPanel>((int) rep.panel.size())
        for (GPathResult p in rep.panel) {
            OverviewPanel panel = new OverviewPanel(
                controller: p.controller.text(),
                action: p.action.text(),
                defTitle: p.title.findAll { !it.@lang.text() }[0].text(),
                defDescription:
                    p.description.findAll { !it.@lang.text() }[0].text(),
                style: p.style?.text(),
                additionalHeaderTemplate: p.additionalHeaderTemplate?.text()
            )
            p.title.each { GPathResult title ->
                if (title.@lang.text()) {
                    panel.addLocalizedTitle(
                        (String) title.@lang.text(), title.text()
                    )
                }
            }
            p.description.each { GPathResult description ->
                if (description.@lang.text()) {
                    panel.addLocalizedDescription(
                        (String) description.@lang.text(), description.text()
                    )
                }
            }
            m[(String) p.@id.text()] = panel
        }

        repository = Collections.unmodifiableMap(m)
    }


    //-- Inner classes --------------------------

    private static class InstanceHolder {

        //-- Constants --------------------------

        public static final OverviewPanelRepository INSTANCE =
            new OverviewPanelRepository()
    }
}
