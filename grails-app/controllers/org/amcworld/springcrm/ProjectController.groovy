/*
 * ProjectController.groovy
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

import javax.servlet.http.HttpServletResponse
import org.amcworld.springcrm.elfinder.fs.Volume
import org.codehaus.groovy.grails.commons.GrailsClass


/**
 * The class {@code ProjectController} handles actions concerning projects and
 * project management.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class ProjectController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    FileService fileService


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [projectInstanceList: Project.list(params), projectInstanceTotal: Project.count()]
    }

    def listEmbedded(Long organization, Long person) {
        def l
        def count
        def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            def organizationInstance = Organization.get(organization)
            l = Project.findAllByOrganization(organizationInstance, params)
            count = Project.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            def personInstance = Person.get(person)
            l = Project.findAllByPerson(personInstance, params)
            count = Project.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }
        [projectInstanceList: l, projectInstanceTotal: count, linkParams: linkParams]
    }

    def create() {
        [projectInstance: new Project(params)]
    }

    def copy(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), id])
            redirect action: 'list'
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
        flash.message = message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: projectInstance.id
        }
    }

    def show(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), id])
            redirect action: 'list'
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

        [projectInstance: projectInstance, projectItems: projectItems, projectDocuments: projectDocuments]
    }

    def edit(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), id])
            redirect action: 'list'
            return
        }

        [projectInstance: projectInstance]
    }

    def update(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (projectInstance.version > version) {
                projectInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'project.label', default: 'Project')] as Object[], 'Another user has updated this Project while you were editing')
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
        flash.message = message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: projectInstance.id
        }
    }

    def delete(Long id) {
        def projectInstance = Project.get(id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
            return
        }

        try {
            projectInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'project.label', default: 'Project')])
            redirect action: 'show', id: id
        }
    }

    def setPhase(Long id, String phase) {
        def projectInstance = Project.get(id)
        if (projectInstance) {
            projectInstance.phase = ProjectPhase.valueOf(phase)
            projectInstance.save flush: true
        }
    }

    def setStatus(Long id, String status) {
        def projectInstance = Project.get(id)
        if (projectInstance) {
            def ps = ProjectStatus.get(status)
            if (ps) {
                projectInstance.status = ps
                projectInstance.save flush: true
            }
        }
    }

    def addSelectedItems(Long id, String projectPhase, String itemIds,
                         String documents)
    {
        def projectInstance = Project.get(id)
        if (projectInstance) {
            ProjectPhase pp = ProjectPhase.valueOf(projectPhase)
            String controllerName = params.controllerName
            GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
                'Domain', controllerName
            )
            if (itemIds) {
                itemIds.split(',').each {
                    long itemId = it as long
                    def itemInstance = cls.clazz.'get'(itemId)
                    if (itemInstance) {
                        def projectItem = new ProjectItem(
                            project: projectInstance, phase: pp,
                            controller: controllerName, itemId: itemId,
                            title: itemInstance.toString()
                        )
                        projectItem.save flush: true
                    }
                }
            }
            if (documents) {
                Volume volume = fileService.localVolume
                documents.split(',').each {
                    Map<String, Object> stat = volume.file(it)
                    if (stat) {
                        def projectDoc = new ProjectDocument(
                            project: projectInstance, phase: pp,
                            path: it, title: stat.name
                        )
                        projectDoc.save flush: true
                    }
                }
            }
            projectInstance.phase = pp
            projectInstance.save flush: true
        }
        render status: HttpServletResponse.SC_OK
    }

    def removeItem(Long id) {
        def projectItemInstance = ProjectItem.get(id)
        if (!projectItemInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        projectItemInstance.delete flush: true
        render status: HttpServletResponse.SC_OK
    }
}
