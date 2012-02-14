<fieldset>
  <h4><g:message code="user.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${userInstance}" property="userName" />
      <f:field bean="${userInstance}" property="password" />
      <f:field bean="${userInstance}" property="firstName" />
      <f:field bean="${userInstance}" property="lastName" />
    </div>
    <div class="col col-r">
      <f:field bean="${userInstance}" property="phone" />
      <f:field bean="${userInstance}" property="phoneHome" />
      <f:field bean="${userInstance}" property="mobile" />
      <f:field bean="${userInstance}" property="fax" />
      <f:field bean="${userInstance}" property="email" />
    </div>
  </div>
</fieldset>

<fieldset>
  <h4><g:message code="user.fieldset.permissions.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${userInstance}" property="admin" />
    <f:field bean="${userInstance}" property="allowedModulesAsList" />
  </div>
</fieldset>