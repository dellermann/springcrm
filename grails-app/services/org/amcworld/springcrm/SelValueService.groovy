/*
 * SelValueService.groovy
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

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import grails.gorm.services.Service
import org.bson.Document as MDocument


/**
 * The interface {@code ISelValueService} defines methods for working with
 * selection values.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
interface ISelValueService {

    //-- Public methods -----------------------------

    /**
     * Deletes the selection value with the given ID.
     *
     * @param id    the given ID
     */
    void delete(long id)
}


/**
 * The class {@code SelValueService} implements methods for working with
 * selection values.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(SelValue)
abstract class SelValueService implements ISelValueService {

    //-- Public methods -----------------------------

    /**
     * Finds all selection values of the given type.
     *
     * @param className the given type
     * @return          the matching selection values
     */
    def <T extends SelValue> List<T> findAllByClass(Class<T> clazz) {

        /*
         * We need to implement this method by ourselves because loading
         * instances of derived classes doesn't work in GORM for MongoDB.
         */
        SelValue.collection.find(Filters.eq('_class', clazz.simpleName))
            .sort(Sorts.orderBy(Sorts.ascending('orderId')))
            .collect({
                T instance = clazz.newInstance(it)
                instance.id = it.getLong('_id')

                instance
            })
            .toList() as List<T>
    }

    /**
     * Finds the selection value with the given type and ID.
     *
     * @param clazz the given type
     * @param id    the given ID
     * @return      the selection value or {@code null} if no such a value
     *              exists
     */
    def <T extends SelValue> T findByClassAndId(Class<T> clazz, long id) {
        MDocument doc = SelValue.collection.find(Filters.eq('_id', id))
            .first()

        T res = null
        if (doc != null) {
            res = clazz.newInstance(doc)
            res.id = doc.getLong('_id')
            res.attach()
        }

        res
    }

    /**
     * Saves the given selection value.
     *
     * @param selValue  the given selection value
     * @return          the saved selection value or {@code null} if either the
     *                  given value is {@code null} or it is invalid
     */
    def <T extends SelValue> T save(T selValue) {
        if (selValue == null || !selValue.validate()) {
            return null
        }

        MDocument doc = new MDocument(
                (Map<String, Object>) selValue.properties.findAll {
                    it.key != 'id' && it.key != 'dbo'
                }
            )
            .append('_class', selValue.getClass().simpleName)
        if (selValue.id) {
            SelValue.collection.replaceOne Filters.eq('_id', selValue.id), doc
        } else {
            MDocument idDocument =
                SelValue.DB.getCollection('selValue.next_id').findOneAndUpdate(
                    Filters.eq('_id', 'selValue'),
                    Updates.inc('next_id', 1)
                )
            long id = idDocument.getLong('next_id')
            doc.append '_id', id
            SelValue.collection.insert doc
            selValue.id = id
        }

        selValue
    }
}
