<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'salesOrder.label')}" />
    <g:set var="entitiesName" value="${message(code: 'salesOrder.plural')}" />
    <title
      ><g:message code="invoicingTransaction.edit.label"
        args="[entityName, salesOrderInstance.fullNumber]"
    /></title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="edit"
      model="[type: 'salesOrder', instance: salesOrderInstance]" />

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form" />
      <asset:script>//<![CDATA[
        new SPRINGCRM.InvoicingTransaction(
          $("#sales-order-form"), {
              checkStageTransition: false,
              stageValues: {
                  payment: 803,
                  shipping: 802
              },
              type: "S"
          });
      //]]></asset:script>
    </content>
  </body>
</html>


<%--
  <content tag="scripts">
    <asset:script>//<![CDATA[
      (function ($) {

          "use strict";

          var params;

          params = $("#sales-order-form").invoicingtransaction({
                  checkStageTransition: false,
                  stageValues: {
                      payment: 803,
                      shipping: 802
                  },
                  type: "S"
              })
              .invoicingtransaction("getOrganizationId");
          $("#quote").autocompleteex({
                  loadParameters: params
              });
      }(jQuery));
    //]]></asset:script>
  </content>
--%>
