<r:require modules="noteForm" />
<fieldset>
  <h4><g:message code="note.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="note.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: noteInstance, field: 'number', ' error')}">
          <g:autoNumber prefix="${seqNumberPrefix}" suffix="${seqNumberSuffix}" value="${noteInstance?.number}" /><br />
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
          <input type="text" id="organization" value="${noteInstance?.organization?.name}" size="35" data-find-url="${createLink(controller:'organization', action:'find')}" />
          <input type="hidden" name="organization.id" id="organization.id" value="${noteInstance?.organization?.id}" /><br />
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
          <input type="text" id="person" value="${noteInstance?.person?.fullName}" size="35" data-find-url="${createLink(controller:'person', action:'find')}" />
          <input type="hidden" name="person.id" id="person.id" value="${noteInstance?.person?.id}" /><br />
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
          <g:textArea id="note-content" name="content" cols="80" rows="10" value="${noteInstance?.content}" data-rte-script="${r.resource(uri: '/js/tiny_mce/tiny_mce.js')}" /><br />
          <g:hasErrors bean="${noteInstance}" field="content">
            <span class="error-msg"><g:eachError bean="${noteInstance}" field="content"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>