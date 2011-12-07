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
              </g:each>
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
              &nbsp;<label><g:message code="calendarEvent.recurrence.type.40.months.label" /></label>
            </p>
          </div>
          <div id="tabs-recurrence-type-50">
            <p>
              <label for="recurrence-weekdayOrd-50"><g:message code="calendarEvent.recurrence.type.50.at.label" /></label>&nbsp;
              <g:textField name="recurrence-weekdayOrd-50" size="3" />.
              <g:select name="recurrence-weekdays-50" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}" />&nbsp;
              <label for="recurrence-interval-50"><g:message code="calendarEvent.recurrence.type.50.each.label" /></label>&nbsp;
              <g:textField name="recurrence-interval-50" size="3" />
              &nbsp;<label><g:message code="calendarEvent.recurrence.type.50.months.label" /></label>
            </p>
          </div>
          <div id="tabs-recurrence-type-60">
            <p>
              <label for="recurrence-monthDay-60"><g:message code="calendarEvent.recurrence.type.60.yearlyAt.label" /></label>&nbsp;
              <g:textField name="recurrence-monthDay-60" size="3" />.
              <g:select name="recurrence-month-60" from="${0..11}" optionValue="${{monthNames[it]}}" />
            </p>
          </div>
          <div id="tabs-recurrence-type-70">
            <p>
              <label for="recurrence-monthDay-70"><g:message code="calendarEvent.recurrence.type.70.yearlyAt.label" /></label>&nbsp;
              <g:textField name="recurrence-weekdayOrd-70" size="3" />.
              <g:select name="recurrence-weekdays-70" from="${[*2..7, 1]}" optionValue="${{weekdayNames[it]}}" />&nbsp;
              <label for="recurrence-month-70"><g:message code="calendarEvent.recurrence.type.70.inMonth.label" /></label>&nbsp;
              <g:select name="recurrence-month-70" from="${0..11}" optionValue="${{monthNames[it]}}" />
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

    var $tabs,
        i = -1,
        n,
        recurType,
        wds;

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

    $tabs = $("#tabs-recurrence-type");
    $tabs.tabs()
        .change(function (event) {
            var $target = $(event.target),
                id,
                val;

            if ($target.attr("name") === "recurrence.type") {
                val = Number($target.val());
                $tabs.tabs("select", "tabs-recurrence-type-" + val);
                $("#recurrence-end").toggle(val !== 0);
            } else {
                id = $target.parents(".ui-tabs-panel")
                    .attr("id");
                if (id.match(/^tabs-recurrence-type-(\d+)$/)) {
                    $("#recurrence-type-" + RegExp.$1).trigger("click");
                }
            }
        });

    $("#recurrence-end input[name=recurrence\\.endType]")
        .change(function () {
            switch (this.id) {
            case "recurrence.endType-until":
                $("#recurrence\\.until-date").enable()
                    .focus();
                $("#recurrence\\.cnt").disable();
                break;
            case "recurrence.endType-count":
                $("#recurrence\\.cnt").enable()
                    .focus();
                $("#recurrence\\.until-date").disable();
                break;
            case "recurrence.endType-none":
                $("#recurrence\\.until-date").disable();
                $("#recurrence\\.cnt").disable();
                break;
            }
        });
    $("input[name=recurrence\\.endType]:checked").triggerHandler("change");

    recurType = Number($("#tabs-recurrence-type input:radio:checked").val());
    if (recurType === 0) {
        $("#recurrence-end").hide();
    } else {
        $("#recurrence-interval-" + recurType).val(
                $("#recurrence-interval").val()
            );
        $("#recurrence-monthDay-" + recurType).val(
                $("#recurrence-monthDay").val()
            );
        $("#recurrence-weekdayOrd-" + recurType).val(
                $("#recurrence-weekdayOrd").val()
            );
        $("#recurrence-month-" + recurType).val($("#recurrence-month").val());
        if ((recurType === 30) || (recurType === 50) || (recurType === 70)) {
            wds = $("#recurrence-weekdays").val().split(/,/);
            n = wds.length;
            if (recurType === 30) {
                $("#tabs-recurrence-type-30 input:checkbox")
                    .attr("checked", false);
                while (++i < n) {
                    $("#recurrence-weekdays-30-" + wds[i])
                        .attr("checked", true);
                }
            } else if (n > 0) {
                $("#recurrence-weekdays-" + recurType).val(wds[0]);
            }
        }
        $("#recurrence-end").show();
    }
    $tabs.tabs("select", "tabs-recurrence-type-" + recurType);

    $("#calendarEvent-form").bind("submit", function () {
            var recurType,
                val,
                wds;

            recurType = Number(
                    $("#tabs-recurrence-type input:radio:checked").val()
                );
            if (recurType > 0) {
                val = $("#recurrence-interval-" + recurType).val();
                $("#recurrence-interval").val(val || 1);
                $("#recurrence-monthDay").val(
                        $("#recurrence-monthDay-" + recurType).val()
                    );
                $("#recurrence-weekdayOrd").val(
                        $("#recurrence-weekdayOrd-" + recurType).val()
                    );
                $("#recurrence-month").val(
                        $("#recurrence-month-" + recurType).val()
                    );
                if (recurType === 30) {
                    wds = [];
                    $("#tabs-recurrence-type-30 input:checkbox:checked")
                        .each(function () {
                            wds.push(Number($(this).val()));
                        });
                        wds.sort();
                    $("#recurrence-weekdays").val(wds.join(","));
                } else if (recurType === 50) {
                    $("#recurrence-weekdays").val(
                            $("#recurrence-weekdays-50").val()
                        );
                } else if (recurType === 70) {
                    $("#recurrence-weekdays").val(
                            $("#recurrence-weekdays-70").val()
                        );
                }
            }
            return true;
        });
}(jQuery, SPRINGCRM));
//]]></script>
</content>