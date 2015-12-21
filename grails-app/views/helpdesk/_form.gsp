<section>
  <header>
    <h3><g:message code="helpdesk.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${helpdeskInstance}" property="organization" />
    </div>
    <div class="column">
      <p><g:message code="helpdesk.name.description" /></p>
      <f:field bean="${helpdeskInstance}" property="name" />
      <f:field bean="${helpdeskInstance}" property="accessCode" />
      <div class="form-group">
        <label class="control-label"></label>
        <div class="control-container">
          <g:button elementId="generate-access-code" color="success"
            icon="key" message="helpdesk.accessCode.generate" />
        </div>
      </div>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="helpdesk.fieldset.users.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <p><g:message code="helpdesk.users.description" /></p>
      <f:field bean="${helpdeskInstance}" property="users" size="7" />
    </div>
  </div>
</section>
