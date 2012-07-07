<div class="row">
  <div class="label">
    <label for="password"><g:message code="user.password.label" default="Password" /></label>
  </div>
  <div class="field${hasErrors(bean: userInstance, field: 'password', ' error')}">
    <g:passwordField name="password" size="40" /><g:if test="${actionName == 'create'}"><br />
    <span class="info-msg"><g:message code="default.required" default="required" /></span></g:if>
    <g:hasErrors bean="${userInstance}" field="password">
    <span class="error-msg"><g:eachError bean="${userInstance}" field="password"><g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
  </div>
</div>
