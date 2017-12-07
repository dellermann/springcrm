/*
 * ProjectController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import static javax.servlet.http.HttpServletResponse.SC_OK

import grails.core.GrailsClass
import org.apache.commons.vfs2.FileObject
import org.grails.datastore.mapping.query.api.BuildableCriteria


/**
 * The class {@code ProjectController} handles actions concerning projects and
 * project management.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class ProjectController extends GenericDomainController<Project> {

    //-- Fields ---------------------------------

	DocumentService documentService
    OrganizationService organizationService
    PersonService personService
    ProjectItemService projectItemService
    ProjectService projectService


    //-- Constructors ---------------------------

    ProjectController() {
        super(Project)
    }


    //-- Public methods -------------------------

    def addSelectedItems(Long id, String projectPhase, String itemIds) {
        Project projectInstance = getDomainInstanceWithStatus(id)
        if (projectInstance == null) {
            return
        }

        ProjectPhase pp = ProjectPhase.valueOf(projectPhase)

        if (itemIds) {
            String controllerName = params.controllerName
            GrailsClass cls =
                grailsApplication.getArtefactByLogicalPropertyName(
                    'Domain', controllerName
                )
            itemIds.split(',').each {
                long itemId = it as long
                def itemInstance = cls.clazz.'get'(itemId)
                if (itemInstance != null) {
                    ProjectItem item = new ProjectItem(
                        project: projectInstance, phase: pp,
                        controller: controllerName, itemId: itemId,
                        title: itemInstance.toString()
                    )
                    projectItemService.save item
                }
            }
        }

        List<String> documents = params.list('documents[]')
        if (documents) {
            for (String path : documents) {
                FileObject fo = documentService.getFile(path)
                if (fo.exists()) {
                    new ProjectDocument(
                        project: projectInstance, phase: pp,
                        path: path, title: fo.name.baseName
                    )
                    .save failOnError: true, flush: true
                }
            }
        }

        projectInstance.phase = pp
        lowLevelSave projectInstance

        render status: SC_OK
    }

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def index() {
        getIndexModel projectService.list(params), projectService.count()
    }

    /**
     * Renders a list of all active projects in a panel of the overview page.
     *
     * @return  any model data for the view
     * @since   2.1
     */
    def listCurrentProjects() {
        List<Project> projectInstanceList =
            projectService.findAllCurrentProjects()

        [(getDomainInstanceName('List')): projectInstanceList]
    }

    def listEmbedded(Long organization, Long person) {
        List<Project> list = null
        int count = 0
        Map linkParams = null

        if (organization) {
            Organization organizationInstance =
                organizationService.get(organization)
            list = projectService.findAllByOrganization(
                organizationInstance, params
            )
            count = projectService.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            Person personInstance = personService.get(person)
            list = projectService.findAllByPerson(personInstance, params)
            count = projectService.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }

        getListEmbeddedModel list, count, linkParams
    }

    def removeDocument(Long id) {
        ProjectDocument projectDocumentInstance = ProjectDocument.get(id)
        if (projectDocumentInstance == null) {
            render status: SC_NOT_FOUND
            return
        }

        projectDocumentInstance.delete flush: true
        render status: SC_OK
    }

    def removeItem(Long id) {
        ProjectItem projectItemInstance = projectItemService.get(id)
        if (projectItemInstance == null) {
            render status: SC_NOT_FOUND
            return
        }

        projectItemService.delete id
        render status: SC_OK
    }

    def save() {
        super.save()
    }

    def setPhase(Long id, String phase) {
        Project projectInstance = lowLevelGet(id)
        if (projectInstance != null) {
            projectInstance.phase = ProjectPhase.valueOf(phase)
            lowLevelSave projectInstance
        }

        render status: SC_OK
    }

    def setStatus(Long id, Long status) {
        Project projectInstance = lowLevelGet(id)
        if (projectInstance != null) {
            ProjectStatus projectStatus = ProjectStatus.get(status)
            if (projectStatus) {
                projectInstance.status = projectStatus
                lowLevelSave projectInstance
            }
        }

        render status: SC_OK
    }

    def show(Long id) {
        Map model = null
        Project projectInstance = lowLevelGet(id)
        if (projectInstance != null) {
            model = getDomainInstanceModel(projectInstance)
            model.projectItems = getProjectItems(projectInstance)
            model.projectDocuments = getProjectDocuments(projectInstance)
        }

        model
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods -------------------------

    /**
     * Gets a list of items of the given project grouped by project phase.
     *
     * @param projectInstance   the given project instance
     * @return                  the project items grouped by phase
     * @since 2.2
     */
    private static EnumMap<ProjectPhase, List<ProjectDocument>> \
        getProjectDocuments(Project projectInstance)
    {
        EnumMap<ProjectPhase, List<ProjectDocument>> res =
            new EnumMap<>(ProjectPhase)
        List<ProjectDocument> l = ProjectDocument.findAllByProject(
            projectInstance, [sort: 'phase', order: 'asc']
        )
        for (ProjectDocument doc : l) {
            ProjectPhase phase = doc.phase
            List<ProjectDocument> docs = res[phase]
            if (docs == null) {
                docs = []
                res[phase] = docs
            }
            docs << doc
        }

        res
    }

    /**
     * Gets a list of documents of the given project grouped by project phase.
     *
     * @param projectInstance   the given project instance
     * @return                  the project documents grouped by phase
     * @since 2.2
     */
    private static EnumMap<ProjectPhase, List<ProjectItem>> \
        getProjectItems(Project projectInstance)
    {
        BuildableCriteria c = ProjectItem.createCriteria()
        List<ProjectItem> l = (List<ProjectItem>) c.list {
            eq('project', projectInstance)
            and {
                order('phase', 'asc')
                order('controller', 'asc')
            }
        }

        EnumMap<ProjectPhase, List<ProjectItem>> res =
            new EnumMap<>(ProjectPhase)
        for (ProjectItem item : l) {
            ProjectPhase phase = item.phase
            List<ProjectItem> items = res[phase]
            if (items == null) {
                items = []
                res[phase] = items
            }
            items << item
        }

        res
    }

    @Override
    protected void lowLevelDelete(Project instance) {
        projectService.delete instance.id
    }

    @Override
    protected Project lowLevelGet(Long id) {
        projectService.get id
    }

    @Override
    protected Project lowLevelSave(Project instance) {
        projectService.save instance
    }
}
