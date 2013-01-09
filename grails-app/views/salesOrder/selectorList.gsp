<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<g:if test="${salesOrderInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="sales-order-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
    <tr data-item-id="${salesOrderInstance.id}">
      <td class="row-selector"><input type="checkbox" id="sales-order-row-selector-${salesOrderInstance.id}" data-id="${salesOrderInstance.id}" /></td>
      <td class="id sales-order-number"><a href="#"><g:fieldValue bean="${salesOrderInstance}" field="fullNumber" /></a></td>
      <td class="string sales-order-subject"><a href="#"><g:fieldValue bean="${salesOrderInstance}" field="subject" /></a></td>
      <td class="ref sales-order-organization"><g:fieldValue bean="${salesOrderInstance}" field="organization" /></td>
      <td class="status sales-order-stage"><g:fieldValue bean="${salesOrderInstance}" field="stage" /></td>
      <td class="date sales-order-doc-date"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="currency sales-order-total"><g:formatCurrency number="${salesOrderInstance?.total}" displayZero="true" /></td>
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
