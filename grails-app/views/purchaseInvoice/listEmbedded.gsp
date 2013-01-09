<g:if test="${purchaseInvoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="purchase-invoice-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'purchaseInvoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short', default: 'Doc Date')}" />
      <g:sortableColumn scope="col" property="dueDate" title="${message(code: 'purchaseInvoice.dueDate.label', default: 'Due Date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'purchaseInvoice.total.label.short', default: 'Total')}" />
      <g:sortableColumn scope="col" property="balance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="purchase-invoice-row-selector-${purchaseInvoiceInstance.id}" data-id="${purchaseInvoiceInstance.id}" /></td>
      <td class="string purchase-invoice-number"><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="number" /></g:link></td>
      <td class="string purchase-invoice-subject"><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="subject" /></g:link></td>
      <td class="status purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="stage" /></td>
      <td class="date purchase-invoice-doc-date"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
      <td class="date purchase-invoice-due-date"><g:formatDate date="${purchaseInvoiceInstance.dueDate}" formatName="default.format.date" /></td>
      <td class="currency purchase-invoice-total"><g:formatCurrency number="${purchaseInvoiceInstance.total}" displayZero="true" /></td>
      <td class="currency purchase-invoice-balance balance-state balance-state-${purchaseInvoiceInstance?.balanceColor}"><g:formatCurrency number="${purchaseInvoiceInstance?.balance}" displayZero="true" /></td>
      <td class="action-buttons">
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
