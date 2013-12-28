<g:if test="${salesOrderInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="sales-order-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="dueDate" title="${message(code: 'salesOrder.dueDate.label', default: 'Due date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="sales-order-row-selector-${salesOrderInstance.id}" data-id="${salesOrderInstance.id}" /></td>
      <td class="id sales-order-number"><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}"><g:fieldValue bean="${salesOrderInstance}" field="fullNumber" /></g:link></td>
      <td class="string sales-order-subject"><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}"><g:nl2br value="${salesOrderInstance.subject}" /></g:link></td>
      <td class="status sales-order-stage"><g:fieldValue bean="${salesOrderInstance}" field="stage" /></td>
      <td class="date sales-order-doc-date"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="date sales-order-due-date"><g:formatDate date="${salesOrderInstance?.dueDate}" formatName="default.format.date" /></td>
      <td class="currency sales-order-total"><g:formatCurrency number="${salesOrderInstance?.total}" displayZero="true" external="true" /></td>
      <td class="action-buttons">
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
