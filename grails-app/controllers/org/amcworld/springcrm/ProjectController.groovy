/*
 * ProjectController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import org.codehaus.groovy.grails.commons.GrailsClass
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code ProjectController} handles actions concerning projects and
 * project management.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class ProjectController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        return [projectInstanceList: Project.list(params), projectInstanceTotal: Project.count()]
    }

    def listEmbedded() {
        def l
        def count
        def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (params.organization) {
            def organizationInstance = Organization.get(params.organization)
            l = Project.findAllByOrganization(organizationInstance, params)
            count = Project.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (params.person) {
            def personInstance = Person.get(params.person)
            l = Project.findAllByPerson(personInstance, params)
            count = Project.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }
        return [projectInstanceList: l, projectInstanceTotal: count, linkParams: linkParams]
    }

    def create() {
        return [projectInstance: new Project(params)]
    }

    def copy() {
        def projectInstance = Project.get(params.id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
            redirect(action: 'list')
            return
        }

        projectInstance = new Project(projectInstance)
        render(view: 'create', model: [projectInstance: projectInstance])
    }

    def save() {
        def projectInstance = new Project(params)
        if (!projectInstance.save(flush: true)) {
            render(view: 'create', model: [projectInstance: projectInstance])
            return
        }
        params.id = projectInstance.ident()

        projectInstance.index()
		flash.message = message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])
        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'show', id: projectInstance.id)
        }
    }

    def show() {
        def projectInstance = Project.get(params.id)
        if (!projectInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
            redirect(action: 'list')
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

        return [projectInstance: projectInstance, projectItems: projectItems]
    }

    def edit() {
        def projectInstance = Project.get(params.id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
            redirect(action: 'list')
            return
        }

        return [projectInstance: projectInstance]
    }

    def update() {
        def projectInstance = Project.get(params.id)
        if (!projectInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (projectInstance.version > version) {
                projectInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'project.label', default: 'Project')] as Object[], 'Another user has updated this Project while you were editing')
                render(view: 'edit', model: [projectInstance: projectInstance])
                return
            }
        }
        if (params.autoNumber) {
            params.number = projectInstance.number
        }
        projectInstance.properties = params
        if (!projectInstance.save(flush: true)) {
            render(view: 'edit', model: [projectInstance: projectInstance])
            return
        }

        projectInstance.reindex()
		flash.message = message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])
        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'show', id: projectInstance.id)
        }
    }

    def delete() {
        def projectInstance = Project.get(params.id)
        if (!projectInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

        try {
            projectInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project')])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
        } catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'project.label', default: 'Project')])
            redirect(action: 'show', id: params.id)
        }
    }

    def setPhase() {
        def projectInstance = Project.get(params.id)
        if (projectInstance) {
            projectInstance.phase = ProjectPhase.valueOf(params.phase)
            projectInstance.save(flush: true)
        }
    }

    def setStatus() {
        def projectInstance = Project.get(params.id)
        if (projectInstance) {
            def status = ProjectStatus.get(params.status)
            if (status) {
                projectInstance.status = status
                projectInstance.save(flush: true)
            }
        }
    }

    def addSelectedItems() {
        def project = Project.get(params.long('project'))
        if (project) {
            ProjectPhase projectPhase = ProjectPhase.valueOf(params.projectPhase)
            String controllerName = params.controllerName
            GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
                'Domain', controllerName
            )
            params.itemIds.split(',').each {
                long itemId = it as long
                def itemInstance = cls.clazz.'get'(itemId)
                if (itemInstance) {
                    def projectItem = new ProjectItem(
                        project: project, phase: projectPhase,
                        controller: controllerName, itemId: itemId,
                        title: itemInstance.toString()
                    )
                    projectItem.save(flush: true)
                }
            }
        }
        render(status: 200)
    }
}
