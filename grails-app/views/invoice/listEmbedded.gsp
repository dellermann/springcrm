<g:if test="${invoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="invoice-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" params="${linkParams}" />
      <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" params="${linkParams}" />
      <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" params="${linkParams}" />
      <g:sortableColumn property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label', default: 'Due date of payment')}" params="${linkParams}" />
      <g:sortableColumn property="total" title="${message(code: 'invoice.total.label.short', default: 'Total')}" style="width: 6em;" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
    <tr>
      <td><input type="checkbox" id="invoice-multop-${invoiceInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="invoice" action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="invoice" action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "subject")}</g:link></td>
      <td class="payment-state-${invoiceInstance?.paymentStateColor}">${fieldValue(bean: invoiceInstance, field: "stage")}</td>
      <td style="text-align: center;"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
      <td style="text-align: center;"><g:formatDate date="${invoiceInstance?.dueDatePayment}" formatName="default.format.date" /></td>
      <td style="text-align: right;"><g:formatCurrency number="${invoiceInstance?.total}" /></td>
      <td>
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
