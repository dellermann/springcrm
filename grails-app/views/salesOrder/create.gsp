<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="create" model="[
      type: 'salesOrder', instance: salesOrderInstance,
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
