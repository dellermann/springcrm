<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${dunningInstance}" property="number" />
        <f:field bean="${dunningInstance}" property="subject" />
        <f:field bean="${dunningInstance}" property="organization" />
        <f:field bean="${dunningInstance}" property="person" />
        <f:field bean="${dunningInstance}" property="invoice" />
        <f:field bean="${dunningInstance}" property="stage" />
        <f:field bean="${dunningInstance}" property="level" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${dunningInstance}" property="docDate" />
        <f:field bean="${dunningInstance}" property="dueDatePayment" />
        <f:field bean="${dunningInstance}" property="shippingDate" />
        <f:field bean="${dunningInstance}" property="carrier" />
        <f:field bean="${dunningInstance}" property="paymentDate" />
        <f:field bean="${dunningInstance}" property="paymentAmount" />
        <f:field bean="${dunningInstance}" property="paymentMethod" />
      </div>
    </div>
  </div>
</fieldset>
<section id="addresses" class="multicol-content"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <div class="col col-l left-address">
    <fieldset>
      <header>
        <h3><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h3>
        <div class="buttons">
          <g:menuButton color="white" size="small" icon="location-arrow"
            message="default.options.label" />
        </div>
      </header>
      <div class="form-fragment">
        <f:field bean="${dunningInstance}" property="billingAddr" />
      </div>
    </fieldset>
  </div>
  <div class="col col-r right-address">
    <fieldset>
      <header>
        <h3><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h3>
        <div class="buttons">
          <g:menuButton color="white" size="small" icon="location-arrow"
            message="default.options.label" />
        </div>
      </header>
      <div class="form-fragment">
        <f:field bean="${dunningInstance}" property="shippingAddr" />
      </div>
    </fieldset>
  </div>
</section>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${dunningInstance}" property="headerText" cols="80"
      rows="3" />
  </div>
</fieldset>
<fieldset>
  <header>
    <h3><g:message code="dunning.fieldset.items.label" /></h3>
    <div class="buttons">
      <g:button color="green" size="small" class="add-invoicing-item-btn"
        icon="plus" message="invoicingTransaction.button.addRow.label" />
    </div>
  </header>
  <div>
    <g:set var="invoicingTransaction" value="${dunningInstance}" />
    <g:applyLayout name="invoicingItemsForm"
      params="[tableId: 'dunning-items', className: 'dunning']" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${dunningInstance}" property="footerText" cols="80"
      rows="3" />
    <f:field bean="${dunningInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${dunningInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>
