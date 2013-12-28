<g:if test="${dunningInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="dunning-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'dunning.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'dunning.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="dueDatePayment" title="${message(code: 'dunning.dueDatePayment.label', default: 'Due date of payment')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'dunning.total.label.short', default: 'Total')}" />
      <g:sortableColumn scope="col" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="dunning-row-selector-${dunningInstance.id}" data-id="${dunningInstance.id}" /></td>
      <td class="id dunning-number"><g:link controller="dunning" action="show" id="${dunningInstance.id}"><g:fieldValue bean="${dunningInstance}" field="fullNumber" /></g:link></td>
      <td class="string dunning-subject"><g:link controller="dunning" action="show" id="${dunningInstance.id}"><g:nl2br value="${dunningInstance.subject}" /></g:link></td>
      <td class="status dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}"><g:fieldValue bean="${dunningInstance}" field="stage" /></td>
      <td class="date dunning-doc-date"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="date dunning-subjectdue-date-payment"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></td>
      <td class="currency dunning-total"><g:formatCurrency number="${dunningInstance?.total}" displayZero="true" external="true" /></td>
      <td class="currency dunning-closing-balance balance-state balance-state-${dunningInstance?.balanceColor}"><g:formatCurrency number="${dunningInstance?.closingBalance}" displayZero="true" external="true" /></td>
      <td class="action-buttons">
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
