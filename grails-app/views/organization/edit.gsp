

<%@ page import="org.amcworld.springcrm.Organization" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
  <g:set var="entitiesName" value="${message(code: 'organization.plural', default: 'Organizations')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green" onclick="SPRINGCRM.submitForm('organization-form'); return false;"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${organizationInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3>${organizationInstance?.toString()}</h3>
    <g:form name="organization-form" action="update" method="post" >
      <g:hiddenField name="id" value="${organizationInstance?.id}" />
      <g:hiddenField name="version" value="${organizationInstance?.version}" />
      <g:render template="/organization/form" />
    </g:form>
  </section>
  <content tag="jsTexts">
  copyAddressWarning_billingAddr: "${message(code: 'organization.shippingAddr.exists')}",
  copyAddressWarning_shippingAddr: "${message(code: 'organization.billingAddr.exists')}"
  </content>
</body>
</html>
