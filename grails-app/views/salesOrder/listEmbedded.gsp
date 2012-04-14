<g:if test="${salesOrderInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="salesOrder-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" params="${linkParams}" />
      <g:sortableColumn property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" params="${linkParams}" />
      <g:sortableColumn property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" params="${linkParams}" />
      <g:sortableColumn property="dueDate" title="${message(code: 'salesOrder.dueDate.label', default: 'Due date')}" params="${linkParams}" />
      <g:sortableColumn property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" style="width: 6em;" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
    <tr>
      <td><input type="checkbox" id="salesOrder-multop-${salesOrderInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "subject")}</g:link></td>
      <td>${fieldValue(bean: salesOrderInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="align-center"><g:formatDate date="${salesOrderInstance?.dueDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${salesOrderInstance?.total}" /></td>
      <td>
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
