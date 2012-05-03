<g:if test="${creditMemoInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-credit-memo-row-selector"><input type="checkbox" id="credit-memo-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-credit-memo-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-stage" property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-doc-date" property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-payment-date" property="paymentDate" title="${message(code: 'invoicingTransaction.paymentDate.label', default: 'Payment date')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-total" property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-closing-balance" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
      <th id="content-table-headers-credit-memo-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
    <tr>
      <td class="content-table-row-selector" headers="content-table-headers-credit-memo-row-selector"><input type="checkbox" id="credit-memo-row-selector-${creditMemoInstance.id}" data-id="${creditMemoInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-credit-memo-number" headers="content-table-headers-credit-memo-number"><g:link controller="creditMemo" action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</g:link></td>
      <td class="content-table-type-string content-table-column-credit-memo-subject" headers="content-table-headers-credit-memo-subject"><g:link controller="creditMemo" action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "subject")}</g:link></td>
      <td class="content-table-type-status content-table-column-credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}" headers="content-table-headers-credit-memo-stage">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-credit-memo-doc-date" headers="content-table-headers-credit-memo-doc-date"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-date content-table-column-credit-memo-payment-date" headers="content-table-headers-credit-memo-payment-date"><g:formatDate date="${creditMemoInstance?.paymentDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-credit-memo-total" headers="content-table-headers-credit-memo-total"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
      <td class="content-table-type-currency content-table-column-credit-memo-closing-balance balance-state balance-state-${creditMemoInstance?.balanceColor}" headers="content-table-headers-credit-memo-closing-balance"><g:formatCurrency number="${creditMemoInstance?.closingBalance}" displayZero="true" /></td>
      <td class="content-table-buttons" headers="content-table-headers-credit-memo-buttons">
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
