<section>
  <header>
    <h3><g:message code="user.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${userInstance}" property="userName" />
      <f:field bean="${userInstance}" property="password"
        required="${actionName == 'create'}" />
      <f:field bean="${userInstance}" property="firstName" />
      <f:field bean="${userInstance}" property="lastName" />
    </div>
    <div class="column">
      <f:field bean="${userInstance}" property="phone" />
      <f:field bean="${userInstance}" property="phoneHome" />
      <f:field bean="${userInstance}" property="mobile" />
      <f:field bean="${userInstance}" property="fax" />
      <f:field bean="${userInstance}" property="email" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="user.fieldset.permissions.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${userInstance}" property="admin" />
      <f:field bean="${userInstance}" property="allowedModulesNames" />
    </div>
  </div>
</section>
