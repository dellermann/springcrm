/*
 * MailSystemService.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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

import com.naleid.grails.MarkdownService
import grails.gsp.PageRenderer
import grails.plugin.mail.MailService
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.mail.MailMessage


/**
 * The class {@code MailSystemService} contains service methods to send mails.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class MailSystemService {

    //-- Instance variables ---------------------

    GrailsApplication grailsApplication
    MailService mailService
    MarkdownService markdownService
    MessageSource messageSource
    PageRenderer groovyPageRenderer


    //-- Properties -----------------------------

    /**
     * Checks whether or not the mail system is configured at all.
     *
     * @return  {@code true} if the mail system is configured at all;
     *          {@code false} otherwise
     */
    boolean isConfigured() {
        config['mailUseConfig'] != null
    }

    /**
     * Checks whether or not the mail system is configured by the user, that
     * is, using system configuration data.
     *
     * @return  {@code true} if the mail system is user configured;
     *          {@code false} if the mail system is configured by the
     *          underlying system
     */
    boolean isUserConfigured() {
        Boolean b = config['mailUseConfig'] as Boolean
        b != null && b.booleanValue()
    }


    //-- Public methods -------------------------

    /**
     * Converts the given plain text to HTML using Markdown.
     *
     * @param text  the given text
     * @return      the code converted to HTML
     */
    String convertToHtml(String text) {
        markdownService.markdown text
    }

    /**
     * Gets the sender address for e-mails from SpringCRM.
     *
     * @return  the sender address
     */
    String getFromAddress() {
        grailsApplication.config.springcrm.mail.from
    }

    /**
     * Retrieves the text in the given view and converts it to HTML using
     * Markdown.
     *
     * @param controller    the controller that view should be rendered
     * @param view          the view that should be rendered relative to
     *                      "/email/${controller}"
     * @param model         a data the view uses
     * @return              the rendered and converted HTML code
     */
    String getHtmlMessage(String controller, String view, Map model) {
        convertToHtml getTextMessage(controller, view, model)
    }

    /**
     * Retrieves the text in the given view.
     *
     * @param controller    the controller that view should be rendered
     * @param view          the view that should be rendered
     * @param model         a data the view uses
     * @return              the rendered plain text code
     */
    String getTextMessage(String controller, String view, Map model) {
        groovyPageRenderer.render view: "/email/${controller}/${view}", model: model
    }

    /**
     * Sends a mail using the given data.  The given map may contain the
     * following keys:
     * <ul>
     *   <li>from [optional].  The sender address.  If not specified property
     *   {@code fromAddress} is used.</li>
     *   <li>to.  The recipient address.</li>
     *   <li>subject.  The subject of the e-mail.  If it is a {@code Map} the
     *   subject is obtained from message resources using the following map
     *   keys:
     *   <ul>
     *     <li>key.  The message key.</li>
     *     <li>args [optional].  Any message arguments.</li>
     *     <li>defaultMsg [optional].  The default message.</li>
     *     <li>locale [optional].  The locale.</li>
     *   </ul>
     *   Otherwise, the string representation is used.</li>
     *   <li>message.  The plain text message of the e-mail.  If it is a
     *   {@code Map} the message text is obtained from views in
     *   {@code /email/${controller}/${view}} using the following map keys:
     *   <ul>
     *     <li>controller.  The name of the controller.</li>
     *     <li>view.  The name of the view.</li>
     *     <li>model [optional].  The model used to render data into the
     *     view.</li>
     *   </ul>
     *   Otherwise, the string representation is used.</li>
     *   <li>htmlMessage [optional].  The HTML representation of the text
     *   message.  If not set the HTML representation is converted from the
     *   text using Markdown.</li>
     * </ul>
     *
     * @param mail  the mail data
     * @return      the sent mail message; {@code null} if the mail system is
     *              not configured yet
     */
    MailMessage sendMail(Map data) {
        if (!configured) {
            return null
        }

        String msgSubject
        if (data.subject instanceof Map) {
            msgSubject = messageSource.getMessage(
                data.subject.key, data.subject.args as Object[],
                data.subject.defaultMsg ?: '',
                data.subject.locale ?: LCH.locale
            )
        } else {
            msgSubject = data.subject.toString()
        }

        String plainMsgText
        if (data.message instanceof Map) {
            plainMsgText = getTextMessage(
                data.message.controller, data.message.view,
                data.message.model ?: []
            )
        } else {
            plainMsgText = data.message.toString()
        }
        String htmlMsgText = data.htmlMessage ?: convertToHtml(plainMsgText)

        sendRawMail {
            multipart true
            from data.from ?: fromAddress
            to data.to
            subject msgSubject
            text plainMsgText
            html htmlMsgText
        }
    }

    /**
     * Sends a mail using the given data.
     *
     * @param mail  the mail data
     * @return      the sent mail message
     */
    MailMessage sendRawMail(Closure mail) {
        if (!configured) {
            return null
        }

        mailService.sendMail mailConfiguration, mail
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the configuration data from the application wide configuration
     * holder.
     *
     * @return  the configuration holder
     */
    protected ConfigHolder getConfig() {
        ConfigHolder.instance
    }

    /**
     * Gets the configuration data for the mail system.
     *
     * @return  the configuration data
     */
    protected ConfigObject getMailConfiguration() {
        ConfigObject configuration = new ConfigObject()

        configuration.host = (config['mailHost'] as String) ?: 'localhost'
        int port = (config['mailPort'] as Integer) ?: 587
        configuration.port = port
        String s = config['mailUserName'] as String
        if (s) configuration.username = s
        s = config['mailPassword'] as String
        if (s) configuration.password = s

        def props = [: ]
        if (config['mailAuth'] as Boolean) {
            props.'mail.smtp.auth' = true
        }
        String encryption = (config['mailEncryption'] as String) ?: 'none'
        if (encryption == 'ssl') {
            props.'mail.smtp.socketFactory.port' = port
            props.'mail.smtp.socketFactory.class' = 'javax.net.ssl.SSLSocketFactory'
            props.'mail.smtp.socketFactory.fallback' = false
        } else if (encryption == 'starttls') {
            props.'mail.smtp.starttls.enable' = true
            props.'mail.smtp.port' = port
        }
        configuration.props = props

        configuration
    }
}
