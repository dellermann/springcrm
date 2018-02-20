/*
 * ViewTagLibSpec.groovy
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

import com.github.fakemongo.Fongo
import com.mongodb.client.MongoCollection
import grails.testing.web.taglib.TagLibUnitTest
import org.apache.commons.lang.time.FastDateFormat
import org.bson.Document as MDocument
import org.grails.plugins.web.taglib.ApplicationTagLib
import org.grails.plugins.web.taglib.FormatTagLib
import org.grails.taglib.TagOutput
import spock.lang.Ignore
import spock.lang.Specification


class ViewTagLibSpec extends Specification
    implements TagLibUnitTest<ViewTagLib>
{

    //-- Feature methods ------------------------

    void 'Tag autoNumber without value renders correct output'() {
        when: 'the tag is used without parameters'
        String s = applyTemplate('<g:autoNumber/>')

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
    <input type="number" name="number" id="number" class="form-control"
      value="" size="10" disabled="disabled"/>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a prefix'
        s = applyTemplate('<g:autoNumber prefix="R"/>')

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
      <span class="input-group-addon">R-</span>
    
    <input type="number" name="number" id="number" class="form-control"
      value="" size="10" disabled="disabled"/>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a sensitive prefix'
        s = applyTemplate('<g:autoNumber prefix="${prefix}"/>', [prefix: 'R&'])

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
      <span class="input-group-addon">R&amp;-</span>
    
    <input type="number" name="number" id="number" class="form-control"
      value="" size="10" disabled="disabled"/>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a suffix'
        s = applyTemplate('<g:autoNumber suffix="M"/>')

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
    <input type="number" name="number" id="number" class="form-control"
      value="" size="10" disabled="disabled"/>
    
      <span class="input-group-addon">-M</span>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a sensitive suffix'
        s = applyTemplate('<g:autoNumber suffix="${suffix}"/>', [suffix: '&M'])

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
    <input type="number" name="number" id="number" class="form-control"
      value="" size="10" disabled="disabled"/>
    
      <span class="input-group-addon">-&amp;M</span>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a prefix and a suffix'
        s = applyTemplate('<g:autoNumber prefix="R" suffix="M"/>')

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
      <span class="input-group-addon">R-</span>
    
    <input type="number" name="number" id="number" class="form-control"
      value="" size="10" disabled="disabled"/>
    
      <span class="input-group-addon">-M</span>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a sensitive prefix and suffix'
        s = applyTemplate(
            '<g:autoNumber prefix="${prefix}" suffix="${suffix}"/>',
            [prefix: 'R&', suffix: '&M']
        )

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
      <span class="input-group-addon">R&amp;-</span>
    
    <input type="number" name="number" id="number" class="form-control"
      value="" size="10" disabled="disabled"/>
    
      <span class="input-group-addon">-&amp;M</span>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s
    }

    void 'Tag autoNumber with value renders correct output'() {
        when: 'the tag is used with a prefix, a suffix and a value'
        String s = applyTemplate(
            '<g:autoNumber prefix="R" suffix="M" value="38473"/>'
        )

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
      <span class="input-group-addon">R-</span>
    
    <input type="number" name="number" id="number" class="form-control"
      value="38473" size="10" disabled="disabled"/>
    
      <span class="input-group-addon">-M</span>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a prefix, a suffix, a value and the checkbox set'
        params._autoNumber = true
        params.autoNumber = true
        s = applyTemplate('<g:autoNumber prefix="R" suffix="M" value="38473"/>')

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
      <span class="input-group-addon">R-</span>
    
    <input type="number" name="number" id="number" class="form-control"
      value="38473" size="10" disabled="disabled"/>
    
      <span class="input-group-addon">-M</span>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" checked="checked" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s

        when: 'the tag is used with a prefix, a suffix, a value and the checkbox unset'
        params._autoNumber = true
        params.autoNumber = false
        s = applyTemplate('<g:autoNumber prefix="R" suffix="M" value="38473"/>')

        then: 'the correct HTML is rendered'
        '''\
<div class="auto-number">
  <div class="input-group">
    
      <span class="input-group-addon">R-</span>
    
    <input type="number" name="number" id="number" class="form-control"
      value="38473" size="10"/>
    
      <span class="input-group-addon">-M</span>
    
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <input type="hidden" name="_autoNumber" /><input type="checkbox" name="autoNumber" aria-controls="number" id="autoNumber"  />
      default.number.auto.label
    </label>
  </div>
</div>
''' == s
    }

    void 'Tag backLink renders correct output'() {
        given: 'a mocked tag library'
        mockTagLib ApplicationTagLib

        when: 'the tag is used without a return URL'
        String s = applyTemplate(
            '<g:backLink controller="foo" action="index">Link</g:backLink>'
        )

        then: 'the correct link is rendered'
        '<a href="/foo/index">Link</a>' == s

        when: 'the tag is used with a return URL'
        params.returnUrl = '/organization/show/5'
        s = applyTemplate(
            '<g:backLink controller="foo" action="index">Link</g:backLink>'
        )

        then: 'the correct link is rendered'
        '<a href="/organization/show/5">Link</a>' == s
    }

    void 'Tag button renders a correct button'() {
        given: 'a mocked tag library'
        mockTagLib ApplicationTagLib

        and: 'a mocked message'
        tagLib.metaClass.message = { Map args -> "${args.code}" }

        when: 'the tag is used without any attribute'
        String s = applyTemplate('<g:button>Link</g:button>')

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn">Link</button>' == s

        when: 'the tag is used with a color'
        s = applyTemplate('<g:button color="success">Link</g:button>')

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn btn-success">Link</button>' == s

        when: 'the tag is used with a size'
        s = applyTemplate('<g:button size="xs">Link</g:button>')

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn btn-xs">Link</button>' == s

        when: 'the tag is used with an additional classes'
        s = applyTemplate('<g:button class="important right">Link</g:button>')

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn important right">Link</button>' == s

        when: 'the tag is used with color, size, and additional classes'
        s = applyTemplate(
            '<g:button color="primary" size="lg" class="important right">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn btn-primary btn-lg important right">Link</button>' == s

        when: 'the tag is used with an ID'
        s = applyTemplate('<g:button elementId="print-btn">Link</g:button>')

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn" id="print-btn">Link</button>' == s

        when: 'the tag is used with any other attributes'
        s = applyTemplate(
            '<g:button style="text-decoration: underline;" onclick="alert(\'Help\');">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn" style="text-decoration: underline;" onclick="alert(&#39;Help&#39;);">Link</button>' == s

        when: 'the tag is used with an icon'
        s = applyTemplate(
            '<g:button icon="cog">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn"><i class="fa fa-cog"></i> Link</button>' == s

        when: 'the tag is used with an icon and message key'
        s = applyTemplate(
            '<g:button icon="cog" message="default.print.btn">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<button type="button" class="btn"><i class="fa fa-cog"></i> default.print.btn</button>' == s
    }

    void 'Tag button renders a correct anchor element'() {
        given: 'a mocked tag library'
        mockTagLib ApplicationTagLib

        and: 'a mocked message'
        tagLib.metaClass.message = { Map args -> "${args.code}" }

        when: 'the tag is used only with link attributes'
        String s = applyTemplate('<g:button controller="foo" action="index">Link</g:button>')

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" class="btn" role="button">Link</a>' == s

        when: 'the tag is used with link attributes and color'
        s = applyTemplate(
            '<g:button controller="foo" action="index" color="success">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" class="btn btn-success" role="button">Link</a>' == s

        when: 'the tag is used with link attributes and size'
        s = applyTemplate(
            '<g:button controller="foo" action="index" size="sm">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" class="btn btn-sm" role="button">Link</a>' == s

        when: 'the tag is used with link attributes and additional classes'
        s = applyTemplate(
            '<g:button controller="foo" action="index" class="important right">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" class="btn important right" role="button">Link</a>' == s

        when: 'the tag is used with link attributes, color, size, and additional classes'
        s = applyTemplate(
            '<g:button controller="foo" action="index" color="danger" size="xs" class="important right">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" class="btn btn-danger btn-xs important right" role="button">Link</a>' == s

        when: 'the tag is used with link attributes and additional attributes'
        s = applyTemplate(
            '<g:button controller="foo" action="index" elementId="print-btn" style="text-transform: uppercase;">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" id="print-btn" style="text-transform: uppercase;" class="btn" role="button">Link</a>' == s

        when: 'the tag is used with link attributes and an icon'
        s = applyTemplate(
            '<g:button controller="foo" action="index" icon="cog">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" class="btn" role="button"><i class="fa fa-cog"></i> Link</a>' == s

        when: 'the tag is used with link attributes, an icon, and message key'
        s = applyTemplate(
            '<g:button controller="foo" action="index" icon="cog" message="default.print.btn">Link</g:button>'
        )

        then: 'the correct HTML is rendered'
        '<a href="/foo/index" class="btn" role="button"><i class="fa fa-cog"></i> default.print.btn</a>' == s
    }

    void 'Tag button renders a correct back link'() {
        when: 'the tag is used with the back attribute and there is a return URL'
        params.returnUrl = '/organization/show/5'
        String s = applyTemplate('<g:button back="true">Link</g:button>')

        then: 'the correct HTML is rendered'
        '<a href="/organization/show/5" class="btn" role="button">Link</a>' == s

        when: 'there is, however, no return URL'
        params.returnUrl = null
        s = applyTemplate('<g:button back="true">Link</g:button>')

        then: 'the correct HTML is rendered'
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

    void 'Tag currency with German locale renders output'(String currency,
                                                          String e)
    {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService = userService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        1 * configService.getString('currency') >> currency
        tagLib.configService = configService

        expect:
        e == applyTemplate('<g:currency/>')

        where:
        currency    || e
        null        || '€'
        ''          || '€'
        'XX'        || '€'
        'EUR'       || '€'
        'GBP'       || 'GBP'
        'XYZ'       || '€'
        'XXXX'      || '€'
    }

    void 'Tag currency with English locale renders output'(String currency,
                                                           String e)
    {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.UK
        tagLib.userService = userService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        1 * configService.getString('currency') >> currency
        tagLib.configService = configService

        expect:
        e == applyTemplate('<g:currency/>')

        where:
        currency    || e
        null        || '€'
        ''          || '€'
        'XX'        || '€'
        'EUR'       || '€'
        'GBP'       || '£'
        'XYZ'       || '£'
        'XXXX'      || '€'
    }

    void 'Tag dataTypeIcon renders correct output'(String c, String icon) {
        given: 'a mocked message'
        tagLib.metaClass.message = { Map args -> "${args.code}" }

        expect:
        """\
<i class="fa fa-fw fa-${icon} data-type-icon"
  title="${c}.label"></i>
""" == applyTemplate("<g:dataTypeIcon controller='${c}'/>")

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

    void 'Tag dateInput without value renders correct output'() {
        when: 'the tag is used only with the name attribute'
        String s = applyTemplate('<g:dateInput name="start"/>')

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value=""/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value=""
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name and ID attribute'
        s = applyTemplate('<g:dateInput name="start" id="event-start"/>')

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value=""/>

  <div class="input-group date-time-control">


<input type="text" id="event-start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="event-start-time" name="start_time"
    value=""
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name attribute and precision minute'
        s = applyTemplate('<g:dateInput name="start" precision="minute"/>')

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value=""/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value=""
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name attribute and precision hour'
        s = applyTemplate('<g:dateInput name="start" precision="hour"/>')

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value=""/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value=""
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name attribute and precision day'
        s = applyTemplate('<g:dateInput name="start" precision="day"/>')

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value=""/>


<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()

        when: 'the tag is used with the name attribute and precision month'
        s = applyTemplate('<g:dateInput name="start" precision="month"/>')

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value=""/>


<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()

        when: 'the tag is used with the name attribute and precision year'
        s = applyTemplate('<g:dateInput name="start" precision="year"/>')

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value=""/>


<input type="text" id="start-date" name="start_date"
  value=""
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()
    }

    void 'Tag dateInput with Calendar value renders correct output'() {
        given: 'a value map containing the calendar date'
        def map = [c: new GregorianCalendar(2014, Calendar.JANUARY, 8, 19, 5)]

        and: 'a mock implementation to format date and time'
        FormatTagLib formatTagLib = tagLib.tagLibraryLookup.lookupTagLibrary(
            TagOutput.DEFAULT_NAMESPACE, 'formatDate'
        ) as FormatTagLib
        formatTagLib.formatDate = { Map args ->
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

        when: 'the tag is used with the name and value attribute'
        String s = applyTemplate(
            '<g:dateInput name="start" value="${c}"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name, value and ID attribute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" id="event-start"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="event-start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="event-start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name and value attribute and precision minute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="minute"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name attribute and precision hour'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="hour"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name attribute and precision day'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="day"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014"/>


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()

        when: 'the tag is used with the name attribute and precision month'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="month"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014"/>


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()

        when: 'the tag is used with the name attribute and precision year'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="year"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014"/>


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()
    }

    void 'Tag dateInput with Date value renders correct output'() {
        given: 'a value map containing the date'
        def map = [
            c: new GregorianCalendar(2014, Calendar.JANUARY, 8, 19, 5).time
        ]

        and: 'a mock implementation to format date and time'
        FormatTagLib formatTagLib = tagLib.tagLibraryLookup.lookupTagLibrary(
            TagOutput.DEFAULT_NAMESPACE, 'formatDate'
        ) as FormatTagLib
        formatTagLib.formatDate = { Map args ->
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

        when: 'the tag is used with the name and value attribute'
        String s = applyTemplate(
            '<g:dateInput name="start" value="${c}"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name, value and ID attribute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" id="event-start"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="event-start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="event-start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name and value attribute and precision minute'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="minute"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name attribute and precision hour'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="hour"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014 19:05"/>

  <div class="input-group date-time-control">


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>

  <input type="text" id="start-time" name="start_time"
    value="19:05"
    class="form-control date-input-control date-input-time-control"
    maxlength="5"/>
  </div>''' == s.trim()

        when: 'the tag is used with the name attribute and precision day'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="day"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014"/>


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()

        when: 'the tag is used with the name attribute and precision month'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="month"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014"/>


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()

        when: 'the tag is used with the name attribute and precision year'
        s = applyTemplate(
            '<g:dateInput name="start" value="${c}" precision="year"/>', map
        )

        then: 'the correct HTML is rendered'
        '''\
<input type="hidden" name="start"
  value="08.01.2014"/>


<input type="text" id="start-date" name="start_date"
  value="08.01.2014"
  class="form-control date-input-control date-input-date-control"
  maxlength="10"/>''' == s.trim()
    }

    void 'Tag formatCurrency with zero and without displayZero'() {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0]
        tagLib.userService = userService

        and: 'a value map containing the number to format'
        def map = [n: 0]

        expect:
        '' == applyTemplate('<g:formatCurrency number="${n}"/>', map)
        '' == applyTemplate('<g:formatCurrency number="${n}"/>', map)
        '' == applyTemplate('<g:formatCurrency number="${n}"/>', map)
        '' == applyTemplate('<g:formatCurrency number="${n}"/>', map)
        '' == applyTemplate('<g:formatCurrency number="${n}"/>', map)
    }

    void 'Tag formatCurrency with zero and displayZero'() {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0] * 3
        userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1] * 3
        tagLib.userService = userService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        configService.getString('currency') >> 'EUR'
        tagLib.configService = configService

        and: 'a value map containing the number to format'
        def map = [n: 0]

        expect:
        '0,00 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true"/>', map
        )
        '0,000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true"/>', map
        )
        '0,0000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true"/>', map
        )
        '0,0 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true"/>', map
        )
        '0 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true"/>', map
        )

        and:
        '0,00' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'numberOnly="true"/>', map
        )
        '0,000' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'numberOnly="true"/>', map
        )
        '0,0000' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'numberOnly="true"/>', map
        )
        '0,0' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'numberOnly="true"/>', map
        )
        '0' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'numberOnly="true"/>', map
        )

        and:
        '0,000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'external="true" />', map
        )
        '0,0000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'external="true" />', map
        )
        '0,00000 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'external="true" />', map
        )
        '0,00 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'external="true" />', map
        )
        '0,0 €' == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'external="true" />', map
        )
    }

    void 'Tag formatCurrency with zero and minFractionDigits'(Integer d,
                                                              String s)
    {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        userService.getNumFractionDigits() >> 2
        userService.getNumFractionDigitsExt() >> 3
        tagLib.userService = userService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        configService.getString('currency') >> 'EUR'
        tagLib.configService = configService

        and: 'a value map containing the number to format'
        def map = [n: 0, d: d]

        expect:
        s == applyTemplate(
            '<g:formatCurrency number="${n}" displayZero="true" ' +
                'minFractionDigits="${d}"/>', map
        )

        where:
        d       || s
        0       || '0 €'
        1       || '0,0 €'
        2       || '0,00 €'
        3       || '0,000 €'
        4       || '0,0000 €'
        null    || '0,00 €'
    }

    void 'Tag formatCurrency with non-zero'() {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        userService.getNumFractionDigits() >>> [2, 3, 4, 1, 0] * 5
        userService.getNumFractionDigitsExt() >>> [3, 4, 5, 2, 1] * 5
        tagLib.userService = userService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        configService.getString('currency') >> 'EUR'
        tagLib.configService = configService

        and: 'a value map containing the number to format'
        def map = [n: 14739.358079]

        expect:
        '14.739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}"/>', map
        )
        '14.739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}"/>', map
        )
        '14.739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}"/>', map
        )
        '14.739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}"/>', map
        )
        '14.739 €' == applyTemplate(
            '<g:formatCurrency number="${n}"/>', map
        )

        and:
        '14.739,36' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true"/>', map
        )
        '14.739,358' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true"/>', map
        )
        '14.739,3581' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true"/>', map
        )
        '14.739,4' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true"/>', map
        )
        '14.739' == applyTemplate(
            '<g:formatCurrency number="${n}" numberOnly="true"/>', map
        )

        and:
        '14.739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true"/>', map
        )
        '14.739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true"/>', map
        )
        '14.739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true"/>', map
        )
        '14.739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true"/>', map
        )
        '14.739 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="true"/>', map
        )

        and:
        '14739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false"/>', map
        )
        '14739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false"/>', map
        )
        '14739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false"/>', map
        )
        '14739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false"/>', map
        )
        '14739 €' == applyTemplate(
            '<g:formatCurrency number="${n}" groupingUsed="false"/>', map
        )

        and:
        '14.739,358 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true"/>', map
        )
        '14.739,3581 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true"/>', map
        )
        '14.739,35808 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true"/>', map
        )
        '14.739,36 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true"/>', map
        )
        '14.739,4 €' == applyTemplate(
            '<g:formatCurrency number="${n}" external="true"/>', map
        )
    }

    void 'Tag formatCurrency with non-zero and minFractionDigits'(Integer d,
                                                                  String s)
    {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        userService.getNumFractionDigits() >> 2
        userService.getNumFractionDigitsExt() >> 3
        tagLib.userService = userService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        configService.getString('currency') >> 'EUR'
        tagLib.configService = configService

        and: 'a value map containing the number to format'
        def map = [n: 14739.358079, d: d]

        expect:
        s == applyTemplate(
            '<g:formatCurrency number="${n}" minFractionDigits="${d}"/>', map
        )

        where:
        d       || s
        0       || '14.739 €'
        1       || '14.739,4 €'
        2       || '14.739,36 €'
        3       || '14.739,358 €'
        4       || '14.739,3581 €'
        null    || '14.739,36 €'
    }

    void 'Tag formatSize without groupingUsed'(BigInteger n, String s) {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService = userService

        expect:
        s == applyTemplate('<g:formatSize number="${n}"/>', [n: n])
        s == applyTemplate(
            '<g:formatSize number="${n}" groupingUsed="true"/>', [n: n]
        )

        where:
        n                   || s
                        0   ||      ''
                        1   ||     '1 B'
                      145   ||   '145 B'
                    1_023   || '1.023 B'
                    1_024   ||     '1 K'
                    1_025   ||     '1 K'
                    1_030   ||     '1,01 K'
                   14_384   ||    '14,05 K'
                  174_284   ||   '170,2 K'
                1_048_575   || '1.024 K'
                1_048_576   ||     '1 M'
                1_153_434   ||     '1,1 M'
                6_081_741   ||     '5,8 M'
               17_511_219   ||    '16,7 M'
               40_265_318   ||    '38,4 M'
              478_505_180   ||   '456,34 M'
            1_073_741_824   ||     '1 G'
            2_631_741_210   ||     '2,45 G'
           34_003_470_830   ||    '31,67 G'
          907_850_000_682   ||   '845,5 G'
        1_099_511_627_775   || '1.024 G'
        1_099_511_627_776   ||     '1 T'
        2_670_054_036_891   ||     '2,43 T'
    }

    void 'Tag formatSize with groupingUsed false'(BigInteger n, String s) {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService = userService

        expect:
        s == applyTemplate(
            '<g:formatSize number="${n}" groupingUsed="false"/>', [n: n]
        )

        where:
        n                   || s
                        0   ||      ''
                        1   ||     '1 B'
                      145   ||   '145 B'
                    1_023   ||  '1023 B'
                    1_024   ||     '1 K'
                    1_025   ||     '1 K'
                    1_030   ||     '1,01 K'
                   14_384   ||    '14,05 K'
                  174_284   ||   '170,2 K'
                1_048_575   ||  '1024 K'
                1_048_576   ||     '1 M'
                1_153_434   ||     '1,1 M'
                6_081_741   ||     '5,8 M'
               17_511_219   ||    '16,7 M'
               40_265_318   ||    '38,4 M'
              478_505_180   ||   '456,34 M'
            1_073_741_824   ||     '1 G'
            2_631_741_210   ||     '2,45 G'
           34_003_470_830   ||    '31,67 G'
          907_850_000_682   ||   '845,5 G'
        1_099_511_627_775   ||  '1024 G'
        1_099_511_627_776   ||     '1 T'
        2_670_054_036_891   ||     '2,43 T'
    }

    void 'Tag formatSize with other locale'(BigInteger n, String s) {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.UK
        tagLib.userService = userService

        expect:
        s == applyTemplate('<g:formatSize number="${n}"/>', [n: n])

        where:
        n                   || s
                        0   ||      ''
                        1   ||     '1 B'
                      145   ||   '145 B'
                    1_023   || '1,023 B'
                    1_024   ||     '1 K'
                    1_025   ||     '1 K'
                    1_030   ||     '1.01 K'
                   14_384   ||    '14.05 K'
                  174_284   ||   '170.2 K'
                1_048_575   || '1,024 K'
                1_048_576   ||     '1 M'
                1_153_434   ||     '1.1 M'
                6_081_741   ||     '5.8 M'
               17_511_219   ||    '16.7 M'
               40_265_318   ||    '38.4 M'
              478_505_180   ||   '456.34 M'
            1_073_741_824   ||     '1 G'
            2_631_741_210   ||     '2.45 G'
           34_003_470_830   ||    '31.67 G'
          907_850_000_682   ||   '845.5 G'
        1_099_511_627_775   || '1,024 G'
        1_099_511_627_776   ||     '1 T'
        2_670_054_036_891   ||     '2.43 T'
    }

    void 'Tag fullNumber renders correct output'() {
        given: 'a sequence number service instance'
        SeqNumberService seqNumberService = Mock()
        tagLib.seqNumberService = seqNumberService

        and: 'an instance'
        def invoice = new Invoice()

        when: 'the tag is used'
        String s = tagLib.fullNumber([bean: invoice])

        then: 'the output is correctly rendered'
        1 * seqNumberService.getFullNumber(invoice) >> 'I-16012-45037'
        'I-16012-45037' == s
    }

    void 'Tag installStep renders correct output'(int step, String s) {
        expect:
        s == applyTemplate(
            '<g:installStep step="${step}" current="${c}">' +
                '<b>Text</b></g:installStep>',
            [step: step, c: 2]
        )

        where:
        step    || s
        0       || '<li><b>Text</b></li>'
        1       || '<li><b>Text</b></li>'
        2       || '<li class="active"><b>Text</b></li>'
        3       || '<li><b>Text</b></li>'
        4       || '<li><b>Text</b></li>'
    }

    @Ignore('Fongo aggregating does not work in the moment')
    void 'Tag letterBar renders correct output'() {
        given: 'a fake mongo instance'
        Fongo fongo = new Fongo('mongo test server')
        tagLib.mongo = fongo.mongo

        and: 'some note instances'
        String dbName = grailsApplication.config.getProperty(
            'grails.mongodb.databaseName'
        )
        MongoCollection<MDocument> collection =
            fongo.mongo.getDatabase(dbName).getCollection('note')
        ['A', 'B', 'E', 'S', 'Z'].each {
            MDocument doc = new MDocument()
            doc.append 'title', it + ' test'
            doc.append 'content', 'Sample note'
            collection.insertOne doc
        }

        when: 'the tag is used'
        String s = applyTemplate(
            '<g:letterBar clazz="${c}" property="title"/>', [c: Note]
        )
        println s

        then: 'the output is correctly rendered'
        '''\
''' != s.trim()
    }

    void 'Tag month renders correct output'(int month, int year, String s) {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService = userService

        expect:
        s == applyTemplate(
            '<g:month month="${m}" year="${y}"/>', [m: month, y: year]
        )

        where:
        month   | year  || s
        1       | 1980  || 'Januar 1980'
        5       | 1975  || 'Mai 1975'
        9       | 2000  || 'September 2000'
        12      | 2017  || 'Dezember 2017'
    }

    void 'Tag monthBar renders correct output'() {
        given: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentLocale() >> Locale.GERMANY
        tagLib.userService = userService

        when: 'the tag is used'
        String s = applyTemplate('<g:monthBar action="show"/>')

        then: 'the output is correctly rendered'
        '<a href="/test/show" class="btn btn-default btn-month" title="Januar" data-month="1">Jan</a><a href="/test/show" class="btn btn-default btn-month" title="Februar" data-month="2">Feb</a><a href="/test/show" class="btn btn-default btn-month" title="M&auml;rz" data-month="3">Mär</a><a href="/test/show" class="btn btn-default btn-month" title="April" data-month="4">Apr</a><a href="/test/show" class="btn btn-default btn-month" title="Mai" data-month="5">Mai</a><a href="/test/show" class="btn btn-default btn-month" title="Juni" data-month="6">Jun</a><a href="/test/show" class="btn btn-default btn-month" title="Juli" data-month="7">Jul</a><a href="/test/show" class="btn btn-default btn-month" title="August" data-month="8">Aug</a><a href="/test/show" class="btn btn-default btn-month" title="September" data-month="9">Sep</a><a href="/test/show" class="btn btn-default btn-month" title="Oktober" data-month="10">Okt</a><a href="/test/show" class="btn btn-default btn-month" title="November" data-month="11">Nov</a><a href="/test/show" class="btn btn-default btn-month" title="Dezember" data-month="12">Dez</a>' == s

        when: 'the tag is used'
        s = applyTemplate('<g:monthBar action="show" activeMonth="2"/>')

        then: 'the output is correctly rendered'
        '<a href="/test/show" class="btn btn-default btn-month" title="Januar" data-month="1">Jan</a><a href="/test/show" class="btn btn-default btn-month active" title="Februar" data-month="2">Feb</a><a href="/test/show" class="btn btn-default btn-month" title="M&auml;rz" data-month="3">Mär</a><a href="/test/show" class="btn btn-default btn-month" title="April" data-month="4">Apr</a><a href="/test/show" class="btn btn-default btn-month" title="Mai" data-month="5">Mai</a><a href="/test/show" class="btn btn-default btn-month" title="Juni" data-month="6">Jun</a><a href="/test/show" class="btn btn-default btn-month" title="Juli" data-month="7">Jul</a><a href="/test/show" class="btn btn-default btn-month" title="August" data-month="8">Aug</a><a href="/test/show" class="btn btn-default btn-month" title="September" data-month="9">Sep</a><a href="/test/show" class="btn btn-default btn-month" title="Oktober" data-month="10">Okt</a><a href="/test/show" class="btn btn-default btn-month" title="November" data-month="11">Nov</a><a href="/test/show" class="btn btn-default btn-month" title="Dezember" data-month="12">Dez</a>' == s
    }

    void 'Tag nl2Br with value renders correct output'(String v, String s) {
        expect:
        s == applyTemplate('<g:nl2br value="${v}"/>', [v: v])

        where:
        v                       || s
        ''                      || ''
        '\r\n'                  || '<br />'
        '\r'                    || '<br />'
        '\n'                    || '<br />'
        '\r\r'                  || '<br /><br />'
        '\n\n'                  || '<br /><br />'
        '\n\r'                  || '<br /><br />'
        '\r\n\r\n'              || '<br /><br />'
        ' \r\n '                || ' <br /> '
        ' \r '                  || ' <br /> '
        ' \n '                  || ' <br /> '
        ' \r\r '                || ' <br /><br /> '
        ' \n\n '                || ' <br /><br /> '
        ' \n\r '                || ' <br /><br /> '
        ' \r\n\r\n '            || ' <br /><br /> '
        'foo\r\nbar'            || 'foo<br />bar'
        'foo\rwhee\nbar'        || 'foo<br />whee<br />bar'
        'foo\rbar'              || 'foo<br />bar'
        'foo\nbar'              || 'foo<br />bar'
        'foo\r\rbar'            || 'foo<br /><br />bar'
        'foo\rwhee\rbar'        || 'foo<br />whee<br />bar'
        'foo\n\nbar'            || 'foo<br /><br />bar'
        'foo\nwhee\nbar'        || 'foo<br />whee<br />bar'
        'foo\n\rbar'            || 'foo<br /><br />bar'
        'foo\nwhee\rbar'        || 'foo<br />whee<br />bar'
        'foo\r\n\r\nbar'        || 'foo<br /><br />bar'
        'foo\r\nwhee\r\nbar'    || 'foo<br />whee<br />bar'
        'foo\rx\nwhee\ry\nbar'  || 'foo<br />x<br />whee<br />y<br />bar'
    }

    void 'Tag nl2Br with body renders correct output'(String v, String s) {
        expect:
        s == applyTemplate("<g:nl2br>${v}</g:nl2br>")

        where:
        v                       || s
        ''                      || ''
        '\r\n'                  || '<br />'
        '\r'                    || '<br />'
        '\n'                    || '<br />'
        '\r\r'                  || '<br /><br />'
        '\n\n'                  || '<br /><br />'
        '\n\r'                  || '<br /><br />'
        '\r\n\r\n'              || '<br /><br />'
        ' \r\n '                || ' <br /> '
        ' \r '                  || ' <br /> '
        ' \n '                  || ' <br /> '
        ' \r\r '                || ' <br /><br /> '
        ' \n\n '                || ' <br /><br /> '
        ' \n\r '                || ' <br /><br /> '
        ' \r\n\r\n '            || ' <br /><br /> '
        'foo\r\nbar'            || 'foo<br />bar'
        'foo\rwhee\nbar'        || 'foo<br />whee<br />bar'
        'foo\rbar'              || 'foo<br />bar'
        'foo\nbar'              || 'foo<br />bar'
        'foo\r\rbar'            || 'foo<br /><br />bar'
        'foo\rwhee\rbar'        || 'foo<br />whee<br />bar'
        'foo\n\nbar'            || 'foo<br /><br />bar'
        'foo\nwhee\nbar'        || 'foo<br />whee<br />bar'
        'foo\n\rbar'            || 'foo<br /><br />bar'
        'foo\nwhee\rbar'        || 'foo<br />whee<br />bar'
        'foo\r\n\r\nbar'        || 'foo<br /><br />bar'
        'foo\r\nwhee\r\nbar'    || 'foo<br />whee<br />bar'
        'foo\rx\nwhee\ry\nbar'  || 'foo<br />x<br />whee<br />y<br />bar'
    }

    // TODO test paginate
    // TODO test searchResults
    // TODO test title

    void 'Tag url renders correct output'() {
        expect:
        'http://localhost:8080/test/index' == applyTemplate('<g:url/>')
    }
}
