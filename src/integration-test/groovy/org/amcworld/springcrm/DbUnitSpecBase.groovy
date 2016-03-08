/*
 * DbUnitSpecBase.groovy
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

// import ch.gstream.grails.plugins.dbunitoperator.Configuration
// import ch.gstream.grails.plugins.dbunitoperator.DbUnitOperatorImpl
import geb.spock.GebSpec
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


/**
 * The class {@code DbUnitSpecBase} represents a base class for Spock
 * specification classes that use DbUnit.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
abstract class DbUnitSpecBase extends GebSpec {

    //-- Class variables ------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    // DbUnitOperatorImpl operator


    //-- Fixture methods ------------------------

    def setup() {
        // operator = new DbUnitOperatorImpl(new Configuration(), null)

        if (datasets.size() > 1) {
            String first = datasets.remove(0)

            log.debug "Operating dataset '{$first}' within database…"
            // operator.operate dbUnitOperationType, first

            operate "INSERT", datasets
        } else {
            operate dbUnitOperationType, datasets
        }
    }

    def cleanup() {
        for (String filePath in datasets) {
            log.debug "Cleaning up dataset '{$filePath}' within database…"
            // operator.operate dbUnitCleanupOperationType, filePath
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the datasets to process tests on.  The root path for dataset files
     * is the Grails project root path.
     *
     * @return  a list of dataset files
     */
    protected abstract List<String> getDatasets()

    /**
     * Get standard DbUnit cleanup operation.
     *
     * @return  the default DbUnit cleanup operation
     */
    protected getDbUnitCleanupOperationType() {
        'DELETE_ALL'
    }

    /**
     * Get standard DbUnit initial operation.  This operation is used only,
     * when one dataset is provided.  When providing more than one datasets
     * predefined operations will be executed to prevent cleaning of database,
     * before all datasets have been persisted to database.
     *
     * @return  the default DbUnit operation
     */
    protected getDbUnitOperationType() {
        'CLEAN_INSERT'
    }

    private void operate(String type, List<String> datasets) {
        for (String filePath in datasets) {
            log.debug "Operating dataset '{$filePath}' within database…"
            // operator.operate type, filePath
        }
    }
}
