<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
  <g:set var="entitiesName" value="${message(code: 'creditMemo.plural', default: 'CreditMemos')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <meta name="stylesheet" content="invoicing-transaction" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm"
      model="[formName: 'credit-memo']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${creditMemoInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2><g:message code="creditMemo.new.label" default="New ${entityName}" /></h2>
    <g:form name="credit-memo-form" action="save"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="project" value="${project}" />
      <g:hiddenField name="projectPhase" value="${projectPhase}" />
      <g:render template="/creditMemo/form" />
    </g:form>
  </div>
  <content tag="scripts">
    <asset:javascript src="invoicing-transaction-form" />
    <asset:script>//<![CDATA[
      (function ($) {

          "use strict";

          var $form,
              params;

          $form = $("#credit-memo-form");
          params = $form.invoicingtransaction({
                  stageValues: {
                      payment: 2503,
                      shipping: 2502
                  },
                  type: "C"
              })
              .invoicingtransaction("getOrganizationId");
          $("#invoice").autocompleteex({
                  loadParameters: params,
                  select: function (event, ui) {
                      $form.invoicingtransaction(
                          "refreshModifiedClosingBalance",
                          {
                              id: ui.item.value,
                              url: "${createLink(controller: 'invoice', action: 'getClosingBalance')}"
                          }
                      );
                  }
              });
          $("#dunning").autocompleteex({
                  loadParameters: params,
                  select: function (event, ui) {
                      $form.invoicingtransaction(
                          "refreshModifiedClosingBalance",
                          {
                              id: ui.item.value,
                              url: "${createLink(controller: 'dunning', action: 'getClosingBalance')}"
                          }
                      );
                  }
              });
      }(jQuery));
    //]]></asset:script>
  </content>
</body>
</html>
