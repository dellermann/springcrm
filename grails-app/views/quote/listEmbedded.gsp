<g:if test="${quoteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-quote-row-selector"><input type="checkbox" id="quote-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-quote-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-quote-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-quote-stage" property="stage" title="${message(code: 'quote.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-quote-doc-date" property="docDate" title="${message(code: 'quote.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-quote-shipping-date" property="shippingDate" title="${message(code: 'quote.shippingDate.label', default: 'Shipping date')}" />
      <g:sortableColumn id="content-table-headers-quote-total" property="total" title="${message(code: 'quote.total.label.short', default: 'Total')}" />
      <th id="content-table-headers-quote-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
    <tr>
      <td class="content-table-row-selector" headers="content-table-headers-quote-row-selector"><input type="checkbox" id="quote-row-selector-${quoteInstance.id}" data-id="${quoteInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-quote-number" headers="content-table-headers-quote-number"><g:link controller="quote" action="show" id="${quoteInstance.id}">${fieldValue(bean: quoteInstance, field: "fullNumber")}</g:link></td>
      <td class="content-table-type-string content-table-column-quote-subject" headers="content-table-headers-quote-subject"><g:link controller="quote" action="show" id="${quoteInstance.id}">${fieldValue(bean: quoteInstance, field: "subject")}</g:link></td>
      <td class="content-table-type-status content-table-column-quote-stage" headers="content-table-headers-quote-stage">${fieldValue(bean: quoteInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-quote-doc-date" headers="content-table-headers-quote-doc-date"><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-date content-table-column-quote-shipping-date" headers="content-table-headers-quote-shipping-date"><g:formatDate date="${quoteInstance?.shippingDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-quote-total" headers="content-table-headers-quote-total"><g:formatCurrency number="${quoteInstance?.total}" /></td>
      <td class="content-table-buttons" headers="content-table-headers-quote-buttons">
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
