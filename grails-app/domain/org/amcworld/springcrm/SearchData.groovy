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

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import javax.annotation.Nullable


/**
 * Class {@code SearchData} stores an entry in the search index.  The search
 * index is populated with all searchable fields of searchable domain model
 * classes.
 * <p>
 * The content of all searchable fields of a record is stored in field
 * {@code content} of this class.  That field can be browsed for a particular
 * term.  To store the field values field {@code contentStructured} is used.
 * It contains a JSON map consisting of the name and the value of each field.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
class SearchData implements Serializable {

    //-- Class fields ---------------------------

    static constraints = {
        type blank: false, maxSize: 100
        content blank: false, maxSize: Integer.MAX_VALUE
        contentStructured blank: false, maxSize: Integer.MAX_VALUE
        recordTitle maxSize: 65_535
    }
    static mapping = {
        id composite: ['type', 'recordId']
        sort 'orderId'
        version false
    }
    static transients = ['structuredContent']


    //-- Fields ---------------------------------

    /**
     * The plain text content of all searchable fields of the indexed record.
     */
    String content

    /**
     * A JSON map consisting of the name and the value of each searchable field
     * of the indexed record.
     */
    String contentStructured

    /**
     * A criterion used to sort the types.
     */
    int orderId

    /**
     * The ID of the indexed record.
     */
    long recordId

    /**
     * The string representation of the record.
     */
    String recordTitle

    /**
     * The type of the indexed record.
     */
    String type


    //-- Properties -----------------------------

    /**
     * Gets the parsed JSON content which contains the name and the value of
     * each searchable field of the indexed record.
     *
     * @return  a map containing the name and the value of each field
     */
    Map<String, String> getStructuredContent() {
        Map res = null
        if (contentStructured != null) {
            JsonSlurper jsonSlurper = new JsonSlurper()
            res = (Map<String, String>) jsonSlurper.parseText(contentStructured)
        }

        res
    }

    /**
     * Sets the name and the value of each searchable field and populates the
     * plain text content and the structured JSON content.
     *
     * @param content   the name and the value of each field that should be set
     */
    void setStructuredContent(@Nullable Map<String, String> content) {
        this.content = content?.values()?.join('\n')
        contentStructured = content == null ? null : JsonOutput.toJson(content)
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof SearchData && obj.type == type &&
            obj.recordId == recordId
    }

    @Override
    int hashCode() {
        "${type}:${recordId}".toString().hashCode()
    }

    @Override
    String toString() {
        "${type}:${recordId}".toString()
    }
}
