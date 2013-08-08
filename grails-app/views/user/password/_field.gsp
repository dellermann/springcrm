<div class="row">
  <div class="label">
    <label for="password"><g:message code="user.password.label" default="Password" /></label>
  </div>
  <div class="field${hasErrors(bean: userInstance, field: 'password', ' error')}">
    <g:passwordField name="password" size="40" />
    <g:if test="${actionName == 'create'}">
    <ul class="field-msgs">
      <li class="info-msg"><g:message code="default.required" default="required" /></li></g:if>
      <g:eachError bean="${userInstance}" field="password">
      <li class="error-msg"><g:message error="${it}" /></li>
      </g:eachError>
    </ul>
  </div>
</div>
