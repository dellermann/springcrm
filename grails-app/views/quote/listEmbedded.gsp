<g:if test="${quoteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="quote-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'quote.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'quote.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="shippingDate" title="${message(code: 'quote.shippingDate.label', default: 'Shipping date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'quote.total.label.short', default: 'Total')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="quote-row-selector-${quoteInstance.id}" data-id="${quoteInstance.id}" /></td>
      <td class="id quote-number"><g:link controller="quote" action="show" id="${quoteInstance.id}"><g:fieldValue bean="${quoteInstance}" field="fullNumber" /></g:link></td>
      <td class="string quote-subject"><g:link controller="quote" action="show" id="${quoteInstance.id}"><g:fieldValue bean="${quoteInstance}" field="subject" /></g:link></td>
      <td class="status quote-stage"><g:fieldValue bean="${quoteInstance}" field="stage" /></td>
      <td class="date quote-doc-date"><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="date quote-shipping-date"><g:formatDate date="${quoteInstance?.shippingDate}" formatName="default.format.date" /></td>
      <td class="currency quote-total"><g:formatCurrency number="${quoteInstance?.total}" displayZero="true" external="true" /></td>
      <td class="action-buttons">
        <g:link controller="quote" action="edit" id="${quoteInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="quote" action="delete" id="${quoteInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${quoteInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
