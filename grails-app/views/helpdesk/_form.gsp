<r:require modules="helpdeskForm" />
<fieldset>
  <h4><g:message code="helpdesk.fieldset.general.label" /></h4>
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
          <button type="button" id="generate-access-code"
            class="button green medium"
            ><g:message code="helpdesk.accessCode.generate" /></button>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="helpdesk.fieldset.users.label" /></h4>
  <div class="fieldset-content">
    <p><g:message code="helpdesk.users.description" /></p>
    <f:field bean="${helpdeskInstance}" property="users" size="7" style="min-width: 25em;" />
  </div>
</fieldset>
