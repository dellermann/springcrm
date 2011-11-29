<fieldset>
  <h4><g:message code="calendarEvent.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="calendarEvent.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: calendarEventInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${calendarEventInstance?.subject}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${calendarEventInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="start"><g:message code="calendarEvent.start.label" default="Start" /></label>
        </div>
        <div class="field${hasErrors(bean: calendarEventInstance, field: 'start', ' error')}">
          <g:dateInput name="start" precision="minute" value="${calendarEventInstance?.start}" /><br />
          <span class="info-msg"><g:message code="default.format.datetime.label" /></span>
          <span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${calendarEventInstance}" field="start">
            <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="start"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="end"><g:message code="calendarEvent.end.label" default="End" /></label>
        </div>
        <div class="field${hasErrors(bean: calendarEventInstance, field: 'end', ' error')}">
          <g:dateInput name="end" precision="minute" value="${calendarEventInstance?.end}" /><br />
          <span class="info-msg"><g:message code="default.format.datetime.label" /></span>
          <span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${calendarEventInstance}" field="end">
            <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="end"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="allDay"><g:message code="calendarEvent.allDay.label" default="All Day" /></label>
        </div>
        <div class="field${hasErrors(bean: calendarEventInstance, field: 'allDay', ' error')}">
          <g:checkBox name="allDay" value="${calendarEventInstance?.allDay}" /><br />
          <g:hasErrors bean="${calendarEventInstance}" field="allDay">
            <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="allDay"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="calendarEvent.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: calendarEventInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${calendarEventInstance?.organization?.name}" size="35" />
          <input type="hidden" name="organization.id" id="organization-id" value="${calendarEventInstance?.organization?.id}" />
          <g:hasErrors bean="${calendarEventInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="location"><g:message code="calendarEvent.location.label" default="Location" /></label>
        </div>
        <div class="field${hasErrors(bean: calendarEventInstance, field: 'location', ' error')}">
          <g:textField name="location" value="${calendarEventInstance?.location}" size="40" /><br />
          <g:hasErrors bean="${calendarEventInstance}" field="location">
            <span class="error-msg"><g:eachError bean="${calendarEventInstance}" field="location"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="calendarEvent.fieldset.recurrence.label" /></h4>
  <div class="fieldset-content">
    <div id="tabs-recurType">
      <g:set var="selectedWeekdays" value="${calendarEventInstance.recurWeekdaysAsList}"/>
      <g:set var="weekdayNames" value="${java.text.DateFormatSymbols.instance.weekdays}"/>
      <g:set var="monthNames" value="${java.text.DateFormatSymbols.instance.months}"/>
      <ul>
        <li><g:radio id="recurType-0" name="recurType" value="0" checked="${calendarEventInstance.recurType == 0}" /><a href="#tabs-recurType-0"><g:message code="calendarEvent.recurType.0"/></a></li>
        <li><g:radio id="recurType-10" name="recurType" value="10" checked="${calendarEventInstance.recurType == 10}" /><a href="#tabs-recurType-10"><g:message code="calendarEvent.recurType.10"/></a></li>
        <li><g:radio id="recurType-30" name="recurType" value="30" checked="${calendarEventInstance.recurType == 30}" /><a href="#tabs-recurType-30"><g:message code="calendarEvent.recurType.30"/></a></li>
        <li><g:radio id="recurType-40" name="recurType" value="40" checked="${calendarEventInstance.recurType == 40}" /><a href="#tabs-recurType-40"><g:message code="calendarEvent.recurType.40"/></a></li>
        <li><g:radio id="recurType-50" name="recurType" value="50" checked="${calendarEventInstance.recurType == 50}" /><a href="#tabs-recurType-50"><g:message code="calendarEvent.recurType.50"/></a></li>
        <li><g:radio id="recurType-60" name="recurType" value="60" checked="${calendarEventInstance.recurType == 60}" /><a href="#tabs-recurType-60"><g:message code="calendarEvent.recurType.60"/></a></li>
        <li><g:radio id="recurType-70" name="recurType" value="70" checked="${calendarEventInstance.recurType == 70}" /><a href="#tabs-recurType-70"><g:message code="calendarEvent.recurType.70"/></a></li>
      </ul>
      <div id="tabs-recurType-0">
        <p><g:message code="calendarEvent.recurType.0.noOptions.label"/></p>
      </div>
      <div id="tabs-recurType-10">
        <p>
          <label for="recurInterval-10"><g:message code="calendarEvent.recurType.10.each.label"/></label>&nbsp;
          <g:textField name="recurInterval-10" size="3"/>
          &nbsp;<label><g:message code="calendarEvent.recurType.10.days.label"/></label>
        </p>
      </div>
      <div id="tabs-recurType-30">
        <p>
          <label for="recurInterval-30"><g:message code="calendarEvent.recurType.30.at.label"/></label>&nbsp;
          <span><g:checkBox name="recurWeekdays-30-1" checked="${selectedWeekdays?.contains(1)}" />&nbsp;<label for="recurWeekdays-30-1">${weekdayNames[2]}</label></span>
          <span><g:checkBox name="recurWeekdays-30-2" checked="${selectedWeekdays?.contains(2)}" />&nbsp;<label for="recurWeekdays-30-2">${weekdayNames[3]}</label></span>
          <span><g:checkBox name="recurWeekdays-30-3" checked="${selectedWeekdays?.contains(3)}" />&nbsp;<label for="recurWeekdays-30-3">${weekdayNames[4]}</label></span>
          <span><g:checkBox name="recurWeekdays-30-4" checked="${selectedWeekdays?.contains(4)}" />&nbsp;<label for="recurWeekdays-30-4">${weekdayNames[5]}</label></span>
          <span><g:checkBox name="recurWeekdays-30-5" checked="${selectedWeekdays?.contains(5)}" />&nbsp;<label for="recurWeekdays-30-5">${weekdayNames[6]}</label></span>
          <span><g:checkBox name="recurWeekdays-30-6" checked="${selectedWeekdays?.contains(6)}" />&nbsp;<label for="recurWeekdays-30-6">${weekdayNames[7]}</label></span>
          <span><g:checkBox name="recurWeekdays-30-0" checked="${selectedWeekdays?.contains(7)}" />&nbsp;<label for="recurWeekdays-30-0">${weekdayNames[1]}</label></span>
        </p>
        <p>
          <label for="recurInterval-30"><g:message code="calendarEvent.recurType.30.each.label"/></label>&nbsp;
          <g:textField name="recurInterval-30" size="3"/>
          &nbsp;<label><g:message code="calendarEvent.recurType.30.weeks.label"/></label>
        </p>
      </div>
      <div id="tabs-recurType-40">
        <p>
          <label for="recurMonthDay-40"><g:message code="calendarEvent.recurType.40.at.label"/></label>&nbsp;
          <g:textField name="recurMonthDay-40" size="3"/>.
          <label for="recurInterval-40"><g:message code="calendarEvent.recurType.40.each.label"/></label>&nbsp;
          <g:textField name="recurInterval-40" size="3"/>
          &nbsp;<label><g:message code="calendarEvent.recurType.40.months.label"/></label>
        </p>
      </div>
      <div id="tabs-recurType-50">
        <p>
          <label for="recurWeekdayOrd-50"><g:message code="calendarEvent.recurType.50.at.label"/></label>&nbsp;
          <g:textField name="recurWeekdayOrd-50" size="3"/>.
          <g:select name="recurWeekdays-50" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}"/>&nbsp;
          <label for="recurInterval-50"><g:message code="calendarEvent.recurType.50.each.label"/></label>&nbsp;
          <g:textField name="recurInterval-50" size="3"/>
          &nbsp;<label><g:message code="calendarEvent.recurType.50.months.label"/></label>
        </p>
      </div>
      <div id="tabs-recurType-60">
        <p>
          <label for="recurMonthDay-60"><g:message code="calendarEvent.recurType.60.yearlyAt.label"/></label>&nbsp;
          <g:textField name="recurMonthDay-60" size="3"/>.
          <g:select name="recurMonth-60" from="${1..12}" optionValue="${{monthNames[it - 1]}}"/>
        </p>
      </div>
      <div id="tabs-recurType-70">
        <p>
          <label for="recurMonthDay-70"><g:message code="calendarEvent.recurType.70.yearlyAt.label"/></label>&nbsp;
          <g:textField name="recurWeekdayOrd-70" size="3"/>.
          <g:select name="recurWeekdays-70" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}"/>&nbsp;
          <label for="recurMonth-70"><g:message code="calendarEvent.recurType.70.inMonth.label"/></label>&nbsp;
          <g:select name="recurMonth-70" from="${1..12}" optionValue="${{monthNames[it - 1]}}"/>
        </p>
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
<content tag="additionalJavaScript">
<script type="text/javascript">
//<![CDATA[
(function ($, SPRINGCRM) {

    "use strict";

    var $tabs;

    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "organization",
            findUrl: "${createLink(controller:'organization', action:'find')}",
            onSelect: function (value) {
                $.ajax({
                    data: { id: value },
                    dataType: "json",
                    success: function (data) {
                        var s = "";

                        if (data.shippingAddrStreet) {
                            s += data.shippingAddrStreet;
                        }
                        if (data.shippingAddrPostalCode ||
                            data.shippingAddrLocation)
                        {
                            if (s !== "") {
                                s += ", ";
                            }
                            if (data.shippingAddrPostalCode) {
                                s += data.shippingAddrPostalCode + " ";
                            }
                            if (data.shippingAddrLocation) {
                                s += data.shippingAddrLocation;
                            }
                        }
                        $("#location").val(s);
                    },
                    url: "${createLink(controller:'organization', action:'get')}"
                });
            }
        })
        .init();
    $("#allDay").change(function () {
            var checked = this.checked;

            $("#start-time").toggleEnable(!checked);
            $("#end-time").toggleEnable(!checked);
        })
        .triggerHandler("change");
    $tabs = $("#tabs-recurType");
    $tabs.tabs()
        .change(function (event) {
            var $target = $(event.target),
                id;

            if ($target.attr("name") === "recurType") {
                $tabs.tabs("select", "tabs-recurType-" + $target.val());
            } else {
                id = $target.parents(".ui-tabs-panel")
                    .attr("id");
                if (id.match(/^tabs-recurType-(\d+)$/)) {
                    $("#recurType-" + RegExp.$1).trigger("click");
                }
            }
        });
}(jQuery, SPRINGCRM));
//]]></script>
</content>