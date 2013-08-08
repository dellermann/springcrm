<%@ page import="org.amcworld.springcrm.Project" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
  <g:set var="entitiesName" value="${message(code: 'project.plural', default: 'Projects')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'project']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${projectInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2><g:message code="project.new.label" default="New ${entityName}" /></h2>
    <g:form name="project-form" action="save"
      params="[returnUrl: params.returnUrl]">
      <g:render template="form"/>
    </g:form>
  </div>
</body>
</html>
