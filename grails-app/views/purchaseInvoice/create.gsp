<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="purchase-invoice-form" />
  </head>

  <body>
    <g:applyLayout name="create" model="[
        type: 'purchaseInvoice', instance: purchaseInvoiceInstance,
        enctype: 'multipart/form-data'
      ]">
      <g:hiddenField name="project" value="${project}" />
      <g:hiddenField name="projectPhase" value="${projectPhase}" />
    </g:applyLayout>

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

