<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${creditMemoInstance}" property="number" />
      <f:field bean="${creditMemoInstance}" property="subject" />
      <g:ifModuleAllowed modules="contact">
      <f:field bean="${creditMemoInstance}" property="organization" />
      <f:field bean="${creditMemoInstance}" property="person" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="invoice">
      <f:field bean="${creditMemoInstance}" property="invoice" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="dunning">
      <f:field bean="${creditMemoInstance}" property="dunning" />
      </g:ifModuleAllowed>
      <f:field bean="${creditMemoInstance}" property="stage" />
    </div>
    <div class="column">
      <f:field bean="${creditMemoInstance}" property="docDate" />
      <f:field bean="${creditMemoInstance}" property="shippingDate" />
      <f:field bean="${creditMemoInstance}" property="carrier" />
      <f:field bean="${creditMemoInstance}" property="paymentDate" />
      <f:field bean="${creditMemoInstance}" property="paymentAmount" />
      <f:field bean="${creditMemoInstance}" property="paymentMethod" />
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'invoicingTransaction.fieldset.billingAddr.label')
    ]">
    <f:field bean="${creditMemoInstance}" property="billingAddr" />
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'invoicingTransaction.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${creditMemoInstance}" property="shippingAddr" />
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${creditMemoInstance}" property="headerText" rows="3" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="creditMemo.fieldset.items.label" /></h3>
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
      <g:set var="invoicingTransaction" value="${creditMemoInstance}" />
      <g:applyLayout name="invoicingItemsForm"
        params="[tableId: 'credit-memo-items', className: 'creditMemo']" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${creditMemoInstance}" property="footerText" rows="3" />
      <f:field bean="${creditMemoInstance}" property="termsAndConditions" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${creditMemoInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>
