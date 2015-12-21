<g:applyLayout name="listEmbedded"
  model="[list: invoiceInstanceList, total: invoiceInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label')}" />
        <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label')}" />
        <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label.short')}" />
        <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label.short')}" />
        <g:sortableColumn property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label')}" />
        <g:sortableColumn property="total" title="${message(code: 'invoice.total.label.short')}" />
        <th><g:message code="invoicingTransaction.closingBalance.label" /></th>
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
      <tr>
        <td class="col-type-id invoice-number"><g:link controller="invoice" action="show" id="${invoiceInstance.id}"><g:fieldValue bean="${invoiceInstance}" field="fullNumber" /></g:link></td>
        <td class="col-type-string invoice-subject"><g:link controller="invoice" action="show" id="${invoiceInstance.id}"><g:nl2br value="${invoiceInstance.subject.replaceAll(~/_{2,}/, ' ')}" /></g:link></td>
        <td class="col-type-status invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}"><g:fieldValue bean="${invoiceInstance}" field="stage" /></td>
        <td class="col-type-date invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
        <td class="col-type-date invoice-due-date-payment"><g:formatDate date="${invoiceInstance?.dueDatePayment}" formatName="default.format.date" /></td>
        <td class="col-type-currency invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" displayZero="true" external="true" /></td>
        <td class="col-type-currency invoice-closing-balance balance-state balance-state-${invoiceInstance?.balanceColor}"><g:formatCurrency number="${invoiceInstance?.closingBalance}" displayZero="true" external="true" /></td>
        <td class="col-actions">
          <g:if test="${session.user.admin || invoiceInstance.stage.id < 902}">
          <g:button controller="invoice" action="edit"
            id="${invoiceInstance.id}" color="success" size="xs"
            icon="pencil-square-o" message="default.button.edit.label" />
          </g:if>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
