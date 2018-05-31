<html>
  <head>
    <meta name="stylesheet" content="sales-order"/>
  </head>

  <body>
    <g:applyLayout name="create" model="[
      type: 'salesOrder', instance: salesOrder, enctype: 'multipart/form-data'
    ]"/>

    <content tag="scripts">
      <asset:javascript src="modules"/>
      <asset:javascript src="lang/bootstrap-fileinput/${lang}.js"/>
      <asset:javascript src="sales-order-form"/>
      <asset:script>//<![CDATA[
      var SalesOrder = window.modules.require("SalesOrder");
      new SalesOrder(
          $("#sales-order-form"), {
              checkStageTransition: false,
              stageValues: {
                  payment: 803,
                  shipping: 802
              },
              type: "S"
          }
      );
      //]]></asset:script>
    </content>
  </body>
</html>
