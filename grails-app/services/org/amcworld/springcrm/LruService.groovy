/*
 * LruService.groovy
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

import com.mongodb.client.FindIterable
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import grails.core.GrailsApplication
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code LruService} contains service methods to handle LRU (last
 * recently used) entries.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@CompileStatic
class LruService {

    //-- Fields ---------------------------------

    GrailsApplication grailsApplication
    UserService userService


    //-- Public methods -------------------------

    /**
     * Records the given entity of the given controller in the LRU list.
     *
     * @param controller    the controller name
     * @param entity        the given entity
     */
    void recordItem(String controller, GormEntity entity) {
        recordItem controller, (ObjectId) entity.ident(), entity.toString()
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
    void recordItem(String controller, ObjectId id, String name) {
        User user = userService.getCurrentUser()

        /* check whether or not this entry already exists */
        LruEntry lruEntry = LruEntry.find(
                Filters.and(
                    Filters.eq('user', user),
                    Filters.eq('controller', controller),
                    Filters.eq('itemId', id)
                )
            )
            .first()

        /* obtain the maximum position value for further operations */
        LruEntry temp = LruEntry.find(
                Filters.eq('user', user)
            )
            .sort(Sorts.orderBy(Sorts.descending('pos')))
            .limit(1)
            .first()
        Long maxPos = temp?.pos ?: 0L

        if (lruEntry) {

            /* save position of the LRU entry to move */
            long oldPos = lruEntry.pos
            lruEntry.pos = -1
            lruEntry.save()

            /* decrement the position number of all entries after */
            FindIterable<LruEntry> entriesToMove = LruEntry.find(
                Filters.and(
                    Filters.eq('user', user),
                    Filters.gt('pos', oldPos)
                )
            )
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
            LruEntry.collection.deleteMany(
                Filters.and(
                    Filters.eq('user', user),
                    Filters.lte('pos', lruEntry.pos - numOfLruEntries)
                )
            )
        }
    }

    /**
     * Retrieves a list of LRU entries for the currently logged in user.
     *
     * @return  the list of LRU entries
     */
    List<LruEntry> retrieveLruEntries() {
        LruEntry.find(Filters.eq('user', userService.getCurrentUser()))
            .limit(numOfLruEntries)
            .sort(Sorts.orderBy(Sorts.descending('pos')))
            .toList()
    }

    /**
     * Removes all LRU entries which belong to the item with the given ID and
     * controller.
     *
     * @param controller    the controller name
     * @param id            the ID of the item within this controller
     * @since 0.9.14
     */
    void removeItem(String controller, ObjectId id) {
        LruEntry.collection.deleteMany(
            Filters.and(
                Filters.eq('user', userService.getCurrentUser()),
                Filters.eq('controller', controller),
                Filters.eq('itemId', id)
            )
        )
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the number of LRU entries which are stored for a user. The value is
     * obtained from the configuration.
     *
     * @return  the number of simultaneous LRU entries
     */
    private int getNumOfLruEntries() {
        grailsApplication.config.getProperty(
            'springcrm.lruList.numEntries', Integer
        ) ?: 10i
    }
}
