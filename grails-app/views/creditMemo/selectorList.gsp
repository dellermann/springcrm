<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<g:if test="${creditMemoInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="credit-memo-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
    <tr data-item-id="${creditMemoInstance.id}">
      <td class="row-selector"><input type="checkbox" id="credit-memo-row-selector-${creditMemoInstance.id}" data-id="${creditMemoInstance.id}" /></td>
      <td class="id credit-memo-number"><a href="#">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</a></td>
      <td class="string credit-memo-subject"><a href="#"><g:nl2br value="${creditMemoInstance.subject}" /></a></td>
      <td class="ref credit-memo-organization">${fieldValue(bean: creditMemoInstance, field: "organization")}</td>
      <td class="status credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
      <td class="date credit-memo-doc-date"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="currency credit-memo-total"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
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
