/*
 * ProjectController.groovy
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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import static javax.servlet.http.HttpServletResponse.SC_OK

import grails.core.GrailsClass
import org.apache.commons.vfs2.FileObject
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code ProjectController} handles actions concerning projects and
 * project management.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class ProjectController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

	DocumentService documentService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        [
            projectInstanceList: Project.list(params),
            projectInstanceTotal: Project.count()
        ]
    }

    def listEmbedded(Long organization, Long person) {
        List<Project> l
        int count
        Map<String, Object> linkParams

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            Organization organizationInstance = Organization.get(organization)
            l = Project.findAllByOrganization(organizationInstance, params)
            count = Project.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            Person personInstance = Person.get(person)
            l = Project.findAllByPerson(personInstance, params)
            count = Project.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }

        [
            projectInstanceList: l, projectInstanceTotal: count,
            linkParams: linkParams
        ]
    }

    def create() {
        [projectInstance: new Project(params)]
    }

    def copy(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'project.label'), id]
            )
            redirect action: 'index'
            return
        }

        projectInstance = new Project(projectInstance)
        render view: 'create', model: [projectInstance: projectInstance]
    }

    def save() {
        def projectInstance = new Project(params)
        if (!projectInstance.save(flush: true)) {
            render view: 'create', model: [projectInstance: projectInstance]
            return
        }

        request.projectInstance = projectInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'project.label'), projectInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: projectInstance.id
        }
    }

    def show(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'project.label'), id]
            )
            redirect action: 'index'
            return
        }

        def c = ProjectItem.createCriteria()
        def l = c.list {
            eq('project', projectInstance)
            and {
                order('phase', 'asc')
                order('controller', 'asc')
            }
        }

        def projectItems = [: ]
        l.each {
            def phase = it.phase
            def items = projectItems[phase]
            if (!items) {
                items = []
                projectItems[phase] = items
            }
            items << it
        }

        def projectDocuments = [: ]
        l = ProjectDocument.findAllByProject(
            projectInstance, [sort: 'phase', order: 'asc']
        )
        l.each {
            def phase = it.phase
            def docs = projectDocuments[phase]
            if (!docs) {
                docs = []
                projectDocuments[phase] = docs
            }
            docs << it
        }

        [
            projectInstance: projectInstance, projectItems: projectItems,
            projectDocuments: projectDocuments
        ]
    }

    def edit(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'project.label'), id]
            )
            redirect action: 'index'
            return
        }

        [projectInstance: projectInstance]
    }

    def update(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'project.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (projectInstance.version > version) {
                projectInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'project.label')] as Object[],
                    'Another user has updated this Project while you were editing'
                )
                render view: 'edit', model: [projectInstance: projectInstance]
                return
            }
        }
        if (params.autoNumber) {
            params.number = projectInstance.number
        }
        projectInstance.properties = params
        if (!projectInstance.save(flush: true)) {
            render view: 'edit', model: [projectInstance: projectInstance]
            return
        }

        request.projectInstance = projectInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'project.label'), projectInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: projectInstance.id
        }
    }

    def delete(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'project.label'), id]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        request.projectInstance = projectInstance
        try {
            projectInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'project.label')]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'project.label')]
            )
            redirect action: 'show', id: id
        }
    }

    def setPhase(Long id, String phase) {
        def projectInstance = Project.get(id)
        if (projectInstance) {
            projectInstance.phase = ProjectPhase.valueOf(phase)
            projectInstance.save flush: true
        }

		render status: SC_OK
    }

    def setStatus(Long id, Long status) {
        def projectInstance = Project.get(id)
        if (projectInstance) {
            def ps = ProjectStatus.get(status)
            if (ps) {
                projectInstance.status = ps
                projectInstance.save flush: true
            }
        }

		render status: SC_OK
    }

    def addSelectedItems(Long id, String projectPhase, String itemIds) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            render status: SC_NOT_FOUND
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
                if (itemInstance) {
                    new ProjectItem(
	                        project: projectInstance, phase: pp,
	                        controller: controllerName, itemId: itemId,
	                        title: itemInstance.toString()
	                    )
                    	.save flush: true
                }
            }
        }

		List<String> documents = params.list('documents[]')
        if (documents) {
			for (String path in documents) {
				FileObject fo = documentService.getFile(path)
				if (fo.exists()) {
					new ProjectDocument(
							project: projectInstance, phase: pp,
							path: path, title: fo.name.baseName
						)
						.save flush: true
				}
			}
        }

        projectInstance.phase = pp
        projectInstance.save flush: true

        render status: SC_OK
    }

    def removeItem(Long id) {
        def projectItemInstance = ProjectItem.get(id)
        if (!projectItemInstance) {
            render status: SC_NOT_FOUND
            return
        }

        projectItemInstance.delete flush: true
        render status: SC_OK
    }

	def removeDocument(Long id) {
		def projectDocumentInstance = ProjectDocument.get(id)
		if (!projectDocumentInstance) {
			render status: SC_NOT_FOUND
			return
		}

		projectDocumentInstance.delete flush: true
		render status: SC_OK
	}
}
