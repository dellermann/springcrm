/*
 * PersonController.groovy
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

import com.google.gdata.data.extensions.*
import grails.converters.JSON
import net.sf.jmimemagic.Magic
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code PersonController} contains actions which manage persons
 * that belong to an organization.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class PersonController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

	def seqNumberService
	def googleDataContactService
	def ldapService


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.letter) {
			int num = Person.countByLastNameLessThan(params.letter)
			params.sort = 'lastName'
			params.offset = Math.floor(num / params.max) * params.max
		}
		return [personInstanceList: Person.list(params), personInstanceTotal: Person.count()]
    }

	def listEmbedded() {
		def organizationInstance = Organization.get(params.organization)
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		return [personInstanceList: Person.findAllByOrganization(organizationInstance, params), personInstanceTotal: Person.countByOrganization(organizationInstance), linkParams: [organization: organizationInstance.id]]
	}

    def create() {
        def personInstance = new Person()
        personInstance.properties = params
        return [personInstance: personInstance]
    }

	def copy() {
		def personInstance = Person.get(params.id)
		if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
            redirect(action: 'list')
            return
        }

		personInstance = new Person(personInstance)
		render(view: 'create', model: [personInstance: personInstance])
	}

    def save() {
        def personInstance = new Person(params)
        if (!personInstance.save(flush: true)) {
            render(view: 'create', model: [personInstance: personInstance])
            return
        }
        params.id = personInstance.ident()

		personInstance.index()
		if (ldapService) {
			ldapService.save(personInstance)
		}
        flash.message = message(code: 'default.created.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
        	redirect(action: 'show', id: personInstance.id)
		}
    }

    def show() {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
            redirect(action: 'list')
            return
        }

        return [personInstance: personInstance]
    }

    def edit() {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
            redirect(action: 'list')
            return
        }

        return [personInstance: personInstance]
    }

    def update() {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (personInstance.version > version) {
                personInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'person.label', default: 'Person')] as Object[], 'Another user has updated this Person while you were editing')
                render(view: 'edit', model: [personInstance: personInstance])
                return
            }
        }
		byte [] picture = personInstance.picture
		if (params.autoNumber) {
			params.number = personInstance.number
		}
        personInstance.properties = params
		if (params.pictureRemove == '1') {
			personInstance.picture = null
		} else if (params.picture?.isEmpty()) {
			personInstance.picture = picture
		}
        if (!personInstance.save(flush: true)) {
            render(view: 'edit', model: [personInstance: personInstance])
            return
        }

		personInstance.reindex()
		if (ldapService) {
			ldapService.save(personInstance)
		}
        flash.message = message(code: 'default.updated.message', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: personInstance.id)
		}
    }

    def delete() {
        def personInstance = Person.get(params.id)
        if (personInstance && params.confirmed) {
            try {
                personInstance.delete(flush: true)
				if (googleDataContactService) {
					googleDataContactService.markDeleted(personInstance)
				}
				if (ldapService) {
					ldapService.delete(personInstance)
				}
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person')])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'person.label', default: 'Person')])
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

	def getPicture() {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            render(status: 404)
            return
        }

		response.contentType = Magic.getMagicMatch(personInstance.picture).mimeType
		response.contentLength = personInstance.picture.length
		response.outputStream << personInstance.picture
		return null
	}

	def getPhoneNumbers() {
		def personInstance = Person.get(params.id)
		if (!personInstance) {
            render(status: 404)
            return
        }

		def phoneNumbers = [
			personInstance.phone,
			personInstance.phoneHome,
			personInstance.mobile,
			personInstance.fax,
			personInstance.phoneAssistant,
			personInstance.phoneOther,
			personInstance.organization.phone,
			personInstance.organization.phoneOther,
			personInstance.organization.fax
		]
		render phoneNumbers as JSON
	}

	def find() {
		def organizationInstance = Organization.findById(params.organization)
		def c = Person.createCriteria()
		def list = c.list {
			eq('organization', organizationInstance)
			and {
				or {
					ilike('lastName', "${params.name}%")
					ilike('firstName', "${params.name}%")
				}
			}
			order('lastName', 'asc')
		}
		render(contentType: "text/json") {
			array {
				for (p in list) {
					person id: p.id, name: p.fullName
				}
			}
		}
	}

	def gdatasync() {
		if (googleDataContactService) {
			if (params.id) {
				def personInstance = Person.get(params.id)
				if (personInstance) {
					googleDataContactService.sync(personInstance)
					flash.message = message(code: 'default.gdata.sync.success', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])
		        } else {
					flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
				}
				redirect(action: 'show', id: params.id)
				return
			}

			def personInstanceList = Person.list()
			personInstanceList.each { googleDataContactService.sync(it) }
			flash.message = message(code: 'default.gdata.allsync.success', args: [message(code: 'person.plural', default: 'persons')])
			googleDataContactService.deleteMarkedEntries()
		}
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'list')
		}
	}

	def ldapexport() {
		if (ldapService) {
			if (params.id) {
				def personInstance = Person.get(params.id)
				if (personInstance) {
					ldapService.save(personInstance)
					flash.message = message(code: 'default.ldap.export.success', args: [message(code: 'person.label', default: 'Person'), personInstance.toString()])
		        } else {
					flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
				}
				redirect(action: 'show', id: params.id)
                return
			}

			def personInstanceList = Person.list()
			personInstanceList.each { ldapService.save(it) }
			flash.message = message(code: 'default.ldap.allexport.success', args: [message(code: 'person.plural', default: 'persons')])
		}
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'list')
		}
	}
}
