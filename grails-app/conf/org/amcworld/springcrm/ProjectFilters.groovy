/*
 * ProjectFilters.groovy
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


/**
 * The class {@code ProjectFilters} contains filters which handle project
 * related operations for many controllers.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class ProjectFilters {

    //-- Public methods -------------------------

    def filters = {
        rememberProject(controller: '*', action: 'create') {
            before = {
                if (params.project) {
                    def project = Project.get(params.long('project'))
                    if (project) {
                        params.organization = project.organization
                        params.person = project.person
                    }
                }
            }

            after = { Map model ->
                if (params.project && params.projectPhase) {
                    model['project'] = params.project
                    model['projectPhase'] = params.projectPhase
                }
            }
        }

        createProjectItem(controller: '*', action: 'save') {
            after = { model ->
                if (params.id && params.project && params.projectPhase) {
                    GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
                        'Domain', controllerName
                    )
                    long id = params.long('id')
                    def instance = cls.clazz.'get'(id)
                    if (instance) {
                        def project = Project.get(params.long('project'))
                        if (project) {
                            def phase = ProjectPhase.valueOf(params.projectPhase)
                            project.phase = phase
                            project.save(flush: true)
                            def projectItem = new ProjectItem(
                                project: project, phase: phase,
                                controller: controllerName, itemId: id,
                                title: instance.toString()
                            )
                            projectItem.save(flush: true)
                        }
                    }
                }
            }
        }

        updateProjectItem(controller: '*', action: 'update') {
            after = { model ->
                if (params.id) {
                    GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
                        'Domain', controllerName
                    )
                    long id = params.long('id')
                    def instance = cls.clazz.'get'(id)
                    if (instance) {
                        def l = ProjectItem.findAllByControllerAndItemId(
                            controllerName, params.long('id')
                        )
                        String title = instance.toString()
                        l.each {
                            it.title = title
                            it.save(flush: true)
                        }
                    }
                }
            }
        }

        deleteProjectItem(controller: '*', action: 'delete') {
            after = { model ->
                if (params.id && params.confirmed) {
                    def l = ProjectItem.findAllByControllerAndItemId(
                        controllerName, params.long('id')
                    )
                    l.each { it.delete(flush: true) }
                }
            }
        }
    }
}
