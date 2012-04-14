<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<g:if test="${creditMemoInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="creditMemo-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" style="width: 6em;" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
    <tr data-item-id="${creditMemoInstance.id}">
      <td><input type="checkbox" id="creditMemo-multop-${creditMemoInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</a></td>
      <td><a href="#">${fieldValue(bean: creditMemoInstance, field: "subject")}</a></td>
      <td>${fieldValue(bean: creditMemoInstance, field: "organization")}</td>
      <td>${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
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
