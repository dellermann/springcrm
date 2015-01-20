<g:if test="${dunningInstanceList}">
<div class="table-responsive">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label')}" />
        <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label')}" />
        <g:sortableColumn property="stage" title="${message(code: 'dunning.stage.label.short')}" />
        <g:sortableColumn property="docDate" title="${message(code: 'dunning.docDate.label.short')}" />
        <g:sortableColumn property="dueDatePayment" title="${message(code: 'dunning.dueDatePayment.label')}" />
        <g:sortableColumn property="total" title="${message(code: 'dunning.total.label.short')}" />
        <g:sortableColumn property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label')}" />
        <th><g:message code="invoicingTransaction.closingBalance.label" /></th>
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
      <tr>
        <td class="col-type-id dunning-number"><g:link controller="dunning" action="show" id="${dunningInstance.id}"><g:fieldValue bean="${dunningInstance}" field="fullNumber" /></g:link></td>
        <td class="col-type-string dunning-subject"><g:link controller="dunning" action="show" id="${dunningInstance.id}"><g:nl2br value="${dunningInstance.subject}" /></g:link></td>
        <td class="col-type-status dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}"><g:fieldValue bean="${dunningInstance}" field="stage" /></td>
        <td class="col-type-date dunning-doc-date"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
        <td class="col-type-date dunning-subjectdue-date-payment"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></td>
        <td class="col-type-currency dunning-total"><g:formatCurrency number="${dunningInstance?.total}" displayZero="true" external="true" /></td>
        <td class="col-type-currency dunning-closing-balance balance-state balance-state-${dunningInstance?.balanceColor}"><g:formatCurrency number="${dunningInstance?.closingBalance}" displayZero="true" external="true" /></td>
        <td class="col-actions">
          <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
          <g:button controller="dunning" action="edit"
            id="${dunningInstance.id}" color="success" size="xs"
            icon="pencil-square-o" message="default.button.edit.label" />
          </g:if>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</div>
<g:render template="/layouts/remoteListPaginate"
  model="[total: dunningInstanceList]" />
</g:if>
<g:else>
  <g:render template="/layouts/remoteListEmpty" />
</g:else>
