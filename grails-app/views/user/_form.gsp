<fieldset>
  <h4><g:message code="user.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${userInstance}" property="userName" />
        <f:field bean="${userInstance}" property="password" />
        <div class="row">
          <div class="label">
            <label for="password-repeat"><g:message code="user.passwordRepeat.label" default="Repeat password" /></label>
          </div>
          <div class="field">
            <g:passwordField name="passwordRepeat" size="40" /><g:if test="${actionName == 'create'}"><br />
            <span class="info-msg"><g:message code="default.required" default="required" /></span></g:if>
          </div>
        </div>
        <f:field bean="${userInstance}" property="firstName" />
        <f:field bean="${userInstance}" property="lastName" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${userInstance}" property="phone" />
        <f:field bean="${userInstance}" property="phoneHome" />
        <f:field bean="${userInstance}" property="mobile" />
        <f:field bean="${userInstance}" property="fax" />
        <f:field bean="${userInstance}" property="email" />
      </div>
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