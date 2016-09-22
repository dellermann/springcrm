/*
 * SearchData.groovy
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

import grails.artefact.Service
import grails.core.GrailsApplication
import grails.core.GrailsClass
import grails.gorm.DetachedCriteria
import grails.transaction.Transactional
import groovy.transform.CompileStatic
import java.lang.reflect.Field
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code SearchService} contains methods to manage the search index
 * and query it.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@Transactional
class SearchService implements Service {

    //-- Constants ----------------------------------

    /**
     * The name of the static field in each searchable domain model class which
     * contains an immutable list of field names that should be indexed.
     */
    public static final String SEARCH_FIELDS_PROPERTY = 'SEARCH_FIELDS'


    //-- Fields -------------------------------------

    GrailsApplication grailsApplication


    //-- Public methods -----------------------------

    /**
     * Re-generates the whole search index.  The method deletes the whole
     * search index and re-builds it for all searchable domain model classes.
     *
     * @throws IllegalStateException    if any domain model class does not
     *                                  define the searchable fields as list of
     *                                  strings
     */
    void bulkIndex() {
        DetachedCriteria<SearchData> criteria = SearchData.where { type != '' }
        criteria.deleteAll()

        for (GrailsClass cls : grailsApplication.getArtefacts('Domain')) {
            def clz = cls.clazz
            if (GormEntity.isAssignableFrom(clz) &&
                getSearchFields(clz) != null)
            {
                bulkIndex clz
            }
        }

        new Config(name: 'searchIndexReady', value: new Date().toString())
            .save flush: true, failOnError: true
    }

    /**
     * Re-generates the whole search index for all records of the given domain
     * model class.
     *
     * @param cls                       the given domain model class
     * @throws IllegalStateException    if the domain model class does not
     *                                  define the searchable fields as list of
     *                                  strings
     */
    public <T extends GormEntity> void bulkIndex(Class<T> cls) {
        List<T> list = cls.list()
        for (T item : list) {
            index item
        }
    }

    /**
     * Stores the given entity instance in the search index.  The method stores
     * all searchable fields defined by the entity class in the index.
     *
     * @param entity                    the entity that should be indexed
     * @throws IllegalStateException    if the class of the entity does not
     *                                  define the searchable fields as list of
     *                                  strings
     */
    @CompileStatic
    void index(GormEntity<?> entity) {
        List<String> searchFields = getSearchFields(entity.getClass())
        if (searchFields == null) {
            return
        }

        String title = entity.toString()
        if (title.length() > 65_535) {
            title = title.substring(0, 65_536)
        }
        SearchData data = new SearchData(
            type: getType(entity.getClass()),
            recordId: (Long) entity.ident(),
            recordTitle: title
        )
        data.structuredContent = extractFields(entity, searchFields)
        data.save insert: true, failOnError: true
    }

    /**
     * Checks whether the search index is ready, that is, all domain model
     * instances have been indexed.
     *
     * @return  {@code true} if the search index is ready; {@code false}
     *          otherwise
     */
    boolean isSearchIndexReady() {
        Config.countByName('searchIndexReady') > 0
    }

    /**
     * Updates the search index for the given entity instance.
     *
     * @param entity                    the entity that should be re-indexed
     * @throws IllegalStateException    if the class of the entity does not
     *                                  define the searchable fields as list of
     *                                  strings
     */
    @CompileStatic
    void reindex(GormEntity<?> entity) {
        removeFromIndex entity
        index entity
    }

    /**
     * Removes the given entity instance from the search index.
     *
     * @param entity    the entity that should be removed
     */
    void removeFromIndex(GormEntity<?> entity) {
        String t = getType(entity.getClass())
        Long id = (Long) entity.ident()
        DetachedCriteria<SearchData> criteria =
            SearchData.where { type == t && recordId == id }
        criteria.deleteAll()
    }

    /**
     * Searches for the given query string in the search index.
     *
     * @param query the given query string
     * @return      a list of search occurrences
     */
    List<SearchData> search(String query) {
        (List<SearchData>) SearchData.createCriteria().list {
            ilike 'content', "%${query}%"
            groupProperty 'type'
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Extracts the values of the given fields from the stated entity.
     *
     * @param entity    the given entity
     * @param fields    the list of fields that should be extracted
     * @return          a map containing the field names and their values
     */
    private static Map<String, String> extractFields(GormEntity<?> entity,
                                                     List<String> fields)
    {
        Map<String, String> res = [: ]
        for (String field : fields) {
            Object obj = field.tokenize('.')
                .inject(entity) { Object obj, String prop ->
                    prop.startsWith('*') ? obj*."${prop.substring(1)}"
                        : obj?."${prop}"
                }
            if (obj instanceof Iterable<?>) {
                int i = 0
                for (def item : obj) {
                    storeInMap res, field.replace('*', i + '.'), item
                    i++
                }
            } else {
                storeInMap res, field, obj
            }
        }

        res
    }

    /**
     * Gets the search fields of the given domain model class.
     *
     * @param cls                       the given domain model class
     * @return                          a list of search fields; {@code null}
     *                                  if no search fields have been declared
     * @throws IllegalStateException    if the domain model class does not
     *                                  define the searchable fields as list of
     *                                  strings
     */
    @CompileStatic
    private static List<String> getSearchFields(Class<?> cls) {
        List<String> res = null

        try {
            Field f = cls.getField(SEARCH_FIELDS_PROPERTY)
            Object obj = f.get(null)
            if (!(obj instanceof List<String>)) {
                throw new IllegalStateException(
                    "Static field ${SEARCH_FIELDS_PROPERTY} of class ${cls.getName()} must be a list of string representing the search fields."
                )
            }

            res = (List<String>) obj
        } catch (NoSuchFieldException ignore) { /* ignored */ }

        res
    }

    /**
     * Converts the name of the given class to a type name starting with a
     * lower case letter.
     *
     * @param cls   the given class
     * @return      the type name
     */
    private static String getType(Class<?> cls) {
        String name = cls.simpleName
        name = name.substring(0, 1).toLowerCase() + name.substring(1)

        name.replaceAll ~/_\$\$.*$/, ''
    }

    /**
     * Stores the value of the given field in the stated map if the value is
     * neither {@code null} nor blank.
     *
     * @param map   the stated map
     * @param field the name of the field
     * @param value the value which is converted to a string and trimmed
     */
    private static void storeInMap(Map<String, String> map, String field,
                                   Object value)
    {
        String s = value?.toString()?.trim()
        if (s) map[field] = s
    }
}
