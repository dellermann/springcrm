<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="config.selValues.title" /> -
    <g:message code="config.title" /></title>
    <meta name="caption" content="${message(code: 'config.title')}" />
    <meta name="subcaption" content="${message(code: 'config.selValues.title')}" />
    <meta name="stylesheet" content="config" />
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarForm" model="[formName: 'config']" />
    </content>

    <g:render template="/layouts/flashMessage" />
    <g:render template="/layouts/errorMessage" />

    <g:form action="saveSelValues" elementId="config-form"
      params="[returnUrl: params.returnUrl]" method="post"
      class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="config.fieldset.selValues.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:render template="/config/selValuesList"
              model="[type: 'salutation']" />
            <g:render template="/config/selValuesList"
              model="[type: 'orgType']" />
            <g:render template="/config/selValuesList"
              model="[type: 'rating']" />
            <g:render template="/config/selValuesList"
              model="[type: 'unit']" />
            <g:render template="/config/selValuesList"
              model="[type: 'paymentMethod']" />
          </div>
          <div class="column">
            <g:render template="/config/selValuesList"
              model="[type: 'carrier']" />
            <g:render template="/config/selValuesList"
              model="[type: 'industry']" />
            <g:render template="/config/selValuesList"
              model="[type: 'termsAndConditions']" />
            <g:render template="/config/selValuesList"
              model="[type: 'productCategory']" />
            <g:render template="/config/selValuesList"
              model="[type: 'serviceCategory']" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="config.fieldset.selValues.invoicing.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:render template="/config/selValuesList"
              model="[type: 'quoteStage']" />
            <g:render template="/config/selValuesList"
              model="[type: 'salesOrderStage']" />
            <g:render template="/config/selValuesList"
              model="[type: 'invoiceStage']" />
          </div>
          <div class="column">
            <g:render template="/config/selValuesList"
              model="[type: 'dunningStage']" />
            <g:render template="/config/selValuesList"
              model="[type: 'dunningLevel']" />
            <g:render template="/config/selValuesList"
              model="[type: 'creditMemoStage']" />
            <g:render template="/config/selValuesList"
              model="[type: 'purchaseInvoiceStage']" />
          </div>
        </div>
      </section>
    </g:form>

    <content tag="scripts">
      <asset:javascript src="config-sel-values" />
    </content>
  </body>
</html>
