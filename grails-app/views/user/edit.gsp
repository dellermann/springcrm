<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'user']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${userInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${userInstance?.toString()} (${userInstance?.userName})</h2>
    <g:form name="user-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${userInstance?.id}" />
      <g:hiddenField name="version" value="${userInstance?.version}" />
      <g:render template="/user/form" />
    </g:form>
  </div>
</body>
</html>
