<html>
  <head>
    <meta name="layout" content="main" />
    <title>
      <g:message code="invoicingTransaction.edit.label"
        args="[
            message(code: 'creditMemo.label'), creditMemoInstance.fullNumber
          ]" />
      - <g:message code="creditMemo.plural" />
    </title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="edit"
      model="[type: 'creditMemo', instance: creditMemoInstance]" />

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form" />
      <asset:script>//<![CDATA[
        new SPRINGCRM.InvoicingTransaction(
          $("#credit-memo-form"), {
              handleInvoiceDunningChange: true,
              stageValues: {
                  payment: 2503,
                  shipping: 2502
              },
              type: "C"
          });
      //]]></asset:script>
    </content>
  </body>
</html>
