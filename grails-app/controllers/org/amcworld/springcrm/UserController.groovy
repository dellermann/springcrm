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

import javax.servlet.http.HttpServletResponse
import org.apache.commons.lang.LocaleUtils


/**
 * The class {@code UserController} contains methods which manager the users of
 * the application.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class UserController extends GenericDomainController<User> {

    //-- Fields ---------------------------------

    GoogleOAuthService googleOAuthService
    InstallService installService
    SecurityService securityService
    UserService userService


    //-- Constructors ---------------------------

    UserController() {
        super(User)
    }


    //-- Public methods -------------------------

    def authenticate() {
        User userInstance = userService.findByUserNameAndPassword(
            params.userName.toString(), params.password.toString()
        )
        if (userInstance == null) {
            flash.message =
                message(code: 'user.authenticate.failed.message') as Object
            redirect action: 'login'
            return
        }

        session['credential'] = new Credential(userInstance)
        setUserLocale()

        redirect controller: 'overview', action: 'index'
    }

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def index() {
        if (params.letter) {
            int num =
                userService.countByUserNameLessThan(params.letter.toString())
            params.sort = 'userName'
            int max = params.int('max')
            params.offset = Math.floor(num / max) * max
        }

        getIndexModel userService.list(params), userService.count()
    }

    def login() {}

    def logout() {
        flash.message = message(code: 'user.logout.message') as Object
        session['credential'] = null
        session.invalidate()

        redirect action: 'login'
    }

    def save() {
        super.save()
    }

    def settingsControl() {
        [saveType: credential.settings['saveType'] ?: 'saveAndClose']
    }

    def settingsControlSave(String saveType) {
        credential.settings['saveType'] = saveType

        redirect action: 'settingsIndex'
    }

    def settingsGoogleAuth() {
        com.google.api.client.auth.oauth2.Credential cred =
            googleOAuthService.loadCredential(credential.userName)

        [authorized: cred != null]
    }

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

    def settingsGoogleAuthResponse() {
        if (params.success.toString() != '200') {
            flash.message = message(
                code: 'user.settings.googleAuth.failed.message'
            ) as Object
            redirect action: 'settingsGoogleAuth'
            return
        }

        boolean res = googleOAuthService.obtainAndStoreCredential(
            credential.userName,
            params.clientId.toString()
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

    def settingsGoogleAuthRevoke() {
        googleOAuthService.revokeAtProxy credential.userName

        flash.message =
            message(code: 'user.settings.googleAuth.revoked.message') as Object
        redirect action: 'settingsIndex'
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

    def settingsSync() {
        List<Long> values =
            credential.settings.excludeFromSync?.split(/,/)?.collect {
                it as Long
            }

        [ratings: Rating.list(), excludeFromSync: values]
    }

    def settingsSyncSave() {
        credential.settings.excludeFromSync = params.excludeFromSync.join ','

        redirect action: 'settingsIndex'
    }

    def show(Long id) {
        super.show id
    }

    def storeSetting() {
        credential.settings.put params.key.toString(), params.value.toString()

        render status: HttpServletResponse.SC_OK
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods ---------------------

    /**
     * Checks whether or not the submitted password has been repeated
     * correctly.
     *
     * @return  {@code} if the password has been repeated correctly;
     *          {@code false} otherwise
     * @since   2.2
     */
    private boolean isPasswordMatch() {
        params.password ==
            securityService.encryptPassword(params.passwordRepeat?.toString())
    }

    @Override
    protected void lowLevelDelete(User instance) {
        userService.delete instance.id
    }

    @Override
    protected User lowLevelGet(Long id) {
        userService.get id
    }

    @Override
    protected User lowLevelSave(User instance) {
        userService.save instance
    }

    @Override
    protected User saveInstance(User instance) {
        User userInstance = new User()
        bindData userInstance, params, [exclude: ['allowedModulesNames']]
        userInstance.allowedModulesNames =
            params.list('allowedModulesNames') as Set

        if (!passwordMatch) {
            userInstance.errors.rejectValue(
                'password', 'user.password.doesNotMatch'
            )
            return null
        }

        lowLevelSave userInstance
    }

    @Override
    protected User updateInstance(User userInstance) {
        String oldPassword = userInstance.password
        bindData userInstance, params, [exclude: ['allowedModulesNames']]
        userInstance.allowedModulesNames =
            params.list('allowedModulesNames') as Set

        if (params.password && !passwordMatch) {
            userInstance.errors.rejectValue(
                'password', 'user.password.doesNotMatch'
            )
            return null
        }

        userInstance.password = params.password ?: oldPassword

        lowLevelSave userInstance
    }

    private void setUserLocale(String locale = null) {
        if (locale) {
            credential.settings.locale = locale
        } else {
            locale = credential.settings.locale
        }
        if (locale) {
            session['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'] =
                LocaleUtils.toLocale(locale)
        }
    }
}
