<fieldset>
  <h4><g:message code="call.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="call.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${callInstance?.subject}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${callInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="notes"><g:message code="call.notes.label" default="Notes" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'notes', ' error')}">
          <g:textArea name="notes" cols="35" rows="5" value="${callInstance?.notes}" /><br />
          <g:hasErrors bean="${callInstance}" field="notes">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="notes"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="start"><g:message code="call.start.label" default="Start" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'start', ' error')}">
          <g:hiddenField name="start" value="${formatDate(date: callInstance?.start, type: 'datetime')}" />
          <g:textField name="start-date" value="${formatDate(date: callInstance?.start, type: 'date')}" size="10" class="date-input date-input-date" />
          <g:textField name="start-time" value="${formatDate(date: callInstance?.start, type: 'time')}" size="5" class="date-input date-input-time" /><br />
          <span class="info-msg"><g:message code="default.format.datetime.label" /></span>
          <g:hasErrors bean="${callInstance}" field="start">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="start"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="call.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${callInstance?.organization?.name}" size="35" />
          <input type="hidden" name="organization.id" id="organization-id" value="${callInstance?.organization?.id}" />
          <g:hasErrors bean="${callInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="person"><g:message code="call.person.label" default="Person" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'person', ' error')}">
          <input type="text" id="person" value="${callInstance?.person?.fullName}" size="35" />
          <input type="hidden" name="person.id" id="person-id" value="${callInstance?.person?.id}" />
          <g:hasErrors bean="${callInstance}" field="person">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="person"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="call.phone.label" default="Phone" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'phone', ' error')}">
          <g:textField name="phone" value="${callInstance?.phone}" size="40" /><br />
          <g:hasErrors bean="${callInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="phone"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="type"><g:message code="call.type.label" default="Type" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'type', ' error')}">
          <g:select name="type" from="${callInstance.constraints.type.inList}" value="${callInstance?.type}" valueMessagePrefix="call.type"  /><br />
          <g:hasErrors bean="${callInstance}" field="type">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="type"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="status"><g:message code="call.status.label" default="Status" /></label>
        </div>
        <div class="field${hasErrors(bean: callInstance, field: 'status', ' error')}">
          <g:select name="status" from="${callInstance.constraints.status.inList}" value="${callInstance?.status}" valueMessagePrefix="call.status"  /><br />
          <g:hasErrors bean="${callInstance}" field="status">
            <span class="error-msg"><g:eachError bean="${callInstance}" field="status"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<content tag="additionalJavaScript">
<script type="text/javascript">
//<![CDATA[
(function(SPRINGCRM) {
    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "organization",
            findUrl: "${createLink(controller:'organization', action:'find')}"
        })
        .init();
    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "person",
            findUrl: "${createLink(controller:'person', action:'find')}",
            parameters: function () {
                return { organization: $("#organization-id").val() };
            }
        })
        .init();
}(SPRINGCRM));
//]]></script>
</content>