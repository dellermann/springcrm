/*
 * LruService.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder


/**
 * The class {@code LruService} contains service methods to handle LRU (last
 * recently used) entries.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class LruService {

    //-- Instance variables ---------------------

    def grailsApplication


    //-- Public methods -------------------------

    /**
     * Records the given domain instance of the given controller in the LRU
     * list.
     *
     * @param controller        the controller name
     * @param domainInstance    the given domain instance
     */
    void recordItem(String controller, def domainInstance) {
        recordItem controller, domainInstance.ident(), domainInstance.toString()
    }

    /**
     * Records the item of the given controller and with the given ID in the
     * LRU list.
     *
     * @param controller    the controller name
     * @param id            the ID of the item within this controller
     * @param name          the descriptive name which is displayed in the LRU
     *                      list
     */
    void recordItem(String controller, long id, String name) {
        User user = User.get(session.user.id)

        /* check whether or not this entry already exists */
        def c = LruEntry.createCriteria()
        LruEntry lruEntry = c.get {
            eq 'user', user
            eq 'controller', controller
            eq 'itemId', id
        }

        /* obtain the maximum position value for further operations */
        c = LruEntry.createCriteria()
        Long maxPos = c.get {
            eq 'user', user
            projections {
                max 'pos'
            }
        } ?: 0L

        if (lruEntry) {

            /* save position of the LRU entry to move */
            def oldPos = lruEntry.pos
            lruEntry.pos = -1
            lruEntry.save()

            /* decrement the position number of all entries after */
            c = LruEntry.createCriteria()
            def entriesToMove = c.list {
                eq 'user', user
                gt 'pos', oldPos
            }
            for (LruEntry entry in entriesToMove) {
                entry.pos--
                entry.save()
            }

            /* set the last position for the LRU entry to move */
            lruEntry.pos = maxPos
            lruEntry.name = name
            lruEntry.save()
        } else {

            /* add a new LRU entry */
            lruEntry = new LruEntry(
                user: user, controller: controller, itemId: id,
                pos: maxPos + 1, name: name
            )
            lruEntry.save flush: true

            /* delete old entries */
            def query = LruEntry.where {
                user == user && pos <= lruEntry.pos - numOfLruEntries
            }
            query.deleteAll()
        }
    }

    /**
     * Retrieves a list of LRU entries for the currently logged in user.
     *
     * @return  the list of LRU entries
     */
    List<LruEntry> retrieveLruEntries() {
        LruEntry.findAllByUser(
            session.user, [max: numOfLruEntries, sort: 'pos', order: 'desc']
        )
    }

    /**
     * Removes all LRU entries which belong to the item with the given ID and
     * controller.
     *
     * @param controller    the controller name
     * @param id            the ID of the item within this controller
     * @since               0.9.14
     */
    void removeItem(String controller, long id) {
        def query = LruEntry.where {
            user == session.user && controller == controller && itemId == id
        }
        query.deleteAll()
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the number of LRU entries which are stored for a user. The value is
     * obtained from the configuration.
     *
     * @return  the number of simultaneous LRU entries
     */
    protected int getNumOfLruEntries() {
        grailsApplication.config.springcrm.lruList.numEntries ?: 10
    }

    /**
     * Returns access to the user session.
     *
     * @return  the session instance
     */
    protected HttpSession getSession() {
        RequestContextHolder.currentRequestAttributes().session
    }
}
