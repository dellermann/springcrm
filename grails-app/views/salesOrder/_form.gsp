<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${salesOrder}" property="number"/>
      <f:field bean="${salesOrder}" property="subject"/>
      <f:field bean="${salesOrder}" property="organization"/>
      <f:field bean="${salesOrder}" property="person"/>
      <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_QUOTE">
        <f:field bean="${salesOrder}" property="quote"/>
      </sec:ifAnyGranted>
      <f:field bean="${salesOrder}" property="stage"/>
      <f:field bean="${salesOrder}" property="createUser"/>
    </div>
    <div class="column">
      <f:field bean="${salesOrder}" property="orderDate"/>
      <f:field bean="${salesOrder}" property="orderMethod"/>
      <f:field bean="${salesOrder}" property="orderDocument"/>
      <f:field bean="${salesOrder}" property="signature"/>
      <f:field bean="${salesOrder}" property="docDate"/>
      <f:field bean="${salesOrder}" property="dueDate"/>
      <f:field bean="${salesOrder}" property="shippingDate"/>
      <f:field bean="${salesOrder}" property="carrier"/>
      <f:field bean="${salesOrder}" property="deliveryDate"/>
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
    <f:field bean="${salesOrder}" property="billingAddr"/>
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'invoicingTransaction.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${salesOrder}" property="shippingAddr"/>
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.header.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${salesOrder}" property="headerText" rows="3"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="salesOrder.fieldset.items.label"/></h3>
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
      <g:set var="invoicingTransaction" value="${salesOrder}"/>
      <g:applyLayout name="invoicingItemsForm"
        params="[tableId: 'sales-order-items', className: 'salesOrder']"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.footer.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${salesOrder}" property="footerText" rows="3"/>
      <f:field bean="${salesOrder}" property="termsAndConditions"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.notes.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${salesOrder}" property="notes" rows="5"/>
    </div>
  </div>
</section>
<g:render template="signatureDialog"/>
