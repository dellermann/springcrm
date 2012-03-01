/*
 * UserController.groovy
 *
 * Copyright (c) 2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


package org.amcworld.springcrm

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code UserController} contains methods which manager the users of
 * the application.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class UserController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.letter) {
			int num = User.countByUserNameLessThan(params.letter)
			params.sort = 'userName'
			params.offset = Math.floor(num / params.max) * params.max
		}
        return [userInstanceList: User.list(params), userInstanceTotal: User.count()]
    }

    def create() {
        def userInstance = new User()
        userInstance.properties = params
        return [userInstance: userInstance]
    }

    def save() {
        def userInstance = new User(params)
        if (!userInstance.save(flush: true)) {
            render(view: 'create', model: [userInstance: userInstance])
            return
        }
        params.id = userInstance.ident()

        flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: userInstance.id)
		}
    }

    def show() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: 'list')
            return
        }

        return [userInstance: userInstance]
    }

    def edit() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: 'list')
            return
        }

        return [userInstance: userInstance]
    }

    def update() {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (userInstance.version > version) {
                userInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'user.label', default: 'User')] as Object[], 'Another user has updated this User while you were editing')
                render(view: 'edit', model: [userInstance: userInstance])
                return
            }
        }
		String passwd = userInstance.password
        userInstance.properties = params
		if (!params.password) {
			userInstance.password = passwd
		}
        if (!userInstance.save(flush: true)) {
            render(view: 'edit', model: [userInstance: userInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), userInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: userInstance.id)
		}
    }

    def delete() {
        def userInstance = User.get(params.id)
        if (userInstance && params.confirmed) {
            try {
                userInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User')])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User')])
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

	def login() {}

	def authenticate() {
		def userInstance = User.findByUserNameAndPassword(params.userName, params.password)
		if (!userInstance) {
            flash.message = message(code: 'user.authenticate.failed.message', default: 'Invalid user name or password. Please retry.')
            redirect(action: 'login')
            return
		}

		session.user = userInstance
		redirect(uri: '/')
	}

	def logout() {
		flash.message = message(code: 'user.logout.message', default: 'You were logged out.')
		session.user = null
		session.invalidate()
		redirect(action: 'login')
	}

	def storeSetting() {
		User sessionUser = session.user
		User dbUser = User.get(sessionUser.id)
		sessionUser.settings[params.key] = params.value
		dbUser.settings[params.key] = params.value
		dbUser.save(flush: true)
		render(status: 200)
	}
}
