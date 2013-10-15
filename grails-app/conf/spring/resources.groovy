/*
 * resources.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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


beans = {

    /* configuration handling */
    springcrmConfig(org.springframework.jndi.JndiObjectFactoryBean) {
        defaultObject = ''
        lookupOnStartup = true
        jndiName = 'java:comp/env/springcrmConfig'
    }

    /* property type registrar */
    appEditorRegistrar(org.amcworld.springcrm.util.AppPropertyEditorRegistrar) {
        messageSource = ref('messageSource')
    }

    /* startup difference sets */
    startupDiffSet2(org.amcworld.springcrm.install.diffset.NoteMarkdownDiffSet)
    {
        markdownService = ref('markdownService')
    }

    /* Google synchronization types */
    googleContactSync(org.amcworld.springcrm.google.GoogleContactSync) {
        googleOAuthService = ref('googleOAuthService')
        messageSource = ref('messageSource')
    }
    googleCalendarSync(org.amcworld.springcrm.google.GoogleCalendarSync) {
        googleOAuthService = ref('googleOAuthService')
        messageSource = ref('messageSource')
    }
}
