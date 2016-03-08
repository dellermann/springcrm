/*
 * OverviewPanelRepository.groovy
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


/**
 * The class {@code OverviewPanelRepository} represents a repository of panels
 * which are used on the overview page. The repository is read from an XML file
 * by calling method {@code initialize()}.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class OverviewPanelRepository {

    //-- Instance variables ---------------------

    private Map<String, OverviewPanel> repository


    //-- Constructors ---------------------------

    private OverviewPanelRepository() {}


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
    void initialize(InputStream is) {
        def slurper = new XmlSlurper()
        def rep = slurper.parse(is)

        HashMap<String, OverviewPanel> m =
            new HashMap<String, OverviewPanel>(rep.panel.size())
        for (def p in rep.panel) {
            OverviewPanel panel = new OverviewPanel(
                controller: p.controller.text(),
                action: p.action.text(),
                defTitle: p.title.findAll { !it.@lang.text() }[0].text(),
                defDescription:
                    p.description.findAll { !it.@lang.text() }[0].text(),
                style: p.style?.text()
            )
            p.title.each {
                if (it.@lang.text()) {
                    panel.addLocalizedTitle it.@lang.text(), it.text()
                }
            }
            p.description.each {
                if (it.@lang.text()) {
                    panel.addLocalizedDescription it.@lang.text(), it.text()
                }
            }
            m[p.@id.text()] = panel
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
