<html>
  <head>
    <title>
      <g:message code="invoicingTransaction.edit.label"
        args="[message(code: 'invoice.label'), fullNumber]"/>
      - <g:message code="invoice.plural"/>
    </title>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="edit" model="[type: 'invoice', instance: invoice]"/>

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form"/>
      <asset:script>//<![CDATA[
        new SPRINGCRM.InvoicingTransaction(
          $("#invoice-form"), {
              stageValues: {
                  payment: 903,
                  shipping: 902
              },
              type: "I"
          });
      //]]></asset:script>
    </content>
  </body>
</html>
