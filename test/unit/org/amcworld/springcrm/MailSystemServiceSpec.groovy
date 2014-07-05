/*
 * MailSystemServiceSpec.groovy
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
import grails.test.mixin.TestFor
import org.springframework.context.MessageSource
import spock.lang.Specification


@TestFor(MailSystemService)
class MailSystemServiceSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Convert plain text to HTML'() {
        given: 'a mock for MarkdownService'
        def control = mockFor(MarkdownService)
        control.demand.markdown(1) { String text ->
            '<p>Hello <strong>World</strong>!</p>'
        }
        service.markdownService = control.createMock()

        when: 'I convert a plain text string to HTML'
        String html = service.convertToHtml('Hello *World*!')

        then: 'I get the correct HTML'
        '<p>Hello <strong>World</strong>!</p>' == html
    }

    def 'Obtain text message from e-mail template'() {
        given: 'a mock for PageRenderer'
        def control = mockFor(PageRenderer)
        control.demand.render(1) { Map data ->
            String res = ''
            if (data.view == '/email/ticket/test' && data.model.name == 'Daniel') {
                res = 'Hello *World*!'
            }
            res
        }
        service.groovyPageRenderer = control.createMock()

        when: 'I obtain a text message'
        String text = service.getTextMessage 'ticket', 'test', [name: 'Daniel']

        then: 'I get the correct text'
        'Hello *World*!' == text
    }

    def 'Obtain HTML message from e-mail template'() {
        given: 'a mock for MarkdownService'
        def control = mockFor(MarkdownService)
        control.demand.markdown(1) { String text ->
            '<p>Hello <strong>World</strong>!</p>'
        }
        service.markdownService = control.createMock()

        and: 'a mock for PageRenderer'
        control = mockFor(PageRenderer)
        control.demand.render(1) { Map data ->
            String res = ''
            if (data.view == '/email/ticket/test' && data.model.name == 'Daniel') {
                res = 'Hello *World*!'
            }
            res
        }
        service.groovyPageRenderer = control.createMock()

        when: 'I obtain an HTML message'
        String html = service.getHtmlMessage 'ticket', 'test', [name: 'Daniel']

        then: 'I get the correct HTML'
        '<p>Hello <strong>World</strong>!</p>' == html
    }

    def 'Send mail with simple data'() {
        given:
        def mailData
        def control = mockFor(MailService)
        control.demand.sendMail(1) { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
            null
        }
        service.mailService = control.createMock()

        when:
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: 'This is a test message.',
            htmlMessage: '<p>This is a test message.</p>'
        )

        then:
        mailData.multipart
        'AMC World system service <noreply@amc-world.de>' == mailData.from.text()
        'Marcus Kampe <m.kampe@kampe.example>' == mailData.to.text()
        'Test email' == mailData.subject.text()
        'This is a test message.' == mailData.text.text()
        '<p>This is a test message.</p>' == mailData.html.text()
    }

    def 'Send mail with simple data without HTML text'() {
        given:
        def mailData
        def control = mockFor(MailService)
        control.demand.sendMail(1) { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
            null
        }
        service.mailService = control.createMock()

        and: 'a mock for MarkdownService'
        control = mockFor(MarkdownService)
        control.demand.markdown(1) { String text ->
            '<p>This is a test message.</p>'
        }
        service.markdownService = control.createMock()

        when:
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: 'This is a test message.'
        )

        then:
        mailData.multipart
        'AMC World system service <noreply@amc-world.de>' == mailData.from.text()
        'Marcus Kampe <m.kampe@kampe.example>' == mailData.to.text()
        'Test email' == mailData.subject.text()
        'This is a test message.' == mailData.text.text()
        '<p>This is a test message.</p>' == mailData.html.text()
    }

    def 'Send mail without sender address'() {
        given:
        def mailData
        def control = mockFor(MailService)
        control.demand.sendMail(1) { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
            null
        }
        service.mailService = control.createMock()

        and: 'a mock for MarkdownService'
        control = mockFor(MarkdownService)
        control.demand.markdown(1) { String text ->
            '<p>This is a test message.</p>'
        }
        service.markdownService = control.createMock()

        and: 'a mock for the getFromAddress method'
        service.metaClass.getFromAddress = { ->
            'AMC World system service <noreply@amc-world.de>'
        }

        when:
        service.sendMail(
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: 'This is a test message.'
        )

        then:
        mailData.multipart
        'AMC World system service <noreply@amc-world.de>' == mailData.from.text()
        'Marcus Kampe <m.kampe@kampe.example>' == mailData.to.text()
        'Test email' == mailData.subject.text()
        'This is a test message.' == mailData.text.text()
        '<p>This is a test message.</p>' == mailData.html.text()
    }

    def 'Send mail with localized subject'() {
        given:
        def mailData
        def control = mockFor(MailService)
        control.demand.sendMail(1) { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
            null
        }
        service.mailService = control.createMock()

        and: 'a mock for MarkdownService'
        control = mockFor(MarkdownService)
        control.demand.markdown(1) { String text ->
            '<p>This is a test message.</p>'
        }
        service.markdownService = control.createMock()

        and: 'a mock for MessageSource'
        control = mockFor(MessageSource)
        control.demand.getMessage(1) { String key, Object [] args, String defMsg, Locale l ->
            (key == 'foo.bar') ? 'Test ' + args[0] : ''
        }
        service.messageSource = control.createMock()

        when:
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: [key: 'foo.bar', args: ['email']],
            message: 'This is a test message.'
        )

        then:
        mailData.multipart
        'AMC World system service <noreply@amc-world.de>' == mailData.from.text()
        'Marcus Kampe <m.kampe@kampe.example>' == mailData.to.text()
        'Test email' == mailData.subject.text()
        'This is a test message.' == mailData.text.text()
        '<p>This is a test message.</p>' == mailData.html.text()
    }

    def 'Send mail with simple data with template text'() {
        given:
        def mailData
        def control = mockFor(MailService)
        control.demand.sendMail(1) { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
            null
        }
        service.mailService = control.createMock()

        and: 'a mock for MarkdownService'
        control = mockFor(MarkdownService)
        control.demand.markdown(1) { String text ->
            '<p>This is a test message.</p>'
        }
        service.markdownService = control.createMock()

        and: 'a mock for PageRenderer'
        control = mockFor(PageRenderer)
        control.demand.render(1) { Map data ->
            String res = ''
            if (data.view == '/email/ticket/test' && data.model.name == 'Daniel') {
                res = 'This is a test message.'
            }
            res
        }
        service.groovyPageRenderer = control.createMock()

        when:
        service.sendMail(
            from: 'AMC World system service <noreply@amc-world.de>',
            to: 'Marcus Kampe <m.kampe@kampe.example>',
            subject: 'Test email',
            message: [
                controller: 'ticket', view: 'test', model: [name: 'Daniel']
            ]
        )

        then:
        mailData.multipart
        'AMC World system service <noreply@amc-world.de>' == mailData.from.text()
        'Marcus Kampe <m.kampe@kampe.example>' == mailData.to.text()
        'Test email' == mailData.subject.text()
        'This is a test message.' == mailData.text.text()
        '<p>This is a test message.</p>' == mailData.html.text()
    }

    def 'Send raw mail message'() {
        given:
        def mailData
        def control = mockFor(MailService)
        control.demand.sendMail(1) { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
            null
        }
        service.mailService = control.createMock()

        when:
        service.sendRawMail {
            multipart true
            from 'AMC World system service <noreply@amc-world.de>'
            to 'Marcus Kampe <m.kampe@kampe.example>'
            subject 'Test email'
            text 'This is a test message.'
            html '<p>This is a test message.</p>'
        }

        then:
        mailData.multipart
        'AMC World system service <noreply@amc-world.de>' == mailData.from.text()
        'Marcus Kampe <m.kampe@kampe.example>' == mailData.to.text()
        'Test email' == mailData.subject.text()
        'This is a test message.' == mailData.text.text()
        '<p>This is a test message.</p>' == mailData.html.text()
    }
}
