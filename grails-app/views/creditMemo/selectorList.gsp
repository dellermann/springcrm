<g:applyLayout name="selectorList"
  model="[list: creditMemoInstanceList, total: creditMemoInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="credit-memo-row-selector" /></th>
        <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label')}" />
        <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label')}" />
        <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label')}" />
        <g:sortableColumn property="stage" title="${message(code: 'creditMemo.stage.label.short')}" />
        <g:sortableColumn property="docDate" title="${message(code: 'creditMemo.docDate.label.short')}" />
        <g:sortableColumn property="total" title="${message(code: 'creditMemo.total.label.short')}" />
      </tr>
    </thead>
    <tbody>
    <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
      <tr data-item-id="${creditMemoInstance.id}">
        <td class="col-type-row-selector"><input type="checkbox" id="credit-memo-row-selector-${creditMemoInstance.id}" data-id="${creditMemoInstance.id}" /></td>
        <td class="col-type-id credit-memo-number"><a href="#">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</a></td>
        <td class="col-type-string credit-memo-subject"><a href="#"><g:nl2br value="${creditMemoInstance.subject}" /></a></td>
        <td class="col-type-ref credit-memo-organization">${fieldValue(bean: creditMemoInstance, field: "organization")}</td>
        <td class="col-type-status credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
        <td class="col-type-date credit-memo-doc-date"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
        <td class="col-type-currency credit-memo-total"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
