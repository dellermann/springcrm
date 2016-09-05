/*
 * MailSystemServiceSpec.groovy
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

import com.naleid.grails.MarkdownService
import grails.gsp.PageRenderer
import grails.plugins.mail.MailService
import grails.test.mixin.TestFor
import org.springframework.context.MessageSource
import spock.lang.Ignore
import spock.lang.Specification


@TestFor(MailSystemService)
class MailSystemServiceSpec extends Specification {

    //-- Feature methods ------------------------

    @Ignore('Unsetting configuration does not work')
    def 'Check if mail system is not configured'() {
        given:
        config.grails.mail.host = null
        println config.grails.mail.toProperties()

        expect:
        !service.configured
    }

    @Ignore('Setting configuration does not work')
    def 'Check if mail system is set to use user configuration'() {
        given:
        config.grails.mail.host = '192.168.100.100'

        expect:
        service.configured
    }

    def 'Convert plain text to HTML'() {
        given: 'a mocked MarkdownService'
        MarkdownService markdownService = Mock()
        1 * markdownService.markdown('Hello *World*!') >> '<p>Hello <strong>World</strong>!</p>'
        service.markdownService = markdownService

        when: 'I convert a plain text string to HTML'
        String html = service.convertToHtml('Hello *World*!')

        then: 'I get the correct HTML'
        '<p>Hello <strong>World</strong>!</p>' == html
    }

    def 'Obtain text message from e-mail template'() {
        given: 'a mocked PageRenderer'
        PageRenderer pageRenderer = Mock()
        1 * pageRenderer.render({ it.view == '/email/ticket/test' && it.model.name == 'Daniel' }) >> 'Hello *World*!'
        service.groovyPageRenderer = pageRenderer

        when: 'I obtain a text message'
        String text = service.getTextMessage('ticket', 'test', [name: 'Daniel'])

        then: 'I get the correct text'
        'Hello *World*!' == text
    }

    def 'Obtain HTML message from e-mail template'() {
        given: 'a mocked MarkdownService'
        MarkdownService markdownService = Mock()
        1 * markdownService.markdown('Hello *World*!') >> '<p>Hello <strong>World</strong>!</p>'
        service.markdownService = markdownService

        and: 'a mocked PageRenderer'
        PageRenderer pageRenderer = Mock()
        1 * pageRenderer.render({ it.view == '/email/ticket/test' && it.model.name == 'Daniel' }) >> 'Hello *World*!'
        service.groovyPageRenderer = pageRenderer

        when: 'I obtain an HTML message'
        String html = service.getHtmlMessage('ticket', 'test', [name: 'Daniel'])

        then: 'I get the correct HTML'
        '<p>Hello <strong>World</strong>!</p>' == html
    }

    def 'Send mail'() {
        given: 'a mocked MailService'
        MailService mailService = Mock()
        service.mailService = mailService

        when: 'I send an e-mail with simple data'
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: 'This is a test message.',
            htmlMessage: '<p>This is a test message.</p>'
        )

        then: 'the mail service is called with the correct data'
        1 * mailService.sendMail(_) >> { Closure mail ->
            def builder = new NodeBuilder()
            def data = builder(mail)
            assert data.multipart
            assert 'AMC World system service <noreply@amc-world.de>' == data.from.text()
            assert 'Marcus Kampe <m.kampe@kampe.example>' == data.to.text()
            assert 'Test email' == data.subject.text()
            assert 'This is a test message.' == data.text.text()
            assert '<p>This is a test message.</p>' == data.html.text()

            null
        }
    }

    def 'Send mail without HTML text'() {
        given: 'a mocked MailService'
        MailService mailService = Mock()
        service.mailService = mailService

        and: 'a mocked MarkdownService'
        MarkdownService markdownService = Mock()
        1 * markdownService.markdown('This is a test message.') >> '<p>This is a test message.</p>'
        service.markdownService = markdownService

        when: 'I send a mail'
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: 'This is a test message.'
        )

        then: 'the mail service is called with the correct data'
        1 * mailService.sendMail(_) >> { Closure mail ->
            def builder = new NodeBuilder()
            def data = builder(mail)
            assert data.multipart
            assert 'AMC World system service <noreply@amc-world.de>' == data.from.text()
            assert 'Marcus Kampe <m.kampe@kampe.example>' == data.to.text()
            assert 'Test email' == data.subject.text()
            assert 'This is a test message.' == data.text.text()
            assert '<p>This is a test message.</p>' == data.html.text()

            null
        }
    }

    def 'Send mail without sender address'() {
        given: 'a mocked MailService'
        MailService mailService = Mock()
        service.mailService = mailService

        and: 'a mocked MarkdownService'
        MarkdownService markdownService = Mock()
        1 * markdownService.markdown('This is a test message.') >> '<p>This is a test message.</p>'
        service.markdownService = markdownService

        and: 'a mock for the getFromAddress method'
        config.springcrm.mail.from = 'AMC World system service <noreply@amc-world.de>'

        when: 'I send a mail'
        service.sendMail(
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: 'This is a test message.'
        )

        then: 'the mail service is called with the correct data'
        1 * mailService.sendMail(_) >> { Closure mail ->
            def builder = new NodeBuilder()
            def data = builder(mail)
            assert data.multipart
            assert 'AMC World system service <noreply@amc-world.de>' == data.from.text()
            assert 'Marcus Kampe <m.kampe@kampe.example>' == data.to.text()
            assert 'Test email' == data.subject.text()
            assert 'This is a test message.' == data.text.text()
            assert '<p>This is a test message.</p>' == data.html.text()

            null
        }
    }

    def 'Send mail with localized subject'() {
        given: 'a mocked MailService'
        MailService mailService = Mock()
        service.mailService = mailService

        and: 'a mocked MarkdownService'
        MarkdownService markdownService = Mock()
        1 * markdownService.markdown('This is a test message.') >> '<p>This is a test message.</p>'
        service.markdownService = markdownService

        and: 'a mocked MessageSource'
        MessageSource messageSource = Mock()
        1 * messageSource.getMessage('foo.bar', ['email'] as Object[], _, _) >> 'Test email'
        service.messageSource = messageSource

        when: 'I send a mail'
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: [key: 'foo.bar', args: ['email']],
            message: 'This is a test message.'
        )

        then: 'the mail service is called with the correct data'
        1 * mailService.sendMail(_) >> { Closure mail ->
            def builder = new NodeBuilder()
            def data = builder(mail)
            assert data.multipart
            assert 'AMC World system service <noreply@amc-world.de>' == data.from.text()
            assert 'Marcus Kampe <m.kampe@kampe.example>' == data.to.text()
            assert 'Test email' == data.subject.text()
            assert 'This is a test message.' == data.text.text()
            assert '<p>This is a test message.</p>' == data.html.text()

            null
        }
    }

    def 'Send mail with template text'() {
        given: 'a mocked MailService'
        MailService mailService = Mock()
        service.mailService = mailService

        and: 'a mocked MarkdownService'
        MarkdownService markdownService = Mock()
        1 * markdownService.markdown('This is a test message.') >> '<p>This is a test message.</p>'
        service.markdownService = markdownService

        and: 'a mocked PageRenderer'
        PageRenderer pageRenderer = Mock()
        1 * pageRenderer.render({ it.view == '/email/ticket/test' && it.model.name == 'Daniel' }) >> 'This is a test message.'
        service.groovyPageRenderer = pageRenderer

        when: 'I send a mail'
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: [
                controller: 'ticket', view: 'test', model: [name: 'Daniel']
            ]
        )

        then: 'the mail service is called with the correct data'
        1 * mailService.sendMail(_) >> { Closure mail ->
            def builder = new NodeBuilder()
            def data = builder(mail)
            assert data.multipart
            assert 'AMC World system service <noreply@amc-world.de>' == data.from.text()
            assert 'Marcus Kampe <m.kampe@kampe.example>' == data.to.text()
            assert 'Test email' == data.subject.text()
            assert 'This is a test message.' == data.text.text()
            assert '<p>This is a test message.</p>' == data.html.text()

            null
        }
    }
}
