<g:if test="${creditMemoInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="credit-memo-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="paymentDate" title="${message(code: 'invoicingTransaction.paymentDate.label', default: 'Payment date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" />
      <g:sortableColumn scope="col" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="credit-memo-row-selector-${creditMemoInstance.id}" data-id="${creditMemoInstance.id}" /></td>
      <td class="id credit-memo-number"><g:link controller="creditMemo" action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</g:link></td>
      <td class="string credit-memo-subject"><g:link controller="creditMemo" action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "subject")}</g:link></td>
      <td class="status credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
      <td class="date credit-memo-doc-date"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="date credit-memo-payment-date"><g:formatDate date="${creditMemoInstance?.paymentDate}" formatName="default.format.date" /></td>
      <td class="currency credit-memo-total"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
      <td class="currency credit-memo-closing-balance balance-state balance-state-${creditMemoInstance?.balanceColor}"><g:formatCurrency number="${creditMemoInstance?.closingBalance}" displayZero="true" /></td>
      <td class="action-buttons">
        <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
        <g:link controller="creditMemo" action="edit" id="${creditMemoInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="creditMemo" action="delete" id="${creditMemoInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
        </g:if>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${creditMemoInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
