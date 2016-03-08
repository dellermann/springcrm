/*
 * ViewTagLibSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang.time.FastDateFormat
import org.springframework.context.MessageSource
import spock.lang.Specification


@TestFor(ViewTagLib)
@Mock([Config])
class ViewTagLibSpec extends Specification {

    //-- Feature methods ------------------------

    def 'AutoNumber without value'() {
        when: 'I use the tag without parameters'
        String s = applyTemplate('<g:autoNumber />')

        then: 'I get an empty autonumber widget'
        '<div class="auto-number"><div class="input-group"><input type="number" name="number" id="number" class="form-control" value="" size="10" disabled="disabled" /></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a prefix'
        s = applyTemplate('<g:autoNumber prefix="R" />')

        then: 'I get an empty autonumber widget with prefix'
        '<div class="auto-number"><div class="input-group"><span class="input-group-addon">R-</span><input type="number" name="number" id="number" class="form-control" value="" size="10" disabled="disabled" /></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a sensitive prefix'
        s = applyTemplate('<g:autoNumber prefix="${prefix}" />', [prefix: 'R&'])

        then: 'I get an empty autonumber widget with prefix'
        '<div class="auto-number"><div class="input-group"><span class="input-group-addon">R&amp;-</span><input type="number" name="number" id="number" class="form-control" value="" size="10" disabled="disabled" /></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a suffix'
        s = applyTemplate('<g:autoNumber suffix="M" />')

        then: 'I get an empty autonumber widget with suffix'
        '<div class="auto-number"><div class="input-group"><input type="number" name="number" id="number" class="form-control" value="" size="10" disabled="disabled" /><span class="input-group-addon">-M</span></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a sensitive suffix'
        s = applyTemplate('<g:autoNumber suffix="${suffix}" />', [suffix: '&M'])

        then: 'I get an empty autonumber widget with suffix'
        '<div class="auto-number"><div class="input-group"><input type="number" name="number" id="number" class="form-control" value="" size="10" disabled="disabled" /><span class="input-group-addon">-&amp;M</span></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a prefix and a suffix'
        s = applyTemplate('<g:autoNumber prefix="R" suffix="M" />')

        then: 'I get an empty autonumber widget with prefix and suffix'
        '<div class="auto-number"><div class="input-group"><span class="input-group-addon">R-</span><input type="number" name="number" id="number" class="form-control" value="" size="10" disabled="disabled" /><span class="input-group-addon">-M</span></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a sensitive prefix and suffix'
        s = applyTemplate(
            '<g:autoNumber prefix="${prefix}" suffix="${suffix}" />',
            [prefix: 'R&', suffix: '&M']
        )

        then: 'I get an empty autonumber widget with suffix'
        '<div class="auto-number"><div class="input-group"><span class="input-group-addon">R&amp;-</span><input type="number" name="number" id="number" class="form-control" value="" size="10" disabled="disabled" /><span class="input-group-addon">-&amp;M</span></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s
    }

    def 'AutoNumber with value'() {
        when: 'I use the tag with a prefix, a suffix, and a value'
        String s = applyTemplate(
            '<g:autoNumber prefix="R" suffix="M" value="38473" />'
        )

        then: 'I get an autonumber widget with prefix and suffix'
        '<div class="auto-number"><div class="input-group"><span class="input-group-addon">R-</span><input type="number" name="number" id="number" class="form-control" value="38473" size="10" disabled="disabled" /><span class="input-group-addon">-M</span></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a prefix, a suffix, a value, and the checkbox set'
        params._autoNumber = true
        params.autoNumber = true
        s = applyTemplate(
            '<g:autoNumber prefix="R" suffix="M" value="38473" />'
        )

        then: 'I get an autonumber widget with prefix, suffix, and checkbox checked'
        '<div class="auto-number"><div class="input-group"><span class="input-group-addon">R-</span><input type="number" name="number" id="number" class="form-control" value="38473" size="10" disabled="disabled" /><span class="input-group-addon">-M</span></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s

        when: 'I use the tag with a prefix, a suffix, a value, and the checkbox unset'
        params._autoNumber = true
        params.autoNumber = false
        s = applyTemplate(
            '<g:autoNumber prefix="R" suffix="M" value="38473" />'
        )

        then: 'I get an autonumber widget with prefix, suffix, and checkbox unchecked'
        '<div class="auto-number"><div class="input-group"><span class="input-group-addon">R-</span><input type="number" name="number" id="number" class="form-control" value="38473" size="10" /><span class="input-group-addon">-M</span></div><div class="checkbox"><label class="checkbox-inline"><input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" aria-controls="number" id="autoNumber"  />default.number.auto.label</label></div></div>' == s
    }

    def 'BackLink'() {
        when: 'I use the tag without return URL'
        String s = applyTemplate(
            '<g:backLink controller="foo" action="index">Link</g:backLink>'
        )

        then: 'I get a default link'
        '<a href="/foo/index">Link</a>' == s

        when: 'I use the tag with a return URL'
        params.returnUrl = '/organization/show/5'
        s = applyTemplate(
            '<g:backLink controller="foo" action="index">Link</g:backLink>'
        )

        then: 'I get a link referring the return URL instead'
        '<a href="/organization/show/5">Link</a>' == s
    }

    def 'Button as button element'() {
        setup:
        tagLib.metaClass.message = { Map args -> "${args.code}" }

        when: 'I use the tag without any attribute'
        String s = applyTemplate('<g:button>Link</g:button>')

        then: 'I get a button as <button> element'
        '<button type="button" class="btn">Link</button>' == s

        when: 'I use the tag with a color'
        s = applyTemplate('<g:button color="success">Link</g:button>')

        then: 'I get a button as <button> element with color'
        '<button type="button" class="btn btn-success">Link</button>' == s

        when: 'I use the tag with a size'
        s = applyTemplate('<g:button size="xs">Link</g:button>')

        then: 'I get a button as <button> element with a particular size'
        '<button type="button" class="btn btn-xs">Link</button>' == s

        when: 'I use the tag with an additional classes'
        s = applyTemplate('<g:button class="important right">Link</g:button>')

        then: 'I get a button as <button> element with the given classes'
        '<button type="button" class="btn important right">Link</button>' == s

        when: 'I use the tag with color, size, and additional classes'
        s = applyTemplate(
            '<g:button color="primary" size="lg" class="important right">Link</g:button>'
        )

        then: 'I get a button as <span> element with these attributes'
        '<button type="button" class="btn btn-primary btn-lg important right">Link</button>' == s

        when: 'I use the tag with an ID'
        s = applyTemplate(
            '<g:button elementId="print-btn">Link</g:button>'
        )

        then: 'I get a button as <span> element with these attributes'
        '<button type="button" class="btn" id="print-btn">Link</button>' == s

        when: 'I use the tag with any other attributes'
        s = applyTemplate(
            '<g:button style="text-decoration: underline;" onclick="alert(\'Help\');">Link</g:button>'
        )

        then: 'I get a button as <span> element with these attributes'
        '<button type="button" class="btn" style="text-decoration: underline;" onclick="alert(&#39;Help&#39;);">Link</button>' == s

        when: 'I use the tag with an icon'
        s = applyTemplate(
            '<g:button icon="cog">Link</g:button>'
        )

        then: 'I get a button as <span> element with this icon'
        '<button type="button" class="btn"><i class="fa fa-cog"></i> Link</button>' == s

        when: 'I use the tag with an icon and message key'
        s = applyTemplate(
            '<g:button icon="cog" message="default.print.btn">Link</g:button>'
        )

        then: 'I get a button as <span> element with this icon'
        '<button type="button" class="btn"><i class="fa fa-cog"></i> default.print.btn</button>' == s
    }

    def 'Button as anchor element'() {
        setup:
        tagLib.metaClass.message = { Map args -> "${args.code}" }

        when: 'I use the tag only with link attributes'
        String s = applyTemplate('<g:button controller="foo" action="index">Link</g:button>')

        then: 'I get a button as <a> element'
        '<a href="/foo/index" class="btn" role="button">Link</a>' == s

        when: 'I use the tag with link attributes and color'
        s = applyTemplate(
            '<g:button controller="foo" action="index" color="success">Link</g:button>'
        )

        then: 'I get a button as <a> element'
        '<a href="/foo/index" class="btn btn-success" role="button">Link</a>' == s

        when: 'I use the tag with link attributes and size'
        s = applyTemplate(
            '<g:button controller="foo" action="index" size="sm">Link</g:button>'
        )

        then: 'I get a button as <a> element'
        '<a href="/foo/index" class="btn btn-sm" role="button">Link</a>' == s

        when: 'I use the tag with link attributes and additional classes'
        s = applyTemplate(
            '<g:button controller="foo" action="index" class="important right">Link</g:button>'
        )

        then: 'I get a button as <a> element'
        '<a href="/foo/index" class="btn important right" role="button">Link</a>' == s

        when: 'I use the tag with link attributes, color, size, and additional classes'
        s = applyTemplate(
            '<g:button controller="foo" action="index" color="danger" size="xs" class="important right">Link</g:button>'
        )

        then: 'I get a button as <a> element'
        '<a href="/foo/index" class="btn btn-danger btn-xs important right" role="button">Link</a>' == s

        when: 'I use the tag with link attributes and additional attributes'
        s = applyTemplate(
            '<g:button controller="foo" action="index" elementId="print-btn" style="text-transform: uppercase;">Link</g:button>'
        )

        then: 'I get a button as <a> element'
        '<a href="/foo/index" id="print-btn" style="text-transform: uppercase;" class="btn" role="button">Link</a>' == s

        when: 'I use the tag with link attributes and an icon'
        s = applyTemplate(
            '<g:button controller="foo" action="index" icon="cog">Link</g:button>'
        )

        then: 'I get a button as <a> element with this icon'
        '<a href="/foo/index" class="btn" role="button"><i class="fa fa-cog"></i> Link</a>' == s

        when: 'I use the tag with link attributes, an icon, and message key'
        s = applyTemplate(
            '<g:button controller="foo" action="index" icon="cog" message="default.print.btn">Link</g:button>'
        )

        then: 'I get a button as <a> element with this icon'
        '<a href="/foo/index" class="btn" role="button"><i class="fa fa-cog"></i> default.print.btn</a>' == s
    }

    def 'Button as back link'() {
        when: 'I use the tag with the back attribute and there is a return URL'
        params.returnUrl = '/organization/show/5'
        String s = applyTemplate('<g:button back="true">Link</g:button>')

        then: 'I get a button as <a> element referring this URL'
        '<a href="/organization/show/5" class="btn" role="button">Link</a>' == s

        when: 'there is, however, no return URL'
        params.returnUrl = null
        s = applyTemplate('<g:button back="true">Link</g:button>')

        then: 'I get a button as <span> element'
        '<button type="button" class="btn">Link</button>' == s
    }

//    def 'CalendarViewBackLink'() {
//        setup:
//        tagLib.calendarEventService = Mock(CalendarEventService)
//        tagLib.calendarEventService.getCurrentCalendarView() >>> [
//            'list', 'list', 'month', 'month', 'day', 'day'
//        ]
//
//        when: 'I use the tag without attributes and there is a return URL'
//        params.returnUrl = '/organization/show/5'
//        String s = applyTemplate(
//            '<g:calendarViewBackLink>Link</g:calendarViewBackLink>'
//        )
//
//        then: 'I get a button referring that URL'
//        '<a href="/organization/show/5" class="button">Link</a>' == s
//
//        when: 'I use the tag without attributes and there is no return URL'
//        params.returnUrl = null
//        s = applyTemplate(
//            '<g:calendarViewBackLink>Link</g:calendarViewBackLink>'
//        )
//
//        then: 'I get a button referring the second calendar view'
//        '<a href="/calendar-event/list" class="button">Link</a>' == s
//
//        when: 'I use the tag with any attributes and there is a return URL'
//        params.returnUrl = '/organization/show/5'
//        s = applyTemplate(
//            '<g:calendarViewBackLink color="red" size="large">Link</g:calendarViewBackLink>'
//        )
//
//        then: 'I get a button referring that URL'
//        '<a href="/organization/show/5" class="button red large">Link</a>' == s
//
//        when: 'I use the tag with any attributes and there is no return URL'
//        params.returnUrl = null
//        s = applyTemplate(
//            '<g:calendarViewBackLink color="red" size="large">Link</g:calendarViewBackLink>'
//        )
//
//        then: 'I get a button referring the 4th calendar view'
//        '<a href="/calendar-event/month" class="button red large">Link</a>' == s
//
//        when: 'I use the tag with protected attributes and there is a return URL'
//        params.returnUrl = '/organization/show/5'
//        s = applyTemplate(
//            '<g:calendarViewBackLink controller="foo" action="bar">Link</g:calendarViewBackLink>'
//        )
//
//        then: 'I get a button referring that URL'
//        '<a href="/organization/show/5" class="button">Link</a>' == s
//
//        when: 'I use the tag with protected attributes and there is no return URL'
//        params.returnUrl = null
//        s = applyTemplate(
//            '<g:calendarViewBackLink controller="foo" action="bar">Link</g:calendarViewBackLink>'
//        )
//
//        then: 'I get a button referring the 6th calendar view'
//        '<a href="/calendar-event/day" class="button">Link</a>' == s
//    }
//
//    def 'CalendarViewLink'() {
//        setup:
//        tagLib.calendarEventService = Mock(CalendarEventService)
//        tagLib.calendarEventService.getCurrentCalendarView() >>> ['list', 'month']
//
//        when: 'I use the tag without attributes'
//        String s = applyTemplate(
//            '<g:calendarViewLink>Link</g:calendarViewLink>'
//        )
//
//        then: 'I get a button referring the first calendar view'
//        '<a href="/calendar-event/list" class="button">Link</a>' == s
//
//        when: 'I use the tag with any attributes'
//        s = applyTemplate(
//            '<g:calendarViewLink color="red" size="large">Link</g:calendarViewLink>'
//        )
//
//        then: 'I get a button referring the second calendar view'
//        '<a href="/calendar-event/month" class="button red large">Link</a>' == s
//
//        when: 'I use the tag with protected attributes'
//        s = applyTemplate(
//            '<g:calendarViewLink controller="foo" action="bar">Link</g:calendarViewLink>'
//        )
//
//        then: 'I get a button referring the second calendar view'
//        '<a href="/calendar-event/month" class="button">Link</a>' == s
//    }

    def 'Create back link'() {
        when: 'I use the tag and there is a return URL'
        params.returnUrl = '/organization/show/5'
        String s = applyTemplate(
            '<g:createBackLink controller="foo" action="bar" />'
        )

        then: 'I get that URL'
        '/organization/show/5' == s

        when: 'I use the tag and there is no return URL'
        params.returnUrl = null
        s = applyTemplate('<g:createBackLink controller="foo" action="bar" />')

        then: 'I get the requested URL'
        '/foo/bar' == s
    }

    def 'Currency Euro'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >>> [
            Locale.GERMANY, Locale.UK, Locale.US, new Locale('xy', 'z')
        ]

        and:
        new Config(name: 'currency', value: 'EUR').save failOnError: true

        expect: 'Euro in locale specific representations'
        '€' == applyTemplate('<g:currency />')
        '€' == applyTemplate('<g:currency />')
        'EUR' == applyTemplate('<g:currency />')
        'EUR' == applyTemplate('<g:currency />')
    }

    def 'Currency British Pound'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >>> [
            Locale.GERMANY, Locale.UK, Locale.US, new Locale('xy', 'z')
        ]

        and:
        new Config(name: 'currency', value: 'GBP').save failOnError: true

        expect: 'British Pound in locale specific representations'
        'GBP' == applyTemplate('<g:currency />')
        '£' == applyTemplate('<g:currency />')
        'GBP' == applyTemplate('<g:currency />')
        'GBP' == applyTemplate('<g:currency />')
    }

    def 'Currency invalid currency'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >>> [
            Locale.GERMANY, Locale.UK, Locale.US, new Locale('xy', 'z')
        ]

        and:
        new Config(name: 'currency', value: 'XYZ').save failOnError: true

        expect: 'the local currency in locale specific representation'
        '€' == applyTemplate('<g:currency />')
        '£' == applyTemplate('<g:currency />')
        '$' == applyTemplate('<g:currency />')

        and: 'Euro as default currency for unknown countries'
        'EUR' == applyTemplate('<g:currency />')
    }

    def 'Currency unset currency'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >>> [
            Locale.GERMANY, Locale.UK, Locale.US, new Locale('xy', 'z')
        ]

        expect: 'Euro as default currency if currency is unset'
        '€' == applyTemplate('<g:currency />')
        '€' == applyTemplate('<g:currency />')
        'EUR' == applyTemplate('<g:currency />')
        'EUR' == applyTemplate('<g:currency />')
    }

    def 'Data type icon'() {
        setup:
        tagLib.metaClass.message = { Map args -> "${args.code}" }

        when: 'I use the tag with the given controller'
        String s = applyTemplate("<g:dataTypeIcon controller='${c}' />")

        then: 'I get a <i> element representing the associated icon'
        "<i class=\"fa fa-fw fa-${icon} data-type-icon\" title=\"${c}.label\"></i> " == s

        where:
        c               | icon
        'organization'  | 'users'
        'person'        | 'male'
        'note'          | 'pencil'
        'product'       | 'cog'
        'quote'         | 'briefcase'
        'ticket'        | 'ticket'
        'xyz'           | ''
        ''              | ''
    }

    def 'DateInput without value'() {
        when: 'I use the tag only with the name attribute'
        String s = applyTemplate('<g:dateInput name="start" />')

        then: 'I get an empty date/time widget'
        '''<input type="hidden" name="start"
  value="" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value=""
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name and ID attribute'
        s = applyTemplate('<g:dateInput name="start" id="event-start" />')

        then: 'I get an empty date/time widget with this ID'
        '''<input type="hidden" name="start"
  value="" />
<div class="input-group date-time-control"><input type="text" id="event-start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="event-start-time" name="start_time"
  value=""
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name attribute and precision minute'
        s = applyTemplate('<g:dateInput name="start" precision="minute" />')

        then: 'I get an empty date/time widget'
        '''<input type="hidden" name="start"
  value="" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value=""
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name attribute and precision hour'
        s = applyTemplate('<g:dateInput name="start" precision="hour" />')

        then: 'I get an empty date/time widget'
        '''<input type="hidden" name="start"
  value="" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value=""
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name attribute and precision day'
        s = applyTemplate('<g:dateInput name="start" precision="day" />')

        then: 'I get an empty date widget'
        '''<input type="hidden" name="start"
  value="" />
<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s

        when: 'I use the tag with the name attribute and precision month'
        s = applyTemplate('<g:dateInput name="start" precision="month" />')

        then: 'I get an empty date widget'
        '''<input type="hidden" name="start"
  value="" />
<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s

        when: 'I use the tag with the name attribute and precision year'
        s = applyTemplate('<g:dateInput name="start" precision="year" />')

        then: 'I get an empty date widget'
        '''<input type="hidden" name="start"
  value="" />
<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s
    }

    def 'DateInput with Calendar value'() {
        given: 'a value map containing the calendar date'
        def map = [c: new GregorianCalendar(2014, Calendar.JANUARY, 8, 19, 5)]

        and: 'a mock implementation to format date and time'
        tagLib.metaClass.formatDate = { Map args ->
            String pattern = null
            switch (args.formatName) {
            case 'default.format.date':
                pattern = 'dd.MM.yyyy'
                break
            case 'default.format.time':
                pattern = 'HH:mm'
                break
            case 'default.format.datetime':
                pattern = 'dd.MM.yyyy HH:mm'
                break
            }

            if (pattern) {
                def f = FastDateFormat.getInstance(
                    pattern, TimeZone.default, Locale.GERMANY
                )
                return f.format(args.date)
            }

            ''
        }

        when: 'I use the tag with the name and value attribute'
        String s = applyTemplate(
            '<g:dateInput name="start" value="${c}" />', map
        )

        then: 'I get a date/time widget'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name, value, and ID attribute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" id="event-start" />', map
        )

        then: 'I get a date/time widget with this ID'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="event-start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="event-start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name and value attribute and precision minute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="minute" />', map
        )

        then: 'I get a date/time widget'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name attribute and precision hour'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="hour" />', map
        )

        then: 'I get a date/time widget'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name attribute and precision day'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="day" />', map
        )

        then: 'I get a date widget'
        '''<input type="hidden" name="start"
  value="08.01.2014" />
<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s

        when: 'I use the tag with the name attribute and precision month'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="month" />', map
        )

        then: 'I get a date widget'
        '''<input type="hidden" name="start"
  value="08.01.2014" />
<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s

        when: 'I use the tag with the name attribute and precision year'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="year" />', map
        )

        then: 'I get a date widget'
        '''<input type="hidden" name="start"
  value="08.01.2014" />
<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s
    }

    def 'DateInput with Date value'() {
        given: 'a value map containing the calendar date'
        def map = [
            c: new GregorianCalendar(2014, Calendar.JANUARY, 8, 19, 5).time
        ]

        and: 'a mock implementation to format date and time'
        tagLib.metaClass.formatDate = { Map args ->
            String pattern = null
            switch (args.formatName) {
            case 'default.format.date':
                pattern = 'dd.MM.yyyy'
                break
            case 'default.format.time':
                pattern = 'HH:mm'
                break
            case 'default.format.datetime':
                pattern = 'dd.MM.yyyy HH:mm'
                break
            }

            if (pattern) {
                def f = FastDateFormat.getInstance(
                    pattern, TimeZone.default, Locale.GERMANY
                )
                return f.format(args.date)
            }

            ''
        }

        when: 'I use the tag with the name and value attribute'
        String s = applyTemplate(
            '<g:dateInput name="start" value="${c}" />', map
        )

        then: 'I get a date/time widget'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name, value, and ID attribute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" id="event-start" />', map
        )

        then: 'I get a date/time widget with this ID'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="event-start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="event-start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name and value attribute and precision minute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="minute" />', map
        )

        then: 'I get a date/time widget'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name attribute and precision hour'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="hour" />', map
        )

        then: 'I get a date/time widget'
        '''<input type="hidden" name="start"
  value="08.01.2014 19:05" />
<div class="input-group date-time-control"><input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
<input type="text" id="start-time" name="start_time"
  value="19:05"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
''' == s

        when: 'I use the tag with the name attribute and precision day'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="day" />', map
        )

        then: 'I get a date widget'
        '''<input type="hidden" name="start"
  value="08.01.2014" />
<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s

        when: 'I use the tag with the name attribute and precision month'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="month" />', map
        )

        then: 'I get a date widget'
        '''<input type="hidden" name="start"
  value="08.01.2014" />
<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s

        when: 'I use the tag with the name attribute and precision year'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="year" />', map
        )

        then: 'I get a date widget'
        '''<input type="hidden" name="start"
  value="08.01.2014" />
<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
''' == s
    }

    def 'FormatCurrency with zero and without displayZero'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]

        and: 'a value map containing the number to format'
        def map = [n: 0]

        expect: 'always an empty string'
        '' == applyTemplate('<g:formatCurrency number="${n}" />', map)
        '' == applyTemplate('<g:formatCurrency number="${n}" />', map)
        '' == applyTemplate('<g:formatCurrency number="${n}" />', map)
        '' == applyTemplate('<g:formatCurrency number="${n}" />', map)
        '' == applyTemplate('<g:formatCurrency number="${n}" />', map)
    }

    def 'FormatCurrency with zero and displayZero'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]
        tagLib.userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1]

        and: 'a value map containing the number to format'
        def map = [n: 0]

        expect:
        '0,00 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" />', map
        )
        '0,000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" />', map
        )
        '0,0000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" />', map
        )
        '0,0 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" />', map
        )
        '0 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" />', map
        )
    }

    def 'FormatCurrency with zero and minFractionDigits'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >> 2
        tagLib.userService.getNumFractionDigitsExt() >> 3

        expect:
        def map = [n: 0, d: d]
        s == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" minFractionDigits="${d}" />', map
        )

        where:
        d       | s
        0       | '0 €'
        1       | '0,0 €'
        2       | '0,00 €'
        3       | '0,000 €'
        4       | '0,0000 €'
        null    | '0,00 €'
    }

    def 'FormatCurrency with zero and numberOnly'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]
        tagLib.userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1]

        and: 'a value map containing the number to format'
        def map = [n: 0]

        expect:
        '0,00' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" numberOnly="true" />', map
        )
        '0,000' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" numberOnly="true" />', map
        )
        '0,0000' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" numberOnly="true" />', map
        )
        '0,0' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" numberOnly="true" />', map
        )
        '0' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" numberOnly="true" />', map
        )
    }

    def 'FormatCurrency with zero and external'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]
        tagLib.userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1]

        and: 'a value map containing the number to format'
        def map = [n: 0]

        expect:
        '0,000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" external="true" />', map
        )
        '0,0000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" external="true" />', map
        )
        '0,00000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" external="true" />', map
        )
        '0,00 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" external="true" />', map
        )
        '0,0 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" external="true" />', map
        )
    }

    def 'FormatCurrency with non-zero'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]
        tagLib.userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1]

        and: 'a value map containing the number to format'
        def map = [n: 14739.358079]

        expect:
        '14.739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}" />', map
        )
        '14.739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}" />', map
        )
        '14.739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}" />', map
        )
        '14.739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}" />', map
        )
        '14.739 €' == applyTemplate(
            '<g:formatCurrency number="${n}" />', map
        )
    }

    def 'FormatCurrency with non-zero and minFractionDigits'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >> 2
        tagLib.userService.getNumFractionDigitsExt() >> 3

        expect:
        def map = [n: 14739.358079, d: d]
        s == applyTemplate(
            '<g:formatCurrency number="${n}" minFractionDigits="${d}" />', map
        )

        where:
        d       | s
        0       | '14.739 €'
        1       | '14.739,4 €'
        2       | '14.739,36 €'
        3       | '14.739,358 €'
        4       | '14.739,3581 €'
        null    | '14.739,36 €'
    }

    def 'FormatCurrency with non-zero and numberOnly'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]
        tagLib.userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1]

        and: 'a value map containing the number to format'
        def map = [n: 14739.358079]

        expect:
        '14.739,36' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true" />', map
        )
        '14.739,358' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true" />', map
        )
        '14.739,3581' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true" />', map
        )
        '14.739,4' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true" />', map
        )
        '14.739' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true" />', map
        )
    }

    def 'FormatCurrency with non-zero and groupingUsed'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [
            3, 4, 5, 2, 1, 3, 4, 5, 2, 1
        ]
        tagLib.userService.getNumFractionDigitsExt() >>> [
            2, 3, 4, 1, 0, 2, 3, 4, 1, 0
        ]

        and: 'a value map containing the number to format'
        def map = [n: 14739.358079]

        expect:
        '14.739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true" />', map
        )
        '14.739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true" />', map
        )
        '14.739,35808 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true" />', map
        )
        '14.739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true" />', map
        )
        '14.739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true" />', map
        )

        and:
        '14739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false" />', map
        )
        '14739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false" />', map
        )
        '14739,35808 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false" />', map
        )
        '14739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false" />', map
        )
        '14739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false" />', map
        )
    }

    def 'FormatCurrency with non-zero and external'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]
        tagLib.userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1]

        and: 'a value map containing the number to format'
        def map = [n: 14739.358079]

        expect:
        '14.739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true" />', map
        )
        '14.739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true" />', map
        )
        '14.739,35808 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true" />', map
        )
        '14.739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true" />', map
        )
        '14.739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true" />', map
        )
    }

    def 'FormatSize without groupingUsed'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY

        expect:
        s == applyTemplate('<g:formatSize number="${n}" />', [n: n])

        where:
        n                   | s
                        0   |      ''
                        1   |     '1 B'
                      145   |   '145 B'
                    1_023   | '1.023 B'
                    1_024   |     '1 K'
                    1_025   |     '1 K'
                    1_030   |     '1,01 K'
                   14_384   |    '14,05 K'
                  174_284   |   '170,2 K'
                1_048_575   | '1.024 K'
                1_048_576   |     '1 M'
                1_153_434   |     '1,1 M'
                6_081_741   |     '5,8 M'
               17_511_219   |    '16,7 M'
               40_265_318   |    '38,4 M'
              478_505_180   |   '456,34 M'
            1_073_741_824   |     '1 G'
            2_631_741_210   |     '2,45 G'
           34_003_470_830   |    '31,67 G'
          907_850_000_682   |   '845,5 G'
        1_099_511_627_775   | '1.024 G'
        1_099_511_627_776   |     '1 T'
        2_670_054_036_891   |     '2,43 T'
    }

    def 'FormatSize with groupingUsed true'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY

        expect:
        s == applyTemplate(
            '<g:formatSize number="${n}" groupingUsed="true" />', [n: n]
        )

        where:
        n                   | s
                        0   |      ''
                        1   |     '1 B'
                      145   |   '145 B'
                    1_023   | '1.023 B'
                    1_024   |     '1 K'
                    1_025   |     '1 K'
                    1_030   |     '1,01 K'
                   14_384   |    '14,05 K'
                  174_284   |   '170,2 K'
                1_048_575   | '1.024 K'
                1_048_576   |     '1 M'
                1_153_434   |     '1,1 M'
                6_081_741   |     '5,8 M'
               17_511_219   |    '16,7 M'
               40_265_318   |    '38,4 M'
              478_505_180   |   '456,34 M'
            1_073_741_824   |     '1 G'
            2_631_741_210   |     '2,45 G'
           34_003_470_830   |    '31,67 G'
          907_850_000_682   |   '845,5 G'
        1_099_511_627_775   | '1.024 G'
        1_099_511_627_776   |     '1 T'
        2_670_054_036_891   |     '2,43 T'
    }

    def 'FormatSize with groupingUsed false'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.GERMANY

        expect:
        s == applyTemplate(
            '<g:formatSize number="${n}" groupingUsed="false" />', [n: n]
        )

        where:
        n                   | s
                        0   |      ''
                        1   |     '1 B'
                      145   |   '145 B'
                    1_023   |  '1023 B'
                    1_024   |     '1 K'
                    1_025   |     '1 K'
                    1_030   |     '1,01 K'
                   14_384   |    '14,05 K'
                  174_284   |   '170,2 K'
                1_048_575   |  '1024 K'
                1_048_576   |     '1 M'
                1_153_434   |     '1,1 M'
                6_081_741   |     '5,8 M'
               17_511_219   |    '16,7 M'
               40_265_318   |    '38,4 M'
              478_505_180   |   '456,34 M'
            1_073_741_824   |     '1 G'
            2_631_741_210   |     '2,45 G'
           34_003_470_830   |    '31,67 G'
          907_850_000_682   |   '845,5 G'
        1_099_511_627_775   |  '1024 G'
        1_099_511_627_776   |     '1 T'
        2_670_054_036_891   |     '2,43 T'
    }

    def 'FormatSize with other locale'() {
        setup:
        tagLib.userService = Mock(UserService)
        tagLib.userService.getCurrentLocale() >> Locale.UK

        expect:
        s == applyTemplate('<g:formatSize number="${n}" />', [n: n])

        where:
        n                   | s
                        0   |      ''
                        1   |     '1 B'
                      145   |   '145 B'
                    1_023   | '1,023 B'
                    1_024   |     '1 K'
                    1_025   |     '1 K'
                    1_030   |     '1.01 K'
                   14_384   |    '14.05 K'
                  174_284   |   '170.2 K'
                1_048_575   | '1,024 K'
                1_048_576   |     '1 M'
                1_153_434   |     '1.1 M'
                6_081_741   |     '5.8 M'
               17_511_219   |    '16.7 M'
               40_265_318   |    '38.4 M'
              478_505_180   |   '456.34 M'
            1_073_741_824   |     '1 G'
            2_631_741_210   |     '2.45 G'
           34_003_470_830   |    '31.67 G'
          907_850_000_682   |   '845.5 G'
        1_099_511_627_775   | '1,024 G'
        1_099_511_627_776   |     '1 T'
        2_670_054_036_891   |     '2.43 T'
    }

    def 'InstallStep'() {
        expect:
        s == applyTemplate(
            '<g:installStep step="${step}" current="${c}"><b>Text</b></g:installStep>',
            [step: step, c: 2]
        )

        where:
        step    | s
        0       | '<li><b>Text</b></li>'
        1       | '<li><b>Text</b></li>'
        2       | '<li class="current"><b>Text</b></li>'
        3       | '<li><b>Text</b></li>'
        4       | '<li><b>Text</b></li>'
    }

    // TODO test letterBar
    // TODO test menuButton

    def 'Nl2Br with value'() {
        expect:
        s == applyTemplate('<g:nl2br value="${v}" />', [v: v])

        where:
        v                       | s
        ''                      | ''
        '\r\n'                  | '<br />'
        '\r'                    | '<br />'
        '\n'                    | '<br />'
        '\r\r'                  | '<br /><br />'
        '\n\n'                  | '<br /><br />'
        '\n\r'                  | '<br /><br />'
        '\r\n\r\n'              | '<br /><br />'
        ' \r\n '                | ' <br /> '
        ' \r '                  | ' <br /> '
        ' \n '                  | ' <br /> '
        ' \r\r '                | ' <br /><br /> '
        ' \n\n '                | ' <br /><br /> '
        ' \n\r '                | ' <br /><br /> '
        ' \r\n\r\n '            | ' <br /><br /> '
        'foo\r\nbar'            | 'foo<br />bar'
        'foo\rwhee\nbar'        | 'foo<br />whee<br />bar'
        'foo\rbar'              | 'foo<br />bar'
        'foo\nbar'              | 'foo<br />bar'
        'foo\r\rbar'            | 'foo<br /><br />bar'
        'foo\rwhee\rbar'        | 'foo<br />whee<br />bar'
        'foo\n\nbar'            | 'foo<br /><br />bar'
        'foo\nwhee\nbar'        | 'foo<br />whee<br />bar'
        'foo\n\rbar'            | 'foo<br /><br />bar'
        'foo\nwhee\rbar'        | 'foo<br />whee<br />bar'
        'foo\r\n\r\nbar'        | 'foo<br /><br />bar'
        'foo\r\nwhee\r\nbar'    | 'foo<br />whee<br />bar'
        'foo\rx\nwhee\ry\nbar'  | 'foo<br />x<br />whee<br />y<br />bar'
    }

    def 'Nl2Br with body'() {
        expect:
        s == applyTemplate("<g:nl2br>${v}</g:nl2br>")

        where:
        v                       | s
        ''                      | ''
        '\r\n'                  | '<br />'
        '\r'                    | '<br />'
        '\n'                    | '<br />'
        '\r\r'                  | '<br /><br />'
        '\n\n'                  | '<br /><br />'
        '\n\r'                  | '<br /><br />'
        '\r\n\r\n'              | '<br /><br />'
        ' \r\n '                | ' <br /> '
        ' \r '                  | ' <br /> '
        ' \n '                  | ' <br /> '
        ' \r\r '                | ' <br /><br /> '
        ' \n\n '                | ' <br /><br /> '
        ' \n\r '                | ' <br /><br /> '
        ' \r\n\r\n '            | ' <br /><br /> '
        'foo\r\nbar'            | 'foo<br />bar'
        'foo\rwhee\nbar'        | 'foo<br />whee<br />bar'
        'foo\rbar'              | 'foo<br />bar'
        'foo\nbar'              | 'foo<br />bar'
        'foo\r\rbar'            | 'foo<br /><br />bar'
        'foo\rwhee\rbar'        | 'foo<br />whee<br />bar'
        'foo\n\nbar'            | 'foo<br /><br />bar'
        'foo\nwhee\nbar'        | 'foo<br />whee<br />bar'
        'foo\n\rbar'            | 'foo<br /><br />bar'
        'foo\nwhee\rbar'        | 'foo<br />whee<br />bar'
        'foo\r\n\r\nbar'        | 'foo<br /><br />bar'
        'foo\r\nwhee\r\nbar'    | 'foo<br />whee<br />bar'
        'foo\rx\nwhee\ry\nbar'  | 'foo<br />x<br />whee<br />y<br />bar'
    }

    // TODO test paginate, searchResults, title and url
}
