<g:applyLayout name="listEmbedded"
  model="[list: purchaseInvoiceInstanceList, total: purchaseInvoiceInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="number" title="${message(code: 'purchaseInvoice.number.label')}" />
        <g:sortableColumn property="subject" title="${message(code: 'purchaseInvoice.subject.label')}" />
        <g:sortableColumn property="stage" title="${message(code: 'purchaseInvoice.stage.label.short')}" />
        <g:sortableColumn property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short')}" />
        <g:sortableColumn property="dueDate" title="${message(code: 'purchaseInvoice.dueDate.label')}" />
        <g:sortableColumn property="total" title="${message(code: 'purchaseInvoice.total.label.short')}" />
        <g:sortableColumn property="balance" title="${message(code: 'invoicingTransaction.closingBalance.label')}" />
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each in="${purchaseInvoiceInstanceList}" status="i"
      var="purchaseInvoiceInstance">
      <tr>
        <td class="col-type-string purchase-invoice-number"><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="invoiceNumber" /></g:link></td>
        <td class="col-type-string purchase-invoice-subject"><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="subject" /></g:link></td>
        <td class="col-type-status purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="stage" /></td>
        <td class="col-type-date purchase-invoice-doc-date"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
        <td class="col-type-date purchase-invoice-due-date"><g:formatDate date="${purchaseInvoiceInstance.dueDate}" formatName="default.format.date" /></td>
        <td class="col-type-currency purchase-invoice-total"><g:formatCurrency number="${purchaseInvoiceInstance.total}" displayZero="true" /></td>
        <td class="col-type-currency purchase-invoice-balance balance-state balance-state-${purchaseInvoiceInstance?.balanceColor}"><g:formatCurrency number="${purchaseInvoiceInstance?.balance}" displayZero="true" /></td>
        <td class="col-actions">
          <g:button controller="purchaseInvoice" action="edit"
            id="${purchaseInvoiceInstance.id}" color="success" size="xs"
            icon="pencil-square-o" message="default.button.edit.label" />
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
