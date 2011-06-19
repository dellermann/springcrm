

<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="javascript:void 0;" class="green" onclick="springcrm.onClickSubmit('user-form');"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${userInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3><g:message code="user.login.label" default="Login" /></h3>
    <g:form name="user-form" action="authenticate" >
      <fieldset>
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
                <g:passwordField name="password" value="${userInstance?.password}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
                <g:hasErrors bean="${userInstance}" field="password">
                  <span class="error-msg"><g:eachError bean="${userInstance}" field="password"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
