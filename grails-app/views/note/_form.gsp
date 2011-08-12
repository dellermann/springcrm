<fieldset>
  <h4><g:message code="note.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="note.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: noteInstance, field: 'number', ' error')}">
          ${seqNumberPrefix}-<g:textField name="number" value="${noteInstance?.number}" size="10" /><br />
          <g:hasErrors bean="${noteInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${noteInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="title"><g:message code="note.title.label" default="Title" /></label>
        </div>
        <div class="field${hasErrors(bean: noteInstance, field: 'title', ' error')}">
          <g:textField name="title" maxlength="200" value="${noteInstance?.title}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${noteInstance}" field="title">
            <span class="error-msg"><g:eachError bean="${noteInstance}" field="title"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="note.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: noteInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${noteInstance?.organization?.name}" size="35" />
          <input type="hidden" name="organization.id" id="organization-id" value="${noteInstance?.organization?.id}" />
          <g:hasErrors bean="${noteInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${noteInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="person"><g:message code="note.person.label" default="Person" /></label>
        </div>
        <div class="field${hasErrors(bean: noteInstance, field: 'person', ' error')}">
          <input type="text" id="person" value="${noteInstance?.person?.fullName}" size="35" />
          <input type="hidden" name="person.id" id="person-id" value="${noteInstance?.person?.id}" />
          <g:hasErrors bean="${noteInstance}" field="person">
            <span class="error-msg"><g:eachError bean="${noteInstance}" field="person"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="note.fieldset.content.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="row">
        <div class="label">
          <label for="content"><g:message code="note.content.label" default="Content" /></label>
        </div>
        <div class="field${hasErrors(bean: noteInstance, field: 'content', ' error')}">
          <g:textArea name="content" cols="80" rows="10" value="${noteInstance?.content}" /><br />
          <g:hasErrors bean="${noteInstance}" field="content">
            <span class="error-msg"><g:eachError bean="${noteInstance}" field="content"><g:message error="${it}" /> </g:eachError></span>
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