<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'salesOrder.label')}" />
    <g:set var="entitiesName" value="${message(code: 'salesOrder.plural')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="create"
      model="[type: 'salesOrder', instance: salesOrderInstance]">
      <g:hiddenField name="project" value="${project}" />
      <g:hiddenField name="projectPhase" value="${projectPhase}" />
    </g:applyLayout>

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
