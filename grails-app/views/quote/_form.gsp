<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quoteInstance}" property="number" />
      <f:field bean="${quoteInstance}" property="subject" />
      <f:field bean="${quoteInstance}" property="organization" />
      <f:field bean="${quoteInstance}" property="person" />
      <f:field bean="${quoteInstance}" property="stage" />
    </div>
    <div class="column">
      <f:field bean="${quoteInstance}" property="docDate" />
      <f:field bean="${quoteInstance}" property="validUntil" />
      <f:field bean="${quoteInstance}" property="shippingDate" />
      <f:field bean="${quoteInstance}" property="carrier" />
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'invoicingTransaction.fieldset.billingAddr.label')
    ]">
    <f:field bean="${quoteInstance}" property="billingAddr" />
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'invoicingTransaction.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${quoteInstance}" property="shippingAddr" />
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quoteInstance}" property="headerText" rows="3" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="quote.fieldset.items.label" /></h3>
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
      <g:set var="invoicingTransaction" value="${quoteInstance}" />
      <g:applyLayout name="invoicingItemsForm"
        params="[tableId: 'quote-items', className: 'quote']" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quoteInstance}" property="footerText" rows="3" />
      <f:field bean="${quoteInstance}" property="termsAndConditions" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quoteInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>
