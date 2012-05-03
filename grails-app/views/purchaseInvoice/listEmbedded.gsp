<g:if test="${purchaseInvoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-purchase-invoice-row-selector"><input type="checkbox" id="purchase-invoice-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-purchase-invoice-number" property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-subject" property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-stage" property="stage" title="${message(code: 'purchaseInvoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-doc-date" property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short', default: 'Doc Date')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-due-date" property="dueDate" title="${message(code: 'purchaseInvoice.dueDate.label', default: 'Due Date')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-total" property="total" title="${message(code: 'purchaseInvoice.total.label.short', default: 'Total')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-balance" property="balance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
      <th id="content-table-headers-purchase-invoice-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
    <tr>
      <td class="content-table-row-selector" headers="content-table-headers-purchase-invoice-row-selector"><input type="checkbox" id="purchase-invoice-row-selector-${purchaseInvoiceInstance.id}" data-id="${purchaseInvoiceInstance.id}" /></td>
      <td class="content-table-type-string content-table-column-purchase-invoice-number" headers="content-table-headers-purchase-invoice-number"><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "number")}</g:link></td>
      <td class="content-table-type-string content-table-column-purchase-invoice-subject" headers="content-table-headers-purchase-invoice-subject"><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "subject")}</g:link></td>
      <td class="content-table-type-status content-table-column-purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}" headers="content-table-headers-purchase-invoice-stage">${fieldValue(bean: purchaseInvoiceInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-purchase-invoice-doc-date" headers="content-table-headers-purchase-invoice-doc-date"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-date content-table-column-purchase-invoice-due-date" headers="content-table-headers-purchase-invoice-due-date"><g:formatDate date="${purchaseInvoiceInstance.dueDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-purchase-invoice-total" headers="content-table-headers-purchase-invoice-total"><g:formatCurrency number="${purchaseInvoiceInstance.total}" /></td>
      <td class="content-table-type-currency content-table-column-purchase-invoice-balance balance-state balance-state-${purchaseInvoiceInstance?.balanceColor}" headers="content-table-headers-purchase-invoice-balance"><g:formatCurrency number="${purchaseInvoiceInstance?.balance}" displayZero="true" /></td>
      <td class="content-table-buttons" headers="content-table-headers-purchase-invoice-buttons">
        <g:link controller="purchaseInvoice" action="edit" id="${purchaseInvoiceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="purchaseInvoice" action="delete" id="${purchaseInvoiceInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${purchaseInvoiceInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
