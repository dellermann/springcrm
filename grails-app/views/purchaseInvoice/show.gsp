<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: purchaseInvoiceInstance]">
      <section>
        <header>
          <h3><g:message code="purchaseInvoice.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${purchaseInvoiceInstance}" property="number" />
            <f:display bean="${purchaseInvoiceInstance}" property="subject" />
            <f:display bean="${purchaseInvoiceInstance}" property="vendor" />
            <f:display bean="${purchaseInvoiceInstance}"
              property="documentFile" />
            <f:display bean="${purchaseInvoiceInstance}" property="stage" />
          </div>
          <div class="column">
            <f:display bean="${purchaseInvoiceInstance}" property="docDate" />
            <f:display bean="${purchaseInvoiceInstance}" property="dueDate" />
            <f:display bean="${purchaseInvoiceInstance}"
              property="paymentDate" />
            <f:display bean="${purchaseInvoiceInstance}"
              property="paymentAmount" />
            <f:display bean="${purchaseInvoiceInstance}"
              property="paymentMethod" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="invoice.fieldset.items.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction"
              value="${purchaseInvoiceInstance}" />
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'invoice']" />
          </div>
        </div>
      </section>
      <g:if test="${purchaseInvoiceInstance?.notes}">
      <section>
        <header>
          <h3><g:message code="purchaseInvoice.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${purchaseInvoiceInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>
    </g:applyLayout>
  </body>
</html>

