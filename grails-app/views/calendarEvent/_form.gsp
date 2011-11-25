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
(function (SPRINGCRM) {
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
                            if (s != "") {
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
}(SPRINGCRM));
//]]></script>
</content>