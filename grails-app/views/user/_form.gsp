<fieldset>
  <h4><g:message code="user.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="userName"><g:message code="user.userName.label" default="User Name" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'userName', ' error')}">
          <g:textField name="userName" value="${userInstance?.userName}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${userInstance}" field="userName">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="userName"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="password"><g:message code="user.password.label" default="Password" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'password', ' error')}">
          <g:passwordField name="password" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${userInstance}" field="password">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="password"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="firstName"><g:message code="user.firstName.label" default="First Name" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'firstName', ' error')}">
          <g:textField name="firstName" value="${userInstance?.firstName}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${userInstance}" field="firstName">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="firstName"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="lastName"><g:message code="user.lastName.label" default="Last Name" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'lastName', ' error')}">
          <g:textField name="lastName" value="${userInstance?.lastName}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${userInstance}" field="lastName">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="lastName"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="phone"><g:message code="user.phone.label" default="Phone" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'phone', ' error')}">
          <g:textField name="phone" maxlength="40" value="${userInstance?.phone}" /><br />
          <g:hasErrors bean="${userInstance}" field="phone">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="phone"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="phoneHome"><g:message code="user.phoneHome.label" default="Phone Home" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'phoneHome', ' error')}">
          <g:textField name="phoneHome" maxlength="40" value="${userInstance?.phoneHome}" /><br />
          <g:hasErrors bean="${userInstance}" field="phoneHome">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="phoneHome"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="mobile"><g:message code="user.mobile.label" default="Mobile" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'mobile', ' error')}">
          <g:textField name="mobile" maxlength="40" value="${userInstance?.mobile}" /><br />
          <g:hasErrors bean="${userInstance}" field="mobile">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="mobile"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="fax"><g:message code="user.fax.label" default="Fax" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'fax', ' error')}">
          <g:textField name="fax" maxlength="40" value="${userInstance?.fax}" /><br />
          <g:hasErrors bean="${userInstance}" field="fax">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="fax"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="email"><g:message code="user.email.label" default="Email" /></label>
        </div>
        <div class="field${hasErrors(bean: userInstance, field: 'email', ' error')}">
          <g:textField name="email" value="${userInstance?.email}" size="45" /><br />
          <g:hasErrors bean="${userInstance}" field="email">
            <span class="error-msg"><g:eachError bean="${userInstance}" field="email"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>

<fieldset>
  <h4><g:message code="user.fieldset.permissions.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="admin"><g:message code="user.admin.label" default="Administrator" /></label>
      </div>
      <div class="field${hasErrors(bean: userInstance, field: 'admin', ' error')}">
        <g:checkBox name="admin" value="${userInstance?.admin}"/><br />
        <g:hasErrors bean="${userInstance}" field="admin">
          <span class="error-msg"><g:eachError bean="${userInstance}" field="admin"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
    <div class="row">
      <div class="label">
        <label for="allowedModulesAsList"><g:message code="user.allowedModules.label" default="Allowed modules" /></label>
      </div>
      <div class="field${hasErrors(bean: userInstance, field: 'allowedModulesAsList', ' error')}">
        <g:select name="allowedModulesAsList" 
                  from="${grailsApplication.controllerClasses.logicalPropertyName - 'searchable'}"
                  optionValue="${ { message(code:it + '.plural') } }"
                  value="${userInstance?.allowedModulesAsList}"
                  multiple="true" size="7"/><br />
        <g:hasErrors bean="${userInstance}" field="allowedModulesAsList">
          <span class="error-msg"><g:eachError bean="${userInstance}" field="allowedModulesAsList"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
