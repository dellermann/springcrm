<%@ page import="org.amcworld.springcrm.Project" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
  <g:set var="entitiesName" value="${message(code: 'project.plural', default: 'Projects')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <meta name="stylesheet" content="project" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'project']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${projectInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${projectInstance?.toString()}</h2>
    <g:form name="project-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${projectInstance?.id}" />
      <g:hiddenField name="version" value="${projectInstance?.version}" />
      <g:render template="form"/>
    </g:form>
  </div>
  <content tag="scripts">
    <asset:javascript src="project-form" />
  </content>
</body>
</html>
