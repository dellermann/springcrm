package org.amcworld.springcrm



import org.junit.*
import grails.test.mixin.*

@TestFor(ProjectController)
@Mock(Project)
class ProjectControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/project/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.projectInstanceList.size() == 0
        assert model.projectInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.projectInstance != null
    }

    void testSave() {
        controller.save()

        assert model.projectInstance != null
        assert view == '/project/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/project/show/1'
        assert controller.flash.message != null
        assert Project.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/project/list'


        populateValidParams(params)
        def project = new Project(params)

        assert project.save() != null

        params.id = project.id

        def model = controller.show()

        assert model.projectInstance == project
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/project/list'


        populateValidParams(params)
        def project = new Project(params)

        assert project.save() != null

        params.id = project.id

        def model = controller.edit()

        assert model.projectInstance == project
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/project/list'

        response.reset()


        populateValidParams(params)
        def project = new Project(params)

        assert project.save() != null

        // test invalid parameters in update
        params.id = project.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/project/edit"
        assert model.projectInstance != null

        project.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/project/show/$project.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        project.clearErrors()

        populateValidParams(params)
        params.id = project.id
        params.version = -1
        controller.update()

        assert view == "/project/edit"
        assert model.projectInstance != null
        assert model.projectInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/project/list'

        response.reset()

        populateValidParams(params)
        def project = new Project(params)

        assert project.save() != null
        assert Project.count() == 1

        params.id = project.id

        controller.delete()

        assert Project.count() == 0
        assert Project.get(project.id) == null
        assert response.redirectedUrl == '/project/list'
    }
}
