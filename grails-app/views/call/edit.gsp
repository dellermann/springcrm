<%@ page import="org.amcworld.springcrm.Call" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'call.label', default: 'Call')}" />
  <g:set var="entitiesName" value="${message(code: 'call.plural', default: 'Calls')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'call']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${callInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${callInstance?.toString()}</h2>
    <g:form name="call-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${callInstance?.id}" />
      <g:hiddenField name="version" value="${callInstance?.version}" />
      <g:render template="/call/form" />
    </g:form>
  </div>
</body>
</html>
