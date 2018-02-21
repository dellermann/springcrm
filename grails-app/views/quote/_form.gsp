<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quote}" property="number"/>
      <f:field bean="${quote}" property="subject"/>
      <f:field bean="${quote}" property="organization"/>
      <f:field bean="${quote}" property="person"/>
      <f:field bean="${quote}" property="stage"/>
      <f:field bean="${quote}" property="createUser"/>
    </div>
    <div class="column">
      <f:field bean="${quote}" property="docDate"/>
      <f:field bean="${quote}" property="validUntil"/>
      <f:field bean="${quote}" property="shippingDate"/>
      <f:field bean="${quote}" property="carrier"/>
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${
    createLink(controller: 'organization', action: 'get')
  }">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'invoicingTransaction.fieldset.billingAddr.label')
    ]">
    <f:field bean="${quote}" property="billingAddr"/>
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'invoicingTransaction.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${quote}" property="shippingAddr"/>
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.header.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quote}" property="headerText" rows="3"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="quote.fieldset.items.label"/></h3>
    <div class="buttons">
      <div class="btn-group">
        <g:button type="button" color="success" size="xs"
          class="add-invoicing-item-btn" icon="plus-circle"
          message="invoicingTransaction.button.addRow.label"/>
      </div>
    </div>
  </header>
  <div class="column-group">
    <div class="column">
      <g:set var="invoicingTransaction" value="${quote}"/>
      <g:applyLayout name="invoicingItemsForm"
        params="[tableId: 'quote-items', className: 'quote']"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.footer.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quote}" property="footerText" rows="3"/>
      <f:field bean="${quote}" property="termsAndConditions"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.notes.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${quote}" property="notes" rows="5"/>
    </div>
  </div>
</section>
