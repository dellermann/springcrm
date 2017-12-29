/*
 * HelpdeskControllerTests.groovy
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


//@TestFor(HelpdeskController)
//@Mock(Helpdesk)
class HelpdeskControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/helpdesk/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.helpdeskInstanceList.size() == 0
        assert model.helpdeskInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.helpdeskInstance != null
    }

    void testSave() {
        controller.save()

        assert model.helpdeskInstance != null
        assert view == '/helpdesk/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/helpdesk/show/1'
        assert controller.flash.message != null
        assert Helpdesk.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/helpdesk/list'


        populateValidParams(params)
        def helpdesk = new Helpdesk(params)

        assert helpdesk.save() != null

        params.id = helpdesk.id

        def model = controller.show()

        assert model.helpdeskInstance == helpdesk
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/helpdesk/list'


        populateValidParams(params)
        def helpdesk = new Helpdesk(params)

        assert helpdesk.save() != null

        params.id = helpdesk.id

        def model = controller.edit()

        assert model.helpdeskInstance == helpdesk
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/helpdesk/list'

        response.reset()


        populateValidParams(params)
        def helpdesk = new Helpdesk(params)

        assert helpdesk.save() != null

        // test invalid parameters in update
        params.id = helpdesk.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/helpdesk/edit"
        assert model.helpdeskInstance != null

        helpdesk.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/helpdesk/show/$helpdesk.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        helpdesk.clearErrors()

        populateValidParams(params)
        params.id = helpdesk.id
        params.version = -1
        controller.update()

        assert view == "/helpdesk/edit"
        assert model.helpdeskInstance != null
        assert model.helpdeskInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/helpdesk/list'

        response.reset()

        populateValidParams(params)
        def helpdesk = new Helpdesk(params)

        assert helpdesk.save() != null
        assert Helpdesk.count() == 1

        params.id = helpdesk.id

        controller.delete()

        assert Helpdesk.count() == 0
        assert Helpdesk.get(helpdesk.id) == null
        assert response.redirectedUrl == '/helpdesk/list'
    }
}
