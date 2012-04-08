<g:if test="${dunningInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="dunning-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" params="${linkParams}" />
      <g:sortableColumn property="stage" title="${message(code: 'dunning.stage.label.short', default: 'Stage')}" params="${linkParams}" />
      <g:sortableColumn property="docDate" title="${message(code: 'dunning.docDate.label.short', default: 'Date')}" params="${linkParams}" />
      <g:sortableColumn property="dueDatePayment" title="${message(code: 'dunning.dueDatePayment.label', default: 'Due date of payment')}" params="${linkParams}" />
      <g:sortableColumn property="total" title="${message(code: 'dunning.total.label.short', default: 'Total')}" style="width: 6em;" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
    <tr>
      <td><input type="checkbox" id="dunning-multop-${dunningInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="dunning" action="show" id="${dunningInstance.id}">${fieldValue(bean: dunningInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="dunning" action="show" id="${dunningInstance.id}">${fieldValue(bean: dunningInstance, field: "subject")}</g:link></td>
      <td class="payment-state-${dunningInstance?.paymentStateColor}">${fieldValue(bean: dunningInstance, field: "stage")}</td>
      <td style="text-align: center;"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
      <td style="text-align: center;"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></td>
      <td style="text-align: right;"><g:formatCurrency number="${dunningInstance?.total}" /></td>
      <td>
        <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
        <g:link controller="dunning" action="edit" id="${dunningInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="dunning" action="delete" id="${dunningInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
        </g:if>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${dunningInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
