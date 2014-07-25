<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${invoiceInstance}" property="number" />
        <f:field bean="${invoiceInstance}" property="subject" />
        <f:field bean="${invoiceInstance}" property="organization" />
        <f:field bean="${invoiceInstance}" property="person" />
        <g:ifModuleAllowed modules="quote">
        <f:field bean="${invoiceInstance}" property="quote" />
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="salesOrder">
        <f:field bean="${invoiceInstance}" property="salesOrder" />
        </g:ifModuleAllowed>
        <f:field bean="${invoiceInstance}" property="stage" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${invoiceInstance}" property="docDate" />
        <f:field bean="${invoiceInstance}" property="dueDatePayment" />
        <f:field bean="${invoiceInstance}" property="shippingDate" />
        <f:field bean="${invoiceInstance}" property="carrier" />
        <f:field bean="${invoiceInstance}" property="paymentDate" />
        <f:field bean="${invoiceInstance}" property="paymentAmount" />
        <f:field bean="${invoiceInstance}" property="paymentMethod" />
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
        <f:field bean="${invoiceInstance}" property="billingAddr" />
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
        <f:field bean="${invoiceInstance}" property="shippingAddr" />
      </div>
    </fieldset>
  </div>
</section>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${invoiceInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <section>
    <header>
      <h3><g:message code="invoice.fieldset.items.label" /></h3>
      <div class="buttons">
        <g:button color="green" size="small" class="add-invoicing-item-btn"
          icon="plus" message="invoicingTransaction.button.addRow.label" />
      </div>
    </header>
  </section>
  <div>
    <g:set var="invoicingTransaction" value="${invoiceInstance}" />
    <g:applyLayout name="invoicingItemsForm" params="[tableId: 'invoice-items', className: 'invoice']" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${invoiceInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${invoiceInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${invoiceInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>
