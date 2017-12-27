/*
 * UserController.groovy
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

import static org.springframework.http.HttpStatus.*

import com.google.api.client.auth.oauth2.Credential
import com.mongodb.client.model.Filters
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.lang.LocaleUtils
import org.bson.types.ObjectId


/**
 * The class {@code UserController} contains methods which manager the users of
 * the application.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured(['ROLE_ADMIN', 'ROLE_USER'])
@Transactional(readOnly = true)
class UserController {

    //-- Class fields -------------------------------

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']


    //-- Fields ---------------------------------

    GoogleOAuthService googleOAuthService
    UserService userService


    //-- Public methods -------------------------

    def copy(User user) {
        respond new User(user), view: 'create'
    }

    def create() {
        respond new User(params)
    }

    @Transactional
    def delete(User user) {
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (userService.isOnlyAdmin(user)) {
            render status: FORBIDDEN
            return
        }

        userService.deleteUser user.id

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.deleted.message',
                    args: [message(code: 'user.label'), user.toString()]
                ) as Object
                redirect(
                    action: 'index', method: 'GET'
                )
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def edit(User user) {
        respond user
    }

    def index(Integer max) {
        if (params.letter) {
            Number num = User.count(
                Filters.lt('username', params.letter.toString())
            )
            params.sort = 'username'
            params.offset = Math.floor(num.toDouble() / max) * max
        }

        respond(
            userService.listUsers(params),
            model: [userCount: userService.countUsers()]
        )
    }

    @Transactional
    def save(User user) {
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (!user.password) {
            user.errors.rejectValue(
                'password', 'default.blank.message',
                [message(code: 'user.password.label')] as Object[], ''
            )
        } else if (params.password != params.passwordRepeat) {
            user.errors.rejectValue 'password', 'user.password.mismatch'
        } else {
            user.password = userService.encodePassword(user.password)
        }

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view: 'create'
            return
        }

        userService.saveUser user

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.created.message',
                    args: [message(code: 'user.label'), user.toString()]
                ) as Object
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }
    }

    @Secured('isAuthenticated()')
    def settingsControl() {
        respond saveType: user.settings['saveType'] ?: 'saveAndClose'
    }

    @Secured('isAuthenticated()')
    def settingsControlSave(String saveType) {
        User user = getUser()
        user.settings['saveType'] = saveType
        userService.saveUser user

        redirect action: 'settingsIndex'
    }

    @Secured('isAuthenticated()')
    def settingsGoogleAuth() {
        Credential cred = googleOAuthService.loadCredential(user.username)

        respond authorized: cred != null
    }

    @Secured('isAuthenticated()')
    def settingsGoogleAuthRequest() {
        String uri = googleOAuthService.registerAtProxy(
            createLink(
                controller: controllerName,
                action: 'settingsGoogleAuthResponse', absolute: true
            ) as CharSequence
        )
        if (uri == null) {
            flash.message = message(
                code: 'user.settings.googleAuth.failed.message'
            ) as Object
            redirect action: 'settingsIndex'
            return
        }

        redirect uri: uri
    }

    @Secured('isAuthenticated()')
    def settingsGoogleAuthResponse() {
        if (params.success.toString() != '200') {
            flash.message = message(
                code: 'user.settings.googleAuth.failed.message'
            ) as Object
            redirect action: 'settingsGoogleAuth'
            return
        }

        boolean res = googleOAuthService.obtainAndStoreCredential(
            user.username, params.clientId.toString()
        )
        if (!res) {
            flash.message = message(
                code: 'user.settings.googleAuth.failed.message'
            ) as Object
            redirect action: 'settingsGoogleAuth'
            return
        }

        flash.message = message(
            code: 'user.settings.googleAuth.succeeded.message'
        ) as Object
        redirect action: 'settingsIndex'
    }

    @Secured('isAuthenticated()')
    def settingsGoogleAuthRevoke() {
        googleOAuthService.revokeAtProxy user.username

        flash.message =
            message(code: 'user.settings.googleAuth.revoked.message') as Object
        redirect action: 'settingsIndex'
    }

    @Secured('isAuthenticated()')
    def settingsIndex() {}

    @Secured('isAuthenticated()')
    def settingsLanguage() {
        Map<String, String> locales =
            userService.availableLocales.collectEntries {
                [it.toString(), it.displayName]
            }
        locales = locales.sort { a, b -> a.value <=> b.value }

        respond(
            currentLocale: userService.currentLocale.toString(),
            locales: locales
        )
    }

    @Secured('isAuthenticated()')
    def settingsLanguageSave(String locale) {
        setUserLocale locale

        redirect action: 'settingsIndex'
    }

    @Secured('isAuthenticated()')
    def settingsSync() {
        List<Long> values =
            user.settings.excludeFromSync?.toString()?.split(/,/)?.collect {
                it as Long
            }

        respond ratings: Rating.list(), excludeFromSync: values
    }

    @Secured('isAuthenticated()')
    def settingsSyncSave() {
        User user = getUser()
        user.settings.excludeFromSync = params.excludeFromSync.join ','
        userService.saveUser user

        redirect action: 'settingsIndex'
    }

    def show(User user) {
        respond user, model: [disableDelete: userService.isOnlyAdmin(user)]
    }

    @Secured('isAuthenticated()')
    def storeSetting() {
        User user = getUser()
        user.settings.put params.key.toString(), params.value
        userService.saveUser user

        render status: OK
    }

    @Transactional
    def update(String id) {
        User user = userService.getUser(new ObjectId(id))
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        bindData user, params, [exclude: ['password', 'authorities']]
        Set<RoleGroup> authorities = new HashSet<>(1)
        if (params.authorities) {
            RoleGroup authority = RoleGroup.get(params.authorities?.toString())
            if (authority != null) {
                authorities.add authority
            }
        }
        user.authorities = authorities
        user.validate()

        String password = params.password
        if (password) {
            if (password == params.passwordRepeat) {
                user.password = userService.encodePassword(password)
            } else {
                user.errors.rejectValue 'password', 'user.password.mismatch'
            }
        }

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view: 'edit'
            return
        }

        userService.saveUser user

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.updated.message',
                    args: [message(code: 'user.label'), user]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { respond user, [status: OK] }
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the currently logged in user.
     *
     * @return  the currently logged in user
     * @since   3.0
     */
    private User getUser() {
        userService.currentUser
    }

    /**
     * Creates a response if the domain model instance has not been found.
     *
     * @since 3.0
     */
    private void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'user.label'), params.id]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }

    private void setUserLocale(String locale = null) {
        User user = getUser()
        if (locale) {
            user.settings.locale = locale
            userService.saveUser user
        } else {
            locale = user.settings.locale
        }
        if (locale) {
            session['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'] =
                LocaleUtils.toLocale(locale)
        }
    }
}
