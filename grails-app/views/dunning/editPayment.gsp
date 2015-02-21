<html>
  <head>
    <meta name="layout" content="main" />
    <title>
      <g:message code="invoicingTransaction.edit.label"
        args="[message(code: 'dunning.label'), dunningInstance.fullNumber]" />
      - <g:message code="dunning.plural" />
    </title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <content tag="toolbar">
      <button type="submit" form="dunning-form" class="btn btn-success">
        <i class="fa fa-save"></i>
        <g:message code="default.button.save.label" />
      </button>
      <g:button action="index" back="true" color="danger" icon="close"
        class="hidden-xs" message="default.button.cancel.label" />
    </content>
    <content tag="captionActionBar">
      <div class="caption"><h2>${dunningInstance}</h2></div>
    </content>

    <form action="${createLink(action: 'updatePayment')}" id="dunning-form"
      class="form-horizontal data-form form-view" method="post">
      <g:if test="${flash.message}">
      <div class="alert alert-success" role="alert">
        ${raw(flash.message)}
      </div>
      </g:if>
      <g:hasErrors bean="${instance}">
      <div class="alert alert-danger" role="alert">
        <g:message code="default.form.errorHint" />
      </div>
      </g:hasErrors>
      <g:hiddenField name="id" value="${dunningInstance?.id}" />
      <g:hiddenField name="version" value="${dunningInstance?.version}" />
      <section>
        <header>
          <h3
            ><g:message code="invoicingTransaction.fieldset.general.label"
          /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:field bean="${dunningInstance}" property="stage" />
          </div>
          <div class="column">
            <f:field bean="${dunningInstance}" property="paymentDate" />
            <f:field bean="${dunningInstance}" property="paymentAmount" />
            <f:field bean="${dunningInstance}" property="paymentMethod" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="invoice.fieldset.items.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column detail-view">
            <g:set var="invoicingTransaction" value="${dunningInstance}" />
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'dunning']" />
          </div>
        </div>
      </section>
    </form>

    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-form" />
      <asset:script>//<![CDATA[
        new SPRINGCRM.InvoicingTransaction(
          $("#dunning-form"), {
              checkStageTransition: false,
              reducedView: true,
              stageValues: {
                  payment: 2203,
                  shipping: 2202
              },
              type: "D"
          });
      //]]></asset:script>
    </content>
  </body>
</html>
