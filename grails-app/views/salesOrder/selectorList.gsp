<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<g:if test="${salesOrderInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-sales-order-row-selector"><input type="checkbox" id="sales-order-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-sales-order-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-sales-order-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-sales-order-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn id="content-table-headers-sales-order-stage" property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-sales-order-doc-date" property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-sales-order-total" property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
    <tr data-item-id="${salesOrderInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-sales-order-row-selector"><input type="checkbox" id="sales-order-row-selector-${salesOrderInstance.id}" data-id="${salesOrderInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-sales-order-number" headers="content-table-headers-sales-order-number"><a href="#">${fieldValue(bean: salesOrderInstance, field: "fullNumber")}</a></td>
      <td class="content-table-type-string content-table-column-sales-order-subject" headers="content-table-headers-sales-order-subject"><a href="#">${fieldValue(bean: salesOrderInstance, field: "subject")}</a></td>
      <td class="content-table-type-ref content-table-column-sales-order-organization" headers="content-table-headers-sales-order-organization">${fieldValue(bean: salesOrderInstance, field: "organization")}</td>
      <td class="content-table-type-status content-table-column-sales-order-stage" headers="content-table-headers-sales-order-stage">${fieldValue(bean: salesOrderInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-sales-order-doc-date" headers="content-table-headers-sales-order-doc-date"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-sales-order-total" headers="content-table-headers-sales-order-total"><g:formatCurrency number="${salesOrderInstance?.total}" /></td>
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
