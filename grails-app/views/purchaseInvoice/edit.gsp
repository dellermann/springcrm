<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm"
      model="[formName: 'purchaseInvoice']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${purchaseInvoiceInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${purchaseInvoiceInstance?.toString()}</h2>
    <g:uploadForm name="purchaseInvoice-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${purchaseInvoiceInstance?.id}" />
      <g:hiddenField name="version"
        value="${purchaseInvoiceInstance?.version}" />
      <g:render template="/purchaseInvoice/form" />
    </g:uploadForm>
  </div>
</body>
</html>
