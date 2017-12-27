/*
 * PanelService.groovy
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

import grails.gorm.services.Service
import org.bson.types.ObjectId


/**
 * The interface {@code PanelService} contains general methods to handle panels
 * in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(Panel)
interface PanelService {

    //-- Public methods -------------------------

    /**
     * Deletes the panel with the given ID.
     *
     * @param id    the ID of the panel
     */
    void deletePanel(ObjectId id)

    /**
     * Finds all panels which belong to the given user.
     *
     * @param user  the given user
     * @return      a list of panels of this user
     */
    List<Panel> findAllByUser(User user)

    /**
     * Finds the panel with the given ID which belongs to the given user.
     *
     * @param user      the given user
     * @param panelId   the given panel ID
     * @return          the panel or {@code null} if no such panel exists
     */
    Panel findByUserAndPanelId(User user, String panelId)

    /**
     * Saves the given panel.
     *
     * @param panel the panel which should be saved
     * @return      the saved panel
     */
    Panel savePanel(Panel panel)
}
