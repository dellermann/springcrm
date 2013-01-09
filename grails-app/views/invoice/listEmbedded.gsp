<g:if test="${invoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="invoice-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label', default: 'Due date of payment')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'invoice.total.label.short', default: 'Total')}" />
      <g:sortableColumn scope="col" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="invoice-row-selector-${invoiceInstance.id}" data-id="${invoiceInstance.id}" /></td>
      <td class="id invoice-number"><g:link controller="invoice" action="show" id="${invoiceInstance.id}"><g:fieldValue bean="${invoiceInstance}" field="fullNumber" /></g:link></td>
      <td class="string invoice-subject"><g:link controller="invoice" action="show" id="${invoiceInstance.id}"><g:fieldValue bean="${invoiceInstance}" field="subject" /></g:link></td>
      <td class="status invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}"><g:fieldValue bean="${invoiceInstance}" field="stage" /></td>
      <td class="date invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="date invoice-due-date-payment"><g:formatDate date="${invoiceInstance?.dueDatePayment}" formatName="default.format.date" /></td>
      <td class="currency invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" displayZero="true" /></td>
      <td class="currency invoice-closing-balance balance-state balance-state-${invoiceInstance?.balanceColor}"><g:formatCurrency number="${invoiceInstance?.closingBalance}" displayZero="true" /></td>
      <td class="action-buttons">
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
