<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <meta name="stylesheet" content="person-form" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'person']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${personInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${personInstance?.toString()}</h2>
    <g:uploadForm name="person-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${personInstance?.id}" />
      <g:hiddenField name="version" value="${personInstance?.version}" />
      <g:render template="/person/form" />
    </g:uploadForm>
  </div>
  <content tag="scripts">
    <asset:javascript src="person-form" />
  </content>
</body>
</html>
