<section>
  <header>
    <h3><g:message code="user.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${user}" property="username"/>
      <f:field bean="${user}" property="password"
        required="${actionName == 'create'}"/>
      <f:field bean="${user}" property="firstName"/>
      <f:field bean="${user}" property="lastName"/>
    </div>
    <div class="column">
      <f:field bean="${user}" property="phone"/>
      <f:field bean="${user}" property="phoneHome"/>
      <f:field bean="${user}" property="mobile"/>
      <f:field bean="${user}" property="fax"/>
      <f:field bean="${user}" property="email"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="user.fieldset.permissions.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${user}" property="admin"/>
      <f:field bean="${user}" property="allowedModulesNames"/>
    </div>
  </div>
</section>
