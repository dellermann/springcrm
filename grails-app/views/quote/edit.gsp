<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quote.label')}" />
    <g:set var="entitiesName" value="${message(code: 'quote.plural')}" />
    <title
      ><g:message code="invoicingTransaction.edit.label"
        args="[entityName, quoteInstance.fullNumber]"
    /></title>
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
