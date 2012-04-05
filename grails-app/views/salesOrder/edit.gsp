<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <g:set var="entitiesName" value="${message(code: 'salesOrder.plural', default: 'SalesOrders')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="sales-order-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="list" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${salesOrderInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3>${salesOrderInstance?.toString()}</h3>
    <g:form name="sales-order-form" action="update" method="post" params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${salesOrderInstance?.id}" />
      <g:hiddenField name="version" value="${salesOrderInstance?.version}" />
      <g:render template="/salesOrder/form" />
    </g:form>
  </section>
</body>
</html>
