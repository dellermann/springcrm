<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${salesOrderInstance}" property="number"/>
      <f:field bean="${salesOrderInstance}" property="subject"/>
      <f:field bean="${salesOrderInstance}" property="organization"/>
      <f:field bean="${salesOrderInstance}" property="person"/>
      <g:ifModuleAllowed modules="QUOTE">
      <f:field bean="${salesOrderInstance}" property="quote"/>
      </g:ifModuleAllowed>
      <f:field bean="${salesOrderInstance}" property="stage"/>
      <f:field bean="${salesOrderInstance}" property="createUser"/>
    </div>
    <div class="column">
      <f:field bean="${salesOrderInstance}" property="orderDate"/>
      <f:field bean="${salesOrderInstance}" property="orderMethod"/>
      <f:field bean="${salesOrderInstance}" property="orderDocument"/>
      <f:field bean="${salesOrderInstance}" property="signature"/>
      <f:field bean="${salesOrderInstance}" property="docDate"/>
      <f:field bean="${salesOrderInstance}" property="dueDate"/>
      <f:field bean="${salesOrderInstance}" property="shippingDate"/>
      <f:field bean="${salesOrderInstance}" property="carrier"/>
      <f:field bean="${salesOrderInstance}" property="deliveryDate"/>
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'invoicingTransaction.fieldset.billingAddr.label')
    ]">
    <f:field bean="${salesOrderInstance}" property="billingAddr"/>
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'invoicingTransaction.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${salesOrderInstance}" property="shippingAddr"/>
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.header.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${salesOrderInstance}" property="headerText" rows="3"/>
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
      <g:set var="invoicingTransaction" value="${salesOrderInstance}"/>
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
      <f:field bean="${salesOrderInstance}" property="footerText" rows="3"/>
      <f:field bean="${salesOrderInstance}" property="termsAndConditions"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="invoicingTransaction.fieldset.notes.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${salesOrderInstance}" property="notes" rows="5"/>
    </div>
  </div>
</section>
<g:render template="signatureDialog"/>
