<r:require modules="calendarForm" />
<fieldset>
  <h4><g:message code="calendarEvent.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${calendarEventInstance}" property="subject" />
        <f:field bean="${calendarEventInstance}" property="start" precision="minute" />
        <f:field bean="${calendarEventInstance}" property="end" precision="minute" />
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
  <h4><g:message code="calendarEvent.fieldset.recurrence.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label><g:message code="calendarEvent.recurrence.pattern.label" /></label>
      </div>
      <div class="field">
        <input type="hidden" id="recurrence-interval" name="recurrence.interval" value="${calendarEventInstance.recurrence.interval}" />
        <input type="hidden" id="recurrence-monthDay" name="recurrence.monthDay" value="${calendarEventInstance.recurrence.monthDay}" />
        <input type="hidden" id="recurrence-weekdays" name="recurrence.weekdays" value="${calendarEventInstance.recurrence.weekdays}" />
        <input type="hidden" id="recurrence-weekdayOrd" name="recurrence.weekdayOrd" value="${calendarEventInstance.recurrence.weekdayOrd}" />
        <input type="hidden" id="recurrence-month" name="recurrence.month" value="${calendarEventInstance.recurrence.month}" />
        <div id="tabs-recurrence-type">
          <g:set var="selectedWeekdays" value="${calendarEventInstance.recurrence.weekdaysAsList}" />
          <g:set var="weekdayNames" value="${java.text.DateFormatSymbols.instance.weekdays}" />
          <g:set var="monthNames" value="${java.text.DateFormatSymbols.instance.months}" />
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
            <p>
              <label for="recurrence-interval-10"><g:message code="calendarEvent.recurrence.type.10.each.label" /></label>&nbsp;
              <g:textField name="recurrence-interval-10" size="3" />
              &nbsp;<label><g:message code="calendarEvent.recurrence.type.10.days.label" /></label>
            </p>
          </div>
          <div id="tabs-recurrence-type-30">
            <p>
              <label><g:message code="calendarEvent.recurrence.type.30.at.label" /></label>&nbsp;
              <g:each in="${[*2..7, 1]}" var="i">
              <span><g:checkBox name="recurrence-weekdays-30-${i}" checked="${selectedWeekdays?.contains(i)}" value="${i}" />&nbsp;<label for="recurrence-weekdays-30-${i}">${weekdayNames[i]}</label></span>
              </g:each><br />
              <span class="info-msg"><g:message code="default.required" default="required" /></span>
              <g:hasErrors bean="${calendarEventInstance}" field="recurrence.weekdays">
              <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="recurrence.weekdays"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </p>
            <p>
              <label for="recurrence-interval-30"><g:message code="calendarEvent.recurrence.type.30.each.label" /></label>&nbsp;
              <g:textField name="recurrence-interval-30" size="3" />
              &nbsp;<label><g:message code="calendarEvent.recurrence.type.30.weeks.label" /></label>
            </p>
          </div>
          <div id="tabs-recurrence-type-40">
            <p>
              <label for="recurrence-monthDay-40"><g:message code="calendarEvent.recurrence.type.40.at.label" /></label>&nbsp;
              <g:textField name="recurrence-monthDay-40" size="3" />.
              <label for="recurrence-interval-40"><g:message code="calendarEvent.recurrence.type.40.each.label" /></label>&nbsp;
              <g:textField name="recurrence-interval-40" size="3" />
              &nbsp;<label><g:message code="calendarEvent.recurrence.type.40.months.label" /></label><br />
              <span class="info-msg"><g:message code="default.required" default="required" /></span>
              <g:hasErrors bean="${calendarEventInstance}" field="recurrence.monthDay">
              <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="recurrence.monthDay"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </p>
          </div>
          <div id="tabs-recurrence-type-50">
            <p>
              <label for="recurrence-weekdayOrd-50"><g:message code="calendarEvent.recurrence.type.50.at.label" /></label>&nbsp;
              <g:textField name="recurrence-weekdayOrd-50" size="3" />.
              <g:select name="recurrence-weekdays-50" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}" />&nbsp;
              <label for="recurrence-interval-50"><g:message code="calendarEvent.recurrence.type.50.each.label" /></label>&nbsp;
              <g:textField name="recurrence-interval-50" size="3" />
              &nbsp;<label><g:message code="calendarEvent.recurrence.type.50.months.label" /></label><br />
              <span class="info-msg"><g:message code="default.required" default="required" /></span>
              <g:hasErrors bean="${calendarEventInstance}" field="recurrence.weekdayOrd">
              <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="recurrence.weekdayOrd"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </p>
          </div>
          <div id="tabs-recurrence-type-60">
            <p>
              <label for="recurrence-monthDay-60"><g:message code="calendarEvent.recurrence.type.60.yearlyAt.label" /></label>&nbsp;
              <g:textField name="recurrence-monthDay-60" size="3" />.
              <g:select name="recurrence-month-60" from="${0..11}" optionValue="${{monthNames[it]}}" /><br />
              <span class="info-msg"><g:message code="default.required" default="required" /></span>
              <g:hasErrors bean="${calendarEventInstance}" field="recurrence.monthDay">
              <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="recurrence.monthDay"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </p>
          </div>
          <div id="tabs-recurrence-type-70">
            <p>
              <label for="recurrence-monthDay-70"><g:message code="calendarEvent.recurrence.type.70.yearlyAt.label" /></label>&nbsp;
              <g:textField name="recurrence-weekdayOrd-70" size="3" />.
              <g:select name="recurrence-weekdays-70" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}" />&nbsp;
              <label for="recurrence-month-70"><g:message code="calendarEvent.recurrence.type.70.inMonth.label" /></label>&nbsp;
              <g:select name="recurrence-month-70" from="${0..11}" optionValue="${{monthNames[it]}}" /><br />
              <span class="info-msg"><g:message code="default.required" default="required" /></span>
              <g:hasErrors bean="${calendarEventInstance}" field="recurrence.weekdayOrd">
              <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="recurrence.weekdayOrd"><g:message error="${it}" /> </g:eachError></span>
              </g:hasErrors>
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="row" id="recurrence-end">
      <div class="label">
        <label for="recurrence.endType-until"><g:message code="calendarEvent.recurrence.end.label" default="Ends" /></label>
      </div>
      <div class="field${hasErrors(bean: calendarEventInstance, field: 'recurrence.until', ' error')}">
        <ul class="checkbox-area">
          <li>
            <g:radio id="recurrence.endType-until" name="recurrence.endType" value="until" checked="${calendarEventInstance.recurrence.until}" />&nbsp;<label for="recurrence.endType-until"><g:message code="calendarEvent.recurrence.until.label" default="at" /></label>&nbsp;
            <g:dateInput name="recurrence.until" precision="day" value="${calendarEventInstance?.recurrence.until}" /><br />
            <span class="info-msg"><g:message code="default.format.datetime.label" /></span>
          </li>
          <li>
            <g:radio id="recurrence.endType-count" name="recurrence.endType" value="count" />&nbsp;<label for="recurrence.endType-count"><g:message code="calendarEvent.recurrence.cnt.label" default="after" /></label>&nbsp;
            <g:textField name="recurrence.cnt" value="${params['recurrence.cnt']}" size="5" />
            <label for="recurrence.cnt"><g:message code="calendarEvent.recurrence.cnt.events.label" default="events" /></label>
          </li>
          <li>
            <g:radio id="recurrence.endType-none" name="recurrence.endType" value="none" checked="${calendarEventInstance.recurrence.until == null}" />&nbsp;<label for="recurrence.endType-none"><g:message code="calendarEvent.recurrence.none.label" default="none" /></label>
          </li>
        </ul>
        <g:hasErrors bean="${calendarEventInstance}" field="recurrence.until">
          <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="recurrence.until"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="calendarEvent.fieldset.reminder.label" /></h4>
  <div class="fieldset-content">
    <input type="hidden" id="reminders" name="reminders" value="${reminderRules}" />
    <div class="row">
      <div class="label">
        <label for="description"><g:message code="calendarEvent.reminder.label" default="Reminder" /></label>
      </div>
      <div class="field">
        <div id="reminder-selectors"></div>
        <a id="reminder-add-btn" href="#" class="button small green"><g:message code="calendarEvent.reminder.add.label" /></a>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="calendarEvent.fieldset.description.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="description"><g:message code="calendarEvent.description.label" default="Description" /></label>
      </div>
      <div class="field${hasErrors(bean: calendarEventInstance, field: 'description', ' error')}">
        <g:textArea name="description" cols="80" rows="5" value="${calendarEventInstance?.description}" /><br />
        <g:hasErrors bean="${calendarEventInstance}" field="description">
          <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="description"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>