<g:if test="${invoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-invoice-row-selector"><input type="checkbox" id="invoice-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-invoice-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-invoice-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-invoice-stage" property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-invoice-doc-date" property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-invoice-due-date-payment" property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label', default: 'Due date of payment')}" />
      <g:sortableColumn id="content-table-headers-invoice-total" property="total" title="${message(code: 'invoice.total.label.short', default: 'Total')}" />
      <g:sortableColumn id="content-table-headers-invoice-closing-balance" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
      <th id="content-table-headers-invoice-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
    <tr>
      <td class="content-table-row-selector" headers="content-table-headers-invoice-row-selector"><input type="checkbox" id="invoice-row-selector-${invoiceInstance.id}" data-id="${invoiceInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-invoice-number" headers="content-table-headers-invoice-number"><g:link controller="invoice" action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</g:link></td>
      <td class="content-table-type-string content-table-column-invoice-subject" headers="content-table-headers-invoice-subject"><g:link controller="invoice" action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "subject")}</g:link></td>
      <td class="content-table-type-status content-table-column-invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}" headers="content-table-headers-invoice-stage">${fieldValue(bean: invoiceInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-invoice-doc-date" headers="content-table-headers-invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-date content-table-column-invoice-due-date-payment" headers="content-table-headers-invoice-due-date-payment"><g:formatDate date="${invoiceInstance?.dueDatePayment}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-invoice-total" headers="content-table-headers-invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" /></td>
      <td class="content-table-type-currency content-table-column-invoice-closing-balance balance-state balance-state-${invoiceInstance?.balanceColor}" headers="content-table-headers-invoice-closing-balance"><g:formatCurrency number="${invoiceInstance?.closingBalance}" displayZero="true" /></td>
      <td class="content-table-buttons" headers="content-table-headers-invoice-buttons">
        <g:if test="${session.user.admin || invoiceInstance.stage.id < 902}">
        <g:link controller="invoice" action="edit" id="${invoiceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="invoice" action="delete" id="${invoiceInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
        </g:if>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${invoiceInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
