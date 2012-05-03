<g:if test="${salesOrderInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-sales-order-row-selector"><input type="checkbox" id="sales-order-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-sales-order-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-sales-order-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-sales-order-stage" property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-sales-order-doc-date" property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-sales-order-due-date" property="dueDate" title="${message(code: 'salesOrder.dueDate.label', default: 'Due date')}" />
      <g:sortableColumn id="content-table-headers-sales-order-total" property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" />
      <th id="content-table-headers-sales-order-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
    <tr>
      <td class="content-table-row-selector" headers="content-table-headers-sales-order-row-selector"><input type="checkbox" id="sales-order-row-selector-${salesOrderInstance.id}" data-id="${salesOrderInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-sales-order-number" headers="content-table-headers-sales-order-number"><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "fullNumber")}</g:link></td>
      <td class="content-table-type-string content-table-column-sales-order-subject" headers="content-table-headers-sales-order-subject"><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "subject")}</g:link></td>
      <td class="content-table-type-status content-table-column-sales-order-stage" headers="content-table-headers-sales-order-stage">${fieldValue(bean: salesOrderInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-sales-order-doc-date" headers="content-table-headers-sales-order-doc-date"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-date content-table-column-sales-order-due-date" headers="content-table-headers-sales-order-due-date"><g:formatDate date="${salesOrderInstance?.dueDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-sales-order-total" headers="content-table-headers-sales-order-total"><g:formatCurrency number="${salesOrderInstance?.total}" /></td>
      <td class="content-table-buttons" headers="content-table-headers-sales-order-buttons">
        <g:link controller="salesOrder" action="edit" id="${salesOrderInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="salesOrder" action="delete" id="${salesOrderInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${salesOrderInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
