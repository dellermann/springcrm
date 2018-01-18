<html>
  <head>
    <title>
      <g:message code="invoicingTransaction.edit.label"
        args="[message(code: 'creditMemo.label'), fullNumber]"/>
      - <g:message code="creditMemo.plural"/>
    </title>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <content tag="toolbar">
      <button type="submit" form="credit-memo-form" class="btn btn-success">
        <i class="fa fa-save"></i>
        <g:message code="default.button.save.label"/>
      </button>
      <g:button action="index" back="true" color="danger" icon="close"
        class="hidden-xs" message="default.button.cancel.label"/>
    </content>
    <content tag="captionActionBar">
      <div class="caption"><h2>${creditMemo}</h2></div>
    </content>

    <form action="${createLink(action: 'updatePayment')}" id="credit-memo-form"
      class="form-horizontal data-form form-view" method="post">
      <g:render template="/layouts/flashMessage"/>
      <g:render template="/layouts/errorMessage"
        model="[instance: creditMemo]"/>
      <g:hiddenField name="id" value="${creditMemo?.id}"/>
      <g:hiddenField name="version" value="${creditMemo?.version}"/>
      <section>
        <header>
          <h3
            ><g:message code="invoicingTransaction.fieldset.general.label"
          /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:field bean="${creditMemo}" property="stage"/>
          </div>
          <div class="column">
            <f:field bean="${creditMemo}" property="paymentDate"/>
            <f:field bean="${creditMemo}" property="paymentAmount"/>
            <f:field bean="${creditMemo}" property="paymentMethod"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="invoice.fieldset.items.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column detail-view">
            <g:set var="invoicingTransaction" value="${creditMemo}"/>
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'creditMemo']"/>
          </div>
        </div>
      </section>
    </form>

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form"/>
      <asset:script>//<![CDATA[
        new SPRINGCRM.InvoicingTransaction(
          $("#credit-memo-form"), {
              checkStageTransition: false,
              reducedView: true,
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
