/*
 * DataFileTest.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * The class {@code DataFileTests} contains the unit test cases for
 * {@code DataFile}.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
@TestFor(DataFile)
@Mock([DataFile])
class DataFileTests {

    //-- Public methods -------------------------

    void testGetStorageName() {
        mockDomain DataFile, [
            [dataType: DataType.purchaseInvoice, fileName: 'test.odt', fileSize: 128000L]
        ]
        def dataFile = DataFile.get(1L)
        assert '0000000000000001.dat' == dataFile.storageName
    }

    void testToString() {
        mockDomain DataFile, [
            [dataType: DataType.purchaseInvoice, fileName: 'test.odt', fileSize: 128000L]
        ]
        def dataFile = DataFile.get(1L)
        assert 'test.odt' == dataFile.toString()
    }
}
