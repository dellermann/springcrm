<html>
  <head>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="create" model="[type: 'quote', instance: quote]"/>

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form"/>
      <asset:script>//<![CDATA[
        new SPRINGCRM.InvoicingTransaction(
          $("#quote-form"), {
              checkStageTransition: false,
              stageValues: {
                  shipping: 602
              },
              type: "Q"
          });
      //]]></asset:script>
    </content>
  </body>
</html>
