<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'helpdesk.label', default: 'Helpdesk')}" />
  <g:set var="entitiesName" value="${message(code: 'helpdesk.plural', default: 'Helpdesks')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'helpdesk']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${helpdeskInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${helpdeskInstance?.toString()}</h2>
    <g:form name="helpdesk-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${helpdeskInstance?.id}" />
      <g:hiddenField name="version" value="${helpdeskInstance?.version}" />
      <g:render template="form"/>
    </g:form>
  </div>
  <content tag="scripts">
    <asset:javascript src="helpdesk-form" />
  </content>
</body>
</html>
