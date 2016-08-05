<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="purchase-invoice-form" />
  </head>

  <body>
    <g:applyLayout name="create" model="[
        type: 'purchaseInvoice', instance: purchaseInvoiceInstance,
        enctype: 'multipart/form-data'
      ]"/>

    <content tag="scripts">
      <asset:javascript src="purchase-invoice-form" />
      <asset:script>//<![CDATA[
        new SPRINGCRM.PurchaseInvoice(
          $("#purchase-invoice-form"), {
              checkStageTransition: false,
              stageValues: {
                  payment: 2102
              },
              type: "P"
          });
      //]]></asset:script>
    </content>
  </body>
</html>

