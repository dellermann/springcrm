package org.amcworld.springcrm

import com.google.gdata.data.extensions.*
import grails.converters.JSON
import net.sf.jmimemagic.Magic
import org.springframework.dao.DataIntegrityViolationException

class PersonController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def seqNumberService
	def googleDataContactService
	def ldapService

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
		[personInstanceList: Person.list(params), personInstanceTotal: Person.count()]
    }

	def listEmbedded() {
		def organizationInstance = Organization.get(params.organization)
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[personInstanceList: Person.findAllByOrganization(organizationInstance, params), personInstanceTotal: Person.countByOrganization(organizationInstance), linkParams: [organization: organizationInstance.id]]
	}

    def create() {
        def personInstance = new Person()
        personInstance.properties = params
        return [personInstance: personInstance]
    }

	def copy() {
		def personInstance = Person.get(params.id)
		if (personInstance) {
			personInstance = new Person(personInstance)
			render(view: 'create', model: [personInstance: personInstance])
		} else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
			redirect(action: 'show', id: personInstance.id)
		}
	}

    def save() {
        def personInstance = new Person(params)
        if (personInstance.save(flush: true)) {
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
        } else {
            render(view: 'create', model: [personInstance: personInstance])
        }
    }

    def show() {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
            redirect(action: 'list')
        } else {
            [personInstance: personInstance]
        }
    }

    def edit() {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
            redirect(action: 'list')
        } else {
            return [personInstance: personInstance]
        }
    }

    def update() {
        def personInstance = Person.get(params.id)
        if (personInstance) {
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
				personInstance.picture = null;
			} else if (params.picture?.isEmpty()) {
				personInstance.picture = picture
			}
            if (!personInstance.hasErrors() && personInstance.save(flush: true)) {
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
            } else {
                render(view: 'edit', model: [personInstance: personInstance])
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])
            redirect(action: 'list')
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
        if (personInstance) {
			response.contentType = Magic.getMagicMatch(personInstance.picture).mimeType
			response.contentLength = personInstance.picture.length
			response.outputStream << personInstance.picture
			return null
		} else {
			render(status: 404)
		}
	}

	def getPhoneNumbers() {
		def personInstance = Person.get(params.id)
		if (personInstance) {
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
		} else {
			render(status: 404)
		}
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
			} else {
				def personInstanceList = Person.list()
				personInstanceList.each { googleDataContactService.sync(it) }
				flash.message = message(code: 'default.gdata.allsync.success', args: [message(code: 'person.plural', default: 'persons')])
			}
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
			} else {
				def personInstanceList = Person.list()
				personInstanceList.each { ldapService.save(it) }
				flash.message = message(code: 'default.ldap.allexport.success', args: [message(code: 'person.plural', default: 'persons')])
			}
		}
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'list')
		}
	}
}
