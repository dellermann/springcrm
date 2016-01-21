<section>
  <header>
    <h3><g:message code="purchaseInvoice.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${purchaseInvoiceInstance}" property="number" />
      <f:field bean="${purchaseInvoiceInstance}" property="subject" />
      <g:ifModuleAllowed modules="CONTACT">
      <f:field bean="${purchaseInvoiceInstance}" property="vendorName" />
      </g:ifModuleAllowed>
      <f:field bean="${purchaseInvoiceInstance}" property="documentFile" />
      <f:field bean="${purchaseInvoiceInstance}" property="stage" />
    </div>
    <div class="column">
      <f:field bean="${purchaseInvoiceInstance}" property="docDate" />
      <f:field bean="${purchaseInvoiceInstance}" property="dueDate" />
      <f:field bean="${purchaseInvoiceInstance}" property="paymentDate" />
      <f:field bean="${purchaseInvoiceInstance}" property="paymentAmount" />
      <f:field bean="${purchaseInvoiceInstance}" property="paymentMethod" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="purchaseInvoice.fieldset.items.label" /></h3>
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
      <g:set var="invoicingTransaction" value="${purchaseInvoiceInstance}" />
      <g:applyLayout name="invoicingItemsForm" params="[
          tableId: 'purchaseInvoice-items', className: 'purchaseInvoice'
        ]" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="purchaseInvoice.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${purchaseInvoiceInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>
