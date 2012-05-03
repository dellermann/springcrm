<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<g:if test="${creditMemoInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-credit-memo-row-selector"><input type="checkbox" id="credit-memo-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-credit-memo-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-stage" property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-doc-date" property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-credit-memo-total" property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
    <tr data-item-id="${creditMemoInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-credit-memo-row-selector"><input type="checkbox" id="credit-memo-row-selector-${creditMemoInstance.id}" data-id="${creditMemoInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-credit-memo-number" headers="content-table-headers-credit-memo-number"><a href="#">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</a></td>
      <td class="content-table-type-string content-table-column-credit-memo-subject" headers="content-table-headers-credit-memo-subject"><a href="#">${fieldValue(bean: creditMemoInstance, field: "subject")}</a></td>
      <td class="content-table-type-ref content-table-column-credit-memo-organization" headers="content-table-headers-credit-memo-organization">${fieldValue(bean: creditMemoInstance, field: "organization")}</td>
      <td class="content-table-type-status content-table-column-credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}" headers="content-table-headers-credit-memo-stage">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-credit-memo-doc-date" headers="content-table-headers-credit-memo-doc-date"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-credit-memo-total" headers="content-table-headers-credit-memo-total"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${creditMemoInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
