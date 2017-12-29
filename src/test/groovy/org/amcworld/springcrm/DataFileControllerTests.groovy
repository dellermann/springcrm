/*
 * DataFileControllerTests.groovy
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


//@TestFor(DataFileController)
//@Mock(DataFile)
class DataFileControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/dataFile/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.dataFileInstanceList.size() == 0
        assert model.dataFileInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.dataFileInstance != null
    }

    void testSave() {
        controller.save()

        assert model.dataFileInstance != null
        assert view == '/dataFile/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/dataFile/show/1'
        assert controller.flash.message != null
        assert DataFile.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/dataFile/list'


        populateValidParams(params)
        def dataFile = new DataFile(params)

        assert dataFile.save() != null

        params.id = dataFile.id

        def model = controller.show()

        assert model.dataFileInstance == dataFile
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/dataFile/list'


        populateValidParams(params)
        def dataFile = new DataFile(params)

        assert dataFile.save() != null

        params.id = dataFile.id

        def model = controller.edit()

        assert model.dataFileInstance == dataFile
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/dataFile/list'

        response.reset()


        populateValidParams(params)
        def dataFile = new DataFile(params)

        assert dataFile.save() != null

        // test invalid parameters in update
        params.id = dataFile.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/dataFile/edit"
        assert model.dataFileInstance != null

        dataFile.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/dataFile/show/$dataFile.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        dataFile.clearErrors()

        populateValidParams(params)
        params.id = dataFile.id
        params.version = -1
        controller.update()

        assert view == "/dataFile/edit"
        assert model.dataFileInstance != null
        assert model.dataFileInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/dataFile/list'

        response.reset()

        populateValidParams(params)
        def dataFile = new DataFile(params)

        assert dataFile.save() != null
        assert DataFile.count() == 1

        params.id = dataFile.id

        controller.delete()

        assert DataFile.count() == 0
        assert DataFile.get(dataFile.id) == null
        assert response.redirectedUrl == '/dataFile/list'
    }
}
