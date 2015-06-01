<g:applyLayout name="selectorList"
  model="[list: purchaseInvoiceInstanceList, total: purchaseInvoiceInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="purchase-invoice-row-selector" /></th>
        <g:sortableColumn property="number" title="${message(code: 'purchaseInvoice.number.label')}" />
        <g:sortableColumn property="subject" title="${message(code: 'purchaseInvoice.subject.label')}" />
        <g:sortableColumn property="vendor.name" title="${message(code: 'purchaseInvoice.vendor.label')}" />
        <g:sortableColumn property="stage" title="${message(code: 'purchaseInvoice.stage.label.short')}" />
        <g:sortableColumn property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short')}" />
        <g:sortableColumn property="total" title="${message(code: 'purchaseInvoice.total.label.short')}" />
      </tr>
    </thead>
    <tbody>
    <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
      <tr data-item-id="${purchaseInvoiceInstance.id}">
        <td class="col-type-row-selector"><input type="checkbox" id="purchase-invoice-row-selector-${purchaseInvoiceInstance.id}" data-id="${purchaseInvoiceInstance.id}" /></td>
        <td class="col-type-string purchase-invoice-number"><a href="#"><g:fieldValue bean="${purchaseInvoiceInstance}" field="number" /></a></td>
        <td class="col-type-string purchase-invoice-subject"><a href="#"><g:fieldValue bean="${purchaseInvoiceInstance}" field="subject" /></a></td>
        <td class="col-type-ref purchase-invoice-vendor"><g:fieldValue bean="${purchaseInvoiceInstance}" field="vendorName" /></td>
        <td class="col-type-status purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="stage" /></td>
        <td class="col-type-date purchase-invoice-doc-date"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
        <td class="col-type-currency purchase-invoice-total"><g:formatCurrency number="${purchaseInvoiceInstance.total}" displayZero="true" /></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
