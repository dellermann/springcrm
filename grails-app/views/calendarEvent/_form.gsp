<fieldset>
  <header><h3><g:message code="calendarEvent.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${calendarEventInstance}" property="subject" />
        <f:field bean="${calendarEventInstance}" property="start"
          precision="minute" />
        <f:field bean="${calendarEventInstance}" property="end"
          precision="minute" />
        <f:field bean="${calendarEventInstance}" property="allDay" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <g:ifModuleAllowed modules="contact">
        <f:field bean="${calendarEventInstance}" property="organization" />
        </g:ifModuleAllowed>
        <f:field bean="${calendarEventInstance}" property="location" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="calendarEvent.fieldset.recurrence.label" /></h3></header>
  <div class="form-fragment">
    <div class="row">
      <div class="label">
        <label><g:message code="calendarEvent.recurrence.pattern.label" /></label>
      </div>
      <div class="field" style="padding-bottom: 2em;">
        <input type="hidden" id="recurrence-interval"
          name="recurrence.interval"
          value="${calendarEventInstance.recurrence.interval}" />
        <input type="hidden" id="recurrence-monthDay"
          name="recurrence.monthDay"
          value="${calendarEventInstance.recurrence.monthDay}" />
        <input type="hidden" id="recurrence-weekdays"
          name="recurrence.weekdays"
          value="${calendarEventInstance.recurrence.weekdays}" />
        <input type="hidden" id="recurrence-weekdayOrd"
          name="recurrence.weekdayOrd"
          value="${calendarEventInstance.recurrence.weekdayOrd}" />
        <input type="hidden" id="recurrence-month" name="recurrence.month"
          value="${calendarEventInstance.recurrence.month}" />
        <div id="tabs-recurrence-type">
          <g:set var="selectedWeekdays"
            value="${calendarEventInstance.recurrence.weekdaysAsList}" />
          <g:set var="weekdayNames"
            value="${java.text.DateFormatSymbols.instance.weekdays}" />
          <g:set var="monthNames"
            value="${java.text.DateFormatSymbols.instance.months}" />
          <ul>
            <li><g:radio id="recurrence-type-0" name="recurrence.type" value="0" checked="${calendarEventInstance.recurrence.type == 0}" /><a href="#tabs-recurrence-type-0"><g:message code="calendarEvent.recurrence.type.0" /></a></li>
            <li><g:radio id="recurrence-type-10" name="recurrence.type" value="10" checked="${calendarEventInstance.recurrence.type == 10}" /><a href="#tabs-recurrence-type-10"><g:message code="calendarEvent.recurrence.type.10" /></a></li>
            <li><g:radio id="recurrence-type-30" name="recurrence.type" value="30" checked="${calendarEventInstance.recurrence.type == 30}" /><a href="#tabs-recurrence-type-30"><g:message code="calendarEvent.recurrence.type.30" /></a></li>
            <li><g:radio id="recurrence-type-40" name="recurrence.type" value="40" checked="${calendarEventInstance.recurrence.type == 40}" /><a href="#tabs-recurrence-type-40"><g:message code="calendarEvent.recurrence.type.40" /></a></li>
            <li><g:radio id="recurrence-type-50" name="recurrence.type" value="50" checked="${calendarEventInstance.recurrence.type == 50}" /><a href="#tabs-recurrence-type-50"><g:message code="calendarEvent.recurrence.type.50" /></a></li>
            <li><g:radio id="recurrence-type-60" name="recurrence.type" value="60" checked="${calendarEventInstance.recurrence.type == 60}" /><a href="#tabs-recurrence-type-60"><g:message code="calendarEvent.recurrence.type.60" /></a></li>
            <li><g:radio id="recurrence-type-70" name="recurrence.type" value="70" checked="${calendarEventInstance.recurrence.type == 70}" /><a href="#tabs-recurrence-type-70"><g:message code="calendarEvent.recurrence.type.70" /></a></li>
          </ul>
          <div id="tabs-recurrence-type-0">
            <p><g:message code="calendarEvent.recurrence.type.0.noOptions.label" /></p>
          </div>
          <div id="tabs-recurrence-type-10">
            <p class="field-text">
              <span class="label"><label for="recurrence-interval-10"><g:message code="calendarEvent.recurrence.type.10.each.label" /></label></span>
              <span class="input"><g:textField name="recurrence-interval-10" size="3" /></span>
              <span class="label"><label><g:message code="calendarEvent.recurrence.type.10.days.label" /></label></span>
            </p>
          </div>
          <div id="tabs-recurrence-type-30">
            <div class="field-text">
              <span class="label"><label><g:message code="calendarEvent.recurrence.type.30.at.label" /></label></span>
              <g:each in="${[*2..7, 1]}" var="i">
              <span class="input checkbox"><g:checkBox name="recurrence-weekdays-30-${i}" checked="${selectedWeekdays?.contains(i)}" value="${i}" /></span>
              <span class="label"><label for="recurrence-weekdays-30-${i}">${weekdayNames[i]}</label></span>
              </g:each>
            </div>
            <ul class="field-msgs" style="margin-bottom: 1em;">
              <li class="info-msg"><g:message code="default.required" default="required" /></li>
              <g:eachError bean="${calendarEventInstance}" field="recurrence.weekdays">
              <li class="error-msg"><g:message error="${it}" /></li>
              </g:eachError>
            </ul>
            <div class="field-text">
              <span class="label"><label for="recurrence-interval-30"><g:message code="calendarEvent.recurrence.type.30.each.label" /></label></span>
              <span class="input"><g:textField name="recurrence-interval-30" size="3" /></span>
              <span class="label"><label><g:message code="calendarEvent.recurrence.type.30.weeks.label" /></label></span>
            </div>
          </div>
          <div id="tabs-recurrence-type-40">
            <div class="field-text">
              <span class="label"><label for="recurrence-monthDay-40"><g:message code="calendarEvent.recurrence.type.40.at.label" /></label></span>
              <span class="input"><g:textField name="recurrence-monthDay-40" size="3" />.</span>
              <span class="label"><label for="recurrence-interval-40"><g:message code="calendarEvent.recurrence.type.40.each.label" /></label></span>
              <span class="input"><g:textField name="recurrence-interval-40" size="3" /></span>
              <span class="label"><label><g:message code="calendarEvent.recurrence.type.40.months.label" /></label></span>
            </div>
            <ul class="field-msgs">
              <li class="info-msg"><g:message code="default.required" default="required" /></li>
              <g:eachError bean="${calendarEventInstance}" field="recurrence.monthDay">
              <li class="error-msg"><g:message error="${it}" /></li>
              </g:eachError>
            </ul>
          </div>
          <div id="tabs-recurrence-type-50">
            <div class="field-text">
              <span class="label"><label for="recurrence-weekdayOrd-50"><g:message code="calendarEvent.recurrence.type.50.at.label" /></label></span>
              <span class="input"><g:textField name="recurrence-weekdayOrd-50" size="3" />.</span>
              <span class="input"><g:select name="recurrence-weekdays-50" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}" /></span>
              <span class="label"><label for="recurrence-interval-50"><g:message code="calendarEvent.recurrence.type.50.each.label" /></label></span>
              <span class="input"><g:textField name="recurrence-interval-50" size="3" /></span>
              <span class="label"><label><g:message code="calendarEvent.recurrence.type.50.months.label" /></label></span>
            </div>
            <ul class="field-msgs">
              <li class="info-msg"><g:message code="default.required" default="required" /></li>
              <g:eachError bean="${calendarEventInstance}" field="recurrence.weekdayOrd">
              <li class="error-msg"><g:message error="${it}" /></li>
              </g:eachError>
            </ul>
          </div>
          <div id="tabs-recurrence-type-60">
            <div class="field-text">
              <span class="label"><label for="recurrence-monthDay-60"><g:message code="calendarEvent.recurrence.type.60.yearlyAt.label" /></label></span>
              <span class="input"><g:textField name="recurrence-monthDay-60" size="3" />.</span>
              <span class="input"><g:select name="recurrence-month-60" from="${0..11}" optionValue="${{monthNames[it]}}" /></span>
            </div>
            <ul class="field-msgs">
              <li class="info-msg"><g:message code="default.required" default="required" /></li>
              <g:eachError bean="${calendarEventInstance}" field="recurrence.monthDay">
              <li class="error-msg"><g:message error="${it}" /></li>
              </g:eachError>
            </ul>
          </div>
          <div id="tabs-recurrence-type-70">
            <div class="field-text">
              <span class="label"><label for="recurrence-monthDay-70"><g:message code="calendarEvent.recurrence.type.70.yearlyAt.label" /></label></span>
              <span class="input"><g:textField name="recurrence-weekdayOrd-70" size="3" />.</span>
              <span class="input"><g:select name="recurrence-weekdays-70" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}" /></span>
              <span class="label"><label for="recurrence-month-70"><g:message code="calendarEvent.recurrence.type.70.inMonth.label" /></label></span>
              <span class="input"><g:select name="recurrence-month-70" from="${0..11}" optionValue="${{monthNames[it]}}" /></span>
            </div>
            <ul class="field-msgs">
              <li class="info-msg"><g:message code="default.required" default="required" /></li>
              <g:eachError bean="${calendarEventInstance}" field="recurrence.weekdayOrd">
              <li class="error-msg"><g:message error="${it}" /></li>
              </g:eachError>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="row" id="recurrence-end">
      <div class="label">
        <label for="recurrence-endType-until"><g:message code="calendarEvent.recurrence.end.label" default="Ends" /></label>
      </div>
      <div class="field${hasErrors(bean: calendarEventInstance, field: 'recurrence.until', ' error')}">
        <dl class="checkbox-area">
          <dt>
            <g:radio id="recurrence-endType-until" name="recurrence.endType"
              value="until" checked="${calendarEventInstance.recurrence.until}"
              />
          </dt>
          <dd>
            <div class="field-text">
              <span class="label"><label for="recurrence-endType-until"><g:message code="calendarEvent.recurrence.until.label" default="at" /></label></span>
              <span class="input"><g:dateInput name="recurrence.until" precision="day" value="${calendarEventInstance?.recurrence.until}" /></span>
            </div>
            <ul class="field-msgs">
              <li class="info-msg"><g:message code="default.format.date.label" /></li>
            </ul>
          </dd>
          <dt>
            <g:radio id="recurrence-endType-count" name="recurrence.endType"
              value="count" />
          </dt>
          <dd class="checkbox-content">
            <div class="field-text">
              <span class="label"><label for="recurrence-endType-count"><g:message code="calendarEvent.recurrence.cnt.label" default="after" /></label></span>
              <span class="input"><g:textField name="recurrence.cnt" value="${params['recurrence.cnt']}" size="5" /></span>
              <span class="label"><label for="recurrence.cnt"><g:message code="calendarEvent.recurrence.cnt.events.label" default="events" /></label></span>
            </div>
          </dd>
          <dt>
            <g:radio id="recurrence-endType-none" name="recurrence.endType"
              value="none"
              checked="${calendarEventInstance.recurrence.until == null}" />
          </dt>
          <dd>
            <label for="recurrence-endType-none"><g:message code="calendarEvent.recurrence.none.label" default="none" /></label>
          </dd>
        </dl>
        <ul class="field-msgs">
          <g:eachError bean="${calendarEventInstance}" field="recurrence.until">
          <li class="error-msg"><g:message error="${it}" /></li>
          </g:eachError>
        </ul>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="calendarEvent.fieldset.reminder.label" /></h3></header>
  <div class="form-fragment">
    <input type="hidden" id="reminders" name="reminders"
      value="${reminderRules}" />
    <div class="row">
      <div class="label">
        <label for="description"><g:message code="calendarEvent.reminder.label" default="Reminder" /></label>
      </div>
      <div class="field">
        <div id="reminder-selectors"></div>
        <g:button elementId="reminder-add-btn" color="green" size="small"
          icon="plus" message="calendarEvent.reminder.add.label" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="calendarEvent.fieldset.description.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${calendarEventInstance}" property="description" cols="80"
      rows="5" />
  </div>
</fieldset>
<script id="add-reminder-template" type="text/html">
  <ul>
    {{#reminders}}
    <li>
      <select>
      {{#options}}
        <option value="{{optionValue}}" {{{selected}}}>{{value}} {{unit}}</option>
      {{/options}}
      </select>
      <g:button color="red" size="small" icon="trash-o"
        message="calendarEvent.reminder.delete.label" />
    </li>
    {{/reminders}}
  </ul>
</script>
