<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<g:if test="${salesOrderInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="salesOrder-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" style="width: 6em;" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
    <tr data-item-id="${salesOrderInstance.id}">
      <td><input type="checkbox" id="salesOrder-multop-${salesOrderInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: salesOrderInstance, field: "fullNumber")}</a></td>
      <td><a href="#">${fieldValue(bean: salesOrderInstance, field: "subject")}</a></td>
      <td>${fieldValue(bean: salesOrderInstance, field: "organization")}</td>
      <td>${fieldValue(bean: salesOrderInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${salesOrderInstance?.total}" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${salesOrderInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
