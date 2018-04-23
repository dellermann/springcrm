<html>
  <head>
    <meta name="layout" content="main" />
    <title>
      <g:message code="invoicingTransaction.edit.label"
        args="[message(code: 'quote.label'), fullNumber(bean: quoteInstance)]"/> -
      <g:message code="quote.plural" />
    </title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="edit"
      model="[type: 'quote', instance: quoteInstance]" />

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form" />
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
