<%@ page import="org.amcworld.springcrm.Organization" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
  <g:if test="${(params.listType ?: 0) as int & 1}">
  <g:set var="entitiesName" value="${message(code: 'organization.customers', default: 'Customers')}" />
  </g:if>
  <g:elseif test="${(params.listType ?: 0) as int & 2}">
  <g:set var="entitiesName" value="${message(code: 'organization.vendors', default: 'Vendors')}" />
  </g:elseif>
  <g:else>
  <g:set var="entitiesName" value="${message(code: 'organization.plural', default: 'Organizations')}" />
  </g:else>
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="save"
          data-form="organization-form" message="default.button.save.label"
          /></li> 
        <li><g:button action="list" params="[type: params.listType]"
          color="red" icon="remove-circle"
          message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${organizationInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${organizationInstance?.toString()}</h2>
    <g:form name="organization-form" action="update" method="post" >
      <g:hiddenField name="id" value="${organizationInstance?.id}" />
      <g:hiddenField name="version" value="${organizationInstance?.version}" />
      <g:hiddenField name="listType" value="${params.listType}" />
      <g:render template="/organization/form" />
    </g:form>
  </div>
</body>
</html>
