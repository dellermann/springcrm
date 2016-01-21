<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${dunningInstance}" property="number" />
      <f:field bean="${dunningInstance}" property="subject" />
      <f:field bean="${dunningInstance}" property="organization" />
      <f:field bean="${dunningInstance}" property="person" />
      <g:ifModuleAllowed modules="INVOICE">
      <f:field bean="${dunningInstance}" property="invoice" />
      </g:ifModuleAllowed>
      <f:field bean="${dunningInstance}" property="stage" />
      <f:field bean="${dunningInstance}" property="level" />
      <f:field bean="${dunningInstance}" property="createUser" />
    </div>
    <div class="column">
      <f:field bean="${dunningInstance}" property="docDate" />
      <f:field bean="${dunningInstance}" property="dueDatePayment" />
      <f:field bean="${dunningInstance}" property="shippingDate" />
      <f:field bean="${dunningInstance}" property="carrier" />
      <f:field bean="${dunningInstance}" property="paymentDate" />
      <f:field bean="${dunningInstance}" property="paymentAmount" />
      <f:field bean="${dunningInstance}" property="paymentMethod" />
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'invoicingTransaction.fieldset.billingAddr.label')
    ]">
    <f:field bean="${dunningInstance}" property="billingAddr" />
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'invoicingTransaction.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${dunningInstance}" property="shippingAddr" />
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${dunningInstance}" property="headerText" rows="3" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="dunning.fieldset.items.label" /></h3>
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
      <g:set var="invoicingTransaction" value="${dunningInstance}" />
      <g:applyLayout name="invoicingItemsForm"
        params="[tableId: 'dunning-items', className: 'dunning']" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${dunningInstance}" property="footerText" rows="3" />
      <f:field bean="${dunningInstance}" property="termsAndConditions" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${dunningInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>
