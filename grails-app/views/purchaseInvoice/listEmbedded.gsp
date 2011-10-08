<g:if test="${purchaseInvoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="purchaseInvoice-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" params="${linkParams}" />
      <g:sortableColumn property="stage" title="${message(code: 'purchaseInvoice.stage.label.short', default: 'Stage')}" params="${linkParams}" />
      <g:sortableColumn property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short', default: 'Date')}" params="${linkParams}" />
      <g:sortableColumn property="dueDate" title="${message(code: 'purchaseInvoice.dueDate.label', default: 'Due date')}" params="${linkParams}" />
      <g:sortableColumn property="total" title="${message(code: 'purchaseInvoice.total.label.short', default: 'Total')}" style="width: 6em;" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
    <tr>
      <td><input type="checkbox" id="invoice-multop-${purchaseInvoiceInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "number")}</g:link></td>
      <td><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "subject")}</g:link></td>
      <td>${fieldValue(bean: purchaseInvoiceInstance, field: "stage")}</td>
      <td style="text-align: center;"><g:formatDate date="${purchaseInvoiceInstance?.docDate}" formatName="default.format.date" /></td>
      <td style="text-align: center;"><g:formatDate date="${purchaseInvoiceInstance?.dueDate}" formatName="default.format.date" /></td>
      <td style="text-align: right;"><g:formatCurrency number="${purchaseInvoiceInstance.total}" /></td>
      <td>
        <g:link controller="purchaseInvoice" action="edit" id="${purchaseInvoiceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="purchaseInvoice" action="delete" id="${purchaseInvoiceInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${purchaseInvoiceInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
