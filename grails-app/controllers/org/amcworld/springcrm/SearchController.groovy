/*
 * SearchController.groovy
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


/**
 * The class {@code SearchController} allows searching for a particular term in
 * the index.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
class SearchController {

    //-- Class fields ---------------------------

    SearchService searchService


    //-- Public methods -------------------------

    /**
     * Searches in the index for the given query term.
     *
     * @param query the given query term
     * @return      the model containing the query term, the number of hits and
     *              the search result of one page
     */
    def index(String query) {
        int offset = params.int('offset') ?: 0
        int max = params.int('max') ?: 10

        [
            query: query.toLowerCase(),
            numHits: searchService.countHits(query),
            searchResults: searchService.search(query, max, offset)
        ]
    }
}
