/*
 * UserController.groovy
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

import com.google.api.client.auth.oauth2.Credential
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code UserController} contains methods which manager the users of
 * the application.
 *
 * @author  Daniel Ellermann
 * @version 1.2
 */
class UserController {

    //-- Constants ------------------------------

    static final String [] AVAILABLE_LANGUAGES = ['de', 'en']


    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    def googleOAuthService
    def installService
    def securityService


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
        boolean passwordMismatch =
            params.password != securityService.encryptPassword(params.passwordRepeat)
        if (passwordMismatch) {
            userInstance.errors.rejectValue('password', 'user.password.doesNotMatch')
        }
        if (passwordMismatch || !userInstance.save(flush: true)) {
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

        boolean passwordMismatch = false
        String passwd = userInstance.password
        userInstance.properties = params
        if (params.password) {
            passwordMismatch = params.password != securityService.encryptPassword(params.passwordRepeat)
            if (passwordMismatch) {
                userInstance.errors.rejectValue('password', 'user.password.doesNotMatch')
            }
        } else {
            userInstance.password = passwd
        }
        if (passwordMismatch || !userInstance.save(flush: true)) {
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
        if (!userInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

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
    }

    def login() {
        def installStatus = org.amcworld.springcrm.Config.findByName('installStatus')
        if (!installStatus || !installStatus.value) {
            installService.enableInstaller()
            redirect(controller: 'install', action: 'index')
            return
        }
    }

    def authenticate() {
        def userInstance = User.findByUserNameAndPassword(params.userName, params.password)
        if (!userInstance) {
            flash.message = message(code: 'user.authenticate.failed.message', default: 'Invalid user name or password. Please retry.')
            redirect(action: 'login')
            return
        }

        session.user = userInstance
        def language = userInstance.settings['language']
        if (language) {
            session['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'] = new Locale(language)
        }
        redirect(uri: '/')
    }

    def logout() {
        flash.message = message(code: 'user.logout.message', default: 'You were logged out.')
        session.user = null
        session.invalidate()
        redirect(action: 'login')
    }

    def storeSetting() {
        session.user.settings[params.key] = params.value
        render(status: 200)
    }

    def settingsIndex() {}

    def settingsLanguage() {
        Map<String, String> locales = [: ]
        for (String code : AVAILABLE_LANGUAGES) {
            Locale l = new Locale(code)
            locales[code] = l.getDisplayLanguage(l)
        }
        Locale currLocale = session['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'] ?: Locale.default
        return [locales: locales, currentLocale: currLocale.language]
    }

    def settingsLanguageSave() {
        session.user.settings['language'] = params.language
        session['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'] = new Locale(params.language)
        redirect(action: 'settingsIndex')
    }

    def settingsGoogleAuth() {
        Credential cred = googleOAuthService.loadCredential()
        return [authorized: cred != null]
    }

    def settingsGoogleAuthRequest() {
        String uri = googleOAuthService.registerAtProxy(
            createLink(controller: controllerName, action: 'settingsGoogleAuthResponse', absolute: true)
        )
        if (uri == null) {
            flash.message = message(code: 'user.settings.googleAuth.failed.message')
            redirect(action: 'settingsIndex')
            return
        }

        redirect(uri: uri)
    }

    def settingsGoogleAuthResponse() {
        if (params.success != '200') {
            flash.message = message(code: 'user.settings.googleAuth.failed.message')
            redirect(action: 'settingsGoogleAuth')
            return
        }

        if (!googleOAuthService.obtainAndStoreCredential(params.clientId)) {
            flash.message = message(code: 'user.settings.googleAuth.failed.message')
            redirect(action: 'settingsGoogleAuth')
            return
        }

        flash.message = message(code: 'user.settings.googleAuth.succeeded.message')
        redirect(action: 'settingsIndex')
    }

    def settingsGoogleAuthRevoke() {
        googleOAuthService.revokeAtProxy()

        flash.message = message(code: 'user.settings.googleAuth.revoked.message')
        redirect(action: 'settingsIndex')
    }
}
