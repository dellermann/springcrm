/*
 * UserController.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
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
class UserController {

    //-- Class fields -------------------------------

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']


    //-- Fields ---------------------------------

    GoogleOAuthService googleOAuthService
    UserService userService
    UserSettingService userSettingService


    //-- Public methods -------------------------

    def copy(User user) {
        respond new User(user), view: 'create'
    }

    def create() {
        respond new User(params)
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        User user = userService.get(new ObjectId(id))
        if (userService.isOnlyAdmin(user)) {
            render status: FORBIDDEN
            return
        }

        userService.delete new ObjectId(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.deleted.message',
                    args: [message(code: 'user.label'), user]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def edit(String id) {
        respond id == null ? null : userService.get(new ObjectId(id))
    }

    def index(Integer max) {
        if (params.letter) {
            Number num =
                userService.countByUsernameLessThan(params.letter.toString())
            params.sort = 'username'
            params.offset = (int) (Math.floor(num.toDouble() / max) * max)
        }

        respond(
            userService.list(params),
            model: [userCount: userService.count()]
        )
    }

    def save(User user) {
        if (user == null) {
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

        try {
            userService.save user
        } catch (ValidationException ignored) {
            respond user.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.created.message',
                    args: [message(code: 'user.label'), user]
                ) as Object
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }
    }

    @Secured('isAuthenticated()')
    def settingsControl() {
        respond(
            saveType: userSettingService.getString(
                currentUser, 'saveType', 'saveAndClose'
            )
        )
    }

    @Secured('isAuthenticated()')
    def settingsControlSave(String saveType) {
        userSettingService.store currentUser, 'saveType', saveType

        redirect action: 'settingsIndex'
    }

    @Secured('isAuthenticated()')
    def settingsGoogleAuth() {
        Credential cred =
            googleOAuthService.loadCredential(currentUser.username)

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
            currentUser.username, params.clientId.toString()
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
        googleOAuthService.revokeAtProxy currentUser.username

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
            userSettingService.getString(currentUser, 'excludeFromSync')
                ?.split(/,/)
                ?.collect { it as Long }

        respond ratings: Rating.list(), excludeFromSync: values
    }

    @Secured('isAuthenticated()')
    def settingsSyncSave() {
        userSettingService.store(
            currentUser, 'excludeFromSync', params.excludeFromSync.join(',')
        )

        redirect action: 'settingsIndex'
    }

    def show(String id) {
        User user = id == null ? null : userService.get(new ObjectId(id))

        respond user, model: [disableDelete: userService.isOnlyAdmin(user)]
    }

    @Secured('isAuthenticated()')
    def storeSetting() {
        userSettingService.store(
            currentUser, params.key.toString(), params.value
        )

        render status: OK
    }

    def update(String id) {
        User user = id == null ? null : userService.get(new ObjectId(id))
        if (user == null) {
            notFound()
            return
        }

        bindData user, params, [exclude: ['password']]
        user.markDirty 'authorities'

        String password = params.password
        if (password) {
            if (password == params.passwordRepeat) {
                user.password = userService.encodePassword(password)
            } else {
                user.errors.rejectValue 'password', 'user.password.mismatch'
            }
        }

        try {
            userService.save user
        } catch (ValidationException ignored) {
            respond user.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.updated.message',
                    args: [message(code: 'user.label'), user]
                ) as Object
                redirect user
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
    private User getCurrentUser() {
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
        if (locale) {
            userSettingService.store currentUser, 'locale', locale
        } else {
            locale = userSettingService.getString(currentUser, 'locale')
        }
        if (locale) {
            session['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'] =
                LocaleUtils.toLocale(locale)
        }
    }
}
