/*
 * UserController.groovy
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

import grails.artefact.Controller
import javax.servlet.http.HttpServletResponse
import org.apache.commons.lang.LocaleUtils
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code UserController} contains methods which manager the users of
 * the application.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class UserController implements Controller {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    GoogleOAuthService googleOAuthService
    InstallService installService
    SecurityService securityService
    UserService userService


    //-- Public methods -------------------------

    def index() {
        int max = params.max =
            Math.min(params.max ? params.int('max') : 10, 100)
        if (params.letter) {
            int num = User.countByUserNameLessThan(params.letter.toString())
            params.sort = 'userName'
            params.offset = Math.floor(num / max) * max
        }

        [userInstanceList: User.list(params), userInstanceTotal: User.count()]
    }

    def create() {
        [userInstance: new User(params)]
    }

    def copy(Long id) {
        User userInstance = User.get(id)
        if (!userInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'user.label'), id]
            )
            redirect action: 'index'
            return
        }

        userInstance = new User(userInstance)
        render view: 'create', model: [userInstance: userInstance]
    }

    def save() {
        User userInstance = new User()
        bindData userInstance, params, [exclude: ['allowedModulesNames']]
        userInstance.allowedModulesNames = params.list('allowedModulesNames') as Set

        boolean passwordMismatch =
            params.password != securityService.encryptPassword(params.passwordRepeat)
        if (passwordMismatch) {
            userInstance.errors.rejectValue(
                'password', 'user.password.doesNotMatch'
            )
        }
        if (passwordMismatch || !userInstance.save(flush: true)) {
            render view: 'create', model: [userInstance: userInstance]
            return
        }

        request.userInstance = userInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'user.label'), userInstance.toString()]
        )

        redirect action: 'show', id: userInstance.id
    }

    def show(Long id) {
        User userInstance = User.get(id)
        if (!userInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'user.label'), id]
            )
            redirect action: 'index'
            return
        }

        [userInstance: userInstance]
    }

    def edit(Long id) {
        User userInstance = User.get(id)
        if (!userInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'user.label'), id]
            )
            redirect action: 'index'
            return
        }

        [userInstance: userInstance]
    }

    def update(Long id) {
        User userInstance = User.get(id)
        if (!userInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'user.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (userInstance.version > version) {
                userInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'user.label')] as Object[],
                    'Another user has updated this User while you were editing'
                )
                render view: 'edit', model: [userInstance: userInstance]
                return
            }
        }

        String passwd = userInstance.password
        bindData userInstance, params, [exclude: ['allowedModulesNames']]
        userInstance.allowedModulesNames = params.list('allowedModulesNames') as Set

        boolean passwordMismatch = false
        if (params.password) {
            passwordMismatch = params.password != securityService.encryptPassword(params.passwordRepeat)
            if (passwordMismatch) {
                userInstance.errors.rejectValue(
                    'password', 'user.password.doesNotMatch'
                )
            }
        } else {
            userInstance.password = passwd
        }
        if (passwordMismatch || !userInstance.save(flush: true)) {
            render view: 'edit', model: [userInstance: userInstance]
            return
        }

        request.userInstance = userInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'user.label'), userInstance.toString()]
        )

        redirect action: 'show', id: userInstance.id
    }

    def delete(Long id) {
        User userInstance = User.get(id)
        if (!userInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'user.label'), id]
            )

            redirect action: 'index'
            return
        }

        request.userInstance = userInstance
        try {
            userInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'user.label')]
            )

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'user.label')]
            )

            redirect action: 'show', id: id
        }
    }

    def login() {}

    def authenticate() {
        User userInstance = User.findByUserNameAndPassword(
            params.userName.toString(), params.password.toString()
        )
        if (!userInstance) {
            flash.message = message(code: 'user.authenticate.failed.message')
            redirect action: 'login'
            return
        }

        session.credential = new Credential(userInstance)
        setUserLocale()

        redirect controller: 'overview', action: 'index'
    }

    def logout() {
        flash.message = message(code: 'user.logout.message')
        session.credential = null
        session.invalidate()

        redirect action: 'login'
    }

    def storeSetting() {
        Credential credential = (Credential) session.credential
        credential.settings[params.key] = params.value

        render status: HttpServletResponse.SC_OK
    }

    def settingsIndex() {}

    def settingsLanguage() {
        Map<String, String> locales =
            userService.availableLocales.collectEntries {
                [it.toString(), it.displayName]
            }
        locales = locales.sort { a, b -> a.value <=> b.value }

        [locales: locales, currentLocale: userService.currentLocale.toString()]
    }

    def settingsLanguageSave(String locale) {
        setUserLocale locale

        redirect action: 'settingsIndex'
    }

    def settingsGoogleAuth() {
        com.google.api.client.auth.oauth2.Credential cred =
            googleOAuthService.loadCredential(session.credential.userName)

        [authorized: cred != null]
    }

    def settingsGoogleAuthRequest() {
        String uri = googleOAuthService.registerAtProxy(
            createLink(
                controller: controllerName,
                action: 'settingsGoogleAuthResponse', absolute: true
            )
        )
        if (uri == null) {
            flash.message = message(
                code: 'user.settings.googleAuth.failed.message'
            )
            redirect action: 'settingsIndex'
            return
        }

        redirect uri: uri
    }

    def settingsGoogleAuthResponse() {
        if (params.success.toString() != '200') {
            flash.message = message(
                code: 'user.settings.googleAuth.failed.message'
            )
            redirect action: 'settingsGoogleAuth'
            return
        }

        boolean res = googleOAuthService.obtainAndStoreCredential(
            ((Credential) session.credential).userName,
            params.clientId.toString()
        )
        if (!res) {
            flash.message = message(
                code: 'user.settings.googleAuth.failed.message'
            )
            redirect action: 'settingsGoogleAuth'
            return
        }

        flash.message = message(
            code: 'user.settings.googleAuth.succeeded.message'
        )
        redirect action: 'settingsIndex'
    }

    def settingsGoogleAuthRevoke() {
        googleOAuthService.revokeAtProxy(
            ((Credential) session.credential).userName
        )

        flash.message = message(
            code: 'user.settings.googleAuth.revoked.message'
        )
        redirect action: 'settingsIndex'
    }

    def settingsSync() {
        Credential credential = (Credential) session.credential
        List<Long> values =
            credential.settings.excludeFromSync?.split(/,/)?.collect {
                it as Long
            }

        [
            ratings: Rating.list(),
            excludeFromSync: values
        ]
    }

    def settingsSyncSave() {
        Credential credential = (Credential) session.credential
        credential.settings.excludeFromSync = params.excludeFromSync.join ','

        redirect action: 'settingsIndex'
    }


    //-- Non-public methods ---------------------

    private void setUserLocale(String locale = null) {
        if (locale) {
            session.credential.settings.locale = locale
        } else {
            locale = session.credential.settings.locale
        }
        if (locale) {
            session['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'] =
                LocaleUtils.toLocale(locale)
        }
    }
}
