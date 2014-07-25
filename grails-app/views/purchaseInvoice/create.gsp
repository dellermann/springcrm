<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <asset:stylesheet src="purchase-invoice-form" />
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
    <h2><g:message code="purchaseInvoice.new.label" default="New ${entityName}" /></h2>
    <g:uploadForm name="purchaseInvoice-form" action="save"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="project" value="${project}" />
      <g:hiddenField name="projectPhase" value="${projectPhase}" />
      <g:render template="/purchaseInvoice/form" />
    </g:uploadForm>
  </div>
  <content tag="scripts">
    <asset:javascript src="purchase-invoice-form" />
    <asset:script>//<![CDATA[
      (function ($) {

          "use strict";

          var params;

          params = $("#purchaseInvoice-form").purchaseinvoice({
                  checkStageTransition: false,
                  loadVendorsUrl: "${createLink(controller: 'organization', action: 'find', params: [type: 2])}",
                  stageValues: {
                      payment: 2102
                  },
                  type: "P"
              })
              .purchaseinvoice("getOrganizationId");
          $("#invoice").autocompleteex({
                  loadParameters: params
              });
          $("#dunning").autocompleteex({
                  loadParameters: params
              });
      }(jQuery));
    //]]></asset:script>
  </content>
</body>
</html>
