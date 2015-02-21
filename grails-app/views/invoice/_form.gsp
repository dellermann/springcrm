<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
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
    <div class="column">
      <f:field bean="${invoiceInstance}" property="docDate" />
      <f:field bean="${invoiceInstance}" property="dueDatePayment" />
      <f:field bean="${invoiceInstance}" property="shippingDate" />
      <f:field bean="${invoiceInstance}" property="carrier" />
      <f:field bean="${invoiceInstance}" property="paymentDate" />
      <f:field bean="${invoiceInstance}" property="paymentAmount" />
      <f:field bean="${invoiceInstance}" property="paymentMethod" />
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'invoicingTransaction.fieldset.billingAddr.label')
    ]">
    <f:field bean="${invoiceInstance}" property="billingAddr" />
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'invoicingTransaction.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${invoiceInstance}" property="shippingAddr" />
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${invoiceInstance}" property="headerText" rows="3" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoice.fieldset.items.label" /></h3>
    <div class="buttons">
      <div class="btn-group">
        <g:button type="button" color="success" size="xs"
          class="add-invoicing-item-btn" icon="plus-circle"
          message="invoicingTransaction.button.addRow.label" />
      </div>
    </div>
  </header>
  <div class="column-group">
    <div class="column">
      <g:set var="invoicingTransaction" value="${invoiceInstance}" />
      <g:applyLayout name="invoicingItemsForm"
        params="[tableId: 'invoice-items', className: 'invoice']" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${invoiceInstance}" property="footerText" rows="3" />
      <f:field bean="${invoiceInstance}" property="termsAndConditions" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${invoiceInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>
