<g:if test="${creditMemoInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="creditMemo-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" params="${linkParams}" />
      <g:sortableColumn property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" params="${linkParams}" />
      <g:sortableColumn property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" params="${linkParams}" />
      <g:sortableColumn property="paymentDate" title="${message(code: 'invoicingTransaction.paymentDate.label', default: 'Payment date')}" params="${linkParams}" />
      <g:sortableColumn property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" style="width: 6em;" />
      <g:sortableColumn property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" style="width: 6em;" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
    <tr>
      <td><input type="checkbox" id="creditMemo-multop-${creditMemoInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="creditMemo" action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="creditMemo" action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "subject")}</g:link></td>
      <td class="payment-state-${creditMemoInstance?.paymentStateColor}">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="align-center"><g:formatDate date="${creditMemoInstance?.paymentDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
      <td class="align-right balance-state-${creditMemoInstance?.balanceColor}"><g:formatCurrency number="${creditMemoInstance?.closingBalance}" displayZero="true" /></td>
      <td>
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
