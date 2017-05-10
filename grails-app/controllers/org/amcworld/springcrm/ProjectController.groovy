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
class ProjectController extends GeneralController<Project> {

    //-- Fields ---------------------------------

	DocumentService documentService


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
                    new ProjectItem(
                        project: projectInstance, phase: pp,
                        controller: controllerName, itemId: itemId,
                        title: itemInstance.toString()
                    )
                    .save failOnError: true, flush: true
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
        projectInstance.save failOnError: true, flush: true

        render status: SC_OK
    }

    def copy(Long id) {
        super.copy id
    }

    def create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def index() {
        super.index()
    }

    /**
     * Renders a list of all active projects in a panel of the overview page.
     *
     * @return  any model data for the view
     * @since   2.1
     */
    def listCurrentProjects() {
        List<Project> projectInstanceList = Project.findAll(
            'from Project as p where p.status.id BETWEEN 2600 AND 2603 ' +
            'order by p.status'
        )

        [(getDomainInstanceName('List')): projectInstanceList]
    }

    def listEmbedded(Long organization, Long person) {
        List<Project> list = null
        int count = 0
        Map<String, Object> linkParams = null

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            Organization organizationInstance = Organization.get(organization)
            list = Project.findAllByOrganization(organizationInstance, params)
            count = Project.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            Person personInstance = Person.get(person)
            list = Project.findAllByPerson(personInstance, params)
            count = Project.countByPerson(personInstance)
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
        ProjectItem projectItemInstance = ProjectItem.get(id)
        if (projectItemInstance == null) {
            render status: SC_NOT_FOUND
            return
        }

        projectItemInstance.delete flush: true
        render status: SC_OK
    }

    def save() {
        super.save()
    }

    def setPhase(Long id, String phase) {
        Project projectInstance = Project.get(id)
        if (projectInstance != null) {
            projectInstance.phase = ProjectPhase.valueOf(phase)
            projectInstance.save failOnError: true, flush: true
        }

        render status: SC_OK
    }

    def setStatus(Long id, Long status) {
        Project projectInstance = Project.get(id)
        if (projectInstance != null) {
            ProjectStatus projectStatus = ProjectStatus.get(status)
            if (projectStatus) {
                projectInstance.status = projectStatus
                projectInstance.save failOnError: true, flush: true
            }
        }

        render status: SC_OK
    }

    def show(Long id) {
        Map<String, Object> model = super.show(id) as Map<String, Object>
        Project projectInstance = model[domainInstanceName] as Project
        if (projectInstance != null) {
            model['projectItems'] = getProjectItems(projectInstance)
            model['projectDocuments'] = getProjectDocuments(projectInstance)
        }

        model
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods ---------------------

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
}
