<fieldset>
  <header><h3><g:message code="helpdesk.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${helpdeskInstance}" property="organization" />
    </div>
    <div class="col col-r">
      <p><g:message code="helpdesk.name.description" /></p>
      <f:field bean="${helpdeskInstance}" property="name" />
      <f:field bean="${helpdeskInstance}" property="accessCode" />
      <div class="row">
        <div class="label"></div>
        <div class="field">
          <g:button elementId="generate-access-code" color="green"
            size="medium" icon="key" message="helpdesk.accessCode.generate" />
        </div>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="helpdesk.fieldset.users.label" /></h3></header>
  <div>
    <p><g:message code="helpdesk.users.description" /></p>
    <f:field bean="${helpdeskInstance}" property="users" size="7"
      style="min-width: 25em;" />
  </div>
</fieldset>
