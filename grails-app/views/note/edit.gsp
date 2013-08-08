<%@ page import="org.amcworld.springcrm.Note" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'note.label', default: 'Note')}" />
  <g:set var="entitiesName" value="${message(code: 'note.plural', default: 'Notes')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'note']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${noteInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${noteInstance?.toString()}</h2>
    <g:form name="note-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${noteInstance?.id}" />
      <g:hiddenField name="version" value="${noteInstance?.version}" />
      <g:render template="/note/form" />
    </g:form>
  </div>
</body>
</html>
