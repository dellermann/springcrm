<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${salesOrderInstance}" property="number" />
        <f:field bean="${salesOrderInstance}" property="subject" />
        <f:field bean="${salesOrderInstance}" property="organization" />
        <f:field bean="${salesOrderInstance}" property="person" />
        <g:ifModuleAllowed modules="quote">
        <f:field bean="${salesOrderInstance}" property="quote" />
        </g:ifModuleAllowed>
        <f:field bean="${salesOrderInstance}" property="stage" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${salesOrderInstance}" property="docDate" />
        <f:field bean="${salesOrderInstance}" property="dueDate" />
        <f:field bean="${salesOrderInstance}" property="shippingDate" />
        <f:field bean="${salesOrderInstance}" property="carrier" />
        <f:field bean="${salesOrderInstance}" property="deliveryDate" />
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
        <f:field bean="${salesOrderInstance}" property="billingAddr" />
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
        <f:field bean="${salesOrderInstance}" property="shippingAddr" />
      </div>
    </fieldset>
  </div>
</section>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${salesOrderInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <header>
    <h3><g:message code="salesOrder.fieldset.items.label" /></h3>
    <div class="buttons">
      <g:button color="green" size="small" class="add-invoicing-item-btn"
        icon="plus" message="invoicingTransaction.button.addRow.label" />
    </div>
  </header>
  <div>
    <g:set var="invoicingTransaction" value="${salesOrderInstance}" />
    <g:applyLayout name="invoicingItemsForm"
      params="[tableId: 'sales-order-items', className: 'salesOrder']" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${salesOrderInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${salesOrderInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${salesOrderInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>
