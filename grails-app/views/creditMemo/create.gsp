<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="create"
      model="[type: 'creditMemo', instance: creditMemoInstance]">
      <g:hiddenField name="project" value="${project}" />
      <g:hiddenField name="projectPhase" value="${projectPhase}" />
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form" />
      <asset:script>//<![CDATA[
        new SPRINGCRM.InvoicingTransaction(
          $("#credit-memo-form"), {
              checkStageTransition: false,
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
