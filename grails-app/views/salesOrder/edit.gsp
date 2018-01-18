<html>
  <head>
    <title>
      <g:message code="invoicingTransaction.edit.label"
        args="[message(code: 'salesOrder.label'), fullNumber]"/> -
      <g:message code="salesOrder.plural"/>
    </title>
    <meta name="stylesheet" content="sales-order"/>
  </head>

  <body>
    <g:applyLayout name="edit" model="[
      type: 'salesOrder', instance: salesOrder,
      enctype: 'multipart/form-data'
    ]"/>

    <content tag="scripts">
      <asset:javascript src="sales-order-form"/>
      <asset:script>//<![CDATA[
        new SPRINGCRM.SalesOrder(
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
