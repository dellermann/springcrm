<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
  <title><g:message code="invoicingTransaction.edit.label" args="[entityName, invoiceInstance.fullNumber]" /></title>
  <asset:stylesheet src="invoicing-transaction" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'invoice']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${invoiceInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${invoiceInstance?.toString()}</h2>
    <g:form name="invoice-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${invoiceInstance?.id}" />
      <g:hiddenField name="version" value="${invoiceInstance?.version}" />
      <g:render template="/invoice/form" />
    </g:form>
  </div>
  <content tag="scripts">
    <asset:javascript src="invoicing-transaction-form" />
    <asset:script>//<![CDATA[
      (function ($) {

          "use strict";

          var params;

          params = $("#invoice-form").invoicingtransaction({
                  stageValues: {
                      payment: 903,
                      shipping: 902
                  },
                  type: "I"
              })
              .invoicingtransaction("getOrganizationId");
          $("#quote").autocompleteex({
                  loadParameters: params
              });
          $("#salesOrder").autocompleteex({
                  loadParameters: params
              });
      }(jQuery));
    //]]></asset:script>
  </content>
</body>
</html>
