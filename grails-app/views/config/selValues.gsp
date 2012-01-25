<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.selValues.title" default="Editor for selector values" /></title>
  <r:require modules="configSelValues" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.selValues.title" default="Editor for selector values" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="config-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="index" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:form name="config-form" action="saveSelValues" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <h4><g:message code="config.fieldset.selValues.general.label" default="General selector lists" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.salutation.label" default="Salutation for persons" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="salutation" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'salutation'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.orgType.label" default="Organization type" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="orgType" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'orgType'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.rating.label" default="Rating of organizations" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="rating" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'rating'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.unit.label" default="Unit" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="unit" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'unit'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.paymentMethod.label" default="Payment method" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="paymentMethod" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'paymentMethod'])}"></div>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.carrier.label" default="Carrier" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="carrier" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'carrier'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.industry.label" default="Industry" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="industry" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'industry'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.termsAndConditions.label" default="Terms and conditions" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="termsAndConditions" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'termsAndConditions'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.productCategory.label" default="Product category" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="productCategory" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'productCategory'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.serviceCategory.label" default="Service category" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="serviceCategory" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'serviceCategory'])}"></div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
      <fieldset>
        <h4><g:message code="config.fieldset.selValues.invoicing.label" default="Selector lists for invoicing transactions" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.quoteStage.label" default="Quote stage" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="quoteStage" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'quoteStage'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.salesOrderStage.label" default="Sales order stage" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="salesOrderStage" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'salesOrderStage'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.invoiceStage.label" default="Invoice stage" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="invoiceStage" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'invoiceStage'])}"></div>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.dunningStage.label" default="Dunning stage" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="dunningStage" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'dunningStage'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.dunningLevel.label" default="Dunning level" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="dunningLevel" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'dunningLevel'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.creditMemoStage.label" default="Credit memo stage" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="creditMemoStage" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'creditMemoStage'])}"></div>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label><g:message code="config.selValues.purchaseInvoiceStage.label" default="Purchase invoice stage" /></label>
              </div>
              <div class="field">
                <div class="sel-values-list" data-list-type="purchaseInvoiceStage" data-load-sel-values-url="${createLink(action: 'loadSelValues', params: [type: 'purchaseInvoiceStage'])}"></div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
